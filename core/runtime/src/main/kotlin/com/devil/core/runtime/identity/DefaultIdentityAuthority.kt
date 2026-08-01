package com.devil.core.runtime.identity

import com.devil.core.model.context.ContextEnvelope

/**
 * Default Stage 2 implementation of identity resolution.
 *
 * The current ContextEnvelope carries no subject identity, so this implementation
 * honestly reports that identity cannot yet be resolved.
 *
 * It performs no authentication, trust evaluation, authorization,
 * reasoning, planning, or execution.
 */
class DefaultIdentityAuthority : IdentityAuthority {

    override fun resolve(
        context: ContextEnvelope,
    ): IdentityResult {
        return IdentityResult.create(
            traceId = context.traceId,
            status = IdentityStatus.UNRESOLVED,
        )
    }
}
