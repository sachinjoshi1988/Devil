package com.devil.core.runtime.verification

import com.devil.core.model.common.TraceId
import com.devil.core.model.verification.VerificationRequest

/**
 * Evaluates one bounded constitutional verification request against genuine
 * verification-evidence state.
 *
 * An evaluator must not fabricate verification evidence, infer success merely
 * from observation, update world state, report final success or failure, change
 * task or plan state, or produce a final Outcome.
 */
interface VerificationEvaluator {

    fun evaluate(
        traceId: TraceId,
        request: VerificationRequest,
        evidence: VerificationEvidenceResult,
    ): VerificationEvaluationResult
}
