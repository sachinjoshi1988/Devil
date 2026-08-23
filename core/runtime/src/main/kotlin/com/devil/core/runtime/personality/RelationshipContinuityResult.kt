package com.devil.core.runtime.personality

import com.devil.core.model.personality.RelationshipContinuityRecord

/**
 * Stage 246 bounded Relationship Continuity result.
 *
 * ESTABLISHED contains exactly one RelationshipContinuityRecord.
 *
 * DEFERRED contains no relationship continuity record.
 *
 * This result does not:
 *
 * - prove, verify, authenticate, or authorize a relationship;
 * - establish trust or ownership;
 * - create another Devil, Brain, Constitution, or authority;
 * - make decisions;
 * - authorize or execute actions;
 * - establish verified truth;
 * - mutate World Model state;
 * - perform Learning;
 * - create, write, persist, recall, or expose Memory;
 * - implement Stage 247 Adaptive Communication Style;
 * - implement Stage 248 Humor & Social Interaction;
 * - implement Stage 249 Persona Presentation;
 * - implement Stage 250 Owner Experience.
 *
 * RELATIONSHIP_CONTINUITY != AUTHORITY.
 * RELATIONSHIP_CONTINUITY != DECISION.
 * RELATIONSHIP_CONTINUITY != AUTHORIZATION.
 * RELATIONSHIP_CONTINUITY != EXECUTION.
 * RELATIONSHIP_CONTINUITY != VERIFICATION.
 * RELATIONSHIP_CONTINUITY != MEMORY.
 */
@ConsistentCopyVisibility
data class RelationshipContinuityResult private constructor(
    val status: RelationshipContinuityStatus,
    val continuity: RelationshipContinuityRecord?,
) {
    companion object {

        fun create(
            status: RelationshipContinuityStatus,
            continuity: RelationshipContinuityRecord? = null,
        ): RelationshipContinuityResult {
            return when (status) {
                RelationshipContinuityStatus.ESTABLISHED -> {
                    requireNotNull(continuity) {
                        "Established Stage 246 Relationship Continuity requires a relationship continuity record."
                    }

                    RelationshipContinuityResult(
                        status = status,
                        continuity = continuity,
                    )
                }

                RelationshipContinuityStatus.DEFERRED -> {
                    require(continuity == null) {
                        "Deferred Stage 246 Relationship Continuity must not contain a relationship continuity record."
                    }

                    RelationshipContinuityResult(
                        status = status,
                        continuity = null,
                    )
                }
            }
        }
    }
}
