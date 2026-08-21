package com.devil.app.communication

/**
 * Stage 185 bounded Messaging Assistance coordinator.
 *
 * This coordinator consumes one exact Stage 184 communication-intelligence
 * result together with explicitly supplied message text.
 *
 * Only AVAILABLE Stage 184 recipient intelligence may produce READY.
 *
 * Stage 184 DEFERRED remains DEFERRED and no prepared message is fabricated.
 *
 * It does not:
 *
 * - infer a recipient from conversation text;
 * - infer or rewrite message content;
 * - request or grant SEND_SMS;
 * - call SmsManager;
 * - create or launch Android messaging Intents;
 * - grant Devil authorization;
 * - establish constitutional Execution APPROVED;
 * - send or deliver a message;
 * - establish Observation, Verification, or Outcome;
 * - implement Stage 186 Media Control.
 *
 * MESSAGE_READY != EXECUTION_APPROVED.
 * MESSAGE_PREPARED != MESSAGE_SENT.
 */
class AndroidMessagingAssistanceCoordinator {

    fun prepare(
        communicationIntelligence: AndroidCommunicationIntelligenceResult,
        messageText: String,
    ): AndroidMessagingAssistanceResult {
        if (
            communicationIntelligence.status !=
            AndroidCommunicationIntelligenceStatus.AVAILABLE
        ) {
            return AndroidMessagingAssistanceResult.create(
                status = AndroidMessagingAssistanceStatus.DEFERRED,
                communicationIntelligence = communicationIntelligence,
            )
        }

        val recipient =
            requireNotNull(communicationIntelligence.recipient) {
                "Available Stage 184 communication intelligence requires one recipient."
            }

        val preparedMessage =
            AndroidPreparedMessage.create(
                recipient = recipient,
                messageText = messageText,
            )

        return AndroidMessagingAssistanceResult.create(
            status = AndroidMessagingAssistanceStatus.READY,
            communicationIntelligence = communicationIntelligence,
            preparedMessage = preparedMessage,
        )
    }
}
