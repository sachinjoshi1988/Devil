package com.devil.core.runtime.conversation

/**
 * Describes the operational result of bounded conversation-record formation.
 *
 * PRODUCED means one ConversationRecord was formed from an existing
 * ConversationIntakeResult and one genuine ConversationId.
 *
 * PRODUCED does not mean that conversation state was persisted, restored,
 * durably stored, ordered across turns, converted into logical memory,
 * executed, or verified.
 *
 * DEFERRED means no justified ConversationRecord can currently be produced.
 *
 * FAILED means conversation-record formation failed with one matching error.
 */
enum class ConversationRecordStatus {
    PRODUCED,
    DEFERRED,
    FAILED,
}
