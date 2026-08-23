package com.devil.core.model.personality

/**
 * Immutable Stage 248 Humor & Social Interaction record.
 *
 * This record preserves:
 *
 * - the exact Stage 247 AdaptiveCommunicationStyleRecord;
 * - one explicitly supplied bounded humor/social-interaction description;
 * - one explicitly supplied bounded social-appropriateness rationale.
 *
 * Stage 248 represents personality-domain humor and social-interaction context only.
 *
 * It does not:
 *
 * - generate a conversational response;
 * - establish that a conversation occurred;
 * - infer emotional or psychological state;
 * - prove a relationship;
 * - perform preference learning;
 * - create or persist Memory;
 * - create or select a Brain Decision;
 * - grant authorization;
 * - execute capabilities;
 * - establish Observation, Verification, or Outcome;
 * - implement Stage 249 Persona Presentation;
 * - implement Stage 250 Owner Experience.
 *
 * HUMOR_SOCIAL_INTERACTION != RESPONSE_GENERATION.
 * HUMOR_SOCIAL_INTERACTION != CONVERSATION_OCCURRED.
 * HUMOR_SOCIAL_INTERACTION != EMOTIONAL_STATE.
 * HUMOR_SOCIAL_INTERACTION != RELATIONSHIP_PROOF.
 * HUMOR_SOCIAL_INTERACTION != PREFERENCE_LEARNING.
 * HUMOR_SOCIAL_INTERACTION != MEMORY.
 * HUMOR_SOCIAL_INTERACTION != BRAIN_DECISION.
 * HUMOR_SOCIAL_INTERACTION != AUTHORIZATION.
 * HUMOR_SOCIAL_INTERACTION != EXECUTION.
 * HUMOR_SOCIAL_INTERACTION != VERIFICATION.
 * HUMOR_SOCIAL_INTERACTION != PERSONA_PRESENTATION.
 */
@ConsistentCopyVisibility
data class HumorSocialInteractionRecord private constructor(
    val adaptiveCommunicationStyle: AdaptiveCommunicationStyleRecord,
    val interactionDescription: String,
    val appropriatenessRationale: String,
) {
    companion object {

        fun create(
            adaptiveCommunicationStyle: AdaptiveCommunicationStyleRecord,
            interactionDescription: String,
            appropriatenessRationale: String,
        ): HumorSocialInteractionRecord {
            val normalizedInteractionDescription =
                interactionDescription.trim()

            val normalizedAppropriatenessRationale =
                appropriatenessRationale.trim()

            require(normalizedInteractionDescription.isNotEmpty()) {
                "Stage 248 humor/social-interaction description must not be blank."
            }

            require(normalizedAppropriatenessRationale.isNotEmpty()) {
                "Stage 248 social-appropriateness rationale must not be blank."
            }

            return HumorSocialInteractionRecord(
                adaptiveCommunicationStyle = adaptiveCommunicationStyle,
                interactionDescription = normalizedInteractionDescription,
                appropriatenessRationale = normalizedAppropriatenessRationale,
            )
        }
    }
}
