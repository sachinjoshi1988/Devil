package com.devil.app.notification

/**
 * Stage 39 coordinator joining bounded notification perception to bounded
 * classification, safety evaluation, and descriptive analysis.
 *
 * Production flow:
 *
 * AndroidNotificationRecord
 * -> AndroidNotificationSafetyPolicy
 * -> AndroidNotificationAnalysisPolicy
 * -> AndroidNotificationAnalysisListener.
 *
 * This coordinator does not:
 *
 * - invoke UnifiedDevilRuntime;
 * - create ConversationInput;
 * - assign ContextSource;
 * - authenticate a sender;
 * - establish subject trust;
 * - infer notification prose as a command;
 * - grant authorization;
 * - speak notification content;
 * - persist notification content;
 * - create memory;
 * - select a capability;
 * - execute an action;
 * - verify an effect;
 * - or establish an Outcome.
 */
class AndroidNotificationAnalysisCoordinator(
    private val safetyPolicy: AndroidNotificationSafetyPolicy =
        AndroidNotificationSafetyPolicy(),
    private val analysisPolicy: AndroidNotificationAnalysisPolicy =
        AndroidNotificationAnalysisPolicy(),
    private val listener: AndroidNotificationAnalysisListener =
        AndroidNotificationAnalysisListener {
            /*
             * Stage 39 production default intentionally stops at the bounded
             * notification-analysis boundary.
             */
        },
) {

    fun analyze(
        record: AndroidNotificationRecord,
    ): AndroidNotificationAnalysisResult {
        val safety =
            safetyPolicy.evaluate(
                record = record,
            )

        val result =
            analysisPolicy.analyze(
                record = record,
                safety = safety,
            )

        listener.onAnalysis(
            result = result,
        )

        return result
    }
}
