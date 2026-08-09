package com.devil.core.model.conversation

/**
 * Records one bounded conversation-domain interaction under an existing
 * conversation identity.
 *
 * The record preserves one ConversationId and one existing
 * ConversationIntakeResult without reinterpreting the intake state or changing
 * its constitutional meaning.
 *
 * A ConversationRecord does not prove that conversation state was persisted,
 * restored, or durably stored. It does not create conversation identity,
 * establish multi-turn ordering, create Android presentation entries, create
 * logical memory, authenticate a subject, grant authorization, execute
 * capabilities, or establish a verified outcome.
 *
 * ConversationId remains distinct from the TraceId preserved by the intake's
 * ConversationInput. One identifies conversation continuity; the other
 * identifies one constitutional runtime flow.
 */
@ConsistentCopyVisibility
data class ConversationRecord private constructor(
    val conversationId: ConversationId,
    val intake: ConversationIntakeResult,
) {
    companion object {
        fun create(
            conversationId: ConversationId,
            intake: ConversationIntakeResult,
        ): ConversationRecord {
            return ConversationRecord(
                conversationId = conversationId,
                intake = intake,
            )
        }
    }
}
