package com.devil.app.conversation

/**
 * Describes the presentation role of one conversation timeline entry.
 *
 * USER represents text visibly supplied by the user.
 *
 * RUNTIME represents truthful immediate runtime status information. It does not
 * represent a fabricated Devil answer or verified task outcome.
 */
enum class ConversationEntryRole {
    USER,
    RUNTIME,
}
