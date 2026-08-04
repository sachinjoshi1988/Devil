package com.devil.core.model.conversation

/**
 * Describes the established intake state of one conversation input.
 *
 * This state records only whether an input entered the bounded conversation
 * intake pipeline. It does not interpret language, establish understanding,
 * resolve identity, evaluate trust, grant authorization, make decisions,
 * create tasks, plan work, execute capabilities, or verify outcomes.
 */
enum class ConversationIntakeState {
    ACCEPTED,
    DEFERRED,
    REJECTED,
}
