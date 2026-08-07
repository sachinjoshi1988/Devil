package com.devil.core.model.memory

/**
 * Represents one bounded request for controlled logical-memory persistence after
 * constitutional logical-memory commitment evaluation established eligibility.
 *
 * The request preserves one existing MemoryCommitmentRequest.
 *
 * Preserving that request does not create, persist, store, expose, recall,
 * delete, or commit logical memory.
 *
 * It does not assign or alter memory class, sensitivity, confidence, retention
 * policy, source attribution, owner-visible reason, storage destination,
 * deletion policy, encryption policy, replication policy, or any other
 * logical-memory metadata.
 *
 * It does not invoke a database, filesystem, cloud service, Android platform
 * API, network service, or external communication mechanism.
 *
 * It does not mutate world state, change task or plan state, bypass
 * constitutional security review, bypass the single Memory Authority, or
 * produce a runtime result.
 *
 * Only a later explicitly authorized persistence mechanism governed by the
 * single Memory Authority may attempt actual logical-memory persistence.
 */
@ConsistentCopyVisibility
data class MemoryPersistenceRequest private constructor(
    val commitmentRequest: MemoryCommitmentRequest,
) {
    companion object {
        fun create(
            commitmentRequest: MemoryCommitmentRequest,
        ): MemoryPersistenceRequest {
            return MemoryPersistenceRequest(
                commitmentRequest = commitmentRequest,
            )
        }
    }
}
