package com.devil.core.runtime.observation

/**
 * Describes whether one bounded constitutional observation request is available.
 *
 * This status reports request availability only. It does not claim that an
 * execution attempt occurred, create observation evidence, verify outcomes, or
 * report final success.
 */
enum class ObservationRequestStatus {
    AVAILABLE,
    UNAVAILABLE,
    FAILED,
}
