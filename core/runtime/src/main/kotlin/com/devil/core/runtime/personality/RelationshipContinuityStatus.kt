package com.devil.core.runtime.personality

/**
 * Stage 246 bounded Relationship Continuity status.
 *
 * ESTABLISHED means one structurally valid RelationshipContinuityRecord has been
 * established from an exact Stage 245 core personality, an already-supplied
 * descriptive OwnerRelationship, and explicit continuity metadata.
 *
 * DEFERRED means Stage 246 cannot truthfully establish relationship continuity
 * from the supplied inputs.
 *
 * RELATIONSHIP_CONTINUITY_ESTABLISHED != RELATIONSHIP_PROOF.
 * RELATIONSHIP_CONTINUITY_ESTABLISHED != TRUST.
 * RELATIONSHIP_CONTINUITY_ESTABLISHED != AUTHENTICATION.
 * RELATIONSHIP_CONTINUITY_ESTABLISHED != AUTHORIZATION.
 * RELATIONSHIP_CONTINUITY_ESTABLISHED != MEMORY.
 * RELATIONSHIP_CONTINUITY_ESTABLISHED != ADAPTIVE_COMMUNICATION_STYLE.
 * RELATIONSHIP_CONTINUITY_ESTABLISHED != VERIFIED_TRUTH.
 */
enum class RelationshipContinuityStatus {
    ESTABLISHED,
    DEFERRED,
}
