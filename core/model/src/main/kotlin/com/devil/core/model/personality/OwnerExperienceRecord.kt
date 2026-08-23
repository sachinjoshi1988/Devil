package com.devil.core.model.personality

import com.devil.core.model.owner.OwnerProfile

/**
 * Immutable Stage 250 Owner Experience record.
 *
 * This record preserves:
 *
 * - the exact Stage 249 PersonaPresentationRecord;
 * - the exact existing OwnerProfile;
 * - one explicitly supplied bounded owner-experience description;
 * - one explicitly supplied bounded owner-experience boundary rationale.
 *
 * Stage 250 establishes descriptive owner-experience architecture only.
 *
 * It does not:
 *
 * - authenticate the owner;
 * - prove ownership;
 * - establish Owner Mode;
 * - establish High-Security Confirmation;
 * - grant authorization;
 * - create or select a Brain Decision;
 * - generate responses;
 * - synthesize voice;
 * - render UI;
 * - execute capabilities;
 * - establish Observation, Verification, or Outcome;
 * - mutate World Model state;
 * - perform Learning;
 * - create, commit, persist, recall, or expose Memory;
 * - implement Stage 251 Final Design System.
 *
 * OWNER_EXPERIENCE != OWNER_AUTHENTICATION.
 * OWNER_EXPERIENCE != OWNERSHIP_PROOF.
 * OWNER_EXPERIENCE != OWNER_MODE.
 * OWNER_EXPERIENCE != HIGH_SECURITY_CONFIRMATION.
 * OWNER_EXPERIENCE != AUTHORIZATION.
 * OWNER_EXPERIENCE != BRAIN_DECISION.
 * OWNER_EXPERIENCE != RESPONSE_GENERATION.
 * OWNER_EXPERIENCE != VOICE_SYNTHESIS.
 * OWNER_EXPERIENCE != UI_RENDERING.
 * OWNER_EXPERIENCE != EXECUTION.
 * OWNER_EXPERIENCE != VERIFICATION.
 * OWNER_EXPERIENCE != MEMORY.
 */
@ConsistentCopyVisibility
data class OwnerExperienceRecord private constructor(
    val personaPresentation: PersonaPresentationRecord,
    val ownerProfile: OwnerProfile,
    val ownerExperienceDescription: String,
    val ownerExperienceBoundaryRationale: String,
) {
    companion object {

        fun create(
            personaPresentation: PersonaPresentationRecord,
            ownerProfile: OwnerProfile,
            ownerExperienceDescription: String,
            ownerExperienceBoundaryRationale: String,
        ): OwnerExperienceRecord {
            val normalizedDescription =
                ownerExperienceDescription.trim()

            val normalizedBoundaryRationale =
                ownerExperienceBoundaryRationale.trim()

            require(normalizedDescription.isNotEmpty()) {
                "Stage 250 owner-experience description must not be blank."
            }

            require(normalizedBoundaryRationale.isNotEmpty()) {
                "Stage 250 owner-experience boundary rationale must not be blank."
            }

            return OwnerExperienceRecord(
                personaPresentation = personaPresentation,
                ownerProfile = ownerProfile,
                ownerExperienceDescription = normalizedDescription,
                ownerExperienceBoundaryRationale =
                    normalizedBoundaryRationale,
            )
        }
    }
}
