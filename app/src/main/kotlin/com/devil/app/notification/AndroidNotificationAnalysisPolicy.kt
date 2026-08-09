package com.devil.app.notification

/**
 * Stage 39 bounded notification-analysis policy.
 *
 * This policy consumes only:
 *
 * - one immutable AndroidNotificationRecord;
 * - and its explicit Stage 39 safety result.
 *
 * It does not inspect package names or notification prose to infer sender
 * identity, truth, intent, importance, urgency, or authority.
 *
 * It does not invoke UnifiedDevilRuntime.
 *
 * It does not create ConversationInput.
 *
 * It does not speak, persist, execute, dismiss, open, reply to, or otherwise
 * act on the notification.
 *
 * Only records explicitly marked ELIGIBLE_FOR_LATER_ANALYSIS by the Stage 39
 * safety policy may receive ANALYZED status.
 */
class AndroidNotificationAnalysisPolicy {

    fun analyze(
        record: AndroidNotificationRecord,
        safety: AndroidNotificationSafetyResult,
    ): AndroidNotificationAnalysisResult {
        val status =
            when (safety.disposition) {
                AndroidNotificationSafetyDisposition.PERCEPTION_ONLY ->
                    AndroidNotificationAnalysisStatus.PERCEPTION_ONLY

                AndroidNotificationSafetyDisposition.ELIGIBLE_FOR_LATER_ANALYSIS ->
                    AndroidNotificationAnalysisStatus.ANALYZED
            }

        return AndroidNotificationAnalysisResult(
            status = status,
            record = record,
            safety = safety,
        )
    }
}
