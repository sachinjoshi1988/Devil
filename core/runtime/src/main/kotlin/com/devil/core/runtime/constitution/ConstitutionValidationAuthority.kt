package com.devil.core.runtime.constitution

import com.devil.core.model.context.ContextEnvelope

/**
 * Validates whether supplied context satisfies Devil's constitutional
 * invariants before later authorities may process it.
 *
 * This authority does not establish identity, evaluate trust, grant
 * authorization, reason, plan, or execute capabilities.
 */
interface ConstitutionValidationAuthority {

    fun validate(
        context: ContextEnvelope,
    ): ConstitutionValidationResult
}
