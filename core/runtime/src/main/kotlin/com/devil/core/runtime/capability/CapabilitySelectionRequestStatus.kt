package com.devil.core.runtime.capability

/**
 * Describes whether one bounded constitutional capability-selection request is
 * available.
 *
 * This status reports request availability only. It does not select a
 * capability, establish availability or health, grant authorization, check
 * operating-system permission, execute actions, observe results, verify
 * outcomes, or report final outcomes.
 */
enum class CapabilitySelectionRequestStatus {
    AVAILABLE,
    UNAVAILABLE,
    FAILED,
}
