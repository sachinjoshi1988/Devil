package com.devil.core.runtime.memory

import com.devil.core.model.common.TraceId
import com.devil.core.model.memory.MemoryAuthorityRequest

/**
 * Default constitutional Memory Authority evaluator.
 *
 * The evaluator remains fail-closed unless genuine Memory Authority evidence
 * has been established.
 *
 * ESTABLISHED evidence makes the bounded MemoryAuthorityRequest eligible for
 * the existing Memory Authority result path. It still does not commit logical
 * memory, persist logical memory, mutate world state, assign memory class,
 * sensitivity, confidence, retention policy, source attribution,
 * owner-visible reason, or storage destination.
 *
 * DEFERRED evidence remains unavailable.
 *
 * FAILED evidence preserves its matching operational error.
 *
 * MEMORY_PROPOSAL != MEMORY_AUTHORITY_EVIDENCE.
 * MEMORY_AUTHORITY_EVIDENCE != MEMORY_AUTHORITY_APPROVAL.
 * MEMORY_AUTHORITY_APPROVAL != MEMORY_COMMITMENT.
 * MEMORY_COMMITMENT != MEMORY_PERSISTENCE.
 */
class DefaultMemoryAuthorityEvaluator :
    MemoryAuthorityEvaluator {

    override fun evaluate(
        traceId: TraceId,
        request: MemoryAuthorityRequest,
        evidence: MemoryAuthorityEvidenceResult,
    ): MemoryAuthorityEvaluationResult {
        require(
            request.proposal
                .learning
                .worldModelUpdate
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
            "Memory Authority evaluator trace and request must use the same trace identity."
        }

        require(evidence.traceId == traceId) {
            "Memory Authority evidence and evaluator must use the same trace identity."
        }

        return when (evidence.status) {
            MemoryAuthorityEvidenceStatus.ESTABLISHED -> {
                val capabilityId =
                    request.proposal
                        .learning
                        .worldModelUpdate
                        .outcome
                        .verification
                        .observation
                        .execution
                        .capability
                        .capabilityId

                require(evidence.capabilityId == capabilityId) {
                    "Memory Authority request and evidence must refer to the same capability identity."
                }

                MemoryAuthorityEvaluationResult.create(
                    traceId = traceId,
                    status = MemoryAuthorityEvaluationStatus.COMMITTABLE,
                    request = request,
                )
            }

            MemoryAuthorityEvidenceStatus.DEFERRED ->
                MemoryAuthorityEvaluationResult.create(
                    traceId = traceId,
                    status = MemoryAuthorityEvaluationStatus.UNAVAILABLE,
                )

            MemoryAuthorityEvidenceStatus.FAILED ->
                MemoryAuthorityEvaluationResult.create(
                    traceId = traceId,
                    status = MemoryAuthorityEvaluationStatus.FAILED,
                    error = requireNotNull(evidence.error),
                )
        }
    }
}
