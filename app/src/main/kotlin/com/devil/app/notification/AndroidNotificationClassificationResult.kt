package com.devil.app.notification

/**
 * Preserves one bounded Stage 39 notification classification result.
 *
 * rawCategory preserves the exact normalized category supplied by Android when
 * available.
 *
 * classification is only the bounded descriptive mapping of that category.
 *
 * No classification result establishes content truth or authority.
 */
data class AndroidNotificationClassificationResult(
    val classification: AndroidNotificationClassification,
    val rawCategory: String?,
)
