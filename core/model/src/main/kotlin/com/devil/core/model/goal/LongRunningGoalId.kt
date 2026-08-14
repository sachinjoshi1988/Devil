package com.devil.core.model.goal

/**
 * Identifies one bounded long-running goal across constitutional reasoning cycles.
 *
 * Goal identity preserves continuity only.
 *
 * It does not establish:
 *
 * - owner identity;
 * - trust;
 * - authentication;
 * - authorization;
 * - an active session;
 * - task or plan identity;
 * - capability readiness;
 * - execution approval;
 * - persistence approval;
 * - Memory eligibility;
 * - or verified Outcome.
 */
@ConsistentCopyVisibility
data class LongRunningGoalId private constructor(
    val value: String,
) {
    companion object {

        fun from(rawValue: String): LongRunningGoalId {
            val normalizedValue = rawValue.trim()

            require(normalizedValue.isNotEmpty()) {
                "Long-running goal identity must not be blank."
            }

            return LongRunningGoalId(
                value = normalizedValue,
            )
        }
    }
}
