package com.devil.core.runtime.memory

/**
 * Describes the bounded result of constitutional memory-proposal evaluation.
 *
 * PROPOSABLE means genuine constitutional evidence established that one bounded
 * memory proposal may be produced for later review by the single Memory
 * Authority.
 *
 * UNAVAILABLE means no justified memory proposal can currently be established.
 * FAILED represents an operational evaluation failure.
 *
 * This status does not create a memory proposal, approve or commit logical
 * memory, mutate world state, change task or plan state, communicate
 * externally, bypass the single Memory Authority, or produce a runtime result.
 */
enum class MemoryProposalEvaluationStatus {
    PROPOSABLE,
    UNAVAILABLE,
    FAILED,
}
