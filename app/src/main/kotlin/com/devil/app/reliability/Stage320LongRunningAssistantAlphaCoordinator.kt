package com.devil.app.reliability

import com.devil.app.performance.DevilLongRunningStabilityCoordinator
import com.devil.app.performance.DevilLongRunningStabilityEvidence
import com.devil.app.performance.DevilLongRunningStabilityStatus
import com.devil.core.model.goal.LongRunningGoalRecord

/**
 * Stage 320 bounded Long-Running Assistant Alpha coordinator.
 *
 * This coordinator composes only:
 *
 * an already-governed LongRunningGoalRecord
 * + explicitly supplied Stage 272 long-running stability evidence
 * -> Stage 272 stability evaluation
 * -> bounded Stage 320 Alpha result.
 *
 * It does not create goals, prepare decisions, create triggers, observe clocks
 * or external events, schedule work, start services, persist state, create
 * watchdogs or polling loops, retry operations, execute recovery, continue
 * execution automatically, grant authorization, or establish constitutional
 * Observation, Verification, Outcome, Learning, or Memory.
 *
 * LONG_RUNNING_GOAL != PERMANENT_AUTHORIZATION.
 * GOAL_CONTINUITY != EXECUTION_CONTINUITY.
 * LONG_RUNNING_ALPHA != BACKGROUND_EXECUTION_AUTHORIZED.
 * LONG_RUNNING_ALPHA != AUTOMATIC_CONTINUATION_AUTHORITY.
 * STABLE != VERIFIED_OUTCOME.
 */
class Stage320LongRunningAssistantAlphaCoordinator(
    private val stabilityCoordinator:
        DevilLongRunningStabilityCoordinator =
        DevilLongRunningStabilityCoordinator(),
) {
    fun prepare(
        goal: LongRunningGoalRecord,
        stabilityEvidence: DevilLongRunningStabilityEvidence,
    ): Stage320LongRunningAssistantAlphaResult {
        val stability =
            stabilityCoordinator.evaluate(
                evidence = stabilityEvidence,
            )

        if (
            stability.status !=
            DevilLongRunningStabilityStatus.STABLE
        ) {
            return deferred()
        }

        return Stage320LongRunningAssistantAlphaResult.create(
            status =
                Stage320LongRunningAssistantAlphaStatus.AVAILABLE,
            goal = goal,
            stability = stability,
        )
    }

    private fun deferred():
        Stage320LongRunningAssistantAlphaResult =
        Stage320LongRunningAssistantAlphaResult.create(
            status =
                Stage320LongRunningAssistantAlphaStatus.DEFERRED,
        )
}
