package com.devil.app.voice

/**
 * Describes one bounded Stage 37 hands-free coordinator result.
 */
enum class HandsFreeConversationResultStatus {
    IGNORED,
    WAKE_ESTABLISHED,
    AUTHENTICATION_PHRASE_REQUIRED,
    AUTHENTICATION_REQUESTED,
    CONVERSATION_INPUT_ALLOWED,
}
