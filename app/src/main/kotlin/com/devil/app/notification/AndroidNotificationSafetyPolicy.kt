package com.devil.app.notification

/**
 * Stage 39 bounded notification safety policy.
 *
 * The policy is deliberately conservative.
 *
 * REMOVED notifications remain perception-only.
 *
 * POSTED notifications may become eligible for later bounded analysis only when
 * they preserve some explicit presentation content.
 *
 * Eligibility for later analysis:
 *
 * != importance
 * != trusted content
 * != authenticated sender
 * != conversation input
 * != permission to interrupt
 * != permission to speak
 * != memory eligibility
 * != Devil authorization
 * != execution approval.
 */
class AndroidNotificationSafetyPolicy(
    private val classificationPolicy:
        AndroidNotificationClassificationPolicy =
        AndroidNotificationClassificationPolicy(),
) {

    fun evaluate(
        record: AndroidNotificationRecord,
    ): AndroidNotificationSafetyResult {
        val classification =
            classificationPolicy.classify(
                rawCategory = record.category,
            )

        val containsPresentationContent =
            record.title != null ||
                record.text != null ||
                record.subText != null

        val disposition =
            if (
                record.eventType ==
                    AndroidNotificationEventType.POSTED &&
                containsPresentationContent
            ) {
                AndroidNotificationSafetyDisposition
                    .ELIGIBLE_FOR_LATER_ANALYSIS
            } else {
                AndroidNotificationSafetyDisposition
                    .PERCEPTION_ONLY
            }

        return AndroidNotificationSafetyResult(
            classification = classification,
            disposition = disposition,
        )
    }
}
