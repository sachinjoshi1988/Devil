package com.devil.core.runtime.task

import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.common.TraceId
import com.devil.core.model.error.ErrorCode
import com.devil.core.model.error.UniversalErrorRecord
import com.devil.core.model.task.TaskId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class TaskIdentityProvisionResultTest {

    @Test
    fun `create preserves available result with task identity`() {
        val traceId = TraceId.from(
            "trace-task-identity-result-001",
        )
        val taskId = TaskId.from(
            "task-identity-result-001",
        )

        val result = TaskIdentityProvisionResult.create(
            traceId = traceId,
            status = TaskIdentityProvisionStatus.AVAILABLE,
            taskId = taskId,
        )

        assertEquals(traceId, result.traceId)
        assertEquals(
            TaskIdentityProvisionStatus.AVAILABLE,
            result.status,
        )
        assertEquals(taskId, result.taskId)
        assertNull(result.error)
    }

    @Test
    fun `create preserves unavailable result without identity or error`() {
        val traceId = TraceId.from(
            "trace-task-identity-result-002",
        )

        val result = TaskIdentityProvisionResult.create(
            traceId = traceId,
            status = TaskIdentityProvisionStatus.UNAVAILABLE,
        )

        assertEquals(traceId, result.traceId)
        assertEquals(
            TaskIdentityProvisionStatus.UNAVAILABLE,
            result.status,
        )
        assertNull(result.taskId)
        assertNull(result.error)
    }

    @Test
    fun `create preserves failed result with matching error`() {
        val traceId = TraceId.from(
            "trace-task-identity-result-003",
        )
        val error = createError(traceId)

        val result = TaskIdentityProvisionResult.create(
            traceId = traceId,
            status = TaskIdentityProvisionStatus.FAILED,
            error = error,
        )

        assertEquals(
            TaskIdentityProvisionStatus.FAILED,
            result.status,
        )
        assertNull(result.taskId)
        assertEquals(error, result.error)
    }

    @Test
    fun `create rejects available result without task identity`() {
        assertFailsWith<IllegalArgumentException> {
            TaskIdentityProvisionResult.create(
                traceId = TraceId.from(
                    "trace-task-identity-result-004",
                ),
                status = TaskIdentityProvisionStatus.AVAILABLE,
            )
        }
    }

    @Test
    fun `create rejects available result with error`() {
        val traceId = TraceId.from(
            "trace-task-identity-result-005",
        )

        assertFailsWith<IllegalArgumentException> {
            TaskIdentityProvisionResult.create(
                traceId = traceId,
                status = TaskIdentityProvisionStatus.AVAILABLE,
                taskId = TaskId.from(
                    "task-identity-result-005",
                ),
                error = createError(traceId),
            )
        }
    }

    @Test
    fun `create rejects unavailable result with task identity`() {
        assertFailsWith<IllegalArgumentException> {
            TaskIdentityProvisionResult.create(
                traceId = TraceId.from(
                    "trace-task-identity-result-006",
                ),
                status = TaskIdentityProvisionStatus.UNAVAILABLE,
                taskId = TaskId.from(
                    "task-identity-result-006",
                ),
            )
        }
    }

    @Test
    fun `create rejects failed result without error`() {
        assertFailsWith<IllegalArgumentException> {
            TaskIdentityProvisionResult.create(
                traceId = TraceId.from(
                    "trace-task-identity-result-007",
                ),
                status = TaskIdentityProvisionStatus.FAILED,
            )
        }
    }

    @Test
    fun `create rejects failed error from a different trace`() {
        assertFailsWith<IllegalArgumentException> {
            TaskIdentityProvisionResult.create(
                traceId = TraceId.from(
                    "trace-task-identity-result-008",
                ),
                status = TaskIdentityProvisionStatus.FAILED,
                error = createError(
                    TraceId.from(
                        "trace-task-identity-error-other",
                    ),
                ),
            )
        }
    }

    private fun createError(
        traceId: TraceId,
    ): UniversalErrorRecord {
        return UniversalErrorRecord.create(
            errorCode = ErrorCode.from(
                "TASK_IDENTITY_PROVISION_FAILED",
            ),
            traceId = traceId,
            occurredAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_000_080_500L,
                ),
            summary =
                "Task identity provision failed.",
        )
    }
}
