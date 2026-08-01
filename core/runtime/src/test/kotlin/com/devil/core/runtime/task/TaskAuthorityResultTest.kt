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
import com.devil.core.model.task.TaskId
import com.devil.core.model.task.TaskRecord
import com.devil.core.model.task.TaskState
import com.devil.core.model.understanding.UnderstandingRecord
import com.devil.core.model.understanding.UnderstandingState
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class TaskAuthorityResultTest {

    @Test
    fun `create preserves created result with matching task`() {
        val decision = createDecision("trace-task-authority-001")
        val task = createTask(decision)

        val result = TaskAuthorityResult.create(
            traceId = decision.understanding.context.traceId,
            status = TaskAuthorityStatus.CREATED,
            task = task,
        )

        assertEquals(decision.understanding.context.traceId, result.traceId)
        assertEquals(TaskAuthorityStatus.CREATED, result.status)
        assertEquals(task, result.task)
        assertNull(result.error)
    }

    @Test
    fun `create preserves deferred result without task or error`() {
        val traceId = TraceId.from("trace-task-authority-002")

        val result = TaskAuthorityResult.create(
            traceId = traceId,
            status = TaskAuthorityStatus.DEFERRED,
        )

        assertEquals(traceId, result.traceId)
        assertEquals(TaskAuthorityStatus.DEFERRED, result.status)
        assertNull(result.task)
        assertNull(result.error)
    }

    @Test
    fun `create preserves failed result with matching error`() {
        val traceId = TraceId.from("trace-task-authority-003")
        val error = createError(traceId)

        val result = TaskAuthorityResult.create(
            traceId = traceId,
            status = TaskAuthorityStatus.FAILED,
            error = error,
        )

        assertEquals(traceId, result.traceId)
        assertEquals(TaskAuthorityStatus.FAILED, result.status)
        assertNull(result.task)
        assertEquals(error, result.error)
    }

    @Test
    fun `create rejects created result without task`() {
        assertFailsWith<IllegalArgumentException> {
            TaskAuthorityResult.create(
                traceId = TraceId.from("trace-task-authority-004"),
                status = TaskAuthorityStatus.CREATED,
            )
        }
    }

    @Test
    fun `create rejects deferred result with task`() {
        val decision = createDecision("trace-task-authority-005")

        assertFailsWith<IllegalArgumentException> {
            TaskAuthorityResult.create(
                traceId = decision.understanding.context.traceId,
                status = TaskAuthorityStatus.DEFERRED,
                task = createTask(decision),
            )
        }
    }

    @Test
    fun `create rejects failed result without error`() {
        assertFailsWith<IllegalArgumentException> {
            TaskAuthorityResult.create(
                traceId = TraceId.from("trace-task-authority-006"),
                status = TaskAuthorityStatus.FAILED,
            )
        }
    }

    @Test
    fun `create rejects task from a different trace`() {
        val decision = createDecision("trace-task-authority-task-other")

        assertFailsWith<IllegalArgumentException> {
            TaskAuthorityResult.create(
                traceId = TraceId.from("trace-task-authority-007"),
                status = TaskAuthorityStatus.CREATED,
                task = createTask(decision),
            )
        }
    }

    @Test
    fun `create rejects error from a different trace`() {
        assertFailsWith<IllegalArgumentException> {
            TaskAuthorityResult.create(
                traceId = TraceId.from("trace-task-authority-008"),
                status = TaskAuthorityStatus.FAILED,
                error = createError(
                    TraceId.from("trace-task-authority-error-other"),
                ),
            )
        }
    }

    private fun createTask(
        decision: DecisionRecord,
    ): TaskRecord {
        return TaskRecord.create(
            taskId = TaskId.from("task-authority-001"),
            decision = decision,
            state = TaskState.CREATED,
            summary = "Open the camera application.",
        )
    }

    private fun createDecision(
        traceValue: String,
    ): DecisionRecord {
        return DecisionRecord.create(
            understanding = createUnderstanding(traceValue),
            state = DecisionState.SELECTED,
            summary = "Open the camera application.",
        )
    }

    private fun createUnderstanding(
        traceValue: String,
    ): UnderstandingRecord {
        return UnderstandingRecord.create(
            context = createContext(traceValue),
            state = UnderstandingState.COMPLETE,
            summary = "Open the camera application.",
        )
    }

    private fun createContext(traceValue: String): ContextEnvelope {
        return ContextEnvelope.create(
            traceId = TraceId.from(traceValue),
            schemaVersion = SchemaVersion.from(1),
            source = ContextSource.TEST,
            trustLevel = ContextTrustLevel.VERIFIED,
            securityLevel = ContextSecurityLevel.RESTRICTED,
            observedAt = DevilTimestamp.fromEpochMilliseconds(1_754_000_017_000L),
        )
    }

    private fun createError(traceId: TraceId): UniversalErrorRecord {
        return UniversalErrorRecord.create(
            errorCode = ErrorCode.from("TASK_CREATION_FAILED"),
            traceId = traceId,
            occurredAt = DevilTimestamp.fromEpochMilliseconds(1_754_000_017_500L),
            summary = "Task creation failed.",
        )
    }
}
