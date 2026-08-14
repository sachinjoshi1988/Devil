package com.devil.core.runtime.goal

import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.common.SchemaVersion
import com.devil.core.model.common.TraceId
import com.devil.core.model.context.ContextEnvelope
import com.devil.core.model.context.ContextSecurityLevel
import com.devil.core.model.context.ContextSource
import com.devil.core.model.context.ContextTrustLevel
import com.devil.core.model.decision.DecisionRecord
import com.devil.core.model.decision.DecisionState
import com.devil.core.model.goal.GoalTriggerId
import com.devil.core.model.goal.GoalTriggerKind
import com.devil.core.model.goal.GoalTriggerRecord
import com.devil.core.model.goal.LongRunningGoalId
import com.devil.core.model.goal.LongRunningGoalRecord
import com.devil.core.model.goal.LongRunningGoalState
import com.devil.core.model.understanding.UnderstandingRecord
import com.devil.core.model.understanding.UnderstandingState
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertSame

class Stage79ScheduledEventTriggerGovernanceTest {

    @Test
    fun `future scheduled trigger remains deferred`() {
        val trigger =
            scheduledTrigger(
                scheduledAt = 2_000L,
            )

        val result =
            GoalTriggerCoordinator().evaluate(
                traceId =
                    TraceId.from(
                        "trace-stage79-scheduled-evaluation-001",
                    ),
                trigger = trigger,
                observedAt =
                    DevilTimestamp.fromEpochMilliseconds(
                        1_999L,
                    ),
            )

        assertEquals(
            GoalTriggerEvaluationStatus.DEFERRED,
            result.status,
        )
        assertNull(result.trigger)
    }

    @Test
    fun `reached scheduled time becomes eligible only for fresh reconsideration`() {
        val trigger =
            scheduledTrigger(
                scheduledAt = 2_000L,
            )

        val evaluationTrace =
            TraceId.from(
                "trace-stage79-scheduled-evaluation-002",
            )

        val result =
            GoalTriggerCoordinator().evaluate(
                traceId = evaluationTrace,
                trigger = trigger,
                observedAt =
                    DevilTimestamp.fromEpochMilliseconds(
                        2_000L,
                    ),
            )

        assertEquals(
            GoalTriggerEvaluationStatus
                .ELIGIBLE_FOR_RECONSIDERATION,
            result.status,
        )
        assertEquals(
            evaluationTrace,
            result.traceId,
        )
        assertSame(
            trigger,
            result.trigger,
        )
    }

    @Test
    fun `matching external event becomes eligible without becoming authorization`() {
        val trigger =
            GoalTriggerRecord.create(
                triggerId =
                    GoalTriggerId.from(
                        "trigger:meeting-calendar-event",
                    ),
                goal = activeGoal(),
                kind = GoalTriggerKind.EXTERNAL_EVENT,
                eventKey = "calendar:meeting-start",
            )

        val result =
            GoalTriggerCoordinator().evaluate(
                traceId =
                    TraceId.from(
                        "trace-stage79-event-evaluation-001",
                    ),
                trigger = trigger,
                observedAt =
                    DevilTimestamp.fromEpochMilliseconds(
                        3_000L,
                    ),
                observedEventKey =
                    "calendar:meeting-start",
            )

        assertEquals(
            GoalTriggerEvaluationStatus
                .ELIGIBLE_FOR_RECONSIDERATION,
            result.status,
        )
        assertSame(
            trigger,
            result.trigger,
        )
    }

    @Test
    fun `nonmatching external event remains deferred`() {
        val trigger =
            GoalTriggerRecord.create(
                triggerId =
                    GoalTriggerId.from(
                        "trigger:meeting-calendar-event-2",
                    ),
                goal = activeGoal(),
                kind = GoalTriggerKind.EXTERNAL_EVENT,
                eventKey = "calendar:meeting-start",
            )

        val result =
            GoalTriggerCoordinator().evaluate(
                traceId =
                    TraceId.from(
                        "trace-stage79-event-evaluation-002",
                    ),
                trigger = trigger,
                observedAt =
                    DevilTimestamp.fromEpochMilliseconds(
                        3_000L,
                    ),
                observedEventKey =
                    "notification:email-arrived",
            )

        assertEquals(
            GoalTriggerEvaluationStatus.DEFERRED,
            result.status,
        )
        assertNull(result.trigger)
    }

    @Test
    fun `terminal long running goal cannot receive new trigger`() {
        val completedGoal =
            LongRunningGoalRecord.create(
                goalId =
                    LongRunningGoalId.from(
                        "goal:completed-stage79",
                    ),
                originatingDecision =
                    selectedDecision(),
                state =
                    LongRunningGoalState.COMPLETED,
                description =
                    "A completed bounded goal.",
            )

        assertFailsWith<IllegalArgumentException> {
            GoalTriggerRecord.create(
                triggerId =
                    GoalTriggerId.from(
                        "trigger:completed-goal",
                    ),
                goal = completedGoal,
                kind =
                    GoalTriggerKind.SCHEDULED_TIME,
                scheduledAt =
                    DevilTimestamp.fromEpochMilliseconds(
                        4_000L,
                    ),
            )
        }
    }

    @Test
    fun `scheduled trigger cannot smuggle external event condition`() {
        assertFailsWith<IllegalArgumentException> {
            GoalTriggerRecord.create(
                triggerId =
                    GoalTriggerId.from(
                        "trigger:mixed-condition",
                    ),
                goal = activeGoal(),
                kind =
                    GoalTriggerKind.SCHEDULED_TIME,
                scheduledAt =
                    DevilTimestamp.fromEpochMilliseconds(
                        5_000L,
                    ),
                eventKey =
                    "event:must-not-be-present",
            )
        }
    }

    private fun scheduledTrigger(
        scheduledAt: Long,
    ): GoalTriggerRecord {
        return GoalTriggerRecord.create(
            triggerId =
                GoalTriggerId.from(
                    "trigger:scheduled-stage79-$scheduledAt",
                ),
            goal = activeGoal(),
            kind = GoalTriggerKind.SCHEDULED_TIME,
            scheduledAt =
                DevilTimestamp.fromEpochMilliseconds(
                    scheduledAt,
                ),
        )
    }

    private fun activeGoal(): LongRunningGoalRecord {
        return LongRunningGoalRecord.create(
            goalId =
                LongRunningGoalId.from(
                    "goal:stage79-long-running",
                ),
            originatingDecision =
                selectedDecision(),
            state =
                LongRunningGoalState.ACTIVE,
            description =
                "Prepare for tomorrow's meeting.",
        )
    }

    private fun selectedDecision(): DecisionRecord {
        val traceId =
            TraceId.from(
                "trace-stage79-originating-goal",
            )

        return DecisionRecord.create(
            understanding =
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
                                    1_000L,
                                ),
                        ),
                    state =
                        UnderstandingState.COMPLETE,
                    summary =
                        "User established a bounded long-running goal.",
                ),
            state =
                DecisionState.SELECTED,
            summary =
                "Preserve the bounded long-running goal.",
        )
    }
}
