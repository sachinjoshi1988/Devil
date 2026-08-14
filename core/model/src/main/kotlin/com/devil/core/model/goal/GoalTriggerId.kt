package com.devil.core.model.goal

/**
 * Identifies one bounded trigger associated with a long-running goal.
 *
 * Trigger identity preserves trigger continuity only.
 *
 * It does not establish:
 *
 * - identity;
 * - trust;
 * - authentication;
 * - authorization;
 * - task identity;
 * - plan identity;
 * - capability readiness;
 * - execution approval;
 * - event authenticity;
 * - verified Outcome;
 * - or automatic continuation authority.
 */
@ConsistentCopyVisibility
data class GoalTriggerId private constructor(
    val value: String,
) {
    companion object {

        fun from(rawValue: String): GoalTriggerId {
            val normalizedValue = rawValue.trim()

            require(normalizedValue.isNotEmpty()) {
                "Goal trigger identity must not be blank."
            }

            return GoalTriggerId(
                value = normalizedValue,
            )
        }
    }
}
