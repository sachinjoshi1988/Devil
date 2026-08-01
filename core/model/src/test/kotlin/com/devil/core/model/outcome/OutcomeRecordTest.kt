package com.devil.core.model.outcome

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

class OutcomeRecordTest {

    @Test
    fun `create preserves and normalizes verified outcome`() {
        val task = createTask()
        val verifiedAt = DevilTimestamp.fromEpochMilliseconds(1_754_000_001_000L)

        val outcome = OutcomeRecord.create(
            task = task,
            state = OutcomeState.VERIFIED_SUCCESS,
            verifiedAt = verifiedAt,
            summary = "  The camera application was verified as opened.  ",
        )

        assertEquals(task, outcome.task)
        assertEquals(OutcomeState.VERIFIED_SUCCESS, outcome.state)
        assertEquals(verifiedAt, outcome.verifiedAt)
        assertEquals(
            "The camera application was verified as opened.",
            outcome.summary,
        )
    }

    @Test
    fun `create rejects blank summary`() {
        assertFailsWith<IllegalArgumentException> {
            OutcomeRecord.create(
                task = createTask(),
                state = OutcomeState.INCONCLUSIVE,
                verifiedAt = DevilTimestamp.fromEpochMilliseconds(1_754_000_001_000L),
                summary = "   ",
            )
        }
    }

    private fun createTask(): TaskRecord {
        val understanding = UnderstandingRecord.create(
            context = ContextEnvelope.create(
                traceId = TraceId.from("trace-outcome-001"),
                schemaVersion = SchemaVersion.from(1),
                source = ContextSource.TEXT,
                trustLevel = ContextTrustLevel.VERIFIED,
                securityLevel = ContextSecurityLevel.RESTRICTED,
                observedAt = DevilTimestamp.fromEpochMilliseconds(1_754_000_000_000L),
            ),
            state = UnderstandingState.COMPLETE,
            summary = "Open the camera application.",
        )

        val decision = DecisionRecord.create(
            understanding = understanding,
            state = DecisionState.SELECTED,
            summary = "Open the camera application.",
        )

        return TaskRecord.create(
            taskId = TaskId.from("task-outcome-001"),
            decision = decision,
            state = TaskState.COMPLETED,
            summary = "Open the camera application.",
        )
    }
}
