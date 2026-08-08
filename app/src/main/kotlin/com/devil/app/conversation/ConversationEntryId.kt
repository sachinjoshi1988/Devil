package com.devil.app.conversation

/**
 * Identifies one presentation entry in the Stage 24 Android conversation
 * timeline.
 *
 * This identity belongs only to the UI presentation layer. It is not a
 * constitutional TraceId, task identity, plan identity, session identity,
 * persistence identity, or proof of runtime processing.
 *
 * Identity generation belongs to the UI coordinator that creates timeline
 * entries. This type only validates and represents an already-created value.
 */
@ConsistentCopyVisibility
data class ConversationEntryId private constructor(
    val value: String,
) {
    companion object {
        fun from(
            rawValue: String,
        ): ConversationEntryId {
            val normalizedValue = rawValue.trim()

            require(normalizedValue.isNotEmpty()) {
                "Conversation entry identity must not be blank."
            }

            return ConversationEntryId(
                value = normalizedValue,
            )
        }
    }
}
