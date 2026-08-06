package com.devil.core.runtime.learning

/**
 * Describes whether one bounded constitutional learning request is available.
 *
 * This status reports request availability only. It does not create learning,
 * create or commit memory, mutate world state, change task or plan state,
 * communicate externally, or produce a runtime result.
 */
enum class LearningRequestStatus {
    AVAILABLE,
    UNAVAILABLE,
    FAILED,
}
