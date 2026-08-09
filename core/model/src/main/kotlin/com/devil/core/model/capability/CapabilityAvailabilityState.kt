package com.devil.core.model.capability

/**
 * Describes whether one registered capability is presently available to its
 * bounded embodiment.
 *
 * AVAILABLE means the capability has genuine availability evidence.
 *
 * UNAVAILABLE means the capability must not currently be treated as available.
 *
 * Availability does not establish authorization, readiness, operating-system
 * permission, execution permission, execution success, observation,
 * verification, or outcome.
 *
 * Registered != Available != Authorized != Ready != Executed.
 */
enum class CapabilityAvailabilityState {
    AVAILABLE,
    UNAVAILABLE,
}
