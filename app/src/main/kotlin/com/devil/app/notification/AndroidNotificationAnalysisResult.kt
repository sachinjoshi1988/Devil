package com.devil.app.notification

/**
 * Preserves one Stage 39 bounded notification-analysis result.
 *
 * record preserves the original Android notification perception record.
 *
 * safety preserves the Stage 39 classification and safety-policy result.
 *
 * No new notification facts are invented by this record.
 *
 * Analysis result
 * != sender authentication
 * != trusted content
 * != importance
 * != urgency
 * != conversation input
 * != user interruption approval
 * != speech approval
 * != memory commitment
 * != Devil authorization
 * != execution approval
 * != verified outcome.
 */
data class AndroidNotificationAnalysisResult(
    val status: AndroidNotificationAnalysisStatus,
    val record: AndroidNotificationRecord,
    val safety: AndroidNotificationSafetyResult,
)
