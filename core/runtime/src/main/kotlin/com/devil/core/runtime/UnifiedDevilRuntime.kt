package com.devil.core.runtime

import com.devil.core.model.conversation.ConversationInput

/**
 * Constitutional entry point into the unified Devil runtime.
 *
 * The runtime accepts one bounded ConversationInput and coordinates the ordered
 * constitutional pipeline. The input owns the authoritative ContextEnvelope.
 *
 * This interface performs no execution itself.
 */
interface UnifiedDevilRuntime {

    fun accept(
        input: ConversationInput,
    ): RuntimeResult
}
