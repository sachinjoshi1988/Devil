package com.devil.core.model.conversation

/**
 * Identifies one bounded conversation continuity domain.
 *
 * This identity belongs to conversation continuity and future conversation
 * persistence. It is distinct from constitutional trace identity, Android
 * presentation-entry identity, security-session identity, task identity,
 * plan identity, and logical-memory identity.
 *
 * Possessing a ConversationId does not prove that any input entered the
 * Unified Devil Runtime, establish authentication, trust, authorization,
 * persistence success, execution, verified outcome, or logical-memory
 * commitment.
 *
 * Conversation identity creation belongs to an approved conversation identity
 * mechanism. This type only validates and represents an already-created value.
 */
@ConsistentCopyVisibility
data class ConversationId private constructor(
    val value: String,
) {
    companion object {
        fun from(rawValue: String): ConversationId {
            val normalizedValue = rawValue.trim()

            require(normalizedValue.isNotEmpty()) {
                "Conversation identity must not be blank."
            }

            return ConversationId(
                value = normalizedValue,
            )
        }
    }
}
