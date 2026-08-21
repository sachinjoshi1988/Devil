package com.devil.app.communication

/**
 * Stage 185 bounded Messaging Assistance result.
 *
 * READY preserves one exact Stage 184 communication-intelligence result
 * together with one bounded prepared message.
 *
 * DEFERRED preserves the exact Stage 184 result and no prepared message.
 *
 * It does not send or deliver a message and does not establish
 * authorization, execution approval, Observation, Verification, or Outcome.
 *
 * MESSAGE_READY != EXECUTION_APPROVED.
 * MESSAGE_PREPARED != MESSAGE_SENT.
 * MESSAGE_SENT != DELIVERED.
 */
@ConsistentCopyVisibility
data class AndroidMessagingAssistanceResult private constructor(
    val status: AndroidMessagingAssistanceStatus,
    val communicationIntelligence: AndroidCommunicationIntelligenceResult,
    val preparedMessage: AndroidPreparedMessage?,
) {
    companion object {
        fun create(
            status: AndroidMessagingAssistanceStatus,
            communicationIntelligence: AndroidCommunicationIntelligenceResult,
            preparedMessage: AndroidPreparedMessage? = null,
        ): AndroidMessagingAssistanceResult {
            when (status) {
                AndroidMessagingAssistanceStatus.READY -> {
                    require(
                        communicationIntelligence.status ==
                            AndroidCommunicationIntelligenceStatus.AVAILABLE,
                    ) {
                        "Ready Android messaging assistance requires available Stage 184 communication intelligence."
                    }

                    val recipient =
                        requireNotNull(communicationIntelligence.recipient) {
                            "Available Stage 184 communication intelligence requires one recipient."
                        }

                    require(preparedMessage != null) {
                        "Ready Android messaging assistance requires one prepared message."
                    }

                    require(preparedMessage.recipient == recipient) {
                        "Prepared Android message must preserve the exact Stage 184 recipient."
                    }
                }

                AndroidMessagingAssistanceStatus.DEFERRED ->
                    require(preparedMessage == null) {
                        "Deferred Android messaging assistance must not contain a prepared message."
                    }
            }

            return AndroidMessagingAssistanceResult(
                status = status,
                communicationIntelligence = communicationIntelligence,
                preparedMessage = preparedMessage,
            )
        }
    }
}
