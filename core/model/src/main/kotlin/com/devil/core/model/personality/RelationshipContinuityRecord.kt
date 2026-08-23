package com.devil.core.model.personality

import com.devil.core.model.owner.OwnerRelationship

/**
 * Immutable Stage 246 Relationship Continuity record.
 *
 * This record preserves one bounded continuity representation between:
 *
 * - the exact Stage 245 Devil core personality;
 * - one already-supplied Stage 43 descriptive owner relationship;
 * - one explicitly supplied description of that continuity.
 *
 * The supplied OwnerRelationship remains descriptive relationship context only.
 * Stage 246 does not prove that relationship, make it reciprocal, establish that
 * it is currently active, or turn it into trust or authority.
 *
 * Relationship continuity at this stage is descriptive companionship architecture.
 *
 * It does not:
 *
 * - create another Devil identity or personality;
 * - modify the Stage 245 core personality;
 * - create or modify the Brain, Constitution, Executive, Planner, or any authority;
 * - create or select a Brain Decision;
 * - reinterpret constitutional Understanding;
 * - create a Task or Plan;
 * - grant authorization;
 * - create an ExecutionRequest;
 * - execute capabilities;
 * - establish Observation, Verification, or Outcome;
 * - mutate World Model state;
 * - perform Learning;
 * - create, commit, persist, recall, or expose Memory;
 * - authenticate a subject;
 * - establish trust, ownership, Owner Mode, or High-Security Confirmation;
 * - prove or verify a relationship;
 * - implement Stage 247 Adaptive Communication Style;
 * - implement Stage 248 Humor & Social Interaction;
 * - implement Stage 249 Persona Presentation;
 * - implement Stage 250 Owner Experience.
 *
 * RELATIONSHIP_CONTINUITY != RELATIONSHIP_PROOF.
 * RELATIONSHIP_CONTINUITY != TRUST.
 * RELATIONSHIP_CONTINUITY != AUTHENTICATION.
 * RELATIONSHIP_CONTINUITY != AUTHORIZATION.
 * RELATIONSHIP_CONTINUITY != OWNER_MODE.
 * RELATIONSHIP_CONTINUITY != BRAIN.
 * RELATIONSHIP_CONTINUITY != DECISION.
 * RELATIONSHIP_CONTINUITY != EXECUTION.
 * RELATIONSHIP_CONTINUITY != VERIFICATION.
 * RELATIONSHIP_CONTINUITY != MEMORY.
 * RELATIONSHIP_CONTINUITY != MEMORY_PERSISTENCE.
 * RELATIONSHIP_CONTINUITY != ADAPTIVE_COMMUNICATION_STYLE.
 */
@ConsistentCopyVisibility
data class RelationshipContinuityRecord private constructor(
    val corePersonality: DevilCorePersonalityRecord,
    val relationship: OwnerRelationship,
    val continuityDescription: String,
) {
    companion object {

        fun create(
            corePersonality: DevilCorePersonalityRecord,
            relationship: OwnerRelationship,
            continuityDescription: String,
        ): RelationshipContinuityRecord {
            val normalizedContinuityDescription =
                continuityDescription.trim()

            require(normalizedContinuityDescription.isNotEmpty()) {
                "Stage 246 relationship continuity description must not be blank."
            }

            return RelationshipContinuityRecord(
                corePersonality = corePersonality,
                relationship = relationship,
                continuityDescription = normalizedContinuityDescription,
            )
        }
    }
}
