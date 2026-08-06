package com.devil.core.runtime.worldmodel

/**
 * Describes the stable operational result of constitutional World Model update
 * evaluation.
 *
 * APPLICABLE means genuine constitutional evidence established that one
 * bounded World Model update may be applied.
 *
 * APPLICABLE does not mutate world state, claim that state changed, change
 * task or plan state, create memory or learning, communicate externally, or
 * bypass unified runtime handling.
 *
 * DEFERRED means no justified World Model update is currently available.
 * FAILED represents an operational failure with one matching error.
 */
enum class WorldModelUpdateStatus {
    APPLICABLE,
    DEFERRED,
    FAILED,
}
