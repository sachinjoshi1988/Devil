package com.devil.core.runtime.memory

/**
 * Represents the availability of one bounded Memory Authority request.
 *
 * AVAILABLE means one constitutionally valid MemoryAuthorityRequest is available
 * for later Memory Authority evaluation.
 *
 * UNAVAILABLE means no justified Memory Authority request can currently be
 * established.
 *
 * FAILED means constitutional request preparation failed with one matching
 * error.
 *
 * This status does not approve, create, persist, or commit logical memory,
 * mutate world state, change task or plan state, communicate externally,
 * bypass the single Memory Authority, or produce a runtime result.
 */
enum class MemoryAuthorityRequestStatus {
    AVAILABLE,
    UNAVAILABLE,
    FAILED,
}
