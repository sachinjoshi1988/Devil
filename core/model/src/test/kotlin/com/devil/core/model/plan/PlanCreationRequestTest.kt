package com.devil.core.model.plan

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

class PlanCreationRequestTest {

    @Test
    fun `create preserves originating task`() {
        val task = createTask()

        val request = PlanCreationRequest.create(
            task = task,
        )

        assertEquals(task, request.task)
    }

    private fun createTask(): TaskRecord {
        return TaskRecord.create(
            taskId = TaskId.from("task-plan-request-001"),
            decision = createDecision(),
            state = TaskState.CREATED,
            summary = "Open the camera application.",
        )
    }

    private fun createDecision(): DecisionRecord {
        return DecisionRecord.create(
            understanding = createUnderstanding(),
            state = DecisionState.SELECTED,
            summary = "Open the camera application.",
        )
    }

    private fun createUnderstanding(): UnderstandingRecord {
        return UnderstandingRecord.create(
            context = ContextEnvelope.create(
                traceId = TraceId.from("trace-plan-request-001"),
                schemaVersion = SchemaVersion.from(1),
                source = ContextSource.TEST,
                trustLevel = ContextTrustLevel.VERIFIED,
                securityLevel = ContextSecurityLevel.RESTRICTED,
                observedAt = DevilTimestamp.fromEpochMilliseconds(
                    1_754_000_022_000L,
                ),
            ),
            state = UnderstandingState.COMPLETE,
            summary = "Open the camera application.",
        )
    }
}
