package com.devil.core.runtime.executive

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
import com.devil.core.model.executive.ExecutiveReadinessRequest
import com.devil.core.model.plan.PlanId
import com.devil.core.model.plan.PlanRecord
import com.devil.core.model.plan.PlanState
import com.devil.core.model.task.TaskId
import com.devil.core.model.task.TaskRecord
import com.devil.core.model.task.TaskState
import com.devil.core.model.understanding.UnderstandingRecord
import com.devil.core.model.understanding.UnderstandingState
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class ExecutiveReadinessRequestResultTest {

    @Test
    fun `create preserves available result with matching request`() {
        val traceId = TraceId.from(
            "trace-executive-request-result-001",
        )
        val request = createRequest(traceId)

        val result = ExecutiveReadinessRequestResult.create(
            traceId = traceId,
            status = ExecutiveReadinessRequestStatus.AVAILABLE,
            request = request,
        )

        assertEquals(traceId, result.traceId)
        assertEquals(
            ExecutiveReadinessRequestStatus.AVAILABLE,
            result.status,
        )
        assertEquals(request, result.request)
        assertNull(result.error)
    }

    @Test
    fun `create preserves unavailable result`() {
        val traceId = TraceId.from(
            "trace-executive-request-result-002",
        )

        val result = ExecutiveReadinessRequestResult.create(
            traceId = traceId,
            status = ExecutiveReadinessRequestStatus.UNAVAILABLE,
        )

        assertEquals(
            ExecutiveReadinessRequestStatus.UNAVAILABLE,
            result.status,
        )
        assertNull(result.request)
        assertNull(result.error)
    }

    @Test
    fun `create preserves failed result with matching error`() {
        val traceId = TraceId.from(
            "trace-executive-request-result-003",
        )
        val error = createError(traceId)

        val result = ExecutiveReadinessRequestResult.create(
            traceId = traceId,
            status = ExecutiveReadinessRequestStatus.FAILED,
            error = error,
        )

        assertEquals(
            ExecutiveReadinessRequestStatus.FAILED,
            result.status,
        )
        assertNull(result.request)
        assertEquals(error, result.error)
    }

    @Test
    fun `create rejects available result without request`() {
        assertFailsWith<IllegalArgumentException> {
            ExecutiveReadinessRequestResult.create(
                traceId = TraceId.from(
                    "trace-executive-request-result-004",
                ),
                status = ExecutiveReadinessRequestStatus.AVAILABLE,
            )
        }
    }

    @Test
    fun `create rejects request from a different trace`() {
        assertFailsWith<IllegalArgumentException> {
            ExecutiveReadinessRequestResult.create(
                traceId = TraceId.from(
                    "trace-executive-request-result-005",
                ),
                status = ExecutiveReadinessRequestStatus.AVAILABLE,
                request = createRequest(
                    TraceId.from(
                        "trace-executive-request-other",
                    ),
                ),
            )
        }
    }

    @Test
    fun `create rejects failed result without error`() {
        assertFailsWith<IllegalArgumentException> {
            ExecutiveReadinessRequestResult.create(
                traceId = TraceId.from(
                    "trace-executive-request-result-006",
                ),
                status = ExecutiveReadinessRequestStatus.FAILED,
            )
        }
    }

    @Test
    fun `create rejects failed error from a different trace`() {
        assertFailsWith<IllegalArgumentException> {
            ExecutiveReadinessRequestResult.create(
                traceId = TraceId.from(
                    "trace-executive-request-result-007",
                ),
                status = ExecutiveReadinessRequestStatus.FAILED,
                error = createError(
                    TraceId.from(
                        "trace-executive-request-error-other",
                    ),
                ),
            )
        }
    }

    private fun createRequest(
        traceId: TraceId,
    ): ExecutiveReadinessRequest {
        return ExecutiveReadinessRequest.create(
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

    private fun createPlan(traceId: TraceId): PlanRecord {
        return PlanRecord.create(
            planId = PlanId.from(
                "plan-executive-request-result",
            ),
            task = TaskRecord.create(
                taskId = TaskId.from(
                    "task-executive-request-result",
                ),
                decision = DecisionRecord.create(
                    understanding = UnderstandingRecord.create(
                        context = ContextEnvelope.create(
                            traceId = traceId,
                            schemaVersion = SchemaVersion.from(1),
                            source = ContextSource.TEXT,
                            trustLevel =
                                ContextTrustLevel.VERIFIED,
                            securityLevel =
                                ContextSecurityLevel.RESTRICTED,
                            observedAt =
                                DevilTimestamp.fromEpochMilliseconds(
                                    1_754_000_095_000L,
                                ),
                        ),
                        state = UnderstandingState.COMPLETE,
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
                "EXECUTIVE_READINESS_REQUEST_FAILED",
            ),
            traceId = traceId,
            occurredAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_000_095_500L,
                ),
            summary =
                "Executive readiness request preparation failed.",
        )
    }
}
