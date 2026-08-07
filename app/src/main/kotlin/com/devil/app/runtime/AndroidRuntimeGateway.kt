package com.devil.app.runtime

import com.devil.core.model.context.ContextEnvelope
import com.devil.core.runtime.RuntimeResult

/**
 * Bounded Android entry gateway into the single Unified Devil Runtime.
 *
 * The supplied ContextEnvelope is authoritative and must already contain
 * its trace identity, schema version, provenance, trust classification,
 * security classification, and observation timestamp.
 *
 * This gateway adapts one textual Android input into the existing
 * ConversationInput contract and submits it through exactly one
 * UnifiedDevilRuntime.
 *
 * It does not create trace identity, access a clock, assign trust or
 * security classification, resolve identity, grant authorization,
 * interpret language, make decisions, plan work, select or execute
 * capabilities, create memory, persist memory, or perform Android actions.
 *
 * It grants no authority and does not bypass the unified runtime path.
 */
interface AndroidRuntimeGateway {

    fun submit(
        context: ContextEnvelope,
        content: String,
    ): RuntimeResult
}
