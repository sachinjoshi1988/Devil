package com.devil.core.runtime.memory

import com.devil.core.model.common.TraceId

/**
 * Default Stage 18 mapping from bounded memory-proposal evaluation into the
 * stable MemoryProposalResult contract.
 *
 * Genuine constitutional proposal evidence becomes operational PROPOSABLE and
 * preserves the bounded MemoryProposalRequest. Evaluation unavailability
 * becomes DEFERRED. Evaluation failure preserves its matching error.
 *
 * This mapper does not create, approve, or commit logical memory, mutate world
 * state, change task or plan state, communicate externally, or bypass the single
 * Memory Authority.
 */
class DefaultMemoryProposalResultMapper :
    MemoryProposalResultMapper {

    override fun map(
        traceId: TraceId,
        evaluation: MemoryProposalEvaluationResult,
    ): MemoryProposalResult {
        require(evaluation.traceId == traceId) {
            "Memory proposal result mapper trace and evaluation result must use the same trace identity."
        }

        return when (evaluation.status) {
            MemoryProposalEvaluationStatus.PROPOSABLE ->
                MemoryProposalResult.create(
                    traceId = traceId,
                    status = MemoryProposalStatus.PROPOSABLE,
                    request = requireNotNull(evaluation.request),
                )

            MemoryProposalEvaluationStatus.UNAVAILABLE ->
                MemoryProposalResult.create(
                    traceId = traceId,
                    status = MemoryProposalStatus.DEFERRED,
                )

            MemoryProposalEvaluationStatus.FAILED ->
                MemoryProposalResult.create(
                    traceId = traceId,
                    status = MemoryProposalStatus.FAILED,
                    error = requireNotNull(evaluation.error),
                )
        }
    }
}
