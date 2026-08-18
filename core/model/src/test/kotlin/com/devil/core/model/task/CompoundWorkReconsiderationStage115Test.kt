package com.devil.core.model.task

import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.common.SchemaVersion
import com.devil.core.model.common.TraceId
import com.devil.core.model.context.ContextEnvelope
import com.devil.core.model.context.ContextSecurityLevel
import com.devil.core.model.context.ContextSource
import com.devil.core.model.context.ContextTrustLevel
import com.devil.core.model.decision.DecisionRecord
import com.devil.core.model.decision.DecisionState
import com.devil.core.model.understanding.UnderstandingRecord
import com.devil.core.model.understanding.UnderstandingState
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertSame

class CompoundWorkReconsiderationStage115Test {

    @Test
    fun `reconsideration request preserves exact continuation and fresh decision`() {
        val continuation =
            continuation(
                originalTrace = "trace-stage115-model-original-001",
            )

        val freshDecision =
            decision(
                trace = "trace-stage115-model-fresh-001",
                state = DecisionState.SELECTED,
            )

        val request =
            CompoundWorkReconsiderationRequest.create(
                continuation = continuation,
                freshDecision = freshDecision,
            )

        val record =
            CompoundWorkReconsiderationRecord.create(
                request = request,
            )

        assertSame(
            continuation,
            request.continuation,
        )

        assertSame(
            freshDecision,
            request.freshDecision,
        )

        assertSame(
            request,
            record.request,
        )

        assertSame(
            continuation.request,
            record.request.continuation.request,
        )

        assertSame(
            continuation.step,
            record.request.continuation.step,
        )
    }

    @Test
    fun `reconsideration request rejects reuse of originating trace`() {
        val trace =
            "trace-stage115-model-reused-002"

        val continuation =
            continuation(
                originalTrace = trace,
            )

        val freshDecision =
            decision(
                trace = trace,
                state = DecisionState.SELECTED,
            )

        assertFailsWith<IllegalArgumentException> {
            CompoundWorkReconsiderationRequest.create(
                continuation = continuation,
                freshDecision = freshDecision,
            )
        }
    }

    @Test
    fun `reconsideration request requires selected fresh decision`() {
        val continuation =
            continuation(
                originalTrace = "trace-stage115-model-original-003",
            )

        val freshDecision =
            decision(
                trace = "trace-stage115-model-fresh-003",
                state = DecisionState.DEFERRED,
            )

        assertFailsWith<IllegalArgumentException> {
            CompoundWorkReconsiderationRequest.create(
                continuation = continuation,
                freshDecision = freshDecision,
            )
        }
    }

    private fun continuation(
        originalTrace: String,
    ): CompoundWorkContinuationRecord {
        val request =
            compoundRequest(
                trace = originalTrace,
            )

        return CompoundWorkContinuationRecord.create(
            request = request,
            step = request.steps[0],
        )
    }

    private fun compoundRequest(
        trace: String,
    ): CompoundWorkRequest {
        val decision =
            decision(
                trace = trace,
                state = DecisionState.SELECTED,
            )

        return CompoundWorkRequest.create(
            decision = decision,
            steps =
                listOf(
                    CompoundWorkStep.create(
                        position = 1,
                        summary = "First exact governed compound-work step.",
                    ),
                    CompoundWorkStep.create(
                        position = 2,
                        summary = "Second exact governed compound-work step.",
                    ),
                ),
        )
    }

    private fun decision(
        trace: String,
        state: DecisionState,
    ): DecisionRecord {
        val understanding =
            UnderstandingRecord.create(
                context =
                    ContextEnvelope.create(
                        traceId = TraceId.from(trace),
                        schemaVersion = SchemaVersion.from(1),
                        source = ContextSource.TEXT,
                        trustLevel = ContextTrustLevel.VERIFIED,
                        securityLevel = ContextSecurityLevel.RESTRICTED,
                        observedAt =
                            DevilTimestamp.fromEpochMilliseconds(
                                1_754_001_150_000L,
                            ),
                    ),
                state = UnderstandingState.COMPLETE,
                summary =
                    "Stage 115 bounded reconsideration understanding.",
            )

        return DecisionRecord.create(
            understanding = understanding,
            state = state,
            summary =
                "Fresh bounded constitutional reconsideration decision.",
        )
    }
}
