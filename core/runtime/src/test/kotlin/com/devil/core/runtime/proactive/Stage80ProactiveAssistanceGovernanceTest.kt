package com.devil.core.runtime.proactive

import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.common.TraceId
import com.devil.core.model.context.ContextEnvelope
import com.devil.core.model.context.ContextSecurityLevel
import com.devil.core.model.context.ContextSource
import com.devil.core.model.context.ContextTrustLevel
import com.devil.core.model.common.SchemaVersion
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
import com.devil.core.runtime.goal.GoalTriggerEvaluationResult
import com.devil.core.runtime.goal.GoalTriggerEvaluationStatus
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class Stage80ProactiveAssistanceGovernanceTest {

    @Test
    fun `eligible trigger plus fresh decision and interruption justification may become presentation eligible`() {
        val traceId =
            TraceId.from(
                "trace-stage80-proactive-001",
            )

        val decision =
            selectedDecision(
                traceId = traceId,
            )

        val trigger =
            scheduledTrigger(
                originatingDecision =
                    selectedDecision(
                        traceId =
                            TraceId.from(
                                "trace-stage80-origin-001",
                            ),
                    ),
            )

        val triggerEvaluation =
            GoalTriggerEvaluationResult.create(
                traceId =
                    TraceId.from(
                        "trace-stage80-trigger-evaluation-001",
                    ),
                status =
                    GoalTriggerEvaluationStatus
                        .ELIGIBLE_FOR_RECONSIDERATION,
                trigger = trigger,
            )

        val result =
            ProactiveAssistanceCoordinator().evaluate(
                traceId = traceId,
                triggerEvaluation = triggerEvaluation,
                decision = decision,
                relevanceEstablished = true,
                interruptionJustified = true,
                message =
                    "Your meeting preparation goal may need attention.",
            )

        assertEquals(
            ProactiveAssistanceEvaluationStatus
                .ELIGIBLE_FOR_PRESENTATION,
            result.status,
        )

        val record =
            assertNotNull(result.record)

        assertEquals(
            trigger,
            record.trigger,
        )

        assertEquals(
            decision,
            record.decision,
        )

        assertEquals(
            "Your meeting preparation goal may need attention.",
            record.message,
        )
    }

    @Test
    fun `eligible trigger without fresh selected decision remains deferred`() {
        val traceId =
            TraceId.from(
                "trace-stage80-proactive-002",
            )

        val decision =
            decision(
                traceId = traceId,
                state = DecisionState.DEFERRED,
            )

        val result =
            ProactiveAssistanceCoordinator().evaluate(
                traceId = traceId,
                triggerEvaluation =
                    eligibleTriggerEvaluation(),
                decision = decision,
                relevanceEstablished = true,
                interruptionJustified = true,
                message = "Bounded proactive message.",
            )

        assertEquals(
            ProactiveAssistanceEvaluationStatus.DEFERRED,
            result.status,
        )

        assertNull(result.record)
    }

    @Test
    fun `relevance alone does not justify interruption`() {
        val traceId =
            TraceId.from(
                "trace-stage80-proactive-003",
            )

        val result =
            ProactiveAssistanceCoordinator().evaluate(
                traceId = traceId,
                triggerEvaluation =
                    eligibleTriggerEvaluation(),
                decision =
                    selectedDecision(
                        traceId = traceId,
                    ),
                relevanceEstablished = true,
                interruptionJustified = false,
                message = "Relevant but not interruptible.",
            )

        assertEquals(
            ProactiveAssistanceEvaluationStatus.DEFERRED,
            result.status,
        )

        assertNull(result.record)
    }

    @Test
    fun `interruption justification without relevance remains deferred`() {
        val traceId =
            TraceId.from(
                "trace-stage80-proactive-004",
            )

        val result =
            ProactiveAssistanceCoordinator().evaluate(
                traceId = traceId,
                triggerEvaluation =
                    eligibleTriggerEvaluation(),
                decision =
                    selectedDecision(
                        traceId = traceId,
                    ),
                relevanceEstablished = false,
                interruptionJustified = true,
                message = "Not relevant.",
            )

        assertEquals(
            ProactiveAssistanceEvaluationStatus.DEFERRED,
            result.status,
        )

        assertNull(result.record)
    }

    @Test
    fun `noneligible trigger cannot become proactive presentation`() {
        val traceId =
            TraceId.from(
                "trace-stage80-proactive-005",
            )

        val result =
            ProactiveAssistanceCoordinator().evaluate(
                traceId = traceId,
                triggerEvaluation =
                    GoalTriggerEvaluationResult.create(
                        traceId =
                            TraceId.from(
                                "trace-stage80-trigger-deferred",
                            ),
                        status =
                            GoalTriggerEvaluationStatus.DEFERRED,
                    ),
                decision =
                    selectedDecision(
                        traceId = traceId,
                    ),
                relevanceEstablished = true,
                interruptionJustified = true,
                message = "Must remain deferred.",
            )

        assertEquals(
            ProactiveAssistanceEvaluationStatus.DEFERRED,
            result.status,
        )

        assertNull(result.record)
    }

    @Test
    fun `blank proactive message remains deferred`() {
        val traceId =
            TraceId.from(
                "trace-stage80-proactive-006",
            )

        val result =
            ProactiveAssistanceCoordinator().evaluate(
                traceId = traceId,
                triggerEvaluation =
                    eligibleTriggerEvaluation(),
                decision =
                    selectedDecision(
                        traceId = traceId,
                    ),
                relevanceEstablished = true,
                interruptionJustified = true,
                message = "   ",
            )

        assertEquals(
            ProactiveAssistanceEvaluationStatus.DEFERRED,
            result.status,
        )

        assertNull(result.record)
    }

    private fun eligibleTriggerEvaluation():
        GoalTriggerEvaluationResult {
        val trigger =
            scheduledTrigger(
                originatingDecision =
                    selectedDecision(
                        traceId =
                            TraceId.from(
                                "trace-stage80-origin-shared",
                            ),
                    ),
            )

        return GoalTriggerEvaluationResult.create(
            traceId =
                TraceId.from(
                    "trace-stage80-trigger-shared",
                ),
            status =
                GoalTriggerEvaluationStatus
                    .ELIGIBLE_FOR_RECONSIDERATION,
            trigger = trigger,
        )
    }

    private fun scheduledTrigger(
        originatingDecision: DecisionRecord,
    ): GoalTriggerRecord {
        val goal =
            LongRunningGoalRecord.create(
                goalId =
                    LongRunningGoalId.from(
                        "goal:stage80-proactive",
                    ),
                originatingDecision =
                    originatingDecision,
                state =
                    LongRunningGoalState.ACTIVE,
                description =
                    "Preserve one bounded goal for proactive-assistance testing.",
            )

        return GoalTriggerRecord.create(
            triggerId =
                GoalTriggerId.from(
                    "trigger:stage80-proactive",
                ),
            goal = goal,
            kind = GoalTriggerKind.SCHEDULED_TIME,
            scheduledAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_000L,
                ),
        )
    }

    private fun selectedDecision(
        traceId: TraceId,
    ): DecisionRecord {
        return decision(
            traceId = traceId,
            state = DecisionState.SELECTED,
        )
    }

    private fun decision(
        traceId: TraceId,
        state: DecisionState,
    ): DecisionRecord {
        val understanding =
            UnderstandingRecord.create(
                context =
                    ContextEnvelope.create(
                        schemaVersion =
                            SchemaVersion.from(
                                1,
                            ),
                        traceId = traceId,
                        source =
                            ContextSource.TEXT,
                        trustLevel =
                            ContextTrustLevel.UNVERIFIED,
                        securityLevel =
                            ContextSecurityLevel.RESTRICTED,
                        observedAt =
                            DevilTimestamp
                                .fromEpochMilliseconds(
                                    1_000L,
                                ),
                    ),
                state =
                    UnderstandingState.COMPLETE,
                summary =
                    "Stage 80 proactive-assistance test understanding.",
            )

        return DecisionRecord.create(
            understanding = understanding,
            state = state,
            summary =
                "Stage 80 proactive-assistance test decision.",
        )
    }
}
