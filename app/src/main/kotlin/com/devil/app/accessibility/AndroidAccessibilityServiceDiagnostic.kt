package com.devil.app.accessibility

/**
 * Bounded diagnosis of Devil's Android AccessibilityService state.
 *
 * message contains truthful user-facing guidance based only on established
 * Android platform evidence.
 *
 * This diagnostic does not change Android settings, restart services,
 * perform accessibility actions, grant authority, or claim recovery.
 */
data class AndroidAccessibilityServiceDiagnostic(
    val status: AndroidAccessibilityServiceDiagnosticStatus,
    val message: String,
)
