package com.devil.app.accessibility

/**
 * Result status for one bounded Stage 38 accessibility platform request.
 *
 * ATTEMPTED means Android AccessibilityService.performAction returned true for
 * one resolved target.
 *
 * ATTEMPTED does not prove that the intended effect occurred.
 *
 * TARGET_NOT_FOUND means no bounded matching actionable node was found.
 *
 * SERVICE_UNAVAILABLE means no connected Devil AccessibilityService instance
 * was available.
 *
 * FAILED means the bounded Android accessibility mechanism failed
 * operationally.
 *
 * Attempted != Observed.
 * Observed != Verified.
 * Verified != Outcome Established.
 */
enum class AndroidAccessibilityActionStatus {
    ATTEMPTED,
    TARGET_NOT_FOUND,
    SERVICE_UNAVAILABLE,
    FAILED,
}
