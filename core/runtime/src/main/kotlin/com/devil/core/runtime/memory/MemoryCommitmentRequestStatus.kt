package com.devil.core.runtime.memory

/**
 * Represents the availability of one bounded logical-memory commitment request.
 *
 * AVAILABLE means one constitutionally valid MemoryCommitmentRequest is
 * available for later controlled commitment evaluation.
 *
 * UNAVAILABLE means no justified commitment request can currently be
 * established.
 *
 * FAILED means commitment-request preparation failed with one matching error.
 *
 * This status does not create, persist, store, expose, recall, or commit
 * logical memory. It does not assign memory class, sensitivity, confidence,
 * retention policy, source attribution, owner-visible reason, storage
 * destination, or deletion policy.
 *
 * It does not invoke storage, mutate world state, change task or plan state,
 * communicate externally, bypass the single Memory Authority, or produce a
 * runtime result.
 */
enum class MemoryCommitmentRequestStatus {
    AVAILABLE,
    UNAVAILABLE,
    FAILED,
}
