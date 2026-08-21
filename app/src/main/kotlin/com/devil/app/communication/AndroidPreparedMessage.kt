package com.devil.app.communication

/**
 * Stage 185 bounded prepared message.
 *
 * This record preserves one exact Stage 184 recipient together with
 * explicitly supplied normalized message text.
 *
 * It does not:
 *
 * - verify recipient identity or address ownership;
 * - infer or rewrite message content;
 * - grant communication authorization;
 * - approve execution;
 * - send or deliver a message;
 * - establish Observation, Verification, or Outcome.
 *
 * MESSAGE_PREPARED != MESSAGE_SENT.
 * MESSAGE_TEXT != USER_AUTHORIZATION.
 */
@ConsistentCopyVisibility
data class AndroidPreparedMessage private constructor(
    val recipient: AndroidCommunicationRecipient,
    val messageText: String,
) {
    companion object {
        fun create(
            recipient: AndroidCommunicationRecipient,
            messageText: String,
        ): AndroidPreparedMessage {
            val normalizedMessageText =
                messageText.trim()

            require(normalizedMessageText.isNotEmpty()) {
                "Android prepared message text must not be blank."
            }

            return AndroidPreparedMessage(
                recipient = recipient,
                messageText = normalizedMessageText,
            )
        }
    }
}
