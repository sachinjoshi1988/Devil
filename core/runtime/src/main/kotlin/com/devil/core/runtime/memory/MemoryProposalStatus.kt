package com.devil.core.runtime.memory

/**
 * Describes the stable operational result of constitutional memory-proposal
 * evaluation.
 *
 * PROPOSABLE means genuine constitutional evidence established that one bounded
 * memory proposal may be produced for later review by the single Memory
 * Authority.
 *
 * PROPOSABLE does not create, approve, or commit logical memory. It does not
 * mutate world state, change task or plan state, communicate externally, bypass
 * the single Memory Authority, or produce final runtime success.
 *
 * DEFERRED means no justified memory proposal is currently available.
 * FAILED represents an operational failure with one matching error.
 */
enum class MemoryProposalStatus {
    PROPOSABLE,
    DEFERRED,
    FAILED,
}
