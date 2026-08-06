package com.devil.core.runtime.verification

import com.devil.core.model.common.TraceId

/**
 * Default Stage 14 mapping from bounded verification-evaluation results into the
 * stable VerificationResult contract.
 *
 * Genuine verification evidence becomes operational VERIFIED and preserves the
 * bounded VerificationRequest. Evaluation unavailability becomes DEFERRED.
 * Evaluation failure preserves its matching error.
 *
 * This mapper does not update world state, report final task success, change
 * task or plan state, or produce a final Outcome.
 */
class DefaultVerificationResultMapper :
    VerificationResultMapper {

    override fun map(
        traceId: TraceId,
        evaluation: VerificationEvaluationResult,
    ): VerificationResult {
        require(evaluation.traceId == traceId) {
            "Verification result mapper trace and evaluation result must use the same trace identity."
        }

        return when (evaluation.status) {
            VerificationEvaluationStatus.VERIFIED ->
                VerificationResult.create(
                    traceId = traceId,
                    status = VerificationStatus.VERIFIED,
                    request = requireNotNull(evaluation.request),
                )

            VerificationEvaluationStatus.UNAVAILABLE ->
                VerificationResult.create(
                    traceId = traceId,
                    status = VerificationStatus.DEFERRED,
                )

            VerificationEvaluationStatus.FAILED ->
                VerificationResult.create(
                    traceId = traceId,
                    status = VerificationStatus.FAILED,
                    error = requireNotNull(evaluation.error),
                )
        }
    }
}
