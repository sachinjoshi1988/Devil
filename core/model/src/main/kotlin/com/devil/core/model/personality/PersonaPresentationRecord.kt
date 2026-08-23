package com.devil.core.model.personality

/**
 * Immutable Stage 249 Persona Presentation record.
 *
 * This record preserves:
 *
 * - the exact Stage 248 HumorSocialInteractionRecord;
 * - one explicitly supplied bounded persona-presentation description;
 * - one explicitly supplied bounded presentation-boundary rationale.
 *
 * Stage 249 establishes descriptive persona-presentation architecture only.
 *
 * It does not:
 *
 * - create or replace Devil identity;
 * - authenticate a subject;
 * - establish trust or ownership;
 * - grant authorization;
 * - create or select a Brain Decision;
 * - generate a conversational response;
 * - synthesize or render voice;
 * - render Android or other UI;
 * - execute capabilities;
 * - establish Observation, Verification, or Outcome;
 * - mutate World Model state;
 * - perform Learning;
 * - create, commit, persist, recall, or expose Memory;
 * - implement Stage 250 Owner Experience.
 *
 * PERSONA_PRESENTATION != DEVIL_IDENTITY.
 * PERSONA_PRESENTATION != AUTHENTICATION.
 * PERSONA_PRESENTATION != TRUST.
 * PERSONA_PRESENTATION != AUTHORIZATION.
 * PERSONA_PRESENTATION != BRAIN_DECISION.
 * PERSONA_PRESENTATION != RESPONSE_GENERATION.
 * PERSONA_PRESENTATION != VOICE_SYNTHESIS.
 * PERSONA_PRESENTATION != UI_RENDERING.
 * PERSONA_PRESENTATION != EXECUTION.
 * PERSONA_PRESENTATION != VERIFICATION.
 * PERSONA_PRESENTATION != MEMORY.
 * PERSONA_PRESENTATION != OWNER_EXPERIENCE.
 */
@ConsistentCopyVisibility
data class PersonaPresentationRecord private constructor(
    val humorSocialInteraction: HumorSocialInteractionRecord,
    val presentationDescription: String,
    val presentationBoundaryRationale: String,
) {
    companion object {

        fun create(
            humorSocialInteraction: HumorSocialInteractionRecord,
            presentationDescription: String,
            presentationBoundaryRationale: String,
        ): PersonaPresentationRecord {
            val normalizedPresentationDescription =
                presentationDescription.trim()

            val normalizedPresentationBoundaryRationale =
                presentationBoundaryRationale.trim()

            require(normalizedPresentationDescription.isNotEmpty()) {
                "Stage 249 persona-presentation description must not be blank."
            }

            require(normalizedPresentationBoundaryRationale.isNotEmpty()) {
                "Stage 249 presentation-boundary rationale must not be blank."
            }

            return PersonaPresentationRecord(
                humorSocialInteraction = humorSocialInteraction,
                presentationDescription =
                    normalizedPresentationDescription,
                presentationBoundaryRationale =
                    normalizedPresentationBoundaryRationale,
            )
        }
    }
}
