package com.devil.core.model.capability

/**
 * Describes the bounded operational health of one registered capability.
 *
 * Health is distinct from registration, availability, authorization, readiness,
 * operating-system permission, execution permission, execution state,
 * observation, verification, and outcome.
 *
 * These states preserve the frozen capability-health vocabulary:
 *
 * INITIALIZING
 * READY
 * BUSY
 * PAUSED
 * DEGRADED
 * UNAVAILABLE
 * RECOVERING
 * RETIRED
 *
 * READY is capability health only. It is not constitutional Executive
 * readiness and does not authorize execution.
 */
enum class CapabilityHealthState {
    INITIALIZING,
    READY,
    BUSY,
    PAUSED,
    DEGRADED,
    UNAVAILABLE,
    RECOVERING,
    RETIRED,
}
