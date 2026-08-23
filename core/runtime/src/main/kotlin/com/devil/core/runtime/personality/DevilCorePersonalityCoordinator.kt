package com.devil.core.runtime.personality

import com.devil.core.model.personality.DevilCorePersonalityRecord

/**
 * Stage 245 bounded Devil Core Personality coordinator.
 *
 * It establishes one descriptive Devil Core Personality from:
 *
 * - one exact ESTABLISHED Stage 244 Personality Foundation V2 result;
 * - one explicitly supplied bounded core-character description;
 * - one explicitly supplied bounded interaction-principles description.
 *
 * Stage 244 remains authoritative for the underlying personality foundation.
 * Stage 245 preserves the exact Stage 244 foundation object rather than reconstructing it.
 *
 * This coordinator establishes bounded core-personality architecture only.
 *
 * It does not:
 *
 * - create or modify Devil identity;
 * - become or modify the Brain;
 * - reinterpret constitutional Understanding;
 * - create or select a Decision;
 * - create Tasks or Plans;
 * - grant authorization;
 * - execute capabilities;
 * - establish Observation, Verification, or Outcome;
 * - mutate World Model state;
 * - perform Learning;
 * - create or persist Memory;
 * - authenticate a subject;
 * - establish trust or ownership;
 * - infer personality from owner or preference data;
 * - implement Stage 246 Relationship Continuity;
 * - implement Stage 247 Adaptive Communication Style;
 * - implement Stage 248 Humor & Social Interaction;
 * - implement Stage 249 Persona Presentation;
 * - implement Stage 250 Owner Experience.
 *
 * CORE_PERSONALITY != BRAIN.
 * CORE_PERSONALITY != AUTHORITY.
 * CORE_PERSONALITY != DECISION.
 * CORE_PERSONALITY != AUTHORIZATION.
 * CORE_PERSONALITY != EXECUTION.
 * CORE_PERSONALITY != VERIFICATION.
 * CORE_PERSONALITY != MEMORY.
 */
class DevilCorePersonalityCoordinator {

    fun establish(
        personalityFoundation: PersonalityFoundationResult,
        coreCharacterDescription: String?,
        interactionPrinciplesDescription: String?,
    ): DevilCorePersonalityResult {
        val foundation =
            personalityFoundation.foundation

        if (
            personalityFoundation.status !=
                PersonalityFoundationStatus.ESTABLISHED ||
            foundation == null ||
            coreCharacterDescription.isNullOrBlank() ||
            interactionPrinciplesDescription.isNullOrBlank()
        ) {
            return DevilCorePersonalityResult.create(
                status = DevilCorePersonalityStatus.DEFERRED,
            )
        }

        return DevilCorePersonalityResult.create(
            status = DevilCorePersonalityStatus.ESTABLISHED,
            corePersonality =
                DevilCorePersonalityRecord.create(
                    personalityFoundation = foundation,
                    coreCharacterDescription =
                        coreCharacterDescription,
                    interactionPrinciplesDescription =
                        interactionPrinciplesDescription,
                ),
        )
    }
}
