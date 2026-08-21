package com.devil.app.notification

/**
 * Stage 183 bounded Notifications Intelligence coordinator.
 *
 * This coordinator consumes one exact Stage 39 notification-analysis result.
 *
 * ANALYZED becomes AVAILABLE.
 *
 * PERCEPTION_ONLY remains DEFERRED.
 *
 * The exact Stage 39 analysis object is preserved unchanged.
 *
 * It does not:
 *
 * - reclassify notification content;
 * - infer sender identity, trust, urgency, importance, or user intent;
 * - create ConversationInput;
 * - speak notification content;
 * - persist notification content or create Memory;
 * - grant Devil authorization;
 * - create or approve execution;
 * - perform notification actions;
 * - establish Observation, Verification, or Outcome;
 * - implement Stage 184 Contacts & Communication.
 *
 * NOTIFICATION_ANALYZED != TRUSTED_CONTENT.
 * NOTIFICATION_INTELLIGENCE_AVAILABLE != EXECUTION_APPROVAL.
 */
class AndroidNotificationIntelligenceCoordinator {

    fun integrate(
        analysis: AndroidNotificationAnalysisResult,
    ): AndroidNotificationIntelligenceResult {
        val status =
            when (analysis.status) {
                AndroidNotificationAnalysisStatus.ANALYZED ->
                    AndroidNotificationIntelligenceStatus.AVAILABLE

                AndroidNotificationAnalysisStatus.PERCEPTION_ONLY ->
                    AndroidNotificationIntelligenceStatus.DEFERRED
            }

        return AndroidNotificationIntelligenceResult.create(
            status = status,
            analysis = analysis,
        )
    }
}
