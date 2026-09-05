package com.devil.core.runtime.capability

/**
 * Canonical Stage 337J capability-domain route established from already-
 * structured Understanding semantics.
 *
 * A route identifies only the capability domain to which one understood intent
 * belongs.
 *
 * It does not select a CapabilityContract, establish registration,
 * availability, health, authorization, operating-system permission, Executive
 * readiness, execution, observation, verification, or Outcome.
 *
 * GENERAL_INTENT_ROUTER != CAPABILITY_SELECTION_AUTHORITY.
 * INTENT_ROUTE != CAPABILITY_SELECTED.
 * ROUTE_CANDIDATE != CAPABILITY_AVAILABLE.
 * ROUTE_CANDIDATE != CAPABILITY_HEALTHY.
 * ROUTE_CANDIDATE != AUTHORIZED.
 * ROUTE_CANDIDATE != EXECUTABLE.
 * NO_CAPABILITY_REQUIRED != FAILURE.
 * UNSUPPORTED_ROUTE != GUESSED_ROUTE.
 */
enum class GeneralIntentCapabilityRoute {
    NO_CAPABILITY_REQUIRED,
    CAMERA,
    SETTINGS,
    DEVICE_CONTROL,
    ALARM,
    MESSAGING,
    CALL,
    MEDIA,
    DEVICE_KNOWLEDGE,
    NOTIFICATIONS,
    GENERAL_INFORMATION,
    UNSUPPORTED,
}
