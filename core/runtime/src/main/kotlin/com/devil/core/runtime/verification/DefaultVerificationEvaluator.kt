package com.devil.core.runtime.verification

import com.devil.core.model.common.TraceId
import com.devil.core.model.verification.VerificationRequest

/**
 * Default Stage 14 constitutional verification evaluator.
 *
 * No genuine verification evidence source or approved constitutional
 * verification policy exists yet. This evaluator therefore preserves trace
 * continuity and returns UNAVAILABLE rather than treating observation as proof
 * that the intended outcome was achieved or inventing verification evidence.
 *
 * It does not update world state, report final success or failure, change task
 * or plan state, or produce a final outcome.
 */
class DefaultVerificationEvaluator : VerificationEvaluator {

    override fun evaluate(
        traceId: TraceId,
        request: VerificationRequest,
    ): VerificationEvaluationResult {
        require(
            request.observation
                .execution
                .plan
                .task
                .decision
                .understanding
                .context
                .traceId == traceId,
        ) {
            "Verification evaluator trace and request must use the same trace identity."
        }

        return VerificationEvaluationResult.create(
            traceId = traceId,
            status = VerificationEvaluationStatus.UNAVAILABLE,
        )
    }
}
