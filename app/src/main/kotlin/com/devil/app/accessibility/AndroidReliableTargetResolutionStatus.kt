package com.devil.app.accessibility

/**
 * Stage 180 bounded Reliable Target Resolution status.
 *
 * RESOLVED means exactly one Stage 179 screen element matched the explicit target.
 *
 * NOT_FOUND means no Stage 179 screen element matched the explicit target.
 *
 * SCREEN_UNAVAILABLE means Stage 179 screen understanding was not available.
 *
 * Resolution does not establish clickability, actionability, authorization,
 * execution approval, execution, Observation, Verification, or Outcome.
 */
enum class AndroidReliableTargetResolutionStatus {
    RESOLVED,
    NOT_FOUND,
    SCREEN_UNAVAILABLE,
}
