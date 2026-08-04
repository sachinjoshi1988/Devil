package com.devil.core.model.conversation

/**
 * Represents the stable model result of one bounded conversation-intake step.
 *
 * The result preserves the established ConversationIntakeRecord without
 * interpreting language, inferring intent, establishing understanding,
 * resolving identity, evaluating trust, granting authorization, creating
 * memory, making decisions, planning work, executing capabilities, observing
 * results, or verifying outcomes.
 */
@ConsistentCopyVisibility
data class ConversationIntakeResult private constructor(
    val record: ConversationIntakeRecord,
) {
    companion object {
        fun create(
            record: ConversationIntakeRecord,
        ): ConversationIntakeResult {
            return ConversationIntakeResult(
                record = record,
            )
        }
    }
}
