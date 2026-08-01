package com.devil.core.runtime

import com.devil.core.model.context.ContextEnvelope

/**
 * Default constitutional runtime implementation.
 *
 * This implementation currently acknowledges accepted work only.
 * Execution, planning, capabilities, observations, and verification
 * will be introduced in later stages.
 */
class DefaultUnifiedDevilRuntime : UnifiedDevilRuntime {

    override fun accept(
        context: ContextEnvelope,
    ): RuntimeResult {
        return RuntimeResult.create(
            traceId = context.traceId,
            status = RuntimeStatus.ACCEPTED,
        )
    }
}
