package com.devil.core.model.plan

/**
 * Identifies one plan throughout its lifecycle.
 *
 * This identity does not imply authorization, readiness, execution, or a
 * verified outcome.
 */
@ConsistentCopyVisibility
data class PlanId private constructor(
    val value: String,
) {
    companion object {
        fun from(rawValue: String): PlanId {
            val normalizedValue = rawValue.trim()

            require(normalizedValue.isNotEmpty()) {
                "Plan identity must not be blank."
            }

            return PlanId(normalizedValue)
        }
    }
}
