package com.devil.core.runtime.capability

/**
 * Describes whether registered capability contracts are available for bounded
 * constitutional capability selection.
 *
 * This status does not register capabilities, select a capability, establish
 * availability or health, grant authorization, check operating-system
 * permission, execute actions, observe results, verify outcomes, or report
 * final outcomes.
 */
enum class CapabilityRegistryStatus {
    AVAILABLE,
    UNAVAILABLE,
    FAILED,
}
