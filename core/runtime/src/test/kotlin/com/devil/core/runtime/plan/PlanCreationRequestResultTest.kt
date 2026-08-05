package com.devil.core.runtime.plan

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
import com.devil.core.model.plan.PlanCreationRequest
import com.devil.core.model.task.TaskId
import com.devil.core.model.task.TaskRecord
import com.devil.core.model.task.TaskState
import com.devil.core.model.understanding.UnderstandingRecord
import com.devil.core.model.understanding.UnderstandingState
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class PlanCreationRequestResultTest {

    @Test
    fun `create preserves available result with matching request`() {
        val traceId = TraceId.from("trace-plan-request-result-001")
        val request = createRequest(traceId)

        val result = PlanCreationRequestResult.create(
            traceId = traceId,
            status = PlanCreationRequestStatus.AVAILABLE,
            request = request,
        )

        assertEquals(traceId, result.traceId)
        assertEquals(PlanCreationRequestStatus.AVAILABLE, result.status)
        assertEquals(request, result.request)
        assertNull(result.error)
    }

    @Test
    fun `create preserves unavailable result without request or error`() {
        val traceId = TraceId.from("trace-plan-request-result-002")

        val result = PlanCreationRequestResult.create(
            traceId = traceId,
            status = PlanCreationRequestStatus.UNAVAILABLE,
        )

        assertEquals(traceId, result.traceId)
        assertEquals(PlanCreationRequestStatus.UNAVAILABLE, result.status)
        assertNull(result.request)
        assertNull(result.error)
    }

    @Test
    fun `create preserves failed result with matching error`() {
        val traceId = TraceId.from("trace-plan-request-result-003")
        val error = createError(traceId)

        val result = PlanCreationRequestResult.create(
            traceId = traceId,
            status = PlanCreationRequestStatus.FAILED,
            error = error,
        )

        assertEquals(PlanCreationRequestStatus.FAILED, result.status)
        assertNull(result.request)
        assertEquals(error, result.error)
    }

    @Test
    fun `create rejects available result without request`() {
        assertFailsWith<IllegalArgumentException> {
            PlanCreationRequestResult.create(
                traceId = TraceId.from("trace-plan-request-result-004"),
                status = PlanCreationRequestStatus.AVAILABLE,
            )
        }
    }

    @Test
    fun `create rejects available request from a different trace`() {
        assertFailsWith<IllegalArgumentException> {
            PlanCreationRequestResult.create(
                traceId = TraceId.from("trace-plan-request-result-005"),
                status = PlanCreationRequestStatus.AVAILABLE,
                request = createRequest(
                    TraceId.from("trace-plan-request-other"),
                ),
            )
        }
    }

    @Test
    fun `create rejects unavailable result with request`() {
        val traceId = TraceId.from("trace-plan-request-result-006")

        assertFailsWith<IllegalArgumentException> {
            PlanCreationRequestResult.create(
                traceId = traceId,
                status = PlanCreationRequestStatus.UNAVAILABLE,
                request = createRequest(traceId),
            )
        }
    }

    @Test
    fun `create rejects failed result without error`() {
        assertFailsWith<IllegalArgumentException> {
            PlanCreationRequestResult.create(
                traceId = TraceId.from("trace-plan-request-result-007"),
                status = PlanCreationRequestStatus.FAILED,
            )
        }
    }

    @Test
    fun `create rejects failed error from a different trace`() {
        assertFailsWith<IllegalArgumentException> {
            PlanCreationRequestResult.create(
                traceId = TraceId.from("trace-plan-request-result-008"),
                status = PlanCreationRequestStatus.FAILED,
                error = createError(
                    TraceId.from("trace-plan-request-error-other"),
                ),
            )
        }
    }

    private fun createRequest(
        traceId: TraceId,
    ): PlanCreationRequest {
        return PlanCreationRequest.create(
            task = createTask(
                traceId = traceId,
                state = TaskState.CREATED,
            ),
        )
    }

    private fun createTask(
        traceId: TraceId,
        state: TaskState,
    ): TaskRecord {
        return TaskRecord.create(
            taskId = TaskId.from("task-plan-request-result-001"),
            decision = DecisionRecord.create(
                understanding = UnderstandingRecord.create(
                    context = ContextEnvelope.create(
                        traceId = traceId,
                        schemaVersion = SchemaVersion.from(1),
                        source = ContextSource.TEXT,
                        trustLevel = ContextTrustLevel.VERIFIED,
                        securityLevel = ContextSecurityLevel.RESTRICTED,
                        observedAt =
                            DevilTimestamp.fromEpochMilliseconds(
                                1_754_000_080_000L,
                            ),
                    ),
                    state = UnderstandingState.COMPLETE,
                    summary = "Bounded understanding was produced.",
                ),
                state = DecisionState.SELECTED,
                summary = "A constitutional decision was selected.",
            ),
            state = state,
            summary = "A bounded task was created.",
        )
    }

    private fun createError(
        traceId: TraceId,
    ): UniversalErrorRecord {
        return UniversalErrorRecord.create(
            errorCode = ErrorCode.from(
                "PLAN_CREATION_REQUEST_FAILED",
            ),
            traceId = traceId,
            occurredAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_000_080_500L,
                ),
            summary = "Plan creation request preparation failed.",
        )
    }
}
