package com.devil.core.model.task

/**
 * Represents one ordered bounded intention inside compound work.
 *
 * A CompoundWorkStep is not a TaskRecord, PlanRecord, ExecutionRequest,
 * capability binding, authorization, execution attempt, observation,
 * verification, or Outcome.
 *
 * The step preserves only:
 *
 * - its explicit position inside the compound work; and
 * - one nonblank bounded intention summary.
 *
 * Creating a step does not create constitutional authority and does not permit
 * the step to bypass the normal Devil runtime chain.
 */
@ConsistentCopyVisibility
data class CompoundWorkStep private constructor(
    val position: Int,
    val summary: String,
) {
    companion object {

        fun create(
            position: Int,
            summary: String,
        ): CompoundWorkStep {
            require(position > 0) {
                "Compound work step position must be positive."
            }

            val normalizedSummary =
                summary.trim()

            require(normalizedSummary.isNotEmpty()) {
                "Compound work step summary must not be blank."
            }

            return CompoundWorkStep(
                position = position,
                summary = normalizedSummary,
            )
        }
    }
}
