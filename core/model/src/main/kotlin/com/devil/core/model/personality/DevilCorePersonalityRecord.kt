package com.devil.core.model.personality

/**
 * Immutable Stage 245 Devil Core Personality record.
 *
 * This record builds directly upon one exact Stage 244 Personality Foundation V2
 * record and establishes bounded descriptive core-personality characteristics.
 *
 * It preserves:
 *
 * - the exact authoritative Stage 244 PersonalityFoundationRecord;
 * - one explicitly supplied description of Devil's stable core character;
 * - one explicitly supplied description of Devil's bounded interaction principles.
 *
 * The Stage 244 personality foundation remains authoritative for the underlying
 * personality identity and constitutional-role descriptions.
 *
 * Stage 245 does not:
 *
 * - create another Devil identity;
 * - create or modify the Brain, Constitution, Executive, Planner, Security Authority,
 *   Authorization Authority, Verification Authority, or Memory Authority;
 * - create or select a Brain Decision;
 * - reinterpret constitutional Understanding;
 * - create a Task or Plan;
 * - grant authorization;
 * - create an ExecutionRequest;
 * - execute capabilities;
 * - establish Observation, Verification, or Outcome;
 * - mutate World Model state;
 * - perform constitutional Learning;
 * - create, commit, persist, recall, or expose Memory;
 * - authenticate a subject;
 * - establish trust, ownership, Owner Mode, or High-Security Confirmation;
 * - infer personality from OwnerProfile, OwnerRelationship, or PreferenceEvidence;
 * - implement Stage 246 Relationship Continuity;
 * - implement Stage 247 Adaptive Communication Style;
 * - implement Stage 248 Humor & Social Interaction;
 * - implement Stage 249 Persona Presentation;
 * - implement Stage 250 Owner Experience.
 *
 * CORE_PERSONALITY != BRAIN.
 * CORE_PERSONALITY != DECISION.
 * CORE_PERSONALITY != AUTHORIZATION.
 * CORE_PERSONALITY != EXECUTION.
 * CORE_PERSONALITY != VERIFICATION.
 * CORE_PERSONALITY != MEMORY.
 * CORE_PERSONALITY != AUTHENTICATION.
 * CORE_PERSONALITY != TRUST.
 * CORE_PERSONALITY != RELATIONSHIP_CONTINUITY.
 * CORE_PERSONALITY != ADAPTIVE_COMMUNICATION_STYLE.
 */
@ConsistentCopyVisibility
data class DevilCorePersonalityRecord private constructor(
    val personalityFoundation: PersonalityFoundationRecord,
    val coreCharacterDescription: String,
    val interactionPrinciplesDescription: String,
) {
    companion object {

        fun create(
            personalityFoundation: PersonalityFoundationRecord,
            coreCharacterDescription: String,
            interactionPrinciplesDescription: String,
        ): DevilCorePersonalityRecord {
            val normalizedCoreCharacterDescription =
                coreCharacterDescription.trim()

            val normalizedInteractionPrinciplesDescription =
                interactionPrinciplesDescription.trim()

            require(normalizedCoreCharacterDescription.isNotEmpty()) {
                "Stage 245 core-character description must not be blank."
            }

            require(normalizedInteractionPrinciplesDescription.isNotEmpty()) {
                "Stage 245 interaction-principles description must not be blank."
            }

            return DevilCorePersonalityRecord(
                personalityFoundation = personalityFoundation,
                coreCharacterDescription =
                    normalizedCoreCharacterDescription,
                interactionPrinciplesDescription =
                    normalizedInteractionPrinciplesDescription,
            )
        }
    }
}
