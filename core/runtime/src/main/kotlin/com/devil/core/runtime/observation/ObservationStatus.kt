package com.devil.core.runtime.observation

/**
 * Describes the stable operational result of constitutional observation.
 *
 * OBSERVED means genuine observation evidence was established for one bounded
 * ObservationRequest. It does not mean the observed state was verified, that an
 * intended outcome was achieved, or that execution succeeded.
 *
 * DEFERRED means no justified observation evidence is currently available.
 * FAILED represents an operational failure with one matching error.
 */
enum class ObservationStatus {
    OBSERVED,
    DEFERRED,
    FAILED,
}
