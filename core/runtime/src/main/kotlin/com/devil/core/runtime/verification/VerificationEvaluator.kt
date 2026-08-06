package com.devil.core.runtime.verification

import com.devil.core.model.common.TraceId
import com.devil.core.model.verification.VerificationRequest

/**
 * Evaluates one bounded constitutional verification request.
 *
 * An evaluator must not fabricate verification evidence, infer success merely
 * from observation, update world state, report final success or failure, change
 * task or plan state, or produce a final outcome.
 */
interface VerificationEvaluator {

    fun evaluate(
        traceId: TraceId,
        request: VerificationRequest,
    ): VerificationEvaluationResult
}
