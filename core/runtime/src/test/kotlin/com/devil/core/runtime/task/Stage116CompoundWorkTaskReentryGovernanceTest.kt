package com.devil.core.runtime.task

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
import com.devil.core.model.task.CompoundWorkContinuationRecord
import com.devil.core.model.task.CompoundWorkReconsiderationRecord
import com.devil.core.model.task.CompoundWorkReconsiderationRequest
import com.devil.core.model.task.CompoundWorkRequest
import com.devil.core.model.task.CompoundWorkStep
import com.devil.core.model.understanding.UnderstandingRecord
import com.devil.core.model.understanding.UnderstandingState
import com.devil.core.runtime.authorization.AuthorizationResult
import com.devil.core.runtime.authorization.AuthorizationStatus
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertSame

class Stage116CompoundWorkTaskReentryGovernanceTest {

    private val coordinator =
        CompoundWorkTaskReentryCoordinator()

    @Test
    fun `prepared reconsideration plus current authorization may prepare Task reentry only`() {
        val originalTrace =
            TraceId.from("trace-stage-116-original-001")

        val freshTrace =
            TraceId.from("trace-stage-116-fresh-001")

        val reconsideration =
            reconsiderationResult(
                originalTrace = originalTrace,
                freshTrace = freshTrace,
            )

        val authorization =
            AuthorizationResult.create(
                traceId = freshTrace,
                status = AuthorizationStatus.AUTHORIZED,
            )

        val result =
            coordinator.prepare(
                currentTraceId = freshTrace,
                reconsideration = reconsideration,
                authorization = authorization,
            )

        assertEquals(
            CompoundWorkTaskReentryStatus.PREPARED,
            result.status,
        )

        val record =
            requireNotNull(result.record)

        assertSame(
            reconsideration.record,
            record.request.reconsideration,
        )

        assertSame(
            reconsideration.record!!
                .request
                .freshDecision,
            record.request
                .reconsideration
                .request
                .freshDecision,
        )

        assertSame(
            reconsideration.record!!
                .request
                .continuation,
            record.request
                .reconsideration
                .request
                .continuation,
        )

        assertSame(
            reconsideration.record!!
                .request
                .continuation
                .request,
            record.request
                .reconsideration
                .request
                .continuation
                .request,
        )

        assertSame(
            reconsideration.record!!
                .request
                .continuation
                .step,
            record.request
                .reconsideration
                .request
                .continuation
                .step,
        )

        assertEquals(
            AuthorizationEvaluationState.AUTHORIZED,
            record.request.authorizationState,
        )
    }

    @Test
    fun `Stage 115 deferred reconsideration cannot prepare Task reentry`() {
        val trace =
            TraceId.from("trace-stage-116-fresh-002")

        val reconsideration =
            CompoundWorkReconsiderationResult.create(
                traceId = trace,
                status = CompoundWorkReconsiderationStatus.DEFERRED,
            )

        val authorization =
            AuthorizationResult.create(
                traceId = trace,
                status = AuthorizationStatus.AUTHORIZED,
            )

        val result =
            coordinator.prepare(
                currentTraceId = trace,
                reconsideration = reconsideration,
                authorization = authorization,
            )

        assertEquals(
            CompoundWorkTaskReentryStatus.DEFERRED,
            result.status,
        )

        assertNull(result.record)
    }

    @Test
    fun `current authorization is mandatory for Task reentry preparation`() {
        val originalTrace =
            TraceId.from("trace-stage-116-original-003")

        val freshTrace =
            TraceId.from("trace-stage-116-fresh-003")

        val reconsideration =
            reconsiderationResult(
                originalTrace = originalTrace,
                freshTrace = freshTrace,
            )

        val authorization =
            AuthorizationResult.create(
                traceId = freshTrace,
                status = AuthorizationStatus.DENIED,
            )

        val result =
            coordinator.prepare(
                currentTraceId = freshTrace,
                reconsideration = reconsideration,
                authorization = authorization,
            )

        assertEquals(
            CompoundWorkTaskReentryStatus.DEFERRED,
            result.status,
        )

        assertNull(result.record)
    }

    @Test
    fun `authorization from another trace cannot prepare Task reentry`() {
        val originalTrace =
            TraceId.from("trace-stage-116-original-004")

        val freshTrace =
            TraceId.from("trace-stage-116-fresh-004")

        val reconsideration =
            reconsiderationResult(
                originalTrace = originalTrace,
                freshTrace = freshTrace,
            )

        val authorization =
            AuthorizationResult.create(
                traceId =
                    TraceId.from(
                        "trace-stage-116-foreign-auth-004",
                    ),
                status = AuthorizationStatus.AUTHORIZED,
            )

        val result =
            coordinator.prepare(
                currentTraceId = freshTrace,
                reconsideration = reconsideration,
                authorization = authorization,
            )

        assertEquals(
            CompoundWorkTaskReentryStatus.DEFERRED,
            result.status,
        )

        assertNull(result.record)
    }

    @Test
    fun `Stage 115 fresh trace must equal current Task reentry trace`() {
        val originalTrace =
            TraceId.from("trace-stage-116-original-005")

        val freshTrace =
            TraceId.from("trace-stage-116-fresh-005")

        val currentTrace =
            TraceId.from("trace-stage-116-current-005")

        val reconsideration =
            reconsiderationResult(
                originalTrace = originalTrace,
                freshTrace = freshTrace,
            )

        val authorization =
            AuthorizationResult.create(
                traceId = currentTrace,
                status = AuthorizationStatus.AUTHORIZED,
            )

        val result =
            coordinator.prepare(
                currentTraceId = currentTrace,
                reconsideration = reconsideration,
                authorization = authorization,
            )

        assertEquals(
            CompoundWorkTaskReentryStatus.DEFERRED,
            result.status,
        )

        assertNull(result.record)
    }

    @Test
    fun `originating compound-work trace cannot become current Task reentry trace`() {
        val originalTrace =
            TraceId.from("trace-stage-116-original-006")

        val freshTrace =
            TraceId.from("trace-stage-116-fresh-006")

        val reconsideration =
            reconsiderationResult(
                originalTrace = originalTrace,
                freshTrace = freshTrace,
            )

        val authorization =
            AuthorizationResult.create(
                traceId = originalTrace,
                status = AuthorizationStatus.AUTHORIZED,
            )

        val result =
            coordinator.prepare(
                currentTraceId = originalTrace,
                reconsideration = reconsideration,
                authorization = authorization,
            )

        assertEquals(
            CompoundWorkTaskReentryStatus.DEFERRED,
            result.status,
        )

        assertNull(result.record)
    }

    private fun reconsiderationResult(
        originalTrace: TraceId,
        freshTrace: TraceId,
    ): CompoundWorkReconsiderationResult {
        val originalDecision =
            decision(
                traceId = originalTrace,
                summary = "Original governed compound-work decision.",
            )

        val compoundRequest =
            CompoundWorkRequest.create(
                decision = originalDecision,
                steps =
                    listOf(
                        CompoundWorkStep.create(
                            position = 1,
                            summary = "Completed predecessor.",
                        ),
                        CompoundWorkStep.create(
                            position = 2,
                            summary = "Exact Stage 77 eligible step.",
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
                traceId = freshTrace,
                summary = "Fresh selected reconsideration decision.",
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

        return CompoundWorkReconsiderationResult.create(
            traceId = freshTrace,
            status = CompoundWorkReconsiderationStatus.PREPARED,
            record = record,
        )
    }

    private fun decision(
        traceId: TraceId,
        summary: String,
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
                                1_754_000_116_500L,
                            ),
                    ),
                state = UnderstandingState.COMPLETE,
                summary =
                    "Stage 116 constitutional Task re-entry understanding.",
            )

        return DecisionRecord.create(
            understanding = understanding,
            state = DecisionState.SELECTED,
            summary = summary,
        )
    }
}
