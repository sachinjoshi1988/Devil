package com.devil.app.accessibility

/**
 * Stage 178 bounded Android Accessibility Foundation V2 state.
 *
 * AVAILABLE means Devil's AccessibilityService is genuinely connected.
 *
 * DEGRADED means Android reports the service enabled, but no live service
 * connection is currently established.
 *
 * UNAVAILABLE means Android reports Devil accessibility as disabled.
 *
 * UNKNOWN means the platform state could not safely be established.
 *
 * None of these states grant Devil authorization, execution approval,
 * observation, verification, or Outcome.
 *
 * ACCESSIBILITY_AVAILABLE != DEVIL_AUTHORIZATION.
 * ACCESSIBILITY_CONNECTED != EXECUTION_APPROVAL.
 */
enum class AndroidAccessibilityFoundationV2Status {
    AVAILABLE,
    DEGRADED,
    UNAVAILABLE,
    UNKNOWN,
}
