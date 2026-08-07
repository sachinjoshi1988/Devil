package com.devil.app.runtime

import com.devil.core.model.context.ContextEnvelope
import com.devil.core.model.conversation.ConversationInput

/**
 * Default bounded Android textual-input adapter.
 *
 * This implementation delegates normalization and validation to the existing
 * ConversationInput constitutional model contract.
 *
 * It preserves the supplied ContextEnvelope exactly and introduces no
 * constitutional interpretation or authority.
 */
class DefaultAndroidConversationInputAdapter :
    AndroidConversationInputAdapter {

    override fun adapt(
        context: ContextEnvelope,
        content: String,
    ): ConversationInput {
        return ConversationInput.create(
            context = context,
            content = content,
        )
    }
}
