package com.devil.core.runtime.outcome

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
import com.devil.core.model.outcome.OutcomeRequest
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

class OutcomeRequestResultTest {

    @Test
    fun `create preserves available result with matching request`() {
        val traceId = TraceId.from(
            "trace-outcome-request-result-001",
        )
        val request = createRequest(traceId)

        val result = OutcomeRequestResult.create(
            traceId = traceId,
            status = OutcomeRequestStatus.AVAILABLE,
            request = request,
        )

        assertEquals(traceId, result.traceId)
        assertEquals(
            OutcomeRequestStatus.AVAILABLE,
            result.status,
        )
        assertEquals(request, result.request)
        assertNull(result.error)
    }

    @Test
    fun `create preserves unavailable result`() {
        val traceId = TraceId.from(
            "trace-outcome-request-result-002",
        )

        val result = OutcomeRequestResult.create(
            traceId = traceId,
            status = OutcomeRequestStatus.UNAVAILABLE,
        )

        assertEquals(
            OutcomeRequestStatus.UNAVAILABLE,
            result.status,
        )
        assertNull(result.request)
        assertNull(result.error)
    }

    @Test
    fun `create preserves failed result with matching error`() {
        val traceId = TraceId.from(
            "trace-outcome-request-result-003",
        )
        val error = createError(traceId)

        val result = OutcomeRequestResult.create(
            traceId = traceId,
            status = OutcomeRequestStatus.FAILED,
            error = error,
        )

        assertEquals(
            OutcomeRequestStatus.FAILED,
            result.status,
        )
        assertNull(result.request)
        assertEquals(error, result.error)
    }

    @Test
    fun `create rejects available result without request`() {
        assertFailsWith<IllegalArgumentException> {
            OutcomeRequestResult.create(
                traceId = TraceId.from(
                    "trace-outcome-request-result-004",
                ),
                status = OutcomeRequestStatus.AVAILABLE,
            )
        }
    }

    @Test
    fun `create rejects request from a different trace`() {
        assertFailsWith<IllegalArgumentException> {
            OutcomeRequestResult.create(
                traceId = TraceId.from(
                    "trace-outcome-request-result-005",
                ),
                status = OutcomeRequestStatus.AVAILABLE,
                request = createRequest(
                    TraceId.from(
                        "trace-outcome-request-other",
                    ),
                ),
            )
        }
    }

    @Test
    fun `create rejects unavailable result with request`() {
        val traceId = TraceId.from(
            "trace-outcome-request-result-006",
        )

        assertFailsWith<IllegalArgumentException> {
            OutcomeRequestResult.create(
                traceId = traceId,
                status = OutcomeRequestStatus.UNAVAILABLE,
                request = createRequest(traceId),
            )
        }
    }

    @Test
    fun `create rejects failed result without error`() {
        assertFailsWith<IllegalArgumentException> {
            OutcomeRequestResult.create(
                traceId = TraceId.from(
                    "trace-outcome-request-result-007",
                ),
                status = OutcomeRequestStatus.FAILED,
            )
        }
    }

    @Test
    fun `create rejects failed error from a different trace`() {
        assertFailsWith<IllegalArgumentException> {
            OutcomeRequestResult.create(
                traceId = TraceId.from(
                    "trace-outcome-request-result-008",
                ),
                status = OutcomeRequestStatus.FAILED,
                error = createError(
                    TraceId.from(
                        "trace-outcome-request-error-other",
                    ),
                ),
            )
        }
    }

    private fun createRequest(
        traceId: TraceId,
    ): OutcomeRequest {
        return OutcomeRequest.create(
            verification = VerificationRequest.create(
                observation = ObservationRequest.create(
                    execution = createExecutionRequest(traceId),
                ),
            ),
        )
    }

    private fun createExecutionRequest(
        traceId: TraceId,
    ): ExecutionRequest {
        return ExecutionRequest.create(
            plan = createPlan(traceId),
            capability = createCapability(),
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
                "plan-outcome-request-result",
            ),
            task = TaskRecord.create(
                taskId = TaskId.from(
                    "task-outcome-request-result",
                ),
                decision = DecisionRecord.create(
                    understanding =
                        UnderstandingRecord.create(
                            context = ContextEnvelope.create(
                                traceId = traceId,
                                schemaVersion =
                                    SchemaVersion.from(1),
                                source = ContextSource.TEXT,
                                trustLevel =
                                    ContextTrustLevel.VERIFIED,
                                securityLevel =
                                    ContextSecurityLevel.RESTRICTED,
                                observedAt =
                                    DevilTimestamp
                                        .fromEpochMilliseconds(
                                            1_754_000_129_000L,
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
                "OUTCOME_REQUEST_FAILED",
            ),
            traceId = traceId,
            occurredAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_000_129_500L,
                ),
            summary =
                "Outcome request preparation failed.",
        )
    }
}
