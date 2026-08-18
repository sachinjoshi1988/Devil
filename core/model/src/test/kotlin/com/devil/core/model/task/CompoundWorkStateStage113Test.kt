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
import com.devil.core.model.outcome.OutcomeRecord
import com.devil.core.model.outcome.OutcomeState
import com.devil.core.model.understanding.UnderstandingRecord
import com.devil.core.model.understanding.UnderstandingState
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame

class CompoundWorkStateStage113Test {

    @Test
    fun `step state record preserves exact Stage 77 step`() {
        val request =
            request(
                "trace-stage-113-model-001",
            )

        val step =
            request.steps[0]

        val record =
            CompoundWorkStepStateRecord.create(
                step = step,
                state = CompoundWorkStepState.ACTIVE,
            )

        assertSame(
            step,
            record.step,
        )

        assertEquals(
            CompoundWorkStepState.ACTIVE,
            record.state,
        )
    }

    @Test
    fun `verified success may be preserved without becoming task completion`() {
        val request =
            request(
                "trace-stage-113-model-002",
            )

        val task =
            TaskRecord.create(
                taskId =
                    TaskId.from(
                        "task-stage-113-model-002",
                    ),
                decision = request.decision,
                state = TaskState.ACTIVE,
                summary =
                    "Bounded task remains active.",
            )

        val outcome =
            OutcomeRecord.create(
                task = task,
                state = OutcomeState.VERIFIED_SUCCESS,
                verifiedAt =
                    DevilTimestamp.fromEpochMilliseconds(
                        1_754_000_113_000L,
                    ),
                summary =
                    "One bounded result was verified successful.",
            )

        val record =
            CompoundWorkStepStateRecord.create(
                step = request.steps[0],
                state = CompoundWorkStepState.COMPLETED,
                outcome = outcome,
            )

        assertEquals(
            OutcomeState.VERIFIED_SUCCESS,
            requireNotNull(record.outcome).state,
        )

        assertEquals(
            TaskState.ACTIVE,
            record.outcome.task.state,
        )
    }

    @Test
    fun `partial success remains distinct from total failure`() {
        val request =
            request(
                "trace-stage-113-model-003",
            )

        val task =
            TaskRecord.create(
                taskId =
                    TaskId.from(
                        "task-stage-113-model-003",
                    ),
                decision = request.decision,
                state = TaskState.ACTIVE,
                summary =
                    "Task remains independently active.",
            )

        val outcome =
            OutcomeRecord.create(
                task = task,
                state = OutcomeState.PARTIAL_SUCCESS,
                verifiedAt =
                    DevilTimestamp.fromEpochMilliseconds(
                        1_754_000_113_001L,
                    ),
                summary =
                    "Only part of the bounded intended result was verified.",
            )

        val record =
            CompoundWorkStepStateRecord.create(
                step = request.steps[1],
                state = CompoundWorkStepState.BLOCKED,
                outcome = outcome,
            )

        assertEquals(
            OutcomeState.PARTIAL_SUCCESS,
            requireNotNull(record.outcome).state,
        )

        assertEquals(
            CompoundWorkStepState.BLOCKED,
            record.state,
        )
    }

    private fun request(
        trace: String,
    ): CompoundWorkRequest {
        val traceId =
            TraceId.from(trace)

        val understanding =
            UnderstandingRecord.create(
                context =
                    ContextEnvelope.create(
                        traceId = traceId,
                        schemaVersion =
                            SchemaVersion.from(1),
                        source =
                            ContextSource.TEXT,
                        trustLevel =
                            ContextTrustLevel.VERIFIED,
                        securityLevel =
                            ContextSecurityLevel.RESTRICTED,
                        observedAt =
                            DevilTimestamp.fromEpochMilliseconds(
                                1_754_000_113_100L,
                            ),
                    ),
                state =
                    UnderstandingState.COMPLETE,
                summary =
                    "Stage 113 bounded compound-work understanding.",
            )

        val decision =
            DecisionRecord.create(
                understanding = understanding,
                state = DecisionState.SELECTED,
                summary =
                    "Preserve one bounded compound goal.",
            )

        return CompoundWorkRequest.create(
            decision = decision,
            steps =
                listOf(
                    CompoundWorkStep.create(
                        position = 1,
                        summary =
                            "First bounded Stage 113 step.",
                    ),
                    CompoundWorkStep.create(
                        position = 2,
                        summary =
                            "Second bounded Stage 113 step.",
                    ),
                ),
        )
    }
}
