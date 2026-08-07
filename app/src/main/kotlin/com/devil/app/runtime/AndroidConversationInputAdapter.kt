package com.devil.app.runtime

import com.devil.core.model.context.ContextEnvelope
import com.devil.core.model.conversation.ConversationInput

/**
 * Adapts one bounded Android textual input into the existing constitutional
 * ConversationInput contract.
 *
 * The supplied ContextEnvelope is authoritative and must already contain its
 * trace identity, schema version, provenance, trust classification, security
 * classification, and observation timestamp.
 *
 * This adapter does not create trace identity, access a clock, assign trust or
 * security classification, resolve identity, grant authorization, interpret
 * language, make decisions, plan work, select or execute capabilities, create
 * memory, persist memory, or invoke the UnifiedDevilRuntime.
 *
 * It grants no authority.
 */
interface AndroidConversationInputAdapter {

    fun adapt(
        context: ContextEnvelope,
        content: String,
    ): ConversationInput
}
