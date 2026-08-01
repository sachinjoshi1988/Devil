package com.devil.core.runtime.identity

import com.devil.core.model.context.ContextEnvelope

/**
 * Resolves the subject identity associated with supplied context.
 *
 * This authority does not authenticate the subject, evaluate trust, establish
 * ownership or relationships, grant authorization, reason, plan, or execute.
 */
interface IdentityAuthority {

    fun resolve(
        context: ContextEnvelope,
    ): IdentityResult
}
