package com.devil.core.runtime.conversation

/**
 * Supplies one bounded conversation-record request when Conversation Intake has
 * produced an established ConversationIntakeResult.
 *
 * A produced intake remains eligible for conversation-domain recording regardless
 * of whether its preserved intake state is ACCEPTED, DEFERRED, or REJECTED.
 *
 * This provider does not reinterpret intake state, decide whether constitutional
 * processing may continue, create conversation identity, create a
 * ConversationRecord, establish multi-turn ordering, persist or restore
 * conversation state, create logical memory, authenticate a subject, grant
 * authorization, execute capabilities, or establish a verified outcome.
 */
interface ConversationRecordRequestProvider {

    fun provide(
        conversationIntake: ConversationIntakeAuthorityResult,
    ): ConversationRecordRequestResult
}
