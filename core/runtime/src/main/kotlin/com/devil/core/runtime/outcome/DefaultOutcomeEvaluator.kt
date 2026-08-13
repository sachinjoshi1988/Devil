package com.devil.core.runtime.outcome

import com.devil.core.model.common.TraceId
import com.devil.core.model.outcome.OutcomeRequest

/**
 * Default constitutional outcome evaluator.
 *
 * Genuine neutral outcome evidence may establish one bounded Outcome evaluation
 * only when trace identity and capability identity both match the OutcomeRequest.
 *
 * DEFERRED evidence remains unavailable.
 *
 * FAILED evidence preserves its matching operational error.
 *
 * This evaluator performs no platform outcome determination, does not infer task
 * or plan completion, update World Model state, perform Learning, commit Memory,
 * or communicate externally.
 */
class DefaultOutcomeEvaluator : OutcomeEvaluator {

    override fun evaluate(
        traceId: TraceId,
        request: OutcomeRequest,
        evidence: OutcomeEvidenceResult,
    ): OutcomeEvaluationResult {
        require(
            request.verification
                .observation
                .execution
                .plan
                .task
                .decision
                .understanding
                .context
                .traceId == traceId,
        ) {
            "Outcome evaluator trace and request must use the same trace identity."
        }

        require(evidence.traceId == traceId) {
            "Outcome evaluator trace and outcome-evidence result must use the same trace identity."
        }

        return when (evidence.status) {
            OutcomeEvidenceStatus.ESTABLISHED -> {
                require(
                    evidence.capabilityId ==
                        request.verification
                            .observation
                            .execution
                            .capability
                            .capabilityId,
                ) {
                    "Outcome evidence and request must refer to the same capability identity."
                }

                OutcomeEvaluationResult.create(
                    traceId = traceId,
                    status = OutcomeEvaluationStatus.ESTABLISHED,
                    request = request,
                )
            }

            OutcomeEvidenceStatus.DEFERRED ->
                OutcomeEvaluationResult.create(
                    traceId = traceId,
                    status = OutcomeEvaluationStatus.UNAVAILABLE,
                )

            OutcomeEvidenceStatus.FAILED ->
                OutcomeEvaluationResult.create(
                    traceId = traceId,
                    status = OutcomeEvaluationStatus.FAILED,
                    error = requireNotNull(evidence.error),
                )
        }
    }
}
