package com.devil.core.runtime.conversation

/**
 * Coordinates bounded conversation-record formation after Conversation Intake has
 * produced one established result.
 *
 * This authority may obtain one bounded ConversationRecordRequest, obtain one
 * genuine ConversationId, and delegate record formation to the bounded resolver.
 *
 * It does not reinterpret conversation-intake state, decide whether constitutional
 * processing may continue, generate conversation identity, establish multi-turn
 * ordering, persist or restore conversation state, create Android presentation
 * state, create logical memory, authenticate a subject, grant authorization,
 * execute capabilities, or establish a verified outcome.
 */
interface ConversationRecordAuthority {

    fun record(
        conversationIntake: ConversationIntakeAuthorityResult,
    ): ConversationRecordResult
}
