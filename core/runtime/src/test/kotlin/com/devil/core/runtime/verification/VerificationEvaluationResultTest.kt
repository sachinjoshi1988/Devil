package com.devil.core.runtime.verification

import com.devil.core.model.capability.CapabilityCategory
import com.devil.core.model.capability.CapabilityContract
import com.devil.core.model.capability.CapabilityId
import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.common.SchemaVersion
import com.devil.core.model.common.TraceId
import com.devil.core.model.context.ContextEnvelope
import com.devil.core.model.context.ContextSecurityLevel
import com.devil.core.model.context.ContextSource
import com.devil.core.model.context.ContextTrustLevel
import com.devil.core.model.decision.DecisionRecord
import com.devil.core.model.decision.DecisionState
import com.devil.core.model.error.ErrorCode
import com.devil.core.model.error.UniversalErrorRecord
import com.devil.core.model.execution.ExecutionRequest
import com.devil.core.model.observation.ObservationRequest
import com.devil.core.model.plan.PlanId
import com.devil.core.model.plan.PlanRecord
import com.devil.core.model.plan.PlanState
import com.devil.core.model.task.TaskId
import com.devil.core.model.task.TaskRecord
import com.devil.core.model.task.TaskState
import com.devil.core.model.understanding.UnderstandingRecord
import com.devil.core.model.understanding.UnderstandingState
import com.devil.core.model.verification.VerificationRequest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class VerificationEvaluationResultTest {

    @Test
    fun `create preserves verified result with matching request`() {
        val traceId = TraceId.from(
            "trace-verification-evaluation-result-001",
        )
        val request = createRequest(traceId)

        val result = VerificationEvaluationResult.create(
            traceId = traceId,
            status = VerificationEvaluationStatus.VERIFIED,
            request = request,
        )

        assertEquals(traceId, result.traceId)
        assertEquals(
            VerificationEvaluationStatus.VERIFIED,
            result.status,
        )
        assertEquals(request, result.request)
        assertNull(result.error)
    }

    @Test
    fun `create preserves unavailable result`() {
        val traceId = TraceId.from(
            "trace-verification-evaluation-result-002",
        )

        val result = VerificationEvaluationResult.create(
            traceId = traceId,
            status = VerificationEvaluationStatus.UNAVAILABLE,
        )

        assertEquals(
            VerificationEvaluationStatus.UNAVAILABLE,
            result.status,
        )
        assertNull(result.request)
        assertNull(result.error)
    }

    @Test
    fun `create preserves failed result with matching error`() {
        val traceId = TraceId.from(
            "trace-verification-evaluation-result-003",
        )
        val error = createError(traceId)

        val result = VerificationEvaluationResult.create(
            traceId = traceId,
            status = VerificationEvaluationStatus.FAILED,
            error = error,
        )

        assertEquals(
            VerificationEvaluationStatus.FAILED,
            result.status,
        )
        assertNull(result.request)
        assertEquals(error, result.error)
    }

    @Test
    fun `create rejects verified result without request`() {
        assertFailsWith<IllegalArgumentException> {
            VerificationEvaluationResult.create(
                traceId = TraceId.from(
                    "trace-verification-evaluation-result-004",
                ),
                status = VerificationEvaluationStatus.VERIFIED,
            )
        }
    }

    @Test
    fun `create rejects verified request from a different trace`() {
        assertFailsWith<IllegalArgumentException> {
            VerificationEvaluationResult.create(
                traceId = TraceId.from(
                    "trace-verification-evaluation-result-005",
                ),
                status = VerificationEvaluationStatus.VERIFIED,
                request = createRequest(
                    TraceId.from(
                        "trace-verification-evaluation-request-other",
                    ),
                ),
            )
        }
    }

    @Test
    fun `create rejects unavailable result with request`() {
        val traceId = TraceId.from(
            "trace-verification-evaluation-result-006",
        )

        assertFailsWith<IllegalArgumentException> {
            VerificationEvaluationResult.create(
                traceId = traceId,
                status = VerificationEvaluationStatus.UNAVAILABLE,
                request = createRequest(traceId),
            )
        }
    }

    @Test
    fun `create rejects failed result without error`() {
        assertFailsWith<IllegalArgumentException> {
            VerificationEvaluationResult.create(
                traceId = TraceId.from(
                    "trace-verification-evaluation-result-007",
                ),
                status = VerificationEvaluationStatus.FAILED,
            )
        }
    }

    @Test
    fun `create rejects failed error from a different trace`() {
        assertFailsWith<IllegalArgumentException> {
            VerificationEvaluationResult.create(
                traceId = TraceId.from(
                    "trace-verification-evaluation-result-008",
                ),
                status = VerificationEvaluationStatus.FAILED,
                error = createError(
                    TraceId.from(
                        "trace-verification-evaluation-error-other",
                    ),
                ),
            )
        }
    }

    private fun createRequest(
        traceId: TraceId,
    ): VerificationRequest {
        return VerificationRequest.create(
            observation = ObservationRequest.create(
                execution = ExecutionRequest.create(
                    plan = createPlan(traceId),
                    capability = createCapability(),
                ),
            ),
        )
    }

    private fun createCapability(): CapabilityContract {
        return CapabilityContract.create(
            capabilityId = CapabilityId.from(
                "capability-camera",
            ),
            category = CapabilityCategory.ACTION,
            name = "Camera",
            description =
                "Performs one bounded registered camera action.",
        )
    }

    private fun createPlan(
        traceId: TraceId,
    ): PlanRecord {
        return PlanRecord.create(
            planId = PlanId.from(
                "plan-verification-evaluation-result",
            ),
            task = TaskRecord.create(
                taskId = TaskId.from(
                    "task-verification-evaluation-result",
                ),
                decision = DecisionRecord.create(
                    understanding =
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
                                        DevilTimestamp
                                            .fromEpochMilliseconds(
                                                1_754_000_122_000L,
                                            ),
                                ),
                            state =
                                UnderstandingState.COMPLETE,
                            summary =
                                "Bounded understanding was produced.",
                        ),
                    state = DecisionState.SELECTED,
                    summary =
                        "A constitutional decision was selected.",
                ),
                state = TaskState.CREATED,
                summary =
                    "A bounded constitutional task was created.",
            ),
            state = PlanState.CREATED,
            summary =
                "Use the constitutionally approved capability path.",
        )
    }

    private fun createError(
        traceId: TraceId,
    ): UniversalErrorRecord {
        return UniversalErrorRecord.create(
            errorCode = ErrorCode.from(
                "VERIFICATION_EVALUATION_FAILED",
            ),
            traceId = traceId,
            occurredAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_000_122_500L,
                ),
            summary =
                "Verification evaluation failed.",
        )
    }
}
