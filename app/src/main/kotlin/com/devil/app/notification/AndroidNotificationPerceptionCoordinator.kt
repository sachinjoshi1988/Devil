package com.devil.app.notification

/**
 * Stage 39 bounded coordinator for Android notification perception.
 *
 * The coordinator preserves one AndroidNotificationRecord and exposes it to the
 * supplied perception listener.
 *
 * It may then approach the bounded Stage 39 analysis coordinator.
 *
 * This is not runtime submission.
 *
 * It does not convert notification content into ConversationInput.
 *
 * It does not assign ContextSource.
 *
 * It does not authenticate a sender, infer a command, grant authorization,
 * create memory, make a constitutional decision, create a task, select a
 * capability, execute an action, verify an effect, or establish an Outcome.
 */
class AndroidNotificationPerceptionCoordinator(
    private val listener: AndroidNotificationPerceptionListener =
        AndroidNotificationPerceptionListener {
            /*
             * Default production perception listener performs no external
             * side effect.
             */
        },
    private val analysisCoordinator: AndroidNotificationAnalysisCoordinator =
        AndroidNotificationAnalysisCoordinator(),
) {

    fun accept(
        record: AndroidNotificationRecord,
    ): AndroidNotificationAnalysisResult {
        listener.onNotification(
            record = record,
        )

        return analysisCoordinator.analyze(
            record = record,
        )
    }
}
