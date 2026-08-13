package com.devil.core.runtime.verification

import com.devil.core.model.common.TraceId
import com.devil.core.model.verification.VerificationRequest

/**
 * Default constitutional verification evaluator.
 *
 * Genuine neutral verification evidence may establish VERIFIED only when trace
 * identity and capability identity both match the VerificationRequest.
 *
 * DEFERRED evidence remains unavailable.
 *
 * FAILED evidence preserves its matching operational error.
 *
 * This evaluator performs no platform verification and does not establish a
 * final Outcome, report task completion, update world state, perform Learning,
 * or commit Memory.
 */
class DefaultVerificationEvaluator : VerificationEvaluator {

    override fun evaluate(
        traceId: TraceId,
        request: VerificationRequest,
        evidence: VerificationEvidenceResult,
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

        require(evidence.traceId == traceId) {
            "Verification evaluator trace and evidence result must use the same trace identity."
        }

        return when (evidence.status) {
            VerificationEvidenceStatus.VERIFIED -> {
                require(
                    evidence.capabilityId ==
                        request.observation.execution.capability.capabilityId,
                ) {
                    "Verification evidence and request must refer to the same capability identity."
                }

                VerificationEvaluationResult.create(
                    traceId = traceId,
                    status = VerificationEvaluationStatus.VERIFIED,
                    request = request,
                )
            }

            VerificationEvidenceStatus.DEFERRED ->
                VerificationEvaluationResult.create(
                    traceId = traceId,
                    status = VerificationEvaluationStatus.UNAVAILABLE,
                )

            VerificationEvidenceStatus.FAILED ->
                VerificationEvaluationResult.create(
                    traceId = traceId,
                    status = VerificationEvaluationStatus.FAILED,
                    error = requireNotNull(evidence.error),
                )
        }
    }
}
