package com.devil.core.runtime.constitution

import com.devil.core.model.context.ContextEnvelope

/**
 * Default Stage 2 implementation of constitutional context validation.
 *
 * The current model types already enforce their structural invariants, so this
 * implementation acknowledges the supplied context as constitutionally valid.
 * It performs no identity, trust, authorization, reasoning, or execution work.
 */
class DefaultConstitutionValidationAuthority :
    ConstitutionValidationAuthority {

    override fun validate(
        context: ContextEnvelope,
    ): ConstitutionValidationResult {
        return ConstitutionValidationResult.create(
            traceId = context.traceId,
            status = ConstitutionValidationStatus.VALID,
        )
    }
}
