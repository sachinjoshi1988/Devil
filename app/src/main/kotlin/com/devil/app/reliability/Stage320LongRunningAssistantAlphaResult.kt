package com.devil.app.reliability

import com.devil.app.performance.DevilLongRunningStabilityResult
import com.devil.app.performance.DevilLongRunningStabilityStatus
import com.devil.core.model.goal.LongRunningGoalRecord

/**
 * Stage 320 bounded Long-Running Assistant Alpha result.
 *
 * AVAILABLE preserves the exact already-governed long-running goal and the
 * exact Stage 272 long-running stability result.
 *
 * DEFERRED exposes no partial Alpha composition.
 *
 * This is composition evidence only. It is not authorization, execution,
 * background scheduling, persistence, automatic continuation, recovery,
 * constitutional Observation, Verification, Outcome, Learning, or Memory.
 */
@ConsistentCopyVisibility
data class Stage320LongRunningAssistantAlphaResult private constructor(
    val status: Stage320LongRunningAssistantAlphaStatus,
    val goal: LongRunningGoalRecord?,
    val stability: DevilLongRunningStabilityResult?,
) {
    companion object {
        fun create(
            status: Stage320LongRunningAssistantAlphaStatus,
            goal: LongRunningGoalRecord? = null,
            stability: DevilLongRunningStabilityResult? = null,
        ): Stage320LongRunningAssistantAlphaResult {
            when (status) {
                Stage320LongRunningAssistantAlphaStatus.AVAILABLE -> {
                    require(goal != null) {
                        "Available Stage 320 Long-Running Assistant Alpha requires an existing long-running goal."
                    }
                    require(stability != null) {
                        "Available Stage 320 Long-Running Assistant Alpha requires Stage 272 stability evidence."
                    }
                    require(
                        stability.status ==
                            DevilLongRunningStabilityStatus.STABLE,
                    ) {
                        "Available Stage 320 Long-Running Assistant Alpha requires STABLE Stage 272 evidence."
                    }
                }

                Stage320LongRunningAssistantAlphaStatus.DEFERRED -> {
                    require(goal == null) {
                        "Deferred Stage 320 result must not expose a long-running goal."
                    }
                    require(stability == null) {
                        "Deferred Stage 320 result must not expose Stage 272 stability evidence."
                    }
                }
            }

            return Stage320LongRunningAssistantAlphaResult(
                status = status,
                goal = goal,
                stability = stability,
            )
        }
    }
}
