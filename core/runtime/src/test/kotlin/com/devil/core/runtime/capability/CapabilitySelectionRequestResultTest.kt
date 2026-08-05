package com.devil.core.runtime.capability

import com.devil.core.model.capability.CapabilitySelectionRequest
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

class CapabilitySelectionRequestResultTest {

    @Test
    fun `create preserves available result with matching request`() {
        val traceId = TraceId.from(
            "trace-capability-request-result-001",
        )
        val request = createRequest(traceId)

        val result = CapabilitySelectionRequestResult.create(
            traceId = traceId,
            status = CapabilitySelectionRequestStatus.AVAILABLE,
            request = request,
        )

        assertEquals(traceId, result.traceId)
        assertEquals(
            CapabilitySelectionRequestStatus.AVAILABLE,
            result.status,
        )
        assertEquals(request, result.request)
        assertNull(result.error)
    }

    @Test
    fun `create preserves unavailable result without request or error`() {
        val traceId = TraceId.from(
            "trace-capability-request-result-002",
        )

        val result = CapabilitySelectionRequestResult.create(
            traceId = traceId,
            status = CapabilitySelectionRequestStatus.UNAVAILABLE,
        )

        assertEquals(traceId, result.traceId)
        assertEquals(
            CapabilitySelectionRequestStatus.UNAVAILABLE,
            result.status,
        )
        assertNull(result.request)
        assertNull(result.error)
    }

    @Test
    fun `create preserves failed result with matching error`() {
        val traceId = TraceId.from(
            "trace-capability-request-result-003",
        )
        val error = createError(traceId)

        val result = CapabilitySelectionRequestResult.create(
            traceId = traceId,
            status = CapabilitySelectionRequestStatus.FAILED,
            error = error,
        )

        assertEquals(
            CapabilitySelectionRequestStatus.FAILED,
            result.status,
        )
        assertNull(result.request)
        assertEquals(error, result.error)
    }

    @Test
    fun `create rejects available result without request`() {
        assertFailsWith<IllegalArgumentException> {
            CapabilitySelectionRequestResult.create(
                traceId = TraceId.from(
                    "trace-capability-request-result-004",
                ),
                status = CapabilitySelectionRequestStatus.AVAILABLE,
            )
        }
    }

    @Test
    fun `create rejects available request from a different trace`() {
        assertFailsWith<IllegalArgumentException> {
            CapabilitySelectionRequestResult.create(
                traceId = TraceId.from(
                    "trace-capability-request-result-005",
                ),
                status = CapabilitySelectionRequestStatus.AVAILABLE,
                request = createRequest(
                    TraceId.from(
                        "trace-capability-request-other",
                    ),
                ),
            )
        }
    }

    @Test
    fun `create rejects unavailable result with request`() {
        val traceId = TraceId.from(
            "trace-capability-request-result-006",
        )

        assertFailsWith<IllegalArgumentException> {
            CapabilitySelectionRequestResult.create(
                traceId = traceId,
                status = CapabilitySelectionRequestStatus.UNAVAILABLE,
                request = createRequest(traceId),
            )
        }
    }

    @Test
    fun `create rejects failed result without error`() {
        assertFailsWith<IllegalArgumentException> {
            CapabilitySelectionRequestResult.create(
                traceId = TraceId.from(
                    "trace-capability-request-result-007",
                ),
                status = CapabilitySelectionRequestStatus.FAILED,
            )
        }
    }

    @Test
    fun `create rejects failed error from a different trace`() {
        assertFailsWith<IllegalArgumentException> {
            CapabilitySelectionRequestResult.create(
                traceId = TraceId.from(
                    "trace-capability-request-result-008",
                ),
                status = CapabilitySelectionRequestStatus.FAILED,
                error = createError(
                    TraceId.from(
                        "trace-capability-request-error-other",
                    ),
                ),
            )
        }
    }

    private fun createRequest(
        traceId: TraceId,
    ): CapabilitySelectionRequest {
        return CapabilitySelectionRequest.create(
            plan = createPlan(
                traceId = traceId,
                state = PlanState.CREATED,
            ),
        )
    }

    private fun createPlan(
        traceId: TraceId,
        state: PlanState,
    ): PlanRecord {
        return PlanRecord.create(
            planId = PlanId.from(
                "plan-capability-request-result-001",
            ),
            task = TaskRecord.create(
                taskId = TaskId.from(
                    "task-capability-request-result-001",
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
                                DevilTimestamp
                                    .fromEpochMilliseconds(
                                        1_754_000_088_000L,
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
                summary = "A bounded task was created.",
            ),
            state = state,
            summary =
                "Use the constitutionally approved capability path.",
        )
    }

    private fun createError(
        traceId: TraceId,
    ): UniversalErrorRecord {
        return UniversalErrorRecord.create(
            errorCode = ErrorCode.from(
                "CAPABILITY_SELECTION_REQUEST_FAILED",
            ),
            traceId = traceId,
            occurredAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_000_088_500L,
                ),
            summary =
                "Capability selection request preparation failed.",
        )
    }
}
