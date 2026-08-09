package com.devil.app.notification

/**
 * Stage 39 bounded safety disposition for perceived notification data.
 *
 * PERCEPTION_ONLY means the record may exist only as bounded Android perception.
 *
 * ELIGIBLE_FOR_LATER_ANALYSIS means the notification contains enough bounded
 * metadata to be considered by a future explicitly governed notification
 * analysis step.
 *
 * Neither state authorizes runtime submission, speech, persistence, memory,
 * execution, or user interruption.
 */
enum class AndroidNotificationSafetyDisposition {
    PERCEPTION_ONLY,
    ELIGIBLE_FOR_LATER_ANALYSIS,
}
