package com.devil.app.notification

/**
 * Stage 183 bounded Notifications Intelligence result.
 *
 * AVAILABLE preserves one exact Stage 39 ANALYZED notification result.
 *
 * DEFERRED preserves one exact Stage 39 PERCEPTION_ONLY result.
 *
 * This result does not:
 *
 * - authenticate a sender;
 * - establish notification content as trusted truth;
 * - infer importance, urgency, or user intent;
 * - create ConversationInput;
 * - speak or persist notification content;
 * - create Memory;
 * - grant Devil authorization;
 * - create or approve execution;
 * - perform notification actions;
 * - establish Observation, Verification, or Outcome;
 * - implement Stage 184 Contacts & Communication.
 *
 * NOTIFICATION_ANALYZED != TRUSTED_CONTENT.
 * NOTIFICATION_INTELLIGENCE_AVAILABLE != DEVIL_AUTHORIZATION.
 * NOTIFICATION_INTELLIGENCE_AVAILABLE != EXECUTION_APPROVAL.
 */
@ConsistentCopyVisibility
data class AndroidNotificationIntelligenceResult private constructor(
    val status: AndroidNotificationIntelligenceStatus,
    val analysis: AndroidNotificationAnalysisResult,
) {
    companion object {

        fun create(
            status: AndroidNotificationIntelligenceStatus,
            analysis: AndroidNotificationAnalysisResult,
        ): AndroidNotificationIntelligenceResult {
            when (status) {
                AndroidNotificationIntelligenceStatus.AVAILABLE ->
                    require(
                        analysis.status ==
                            AndroidNotificationAnalysisStatus.ANALYZED,
                    ) {
                        "Available Android notification intelligence requires an analyzed Stage 39 notification result."
                    }

                AndroidNotificationIntelligenceStatus.DEFERRED ->
                    require(
                        analysis.status ==
                            AndroidNotificationAnalysisStatus.PERCEPTION_ONLY,
                    ) {
                        "Deferred Android notification intelligence requires a perception-only Stage 39 notification result."
                    }
            }

            return AndroidNotificationIntelligenceResult(
                status = status,
                analysis = analysis,
            )
        }
    }
}
