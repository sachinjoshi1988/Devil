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
import com.devil.core.model.task.TaskId
import com.devil.core.model.task.TaskRecord
import com.devil.core.model.task.TaskState
import com.devil.core.model.understanding.UnderstandingRecord
import com.devil.core.model.understanding.UnderstandingState
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class DefaultTaskCreationResultMapperTest {

    @Test
    fun `map preserves created task as operationally created`() {
        val traceId = TraceId.from(
            "trace-task-result-mapper-001",
        )
        val task = createTask(
            traceId = traceId,
            state = TaskState.CREATED,
        )
        val mapper: TaskCreationResultMapper =
            DefaultTaskCreationResultMapper()

        val result = mapper.map(
            traceId = traceId,
            task = task,
        )

        assertEquals(traceId, result.traceId)
        assertEquals(
            TaskAuthorityStatus.CREATED,
            result.status,
        )
        assertEquals(task, result.task)
        assertEquals(
            TaskState.CREATED,
            result.task?.state,
        )
        assertNull(result.error)
    }

    @Test
    fun `map preserves waiting task lifecycle state`() {
        val traceId = TraceId.from(
            "trace-task-result-mapper-002",
        )

        val result = DefaultTaskCreationResultMapper().map(
            traceId = traceId,
            task = createTask(
                traceId = traceId,
                state = TaskState.WAITING,
            ),
        )

        assertEquals(
            TaskAuthorityStatus.CREATED,
            result.status,
        )
        assertEquals(
            TaskState.WAITING,
            result.task?.state,
        )
        assertNull(result.error)
    }

    @Test
    fun `map preserves active task lifecycle state`() {
        val traceId = TraceId.from(
            "trace-task-result-mapper-003",
        )

        val result = DefaultTaskCreationResultMapper().map(
            traceId = traceId,
            task = createTask(
                traceId = traceId,
                state = TaskState.ACTIVE,
            ),
        )

        assertEquals(
            TaskAuthorityStatus.CREATED,
            result.status,
        )
        assertEquals(
            TaskState.ACTIVE,
            result.task?.state,
        )
    }

    @Test
    fun `map rejects task from a different trace`() {
        assertFailsWith<IllegalArgumentException> {
            DefaultTaskCreationResultMapper().map(
                traceId = TraceId.from(
                    "trace-task-result-mapper-004",
                ),
                task = createTask(
                    traceId = TraceId.from(
                        "trace-task-record-other",
                    ),
                    state = TaskState.CREATED,
                ),
            )
        }
    }

    private fun createTask(
        traceId: TraceId,
        state: TaskState,
    ): TaskRecord {
        return TaskRecord.create(
            taskId = TaskId.from(
                "task-result-mapper",
            ),
            decision = DecisionRecord.create(
                understanding = UnderstandingRecord.create(
                    context = ContextEnvelope.create(
                        traceId = traceId,
                        schemaVersion = SchemaVersion.from(1),
                        source = ContextSource.TEST,
                        trustLevel =
                            ContextTrustLevel.VERIFIED,
                        securityLevel =
                            ContextSecurityLevel.RESTRICTED,
                        observedAt =
                            DevilTimestamp.fromEpochMilliseconds(
                                1_754_000_083_000L,
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
            state = state,
            summary =
                "Bounded constitutional task was created.",
        )
    }
}
