package com.devil.core.runtime.conversation

import com.devil.core.model.conversation.ConversationId
import com.devil.core.model.conversation.ConversationIntakeResult
import com.devil.core.model.conversation.ConversationRecord

/**
 * Creates one bounded ConversationRecord from an existing conversation-intake
 * result and one genuine conversation identity.
 *
 * This resolver does not generate conversation identity, reinterpret intake
 * state, establish multi-turn ordering, persist conversation state, create
 * logical memory, authenticate a subject, grant authorization, execute
 * capabilities, or establish a verified outcome.
 */
interface ConversationRecordResolver {

    fun create(
        intake: ConversationIntakeResult,
        conversationId: ConversationId,
    ): ConversationRecord
}
