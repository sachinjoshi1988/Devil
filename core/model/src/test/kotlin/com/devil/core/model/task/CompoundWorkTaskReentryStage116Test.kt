package com.devil.core.model.task

import com.devil.core.model.authorization.AuthorizationEvaluationState
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
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertSame

class CompoundWorkTaskReentryStage116Test {

    @Test
    fun `Task reentry request preserves exact Stage 115 provenance`() {
        val reconsideration =
            reconsiderationRecord(
                originalTrace = "trace-stage-116-original-001",
                freshTrace = "trace-stage-116-fresh-001",
            )

        val request =
            CompoundWorkTaskReentryRequest.create(
                reconsideration = reconsideration,
                authorizationState =
                    AuthorizationEvaluationState.AUTHORIZED,
            )

        val record =
            CompoundWorkTaskReentryRecord.create(
                request = request,
            )

        assertSame(
            reconsideration,
            record.request.reconsideration,
        )

        assertSame(
            reconsideration.request.freshDecision,
            record.request.reconsideration.request.freshDecision,
        )

        assertSame(
            reconsideration.request.continuation,
            record.request.reconsideration.request.continuation,
        )

        assertSame(
            reconsideration.request.continuation.request,
            record.request.reconsideration.request.continuation.request,
        )

        assertSame(
            reconsideration.request.continuation.step,
            record.request.reconsideration.request.continuation.step,
        )

        assertEquals(
            AuthorizationEvaluationState.AUTHORIZED,
            record.request.authorizationState,
        )
    }

    @Test
    fun `Task reentry request rejects non authorized model state`() {
        val reconsideration =
            reconsiderationRecord(
                originalTrace = "trace-stage-116-original-002",
                freshTrace = "trace-stage-116-fresh-002",
            )

        assertFailsWith<IllegalArgumentException> {
            CompoundWorkTaskReentryRequest.create(
                reconsideration = reconsideration,
                authorizationState =
                    AuthorizationEvaluationState.DENIED,
            )
        }
    }

    private fun reconsiderationRecord(
        originalTrace: String,
        freshTrace: String,
    ): CompoundWorkReconsiderationRecord {
        val originalDecision =
            decision(
                trace = originalTrace,
                summary = "Original bounded compound-work decision.",
            )

        val compoundRequest =
            CompoundWorkRequest.create(
                decision = originalDecision,
                steps =
                    listOf(
                        CompoundWorkStep.create(
                            position = 1,
                            summary = "Previously completed exact step.",
                        ),
                        CompoundWorkStep.create(
                            position = 2,
                            summary = "Exact eligible step.",
                        ),
                    ),
            )

        val continuation =
            CompoundWorkContinuationRecord.create(
                request = compoundRequest,
                step = compoundRequest.steps[1],
            )

        val freshDecision =
            decision(
                trace = freshTrace,
                summary = "Fresh constitutional reconsideration decision.",
            )

        val reconsiderationRequest =
            CompoundWorkReconsiderationRequest.create(
                continuation = continuation,
                freshDecision = freshDecision,
            )

        return CompoundWorkReconsiderationRecord.create(
            request = reconsiderationRequest,
        )
    }

    private fun decision(
        trace: String,
        summary: String,
    ): DecisionRecord {
        val traceId =
            TraceId.from(trace)

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
                                1_754_000_116_000L,
                            ),
                    ),
                state = UnderstandingState.COMPLETE,
                summary = "Stage 116 bounded understanding.",
            )

        return DecisionRecord.create(
            understanding = understanding,
            state = DecisionState.SELECTED,
            summary = summary,
        )
    }
}
