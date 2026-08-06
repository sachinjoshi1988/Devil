package com.devil.core.runtime.outcome

/**
 * Describes whether one bounded constitutional outcome request is available.
 *
 * This status reports request availability only. It does not determine final
 * task success or failure, update world state, change task or plan state,
 * create memory or learning, communicate an outcome, or produce the final
 * runtime result.
 */
enum class OutcomeRequestStatus {
    AVAILABLE,
    UNAVAILABLE,
    FAILED,
}
