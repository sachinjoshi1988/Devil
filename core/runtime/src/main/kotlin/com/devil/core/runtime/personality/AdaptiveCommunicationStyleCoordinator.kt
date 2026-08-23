package com.devil.core.runtime.personality

import com.devil.core.model.personality.AdaptiveCommunicationStyleRecord
import com.devil.core.model.personality.RelationshipContinuityRecord

/**
 * Stage 247 bounded Adaptive Communication Style coordinator.
 *
 * It establishes one descriptive adaptive communication-style representation
 * from:
 *
 * - one exact Stage 246 RelationshipContinuityRecord;
 * - one explicitly supplied communication-style description;
 * - one explicitly supplied adaptation rationale.
 *
 * It does not:
 *
 * - infer communication preferences;
 * - perform preference learning;
 * - inspect vocal tone;
 * - infer emotional state;
 * - generate a conversational response;
 * - create or modify the Brain;
 * - create or select a Decision;
 * - grant authorization;
 * - execute capabilities;
 * - establish Observation, Verification, or Outcome;
 * - mutate World Model state;
 * - perform Learning;
 * - create or persist Memory;
 * - implement Stage 248 Humor & Social Interaction;
 * - implement Stage 249 Persona Presentation;
 * - implement Stage 250 Owner Experience.
 *
 * ADAPTIVE_COMMUNICATION_STYLE != RELATIONSHIP_CONTINUITY.
 * ADAPTIVE_COMMUNICATION_STYLE != PREFERENCE_LEARNING.
 * ADAPTIVE_COMMUNICATION_STYLE != RESPONSE_GENERATION.
 * ADAPTIVE_COMMUNICATION_STYLE != BRAIN.
 * ADAPTIVE_COMMUNICATION_STYLE != AUTHORITY.
 * ADAPTIVE_COMMUNICATION_STYLE != DECISION.
 * ADAPTIVE_COMMUNICATION_STYLE != AUTHORIZATION.
 * ADAPTIVE_COMMUNICATION_STYLE != EXECUTION.
 * ADAPTIVE_COMMUNICATION_STYLE != VERIFICATION.
 * ADAPTIVE_COMMUNICATION_STYLE != MEMORY.
 */
class AdaptiveCommunicationStyleCoordinator {

    fun establish(
        relationshipContinuity: RelationshipContinuityRecord?,
        communicationStyleDescription: String?,
        adaptationRationale: String?,
    ): AdaptiveCommunicationStyleResult {
        if (
            relationshipContinuity == null ||
            communicationStyleDescription.isNullOrBlank() ||
            adaptationRationale.isNullOrBlank()
        ) {
            return AdaptiveCommunicationStyleResult.create(
                status = AdaptiveCommunicationStyleStatus.DEFERRED,
            )
        }

        return AdaptiveCommunicationStyleResult.create(
            status = AdaptiveCommunicationStyleStatus.ESTABLISHED,
            style =
                AdaptiveCommunicationStyleRecord.create(
                    relationshipContinuity = relationshipContinuity,
                    communicationStyleDescription =
                        communicationStyleDescription,
                    adaptationRationale = adaptationRationale,
                ),
        )
    }
}
