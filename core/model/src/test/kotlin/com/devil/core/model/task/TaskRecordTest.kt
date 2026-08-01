package com.devil.core.model.task

import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.common.SchemaVersion
import com.devil.core.model.common.TraceId
import com.devil.core.model.context.ContextEnvelope
import com.devil.core.model.context.ContextSecurityLevel
import com.devil.core.model.context.ContextSource
import com.devil.core.model.context.ContextTrustLevel
import com.devil.core.model.decision.DecisionRecord
import com.devil.core.model.decision.DecisionState
import com.devil.core.model.understanding.UnderstandingRecord
import com.devil.core.model.understanding.UnderstandingState
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class TaskRecordTest {

    @Test
    fun `create preserves and normalizes task`() {
        val taskId = TaskId.from("task-001")
        val decision = createDecision()

        val task = TaskRecord.create(
            taskId = taskId,
            decision = decision,
            state = TaskState.CREATED,
            summary = "  Open the camera application.  ",
        )

        assertEquals(taskId, task.taskId)
        assertEquals(decision, task.decision)
        assertEquals(TaskState.CREATED, task.state)
        assertEquals("Open the camera application.", task.summary)
    }

    @Test
    fun `create rejects blank summary`() {
        assertFailsWith<IllegalArgumentException> {
            TaskRecord.create(
                taskId = TaskId.from("task-002"),
                decision = createDecision(),
                state = TaskState.WAITING,
                summary = "   ",
            )
        }
    }

    private fun createDecision(): DecisionRecord {
        val understanding = UnderstandingRecord.create(
            context = ContextEnvelope.create(
                traceId = TraceId.from("trace-task-001"),
                schemaVersion = SchemaVersion.from(1),
                source = ContextSource.TEXT,
                trustLevel = ContextTrustLevel.VERIFIED,
                securityLevel = ContextSecurityLevel.RESTRICTED,
                observedAt = DevilTimestamp.fromEpochMilliseconds(1_754_000_000_000L),
            ),
            state = UnderstandingState.COMPLETE,
            summary = "Open the camera application.",
        )

        return DecisionRecord.create(
            understanding = understanding,
            state = DecisionState.SELECTED,
            summary = "Open the camera application.",
        )
    }
}
