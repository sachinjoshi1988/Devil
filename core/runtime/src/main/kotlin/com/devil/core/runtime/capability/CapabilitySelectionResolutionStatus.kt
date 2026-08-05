package com.devil.core.runtime.capability

/**
 * Describes whether one registered capability was resolved from the bounded
 * constitutional capability-selection inputs.
 *
 * This status does not establish capability availability, health,
 * authorization, operating-system permission, readiness, execution,
 * observation, verification, or final outcome.
 */
enum class CapabilitySelectionResolutionStatus {
    RESOLVED,
    UNAVAILABLE,
    FAILED,
}
