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
import com.devil.core.model.error.ErrorCode
import com.devil.core.model.error.UniversalErrorRecord
import com.devil.core.model.task.TaskCreationRequest
import com.devil.core.model.understanding.UnderstandingRecord
import com.devil.core.model.understanding.UnderstandingState
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class TaskCreationRequestResultTest {

    @Test
    fun `create preserves available result with matching request`() {
        val traceId = TraceId.from(
            "trace-task-request-result-001",
        )
        val request = createRequest(traceId)

        val result = TaskCreationRequestResult.create(
            traceId = traceId,
            status = TaskCreationRequestStatus.AVAILABLE,
            request = request,
        )

        assertEquals(traceId, result.traceId)
        assertEquals(
            TaskCreationRequestStatus.AVAILABLE,
            result.status,
        )
        assertEquals(request, result.request)
        assertNull(result.error)
    }

    @Test
    fun `create preserves unavailable result without request or error`() {
        val traceId = TraceId.from(
            "trace-task-request-result-002",
        )

        val result = TaskCreationRequestResult.create(
            traceId = traceId,
            status = TaskCreationRequestStatus.UNAVAILABLE,
        )

        assertEquals(traceId, result.traceId)
        assertEquals(
            TaskCreationRequestStatus.UNAVAILABLE,
            result.status,
        )
        assertNull(result.request)
        assertNull(result.error)
    }

    @Test
    fun `create preserves failed result with matching error`() {
        val traceId = TraceId.from(
            "trace-task-request-result-003",
        )
        val error = createError(traceId)

        val result = TaskCreationRequestResult.create(
            traceId = traceId,
            status = TaskCreationRequestStatus.FAILED,
            error = error,
        )

        assertEquals(
            TaskCreationRequestStatus.FAILED,
            result.status,
        )
        assertNull(result.request)
        assertEquals(error, result.error)
    }

    @Test
    fun `create rejects available result without request`() {
        assertFailsWith<IllegalArgumentException> {
            TaskCreationRequestResult.create(
                traceId = TraceId.from(
                    "trace-task-request-result-004",
                ),
                status = TaskCreationRequestStatus.AVAILABLE,
            )
        }
    }

    @Test
    fun `create rejects available request from a different trace`() {
        assertFailsWith<IllegalArgumentException> {
            TaskCreationRequestResult.create(
                traceId = TraceId.from(
                    "trace-task-request-result-005",
                ),
                status = TaskCreationRequestStatus.AVAILABLE,
                request = createRequest(
                    TraceId.from(
                        "trace-task-request-other",
                    ),
                ),
            )
        }
    }

    @Test
    fun `create rejects unavailable result with request`() {
        val traceId = TraceId.from(
            "trace-task-request-result-006",
        )

        assertFailsWith<IllegalArgumentException> {
            TaskCreationRequestResult.create(
                traceId = traceId,
                status = TaskCreationRequestStatus.UNAVAILABLE,
                request = createRequest(traceId),
            )
        }
    }

    @Test
    fun `create rejects failed result without error`() {
        assertFailsWith<IllegalArgumentException> {
            TaskCreationRequestResult.create(
                traceId = TraceId.from(
                    "trace-task-request-result-007",
                ),
                status = TaskCreationRequestStatus.FAILED,
            )
        }
    }

    @Test
    fun `create rejects failed error from a different trace`() {
        assertFailsWith<IllegalArgumentException> {
            TaskCreationRequestResult.create(
                traceId = TraceId.from(
                    "trace-task-request-result-008",
                ),
                status = TaskCreationRequestStatus.FAILED,
                error = createError(
                    TraceId.from(
                        "trace-task-request-error-other",
                    ),
                ),
            )
        }
    }

    private fun createRequest(
        traceId: TraceId,
    ): TaskCreationRequest {
        return TaskCreationRequest.create(
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
                                1_754_000_078_000L,
                            ),
                    ),
                    state = UnderstandingState.COMPLETE,
                    summary =
                        "Bounded understanding was produced.",
                ),
                state = DecisionState.SELECTED,
                summary =
                    "Bounded constitutional decision was selected.",
            ),
        )
    }

    private fun createError(
        traceId: TraceId,
    ): UniversalErrorRecord {
        return UniversalErrorRecord.create(
            errorCode = ErrorCode.from(
                "TASK_CREATION_REQUEST_FAILED",
            ),
            traceId = traceId,
            occurredAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_000_078_500L,
                ),
            summary =
                "Task creation request preparation failed.",
        )
    }
}
