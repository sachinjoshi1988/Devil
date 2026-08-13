package com.devil.core.runtime.learning

import com.devil.core.model.common.TraceId
import com.devil.core.model.learning.LearningRequest

/**
 * Default constitutional Learning evaluator.
 *
 * The evaluator remains fail-closed unless genuine Learning evidence has been
 * established.
 *
 * ESTABLISHED evidence makes the bounded Learning request constitutionally
 * learnable for the next Learning authority/result step. It still does not
 * create Learning, propose Memory, invoke Memory Authority, commit Memory,
 * persist Memory, mutate world state, or prove broader completion.
 *
 * DEFERRED evidence remains unavailable.
 *
 * FAILED evidence preserves its matching operational error.
 */
class DefaultLearningEvaluator :
    LearningEvaluator {

    override fun evaluate(
        traceId: TraceId,
        request: LearningRequest,
        evidence: LearningEvidenceResult,
    ): LearningEvaluationResult {
        require(
            request.worldModelUpdate
                .outcome
                .verification
                .observation
                .execution
                .plan
                .task
                .decision
                .understanding
                .context
                .traceId == traceId,
        ) {
            "Learning evaluator trace and request must use the same trace identity."
        }

        require(evidence.traceId == traceId) {
            "Learning evidence and evaluator must use the same trace identity."
        }

        return when (evidence.status) {
            LearningEvidenceStatus.ESTABLISHED -> {
                val capabilityId =
                    request.worldModelUpdate
                        .outcome
                        .verification
                        .observation
                        .execution
                        .capability
                        .capabilityId

                require(evidence.capabilityId == capabilityId) {
                    "Learning request and evidence must refer to the same capability identity."
                }

                LearningEvaluationResult.create(
                    traceId = traceId,
                    status = LearningEvaluationStatus.LEARNABLE,
                    request = request,
                )
            }

            LearningEvidenceStatus.DEFERRED ->
                LearningEvaluationResult.create(
                    traceId = traceId,
                    status = LearningEvaluationStatus.UNAVAILABLE,
                )

            LearningEvidenceStatus.FAILED ->
                LearningEvaluationResult.create(
                    traceId = traceId,
                    status = LearningEvaluationStatus.FAILED,
                    error = requireNotNull(evidence.error),
                )
        }
    }
}
