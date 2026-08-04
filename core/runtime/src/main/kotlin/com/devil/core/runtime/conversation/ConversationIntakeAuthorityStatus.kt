package com.devil.core.runtime.conversation

/**
 * Describes the operational result of the Conversation Intake Authority.
 *
 * This status reports whether a bounded ConversationIntakeResult was produced.
 * The accepted, deferred, or rejected intake state belongs to the preserved
 * ConversationIntakeRecord inside that result.
 *
 * This status does not interpret language, establish understanding, create
 * memory, make decisions, plan work, execute capabilities, or verify outcomes.
 */
enum class ConversationIntakeAuthorityStatus {
    PRODUCED,
    DEFERRED,
    FAILED,
}
