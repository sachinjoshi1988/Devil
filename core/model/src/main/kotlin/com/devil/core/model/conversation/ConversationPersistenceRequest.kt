package com.devil.core.model.conversation

/**
 * Represents one bounded request for controlled conversation persistence after
 * one ConversationRecord has been constitutionally formed.
 *
 * The request preserves one existing ConversationRecord without reinterpreting
 * its conversation identity, intake state, constitutional context, textual
 * content, or runtime trace identity.
 *
 * Preserving this request does not persist, restore, durably store, order,
 * replicate, encrypt, delete, expose, or recall conversation state.
 *
 * It does not create conversation identity, create another ConversationRecord,
 * create Android presentation state, create logical memory, authenticate a
 * subject, grant authorization, execute capabilities, or establish a verified
 * outcome.
 *
 * Actual persistence belongs to a later explicitly authorized conversation
 * persistence mechanism.
 */
@ConsistentCopyVisibility
data class ConversationPersistenceRequest private constructor(
    val record: ConversationRecord,
) {
    companion object {
        fun create(
            record: ConversationRecord,
        ): ConversationPersistenceRequest {
            return ConversationPersistenceRequest(
                record = record,
            )
        }
    }
}
