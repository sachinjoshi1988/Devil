package com.devil.core.runtime.memory

import com.devil.core.model.common.TraceId

/**
 * Default Stage 19 mapping from bounded Memory Authority evaluation into the
 * stable MemoryAuthorityResult contract.
 *
 * Genuine constitutional commitment eligibility becomes operational
 * COMMITTABLE and preserves the bounded MemoryAuthorityRequest. Evaluation
 * unavailability becomes DEFERRED. Evaluation failure preserves its matching
 * error.
 *
 * This mapper does not create, persist, or commit logical memory, mutate world
 * state, change task or plan state, communicate externally, assign memory
 * metadata, or bypass constitutional security review.
 */
class DefaultMemoryAuthorityResultMapper :
    MemoryAuthorityResultMapper {

    override fun map(
        traceId: TraceId,
        evaluation: MemoryAuthorityEvaluationResult,
    ): MemoryAuthorityResult {
        require(evaluation.traceId == traceId) {
            "Memory Authority result mapper trace and evaluation result must use the same trace identity."
        }

        return when (evaluation.status) {
            MemoryAuthorityEvaluationStatus.COMMITTABLE ->
                MemoryAuthorityResult.create(
                    traceId = traceId,
                    status = MemoryAuthorityStatus.COMMITTABLE,
                    request = requireNotNull(evaluation.request),
                )

            MemoryAuthorityEvaluationStatus.UNAVAILABLE ->
                MemoryAuthorityResult.create(
                    traceId = traceId,
                    status = MemoryAuthorityStatus.DEFERRED,
                )

            MemoryAuthorityEvaluationStatus.FAILED ->
                MemoryAuthorityResult.create(
                    traceId = traceId,
                    status = MemoryAuthorityStatus.FAILED,
                    error = requireNotNull(evaluation.error),
                )
        }
    }
}
