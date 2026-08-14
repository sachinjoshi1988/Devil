package com.devil.core.model.creative

/**
 * Immutable Stage 87 representation of one explicitly supplied Creative Media
 * objective.
 *
 * The objective preserves:
 *
 * - one explicitly supplied target medium;
 * - and one nonblank bounded creative objective.
 *
 * This type does not parse raw conversation text, infer creative intent,
 * invent content, generate media, select a model, select a capability, create
 * files, or execute actions.
 *
 * It also does not:
 *
 * - create another Brain;
 * - create another Devil intelligence;
 * - establish identity, trust, authentication, or authorization;
 * - create a Decision, Task, or Plan;
 * - establish capability availability or readiness;
 * - perform Observation, Verification, or Outcome;
 * - mutate World Model state;
 * - perform constitutional Learning;
 * - create or commit Memory;
 * - persist creative state;
 * - create scenes;
 * - create frame sequences;
 * - create animation timelines;
 * - or perform Story-to-Animation.
 *
 * CREATIVE_OBJECTIVE != DECISION.
 * CREATIVE_OBJECTIVE != GENERATION_REQUEST.
 * CREATIVE_OBJECTIVE != EXECUTION.
 * CREATIVE_MEDIA != STORY_TO_ANIMATION.
 */
@ConsistentCopyVisibility
data class CreativeMediaObjective private constructor(
    val medium: CreativeMediaMedium,
    val objective: String,
) {
    companion object {

        fun create(
            medium: CreativeMediaMedium,
            objective: String,
        ): CreativeMediaObjective {
            val normalizedObjective =
                objective.trim()

            require(normalizedObjective.isNotEmpty()) {
                "Creative Media objective must not be blank."
            }

            return CreativeMediaObjective(
                medium = medium,
                objective = normalizedObjective,
            )
        }
    }
}
