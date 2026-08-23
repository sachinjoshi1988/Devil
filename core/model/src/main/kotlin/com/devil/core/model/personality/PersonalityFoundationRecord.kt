package com.devil.core.model.personality

/**
 * Immutable Stage 244 Personality Foundation V2 record.
 *
 * This record establishes one bounded descriptive foundation for Devil's
 * personality and companionship architecture.
 *
 * It preserves:
 *
 * - one explicitly supplied description of Devil's personality identity;
 * - one explicitly supplied description of personality's constitutional role.
 *
 * Personality at this stage is descriptive presentation architecture only.
 *
 * It does not:
 *
 * - create another Devil identity;
 * - create another Brain, Constitution, Executive, Planner, Security Authority,
 *   Authorization Authority, Verification Authority, or Memory Authority;
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
 * - establish authentication, trust, ownership, Owner Mode, or
 *   High-Security Confirmation;
 * - establish relationship continuity;
 * - adapt communication style;
 * - generate humor or social behaviour;
 * - establish persona presentation;
 * - implement Owner Experience;
 * - implement Stage 245 Devil Core Personality.
 *
 * PERSONALITY != IDENTITY_AUTHORITY.
 * PERSONALITY != BRAIN.
 * PERSONALITY != DECISION.
 * PERSONALITY != AUTHORIZATION.
 * PERSONALITY != EXECUTION.
 * PERSONALITY != VERIFICATION.
 * PERSONALITY != MEMORY.
 * PERSONALITY != TRUST.
 * PERSONALITY != AUTHENTICATION.
 * PERSONALITY_PRESENTATION != AUTHORITY.
 */
@ConsistentCopyVisibility
data class PersonalityFoundationRecord private constructor(
    val personalityIdentityDescription: String,
    val constitutionalRoleDescription: String,
) {
    companion object {

        fun create(
            personalityIdentityDescription: String,
            constitutionalRoleDescription: String,
        ): PersonalityFoundationRecord {
            val normalizedIdentityDescription =
                personalityIdentityDescription.trim()

            val normalizedConstitutionalRoleDescription =
                constitutionalRoleDescription.trim()

            require(normalizedIdentityDescription.isNotEmpty()) {
                "Stage 244 personality identity description must not be blank."
            }

            require(normalizedConstitutionalRoleDescription.isNotEmpty()) {
                "Stage 244 constitutional role description must not be blank."
            }

            return PersonalityFoundationRecord(
                personalityIdentityDescription =
                    normalizedIdentityDescription,
                constitutionalRoleDescription =
                    normalizedConstitutionalRoleDescription,
            )
        }
    }
}
