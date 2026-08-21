package com.devil.app.accessibility

/**
 * Android platform boundary for bounded Stage 179 Screen Understanding.
 *
 * Implementations may inspect accessibility-derived screen metadata only.
 *
 * They must not perform actions, infer intent, resolve an execution target,
 * grant authority, or establish Observation, Verification, or Outcome.
 */
fun interface AndroidScreenUnderstandingSource {

    fun inspect(): AndroidScreenUnderstandingResult
}
