package com.devil.core.model.conversation

import com.devil.core.model.context.ContextEnvelope

/**
 * Represents one bounded textual input entering the conversation pipeline.
 *
 * The constitutional context preserves provenance, trust, security
 * classification, observation time, schema version, and trace identity.
 * Content is preserved only as normalized non-blank text.
 *
 * This record does not interpret language, infer intent, establish
 * understanding, alter context classification, resolve identity, evaluate
 * trust, grant authorization, create memory, make decisions, plan work,
 * execute capabilities, or verify outcomes.
 */
@ConsistentCopyVisibility
data class ConversationInput private constructor(
    val context: ContextEnvelope,
    val content: String,
) {
    companion object {
        fun create(
            context: ContextEnvelope,
            content: String,
        ): ConversationInput {
            val normalizedContent = content.trim()

            require(normalizedContent.isNotEmpty()) {
                "Conversation input content must not be blank."
            }

            return ConversationInput(
                context = context,
                content = normalizedContent,
            )
        }
    }
}
