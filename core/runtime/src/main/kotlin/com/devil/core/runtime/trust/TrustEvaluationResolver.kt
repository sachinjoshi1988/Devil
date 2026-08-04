package com.devil.core.runtime.trust

import com.devil.core.model.trust.TrustAssessment
import com.devil.core.model.trust.TrustEvaluationRequest

/**
 * Produces a bounded subject trust assessment from a structured trust request.
 *
 * This resolver does not resolve identity, authenticate a subject, prove
 * ownership, grant authorization, enter Owner Mode, or permit execution.
 */
interface TrustEvaluationResolver {

    fun evaluate(
        request: TrustEvaluationRequest,
    ): TrustAssessment
}
