package com.devil.core.runtime.memory

import com.devil.core.model.common.TraceId

/**
 * Default Stage 20 mapping from bounded logical-memory commitment evaluation
 * into the stable MemoryCommitmentResult contract.
 *
 * Genuine constitutional commitment eligibility becomes operational
 * COMMITTABLE and preserves one bounded MemoryCommitmentRequest. Evaluation
 * unavailability becomes DEFERRED. Evaluation failure preserves its matching
 * error.
 *
 * This mapper does not create, persist, store, expose, recall, or commit logical
 * memory. It does not assign memory metadata, invoke storage, mutate world
 * state, change task or plan state, communicate externally, or bypass the
 * single Memory Authority.
 */
class DefaultMemoryCommitmentResultMapper :
    MemoryCommitmentResultMapper {

    override fun map(
        traceId: TraceId,
        evaluation: MemoryCommitmentEvaluationResult,
    ): MemoryCommitmentResult {
        require(evaluation.traceId == traceId) {
            "Memory commitment result mapper trace and evaluation result must use the same trace identity."
        }

        return when (evaluation.status) {
            MemoryCommitmentEvaluationStatus.COMMITTABLE ->
                MemoryCommitmentResult.create(
                    traceId = traceId,
                    status = MemoryCommitmentStatus.COMMITTABLE,
                    request = requireNotNull(evaluation.request),
                )

            MemoryCommitmentEvaluationStatus.UNAVAILABLE ->
                MemoryCommitmentResult.create(
                    traceId = traceId,
                    status = MemoryCommitmentStatus.DEFERRED,
                )

            MemoryCommitmentEvaluationStatus.FAILED ->
                MemoryCommitmentResult.create(
                    traceId = traceId,
                    status = MemoryCommitmentStatus.FAILED,
                    error = requireNotNull(evaluation.error),
                )
        }
    }
}
