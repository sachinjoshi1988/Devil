package com.devil.core.runtime.capability

/**
 * Describes the operational result of capability selection.
 *
 * Selection identifies a registered capability contract only. It does not
 * establish authorization, availability, health, readiness, permission to
 * execute, execution success, or a verified outcome.
 */
enum class CapabilitySelectionStatus {
    SELECTED,
    DEFERRED,
    FAILED,
}
