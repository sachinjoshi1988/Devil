package com.devil.core.runtime.conversation

/**
 * Coordinates bounded constitutional conversation-persistence evaluation after
 * conversation-record formation.
 *
 * This authority may prepare one ConversationPersistenceRequest, delegate
 * persistence evaluation, and map that evaluation into one stable operational
 * result.
 *
 * It does not itself persist, restore, durably store, order, replicate, encrypt,
 * delete, expose, or recall conversation state.
 *
 * It does not create conversation identity, create logical memory, authenticate
 * a subject, grant authorization, execute capabilities, or establish a verified
 * outcome.
 */
interface ConversationPersistenceAuthority {

    fun evaluatePersistence(
        conversationRecord: ConversationRecordResult,
    ): ConversationPersistenceResult
}
