package com.devil.app.accessibility

/**
 * Stage 179 bounded Screen Understanding coordinator.
 *
 * The coordinator delegates one read-only Android accessibility screen
 * inspection and preserves the result unchanged.
 *
 * It does not:
 *
 * - infer user intent;
 * - resolve an actionable target;
 * - execute accessibility actions;
 * - establish Observation, Verification, or Outcome;
 * - implement Stage 180 Reliable Target Resolution.
 *
 * SCREEN_UNDERSTANDING != TARGET_RESOLUTION.
 * SCREEN_UNDERSTANDING != EXECUTION.
 */
class AndroidScreenUnderstandingCoordinator(
    private val source: AndroidScreenUnderstandingSource,
) {

    fun inspect(): AndroidScreenUnderstandingResult {
        return source.inspect()
    }
}
