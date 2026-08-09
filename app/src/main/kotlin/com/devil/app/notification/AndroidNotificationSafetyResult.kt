package com.devil.app.notification

/**
 * Preserves one bounded Stage 39 notification safety-policy result.
 *
 * classification is descriptive metadata only.
 *
 * disposition controls only whether a record may approach a later Stage 39
 * analysis boundary.
 *
 * This result itself grants no authority.
 */
data class AndroidNotificationSafetyResult(
    val classification: AndroidNotificationClassificationResult,
    val disposition: AndroidNotificationSafetyDisposition,
)
