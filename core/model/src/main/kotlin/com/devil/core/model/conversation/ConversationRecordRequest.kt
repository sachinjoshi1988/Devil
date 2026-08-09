package com.devil.core.model.conversation

/**
 * Represents one structured request for bounded conversation-record formation.
 *
 * The request preserves one existing ConversationIntakeResult without
 * reinterpreting its established intake state, constitutional context, textual
 * input, or runtime trace identity.
 *
 * Preserving this request does not create conversation identity, create a
 * ConversationRecord, establish multi-turn ordering, persist or restore
 * conversation state, create Android presentation state, create logical memory,
 * authenticate a subject, grant authorization, execute capabilities, or
 * establish a verified outcome.
 */
@ConsistentCopyVisibility
data class ConversationRecordRequest private constructor(
    val intake: ConversationIntakeResult,
) {
    companion object {
        fun create(
            intake: ConversationIntakeResult,
        ): ConversationRecordRequest {
            return ConversationRecordRequest(
                intake = intake,
            )
        }
    }
}
