package com.devil.app.communication

/**
 * Stage 184 bounded Contacts & Communication intelligence result.
 *
 * AVAILABLE contains exactly one explicitly supplied and validated recipient.
 *
 * DEFERRED contains no recipient.
 *
 * This result does not:
 *
 * - establish an Android Contacts Provider record;
 * - verify recipient identity or address ownership;
 * - grant Devil authorization;
 * - approve communication execution;
 * - send a message or place a call;
 * - establish Observation, Verification, or Outcome.
 *
 * RECIPIENT_AVAILABLE != IDENTITY_VERIFIED.
 * RECIPIENT_AVAILABLE != COMMUNICATION_AUTHORIZED.
 * COMMUNICATION_INTELLIGENCE != MESSAGE_SENT.
 * COMMUNICATION_INTELLIGENCE != CALL_PLACED.
 */
@ConsistentCopyVisibility
data class AndroidCommunicationIntelligenceResult private constructor(
    val status: AndroidCommunicationIntelligenceStatus,
    val recipient: AndroidCommunicationRecipient?,
) {
    companion object {
        fun create(
            status: AndroidCommunicationIntelligenceStatus,
            recipient: AndroidCommunicationRecipient? = null,
        ): AndroidCommunicationIntelligenceResult {
            when (status) {
                AndroidCommunicationIntelligenceStatus.AVAILABLE ->
                    require(recipient != null) {
                        "Available Android communication intelligence requires one recipient."
                    }

                AndroidCommunicationIntelligenceStatus.DEFERRED ->
                    require(recipient == null) {
                        "Deferred Android communication intelligence must not contain a recipient."
                    }
            }

            return AndroidCommunicationIntelligenceResult(
                status = status,
                recipient = recipient,
            )
        }
    }
}
