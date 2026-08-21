package com.devil.app.communication

/**
 * Stage 185 bounded Messaging Assistance status.
 *
 * READY means one bounded message has been prepared from available Stage 184
 * recipient intelligence and explicitly supplied message text.
 *
 * DEFERRED means no bounded prepared message was established.
 *
 * MESSAGE_READY != EXECUTION_APPROVED.
 * MESSAGE_PREPARED != MESSAGE_SENT.
 */
enum class AndroidMessagingAssistanceStatus {
    READY,
    DEFERRED,
}
