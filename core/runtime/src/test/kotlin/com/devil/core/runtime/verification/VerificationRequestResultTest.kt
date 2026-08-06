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

class VerificationRequestResultTest {

    @Test
    fun `create preserves available result with matching request`() {
        val traceId = TraceId.from(
            "trace-verification-request-result-001",
        )
        val request = createRequest(traceId)

        val result = VerificationRequestResult.create(
            traceId = traceId,
            status = VerificationRequestStatus.AVAILABLE,
            request = request,
        )

        assertEquals(traceId, result.traceId)
        assertEquals(
            VerificationRequestStatus.AVAILABLE,
            result.status,
        )
        assertEquals(request, result.request)
        assertNull(result.error)
    }

    @Test
    fun `create preserves unavailable result`() {
        val traceId = TraceId.from(
            "trace-verification-request-result-002",
        )

        val result = VerificationRequestResult.create(
            traceId = traceId,
            status = VerificationRequestStatus.UNAVAILABLE,
        )

        assertEquals(
            VerificationRequestStatus.UNAVAILABLE,
            result.status,
        )
        assertNull(result.request)
        assertNull(result.error)
    }

    @Test
    fun `create preserves failed result with matching error`() {
        val traceId = TraceId.from(
            "trace-verification-request-result-003",
        )
        val error = createError(traceId)

        val result = VerificationRequestResult.create(
            traceId = traceId,
            status = VerificationRequestStatus.FAILED,
            error = error,
        )

        assertEquals(
            VerificationRequestStatus.FAILED,
            result.status,
        )
        assertNull(result.request)
        assertEquals(error, result.error)
    }

    @Test
    fun `create rejects available result without request`() {
        assertFailsWith<IllegalArgumentException> {
            VerificationRequestResult.create(
                traceId = TraceId.from(
                    "trace-verification-request-result-004",
                ),
                status = VerificationRequestStatus.AVAILABLE,
            )
        }
    }

    @Test
    fun `create rejects request from a different trace`() {
        assertFailsWith<IllegalArgumentException> {
            VerificationRequestResult.create(
                traceId = TraceId.from(
                    "trace-verification-request-result-005",
                ),
                status = VerificationRequestStatus.AVAILABLE,
                request = createRequest(
                    TraceId.from(
                        "trace-verification-request-other",
                    ),
                ),
            )
        }
    }

    @Test
    fun `create rejects unavailable result with request`() {
        val traceId = TraceId.from(
            "trace-verification-request-result-006",
        )

        assertFailsWith<IllegalArgumentException> {
            VerificationRequestResult.create(
                traceId = traceId,
                status = VerificationRequestStatus.UNAVAILABLE,
                request = createRequest(traceId),
            )
        }
    }

    @Test
    fun `create rejects failed result without error`() {
        assertFailsWith<IllegalArgumentException> {
            VerificationRequestResult.create(
                traceId = TraceId.from(
                    "trace-verification-request-result-007",
                ),
                status = VerificationRequestStatus.FAILED,
            )
        }
    }

    @Test
    fun `create rejects failed error from a different trace`() {
        assertFailsWith<IllegalArgumentException> {
            VerificationRequestResult.create(
                traceId = TraceId.from(
                    "trace-verification-request-result-008",
                ),
                status = VerificationRequestStatus.FAILED,
                error = createError(
                    TraceId.from(
                        "trace-verification-request-error-other",
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
                execution = createExecutionRequest(traceId),
            ),
        )
    }

    private fun createExecutionRequest(
        traceId: TraceId,
    ): ExecutionRequest {
        return ExecutionRequest.create(
            plan = PlanRecord.create(
                planId = PlanId.from(
                    "plan-verification-request-result",
                ),
                task = TaskRecord.create(
                    taskId = TaskId.from(
                        "task-verification-request-result",
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
                                                    1_754_000_120_000L,
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
            ),
            capability = CapabilityContract.create(
                capabilityId = CapabilityId.from(
                    "capability-camera",
                ),
                category = CapabilityCategory.ACTION,
                name = "Camera",
                description =
                    "Performs one bounded registered camera action.",
            ),
        )
    }

    private fun createError(
        traceId: TraceId,
    ): UniversalErrorRecord {
        return UniversalErrorRecord.create(
            errorCode = ErrorCode.from(
                "VERIFICATION_REQUEST_FAILED",
            ),
            traceId = traceId,
            occurredAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_000_120_500L,
                ),
            summary =
                "Verification request preparation failed.",
        )
    }
}
