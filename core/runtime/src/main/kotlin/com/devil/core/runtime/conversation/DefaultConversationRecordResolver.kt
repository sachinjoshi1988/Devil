package com.devil.core.runtime.conversation

import com.devil.core.model.conversation.ConversationId
import com.devil.core.model.conversation.ConversationIntakeResult
import com.devil.core.model.conversation.ConversationRecord

/**
 * Default Stage 25 bounded conversation-record resolver.
 *
 * This resolver preserves the supplied ConversationIntakeResult and applies
 * the supplied genuine ConversationId without changing either value's meaning.
 *
 * It does not generate conversation identity, reinterpret intake state,
 * establish multi-turn ordering, persist conversation state, create logical
 * memory, authenticate a subject, grant authorization, execute capabilities,
 * or establish a verified outcome.
 */
class DefaultConversationRecordResolver :
    ConversationRecordResolver {

    override fun create(
        intake: ConversationIntakeResult,
        conversationId: ConversationId,
    ): ConversationRecord {
        return ConversationRecord.create(
            conversationId = conversationId,
            intake = intake,
        )
    }
}
