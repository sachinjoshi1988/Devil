package com.devil.core.runtime.conversation

/**
 * Supplies one bounded conversation-persistence request when conversation-record
 * formation has produced one established ConversationRecord.
 *
 * This provider does not persist, restore, durably store, order, replicate,
 * encrypt, delete, expose, or recall conversation state.
 *
 * It does not create conversation identity, create another ConversationRecord,
 * create logical memory, authenticate a subject, grant authorization, execute
 * capabilities, or establish a verified outcome.
 */
interface ConversationPersistenceRequestProvider {

    fun provide(
        conversationRecord: ConversationRecordResult,
    ): ConversationPersistenceRequestResult
}
