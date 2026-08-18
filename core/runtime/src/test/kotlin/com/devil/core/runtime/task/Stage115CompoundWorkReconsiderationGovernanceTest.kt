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
import com.devil.core.model.task.CompoundWorkContinuationRecord
import com.devil.core.model.task.CompoundWorkRequest
import com.devil.core.model.task.CompoundWorkStep
import com.devil.core.model.understanding.UnderstandingRecord
import com.devil.core.model.understanding.UnderstandingState
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertSame

class Stage115CompoundWorkReconsiderationGovernanceTest {

    private val coordinator =
        CompoundWorkReconsiderationCoordinator()

    @Test
    fun `eligible exact step plus fresh selected decision may prepare reconsideration`() {
        val continuation =
            eligibleContinuation(
                originalTrace =
                    "trace-stage115-governance-original-001",
            )

        val currentTraceId =
            TraceId.from(
                "trace-stage115-governance-fresh-001",
            )

        val freshDecision =
            decision(
                traceId = currentTraceId,
                state = DecisionState.SELECTED,
            )

        val result =
            coordinator.evaluate(
                currentTraceId = currentTraceId,
                continuation = continuation,
                freshDecision = freshDecision,
                reconsiderationEstablished = true,
            )

        assertEquals(
            CompoundWorkReconsiderationStatus.PREPARED,
            result.status,
        )

        val record =
            requireNotNull(result.record)

        assertSame(
            continuation.record,
            record.request.continuation,
        )

        assertSame(
            requireNotNull(continuation.record).request,
            record.request.continuation.request,
        )

        assertSame(
            requireNotNull(continuation.record).step,
            record.request.continuation.step,
        )

        assertSame(
            freshDecision,
            record.request.freshDecision,
        )

        assertEquals(
            currentTraceId,
            result.traceId,
        )
    }

    @Test
    fun `explicit reconsideration relationship remains mandatory`() {
        val continuation =
            eligibleContinuation(
                originalTrace =
                    "trace-stage115-governance-original-002",
            )

        val currentTraceId =
            TraceId.from(
                "trace-stage115-governance-fresh-002",
            )

        val result =
            coordinator.evaluate(
                currentTraceId = currentTraceId,
                continuation = continuation,
                freshDecision =
                    decision(
                        traceId = currentTraceId,
                        state = DecisionState.SELECTED,
                    ),
                reconsiderationEstablished = false,
            )

        assertEquals(
            CompoundWorkReconsiderationStatus.DEFERRED,
            result.status,
        )

        assertNull(result.record)
    }

    @Test
    fun `deferred Stage 114 eligibility cannot become reconsideration`() {
        val originalTraceId =
            TraceId.from(
                "trace-stage115-governance-original-003",
            )

        val continuation =
            CompoundWorkContinuationResult.create(
                traceId = originalTraceId,
                status = CompoundWorkContinuationStatus.DEFERRED,
            )

        val currentTraceId =
            TraceId.from(
                "trace-stage115-governance-fresh-003",
            )

        val result =
            coordinator.evaluate(
                currentTraceId = currentTraceId,
                continuation = continuation,
                freshDecision =
                    decision(
                        traceId = currentTraceId,
                        state = DecisionState.SELECTED,
                    ),
                reconsiderationEstablished = true,
            )

        assertEquals(
            CompoundWorkReconsiderationStatus.DEFERRED,
            result.status,
        )

        assertNull(result.record)
    }

    @Test
    fun `originating compound-work trace cannot be reused as fresh reconsideration trace`() {
        val traceId =
            TraceId.from(
                "trace-stage115-governance-reused-004",
            )

        val continuation =
            eligibleContinuation(
                originalTrace = traceId.value,
            )

        val result =
            coordinator.evaluate(
                currentTraceId = traceId,
                continuation = continuation,
                freshDecision =
                    decision(
                        traceId = traceId,
                        state = DecisionState.SELECTED,
                    ),
                reconsiderationEstablished = true,
            )

        assertEquals(
            CompoundWorkReconsiderationStatus.DEFERRED,
            result.status,
        )

        assertNull(result.record)
    }

    @Test
    fun `fresh reconsideration decision must be selected`() {
        val continuation =
            eligibleContinuation(
                originalTrace =
                    "trace-stage115-governance-original-005",
            )

        val currentTraceId =
            TraceId.from(
                "trace-stage115-governance-fresh-005",
            )

        val result =
            coordinator.evaluate(
                currentTraceId = currentTraceId,
                continuation = continuation,
                freshDecision =
                    decision(
                        traceId = currentTraceId,
                        state = DecisionState.REQUIRES_CLARIFICATION,
                    ),
                reconsiderationEstablished = true,
            )

        assertEquals(
            CompoundWorkReconsiderationStatus.DEFERRED,
            result.status,
        )

        assertNull(result.record)
    }

    @Test
    fun `fresh decision must belong to current reconsideration trace`() {
        val continuation =
            eligibleContinuation(
                originalTrace =
                    "trace-stage115-governance-original-006",
            )

        val currentTraceId =
            TraceId.from(
                "trace-stage115-governance-current-006",
            )

        val unrelatedFreshDecision =
            decision(
                traceId =
                    TraceId.from(
                        "trace-stage115-governance-other-006",
                    ),
                state = DecisionState.SELECTED,
            )

        val result =
            coordinator.evaluate(
                currentTraceId = currentTraceId,
                continuation = continuation,
                freshDecision = unrelatedFreshDecision,
                reconsiderationEstablished = true,
            )

        assertEquals(
            CompoundWorkReconsiderationStatus.DEFERRED,
            result.status,
        )

        assertNull(result.record)
    }

    private fun eligibleContinuation(
        originalTrace: String,
    ): CompoundWorkContinuationResult {
        val originalTraceId =
            TraceId.from(originalTrace)

        val request =
            compoundRequest(
                traceId = originalTraceId,
            )

        val continuationRecord =
            CompoundWorkContinuationRecord.create(
                request = request,
                step = request.steps[0],
            )

        return CompoundWorkContinuationResult.create(
            traceId = originalTraceId,
            status =
                CompoundWorkContinuationStatus
                    .ELIGIBLE_FOR_RECONSIDERATION,
            record = continuationRecord,
        )
    }

    private fun compoundRequest(
        traceId: TraceId,
    ): CompoundWorkRequest {
        val originalDecision =
            decision(
                traceId = traceId,
                state = DecisionState.SELECTED,
            )

        return CompoundWorkRequest.create(
            decision = originalDecision,
            steps =
                listOf(
                    CompoundWorkStep.create(
                        position = 1,
                        summary =
                            "Exact Stage 77 step eligible for fresh reconsideration.",
                    ),
                    CompoundWorkStep.create(
                        position = 2,
                        summary =
                            "Later exact Stage 77 step remains outside this reconsideration.",
                    ),
                ),
        )
    }

    private fun decision(
        traceId: TraceId,
        state: DecisionState,
    ): DecisionRecord {
        val understanding =
            UnderstandingRecord.create(
                context =
                    ContextEnvelope.create(
                        traceId = traceId,
                        schemaVersion = SchemaVersion.from(1),
                        source = ContextSource.TEXT,
                        trustLevel = ContextTrustLevel.VERIFIED,
                        securityLevel = ContextSecurityLevel.RESTRICTED,
                        observedAt =
                            DevilTimestamp.fromEpochMilliseconds(
                                1_754_001_155_000L,
                            ),
                    ),
                state = UnderstandingState.COMPLETE,
                summary =
                    "Stage 115 governed compound-work reconsideration understanding.",
            )

        return DecisionRecord.create(
            understanding = understanding,
            state = state,
            summary =
                "One bounded constitutional reconsideration decision.",
        )
    }
}
