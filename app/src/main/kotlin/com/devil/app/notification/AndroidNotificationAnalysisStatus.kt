package com.devil.app.notification

/**
 * Stage 39 bounded notification-analysis status.
 *
 * PERCEPTION_ONLY means the notification remains confined to the Android
 * perception boundary.
 *
 * ANALYZED means the notification passed the explicit Stage 39 safety gate and
 * bounded descriptive analysis was produced.
 *
 * ANALYZED does not mean:
 *
 * - important;
 * - urgent;
 * - trustworthy;
 * - authenticated;
 * - conversational input;
 * - permission to interrupt;
 * - permission to speak;
 * - memory eligible;
 * - authorized;
 * - executable;
 * - or verified.
 */
enum class AndroidNotificationAnalysisStatus {
    PERCEPTION_ONLY,
    ANALYZED,
}
