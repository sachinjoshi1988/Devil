package com.devil.app.accessibility

/**
 * Stage 179 bounded Screen Understanding status.
 *
 * AVAILABLE means bounded accessibility-derived screen metadata was inspected.
 *
 * SERVICE_UNAVAILABLE means no connected DevilAccessibilityService exists.
 *
 * SCREEN_UNAVAILABLE means no active accessibility root is currently available.
 *
 * None of these states establish authorization, execution, observation,
 * verification, or outcome.
 */
enum class AndroidScreenUnderstandingStatus {
    AVAILABLE,
    SERVICE_UNAVAILABLE,
    SCREEN_UNAVAILABLE,
}
