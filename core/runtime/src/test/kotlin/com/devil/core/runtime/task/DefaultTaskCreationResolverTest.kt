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
import com.devil.core.model.task.TaskCreationRequest
import com.devil.core.model.task.TaskId
import com.devil.core.model.task.TaskState
import com.devil.core.model.understanding.UnderstandingRecord
import com.devil.core.model.understanding.UnderstandingState
import kotlin.test.Test
import kotlin.test.assertEquals

class DefaultTaskCreationResolverTest {

    @Test
    fun `create preserves task identity and selected decision`() {
        val request = createRequest(
            summary = "Open the camera application.",
        )
        val taskId = TaskId.from(
            "task-default-creation-resolver-001",
        )
        val resolver: TaskCreationResolver =
            DefaultTaskCreationResolver()

        val task = resolver.create(
            request = request,
            taskId = taskId,
        )

        assertEquals(taskId, task.taskId)
        assertEquals(request.decision, task.decision)
        assertEquals(TaskState.CREATED, task.state)
        assertEquals(
            "Open the camera application.",
            task.summary,
        )
    }

    @Test
    fun `create does not reinterpret selected decision summary`() {
        val request = createRequest(
            summary =
                "Send the prepared message after confirmation.",
        )

        val task = DefaultTaskCreationResolver().create(
            request = request,
            taskId = TaskId.from(
                "task-default-creation-resolver-002",
            ),
        )

        assertEquals(
            DecisionState.SELECTED,
            task.decision.state,
        )
        assertEquals(
            request.decision.summary,
            task.summary,
        )
    }

    @Test
    fun `create always begins in created lifecycle state`() {
        val task = DefaultTaskCreationResolver().create(
            request = createRequest(
                summary = "Open the settings application.",
            ),
            taskId = TaskId.from(
                "task-default-creation-resolver-003",
            ),
        )

        assertEquals(TaskState.CREATED, task.state)
    }

    private fun createRequest(
        summary: String,
    ): TaskCreationRequest {
        return TaskCreationRequest.create(
            decision = DecisionRecord.create(
                understanding = UnderstandingRecord.create(
                    context = ContextEnvelope.create(
                        traceId = TraceId.from(
                            "trace-default-task-creation-resolver-001",
                        ),
                        schemaVersion = SchemaVersion.from(1),
                        source = ContextSource.TEXT,
                        trustLevel =
                            ContextTrustLevel.VERIFIED,
                        securityLevel =
                            ContextSecurityLevel.RESTRICTED,
                        observedAt =
                            DevilTimestamp.fromEpochMilliseconds(
                                1_754_000_082_000L,
                            ),
                    ),
                    state = UnderstandingState.COMPLETE,
                    summary =
                        "Bounded understanding was produced.",
                ),
                state = DecisionState.SELECTED,
                summary = summary,
            ),
        )
    }
}
