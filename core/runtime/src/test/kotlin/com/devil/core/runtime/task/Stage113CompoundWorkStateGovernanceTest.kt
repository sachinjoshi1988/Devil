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
import kotlin.test.assertFailsWith
import kotlin.test.assertSame

class Stage113CompoundWorkStateGovernanceTest {

    private val coordinator =
        CompoundWorkStateCoordinator()

    @Test
    fun `coordinator preserves exact Stage 77 request and step identities`() {
        val request =
            request(
                "trace-stage-113-governance-001",
            )

        val states =
            listOf(
                CompoundWorkStepStateRecord.create(
                    step = request.steps[0],
                    state =
                        CompoundWorkStepState.COMPLETED,
                ),
                CompoundWorkStepStateRecord.create(
                    step = request.steps[1],
                    state =
                        CompoundWorkStepState.PENDING,
                ),
            )

        val result =
            coordinator.assess(
                request = request,
                stepStates = states,
            )

        assertSame(
            request,
            result.request,
        )

        assertSame(
            request.steps[0],
            result.stepStates[0].step,
        )

        assertSame(
            request.steps[1],
            result.stepStates[1].step,
        )

        assertEquals(
            CompoundWorkState.ACTIVE,
            result.state,
        )
    }

    @Test
    fun `completed step does not authorize or automatically advance next step`() {
        val request =
            request(
                "trace-stage-113-governance-002",
            )

        val first =
            CompoundWorkStepStateRecord.create(
                step = request.steps[0],
                state =
                    CompoundWorkStepState.COMPLETED,
            )

        val second =
            CompoundWorkStepStateRecord.create(
                step = request.steps[1],
                state =
                    CompoundWorkStepState.PENDING,
            )

        val result =
            coordinator.assess(
                request = request,
                stepStates =
                    listOf(
                        first,
                        second,
                    ),
            )

        assertEquals(
            CompoundWorkStepState.COMPLETED,
            result.stepStates[0].state,
        )

        assertEquals(
            CompoundWorkStepState.PENDING,
            result.stepStates[1].state,
        )

        assertEquals(
            CompoundWorkState.ACTIVE,
            result.state,
        )

        assertSame(
            second,
            result.stepStates[1],
        )
    }

    @Test
    fun `partial compound work remains partial rather than total failure`() {
        val request =
            request(
                "trace-stage-113-governance-003",
            )

        val result =
            coordinator.assess(
                request = request,
                stepStates =
                    listOf(
                        CompoundWorkStepStateRecord.create(
                            step = request.steps[0],
                            state =
                                CompoundWorkStepState.COMPLETED,
                        ),
                        CompoundWorkStepStateRecord.create(
                            step = request.steps[1],
                            state =
                                CompoundWorkStepState.FAILED,
                        ),
                    ),
            )

        assertEquals(
            CompoundWorkState.PARTIAL,
            result.state,
        )
    }

    @Test
    fun `all completed steps produce only bounded compound work completion`() {
        val request =
            request(
                "trace-stage-113-governance-004",
            )

        val result =
            coordinator.assess(
                request = request,
                stepStates =
                    request.steps.map { step ->
                        CompoundWorkStepStateRecord.create(
                            step = step,
                            state =
                                CompoundWorkStepState.COMPLETED,
                        )
                    },
            )

        assertEquals(
            CompoundWorkState.COMPLETED,
            result.state,
        )

        assertEquals(
            DecisionState.SELECTED,
            result.request.decision.state,
        )
    }

    @Test
    fun `state assessment rejects a foreign compound work step`() {
        val request =
            request(
                "trace-stage-113-governance-005",
            )

        val foreign =
            CompoundWorkStep.create(
                position = 2,
                summary =
                    "Foreign step must not enter the request.",
            )

        assertFailsWith<IllegalArgumentException> {
            coordinator.assess(
                request = request,
                stepStates =
                    listOf(
                        CompoundWorkStepStateRecord.create(
                            step = request.steps[0],
                            state =
                                CompoundWorkStepState.COMPLETED,
                        ),
                        CompoundWorkStepStateRecord.create(
                            step = foreign,
                            state =
                                CompoundWorkStepState.PENDING,
                        ),
                    ),
            )
        }
    }

    @Test
    fun `state assessment requires exactly one record per exact step`() {
        val request =
            request(
                "trace-stage-113-governance-006",
            )

        val first =
            CompoundWorkStepStateRecord.create(
                step = request.steps[0],
                state =
                    CompoundWorkStepState.ACTIVE,
            )

        assertFailsWith<IllegalArgumentException> {
            coordinator.assess(
                request = request,
                stepStates =
                    listOf(
                        first,
                        first,
                    ),
            )
        }
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
                        schemaVersion =
                            SchemaVersion.from(1),
                        source =
                            ContextSource.TEXT,
                        trustLevel =
                            ContextTrustLevel.VERIFIED,
                        securityLevel =
                            ContextSecurityLevel.RESTRICTED,
                        observedAt =
                            DevilTimestamp.fromEpochMilliseconds(
                                1_754_000_113_500L,
                            ),
                    ),
                state =
                    UnderstandingState.COMPLETE,
                summary =
                    "Stage 113 governed compound-work state understanding.",
            )

        val decision =
            DecisionRecord.create(
                understanding = understanding,
                state =
                    DecisionState.SELECTED,
                summary =
                    "Maintain one bounded compound goal.",
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
