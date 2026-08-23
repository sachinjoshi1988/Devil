package com.devil.core.runtime.personality

import com.devil.core.model.owner.OwnerProfile
import com.devil.core.model.personality.OwnerExperienceRecord
import com.devil.core.model.personality.PersonaPresentationRecord

/**
 * Stage 250 bounded Owner Experience coordinator.
 *
 * It establishes one descriptive owner-experience representation from:
 *
 * - one exact Stage 249 PersonaPresentationRecord;
 * - one existing OwnerProfile;
 * - one explicitly supplied owner-experience description;
 * - one explicitly supplied owner-experience boundary rationale.
 *
 * It does not:
 *
 * - authenticate the owner;
 * - prove ownership;
 * - establish Owner Mode;
 * - establish High-Security Confirmation;
 * - grant authorization;
 * - create or select a Brain Decision;
 * - generate conversational responses;
 * - synthesize voice;
 * - render UI;
 * - execute capabilities;
 * - establish Observation, Verification, or Outcome;
 * - mutate World Model state;
 * - perform Learning;
 * - create or persist Memory;
 * - implement Stage 251 Final Design System.
 *
 * OWNER_EXPERIENCE != OWNER_AUTHENTICATION.
 * OWNER_EXPERIENCE != OWNERSHIP_PROOF.
 * OWNER_EXPERIENCE != OWNER_MODE.
 * OWNER_EXPERIENCE != HIGH_SECURITY_CONFIRMATION.
 * OWNER_EXPERIENCE != AUTHORIZATION.
 * OWNER_EXPERIENCE != BRAIN.
 * OWNER_EXPERIENCE != DECISION.
 * OWNER_EXPERIENCE != RESPONSE_GENERATION.
 * OWNER_EXPERIENCE != VOICE_SYNTHESIS.
 * OWNER_EXPERIENCE != UI_RENDERING.
 * OWNER_EXPERIENCE != EXECUTION.
 * OWNER_EXPERIENCE != VERIFICATION.
 * OWNER_EXPERIENCE != MEMORY.
 */
class OwnerExperienceCoordinator {

    fun establish(
        personaPresentation: PersonaPresentationRecord?,
        ownerProfile: OwnerProfile?,
        ownerExperienceDescription: String?,
        ownerExperienceBoundaryRationale: String?,
    ): OwnerExperienceResult {
        if (
            personaPresentation == null ||
            ownerProfile == null ||
            ownerExperienceDescription.isNullOrBlank() ||
            ownerExperienceBoundaryRationale.isNullOrBlank()
        ) {
            return OwnerExperienceResult.create(
                status = OwnerExperienceStatus.DEFERRED,
            )
        }

        return OwnerExperienceResult.create(
            status = OwnerExperienceStatus.ESTABLISHED,
            experience =
                OwnerExperienceRecord.create(
                    personaPresentation = personaPresentation,
                    ownerProfile = ownerProfile,
                    ownerExperienceDescription =
                        ownerExperienceDescription,
                    ownerExperienceBoundaryRationale =
                        ownerExperienceBoundaryRationale,
                ),
        )
    }
}
