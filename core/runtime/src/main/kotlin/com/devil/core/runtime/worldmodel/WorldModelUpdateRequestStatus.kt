package com.devil.core.runtime.worldmodel

/**
 * Describes whether one bounded constitutional World Model update request is
 * available.
 *
 * This status reports request availability only. It does not mutate world
 * state, claim that world state changed, change task or plan state, create
 * memory or learning, communicate externally, or produce a runtime result.
 */
enum class WorldModelUpdateRequestStatus {
    AVAILABLE,
    UNAVAILABLE,
    FAILED,
}
