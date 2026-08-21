package com.devil.app.communication

/**
 * Stage 184 bounded Contacts & Communication intelligence status.
 *
 * AVAILABLE means one explicitly supplied recipient passed the bounded
 * Stage 184 validation contract.
 *
 * DEFERRED means no recipient intelligence was established.
 *
 * COMMUNICATION_INTELLIGENCE != MESSAGE_SENT.
 * COMMUNICATION_INTELLIGENCE != CALL_PLACED.
 */
enum class AndroidCommunicationIntelligenceStatus {
    AVAILABLE,
    DEFERRED,
}
