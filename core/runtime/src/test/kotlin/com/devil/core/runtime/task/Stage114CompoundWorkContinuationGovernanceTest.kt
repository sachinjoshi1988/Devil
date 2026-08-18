package com.devil.core.runtime.task

import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.common.SchemaVersion
import com.devil.core.model.common.TraceId
import com.devil.core.model.context.ContextEnvelope
import com.devil.core.model.context.ContextSecurityLevel
import com.devil.core.model.context.ContextSource
import com.devil.core.model.context.ContextTrustLevel
import com.devil.core.model.decision.DecisionRecord
import com.devil.core.model.decision.DecisionState
import com.devil.core.model.task.CompoundWorkRequest
import com.devil.core.model.task.CompoundWorkState
import com.devil.core.model.task.CompoundWorkStep
import com.devil.core.model.task.CompoundWorkStepState
import com.devil.core.model.task.CompoundWorkStepStateRecord
import com.devil.core.model.understanding.UnderstandingRecord
import com.devil.core.model.understanding.UnderstandingState
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertSame

class Stage114CompoundWorkContinuationGovernanceTest {

    private val coordinator =
        CompoundWorkContinuationCoordinator()

    @Test
    fun `completed preceding step permits only reconsideration of exact pending next step`() {
        val request =
            request(
                "trace-stage-114-governance-001",
            )

        val stateResult =
            stateResult(
                request = request,
                first = CompoundWorkStepState.COMPLETED,
                second = CompoundWorkStepState.PENDING,
                aggregate = CompoundWorkState.ACTIVE,
            )

        val result =
            coordinator.evaluate(
                stateResult = stateResult,
            )

        assertEquals(
            CompoundWorkContinuationStatus
                .ELIGIBLE_FOR_RECONSIDERATION,
            result.status,
        )

        val record =
            requireNotNull(result.record)

        assertSame(
            request,
            record.request,
        )

        assertSame(
            request.steps[1],
            record.step,
        )

        assertSame(
            stateResult.request,
            record.request,
        )
    }

    @Test
    fun `first pending step may be reconsidered while later pending step is not skipped to`() {
        val request =
            request(
                "trace-stage-114-governance-002",
            )

        val stateResult =
            stateResult(
                request = request,
                first = CompoundWorkStepState.PENDING,
                second = CompoundWorkStepState.PENDING,
                aggregate = CompoundWorkState.ACTIVE,
            )

        val result =
            coordinator.evaluate(
                stateResult = stateResult,
            )

        assertEquals(
            CompoundWorkContinuationStatus
                .ELIGIBLE_FOR_RECONSIDERATION,
            result.status,
        )

        assertSame(
            request.steps[0],
            requireNotNull(result.record).step,
        )
    }

    @Test
    fun `active step blocks automatic next-step advancement`() {
        val request =
            request(
                "trace-stage-114-governance-003",
            )

        val result =
            coordinator.evaluate(
                stateResult =
                    stateResult(
                        request = request,
                        first = CompoundWorkStepState.ACTIVE,
                        second = CompoundWorkStepState.PENDING,
                        aggregate = CompoundWorkState.ACTIVE,
                    ),
            )

        assertEquals(
            CompoundWorkContinuationStatus.DEFERRED,
            result.status,
        )

        assertNull(result.record)
    }

    @Test
    fun `completed then active does not become continuation eligibility`() {
        val request =
            request(
                "trace-stage-114-governance-004",
            )

        val result =
            coordinator.evaluate(
                stateResult =
                    stateResult(
                        request = request,
                        first = CompoundWorkStepState.COMPLETED,
                        second = CompoundWorkStepState.ACTIVE,
                        aggregate = CompoundWorkState.ACTIVE,
                    ),
            )

        assertEquals(
            CompoundWorkContinuationStatus.DEFERRED,
            result.status,
        )

        assertNull(result.record)
    }

    @Test
    fun `blocked failed and terminal aggregate states fail closed`() {
        val cases =
            listOf(
                Triple(
                    CompoundWorkStepState.COMPLETED,
                    CompoundWorkStepState.BLOCKED,
                    CompoundWorkState.PARTIAL,
                ),
                Triple(
                    CompoundWorkStepState.COMPLETED,
                    CompoundWorkStepState.FAILED,
                    CompoundWorkState.PARTIAL,
                ),
                Triple(
                    CompoundWorkStepState.COMPLETED,
                    CompoundWorkStepState.COMPLETED,
                    CompoundWorkState.COMPLETED,
                ),
                Triple(
                    CompoundWorkStepState.FAILED,
                    CompoundWorkStepState.FAILED,
                    CompoundWorkState.FAILED,
                ),
            )

        cases.forEachIndexed { index, case ->
            val request =
                request(
                    "trace-stage-114-governance-terminal-$index",
                )

            val result =
                coordinator.evaluate(
                    stateResult =
                        stateResult(
                            request = request,
                            first = case.first,
                            second = case.second,
                            aggregate = case.third,
                        ),
                )

            assertEquals(
                CompoundWorkContinuationStatus.DEFERRED,
                result.status,
            )

            assertNull(result.record)
        }
    }

    @Test
    fun `eligibility preserves Stage 113 provenance and creates no automatic continuation semantics`() {
        val request =
            request(
                "trace-stage-114-governance-006",
            )

        val first =
            CompoundWorkStepStateRecord.create(
                step = request.steps[0],
                state = CompoundWorkStepState.COMPLETED,
            )

        val second =
            CompoundWorkStepStateRecord.create(
                step = request.steps[1],
                state = CompoundWorkStepState.PENDING,
            )

        val stateResult =
            CompoundWorkStateResult.create(
                traceId =
                    request
                        .decision
                        .understanding
                        .context
                        .traceId,
                request = request,
                stepStates =
                    listOf(
                        first,
                        second,
                    ),
                state = CompoundWorkState.ACTIVE,
            )

        val result =
            coordinator.evaluate(
                stateResult = stateResult,
            )

        val record =
            requireNotNull(result.record)

        assertSame(
            request,
            stateResult.request,
        )

        assertSame(
            request,
            record.request,
        )

        assertSame(
            second.step,
            record.step,
        )

        assertEquals(
            CompoundWorkStepState.COMPLETED,
            stateResult.stepStates[0].state,
        )

        assertEquals(
            CompoundWorkStepState.PENDING,
            stateResult.stepStates[1].state,
        )

        assertEquals(
            DecisionState.SELECTED,
            record.request.decision.state,
        )
    }

    private fun stateResult(
        request: CompoundWorkRequest,
        first: CompoundWorkStepState,
        second: CompoundWorkStepState,
        aggregate: CompoundWorkState,
    ): CompoundWorkStateResult {
        return CompoundWorkStateResult.create(
            traceId =
                request
                    .decision
                    .understanding
                    .context
                    .traceId,
            request = request,
            stepStates =
                listOf(
                    CompoundWorkStepStateRecord.create(
                        step = request.steps[0],
                        state = first,
                    ),
                    CompoundWorkStepStateRecord.create(
                        step = request.steps[1],
                        state = second,
                    ),
                ),
            state = aggregate,
        )
    }

    private fun request(
        trace: String,
    ): CompoundWorkRequest {
        val traceId =
            TraceId.from(trace)

        val understanding =
            UnderstandingRecord.create(
                context =
                    ContextEnvelope.create(
                        traceId = traceId,
                        schemaVersion = SchemaVersion.from(1),
                        source = ContextSource.TEXT,
                        trustLevel =
                            ContextTrustLevel.VERIFIED,
                        securityLevel =
                            ContextSecurityLevel.RESTRICTED,
                        observedAt =
                            DevilTimestamp.fromEpochMilliseconds(
                                1_754_000_114_500L,
                            ),
                    ),
                state = UnderstandingState.COMPLETE,
                summary =
                    "Stage 114 governed continuation-eligibility understanding.",
            )

        val decision =
            DecisionRecord.create(
                understanding = understanding,
                state = DecisionState.SELECTED,
                summary =
                    "Maintain the existing bounded compound-work goal.",
            )

        return CompoundWorkRequest.create(
            decision = decision,
            steps =
                listOf(
                    CompoundWorkStep.create(
                        position = 1,
                        summary =
                            "First governed bounded step.",
                    ),
                    CompoundWorkStep.create(
                        position = 2,
                        summary =
                            "Second governed bounded step.",
                    ),
                ),
        )
    }
}
