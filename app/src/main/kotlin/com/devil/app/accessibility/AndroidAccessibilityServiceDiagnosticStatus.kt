package com.devil.app.accessibility

/**
 * Truthful Android platform state for Devil's AccessibilityService.
 *
 * CONNECTED means Android currently has a live DevilAccessibilityService
 * instance connected to this process.
 *
 * ENABLED_BUT_DISCONNECTED means Android reports Devil's accessibility
 * service as enabled, but no live DevilAccessibilityService instance is
 * currently connected to this process.
 *
 * DISABLED means Android does not report Devil's accessibility service
 * as enabled and no live service is connected.
 *
 * UNKNOWN means the Android enabled-service state could not be established
 * safely.
 *
 * These states describe Android platform connectivity only.
 *
 * They do not establish Devil authorization, execution approval,
 * observation, verification, or Outcome.
 */
enum class AndroidAccessibilityServiceDiagnosticStatus {
    CONNECTED,
    ENABLED_BUT_DISCONNECTED,
    DISABLED,
    UNKNOWN,
}
