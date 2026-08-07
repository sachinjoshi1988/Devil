package com.devil.core.model.memory

/**
 * Represents one bounded request for controlled logical-memory commitment after
 * the single constitutional Memory Authority established commitment
 * eligibility.
 *
 * The request preserves one existing MemoryAuthorityRequest. Preserving that
 * request does not create, persist, store, expose, recall, or commit logical
 * memory.
 *
 * It does not assign or alter memory class, sensitivity, confidence, retention
 * policy, source attribution, owner-visible reason, storage destination, or
 * deletion policy.
 *
 * It does not invoke a database, filesystem, cloud service, Android platform
 * API, or external communication mechanism. It does not mutate world state,
 * change task or plan state, bypass constitutional security review, or produce
 * a runtime result.
 *
 * Only a later explicitly authorized commitment mechanism governed by the
 * single Memory Authority may attempt logical-memory commitment.
 */
@ConsistentCopyVisibility
data class MemoryCommitmentRequest private constructor(
    val authorityRequest: MemoryAuthorityRequest,
) {
    companion object {
        fun create(
            authorityRequest: MemoryAuthorityRequest,
        ): MemoryCommitmentRequest {
            return MemoryCommitmentRequest(
                authorityRequest = authorityRequest,
            )
        }
    }
}
