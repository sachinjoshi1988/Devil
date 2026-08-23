package com.devil.core.model.personality

/**
 * Immutable Stage 247 Adaptive Communication Style record.
 *
 * This record preserves:
 *
 * - the exact Stage 246 RelationshipContinuityRecord;
 * - one explicitly supplied bounded communication-style description;
 * - one explicitly supplied bounded adaptation rationale.
 *
 * Stage 247 represents adaptive communication style only.
 *
 * It does not:
 *
 * - alter or recreate relationship continuity;
 * - infer or learn preferences;
 * - create or persist Memory;
 * - infer emotional or psychological state;
 * - generate a response;
 * - create or select a Brain Decision;
 * - grant authorization;
 * - execute capabilities;
 * - establish Verification or Outcome;
 * - implement Stage 248 Humor & Social Interaction;
 * - implement Stage 249 Persona Presentation;
 * - implement Stage 250 Owner Experience.
 *
 * ADAPTIVE_COMMUNICATION_STYLE != RELATIONSHIP_CONTINUITY.
 * ADAPTIVE_COMMUNICATION_STYLE != PREFERENCE_LEARNING.
 * ADAPTIVE_COMMUNICATION_STYLE != MEMORY.
 * ADAPTIVE_COMMUNICATION_STYLE != EMOTIONAL_STATE.
 * ADAPTIVE_COMMUNICATION_STYLE != RESPONSE_GENERATION.
 * ADAPTIVE_COMMUNICATION_STYLE != BRAIN_DECISION.
 * ADAPTIVE_COMMUNICATION_STYLE != AUTHORIZATION.
 * ADAPTIVE_COMMUNICATION_STYLE != EXECUTION.
 * ADAPTIVE_COMMUNICATION_STYLE != VERIFICATION.
 * ADAPTIVE_COMMUNICATION_STYLE != HUMOR_OR_SOCIAL_INTERACTION.
 */
@ConsistentCopyVisibility
data class AdaptiveCommunicationStyleRecord private constructor(
    val relationshipContinuity: RelationshipContinuityRecord,
    val communicationStyleDescription: String,
    val adaptationRationale: String,
) {
    companion object {

        fun create(
            relationshipContinuity: RelationshipContinuityRecord,
            communicationStyleDescription: String,
            adaptationRationale: String,
        ): AdaptiveCommunicationStyleRecord {
            val normalizedStyle =
                communicationStyleDescription.trim()

            val normalizedRationale =
                adaptationRationale.trim()

            require(normalizedStyle.isNotEmpty()) {
                "Stage 247 communication-style description must not be blank."
            }

            require(normalizedRationale.isNotEmpty()) {
                "Stage 247 adaptation rationale must not be blank."
            }

            return AdaptiveCommunicationStyleRecord(
                relationshipContinuity = relationshipContinuity,
                communicationStyleDescription = normalizedStyle,
                adaptationRationale = normalizedRationale,
            )
        }
    }
}
