package com.devil.core.runtime.memory

import com.devil.core.model.common.TraceId

/**
 * Default Stage 21 mapping from bounded logical-memory persistence evaluation
 * into the stable MemoryPersistenceResult contract.
 *
 * Genuine constitutional persistence eligibility becomes operational
 * PERSISTABLE and preserves one bounded MemoryPersistenceRequest.
 *
 * Evaluation unavailability becomes DEFERRED.
 *
 * Evaluation failure preserves its matching error.
 *
 * This mapper does not create, persist, store, expose, recall, delete, or
 * commit logical memory.
 *
 * It does not assign or alter memory metadata, invoke a database, filesystem,
 * cloud service, Android platform API, network service, or other storage
 * mechanism.
 *
 * It does not mutate world state, change task or plan state, communicate
 * externally, bypass the single Memory Authority, or produce persistence side
 * effects.
 */
class DefaultMemoryPersistenceResultMapper :
    MemoryPersistenceResultMapper {

    override fun map(
        traceId: TraceId,
        evaluation: MemoryPersistenceEvaluationResult,
    ): MemoryPersistenceResult {
        require(evaluation.traceId == traceId) {
            "Memory persistence result mapper trace and evaluation result must use the same trace identity."
        }

        return when (evaluation.status) {
            MemoryPersistenceEvaluationStatus.PERSISTABLE ->
                MemoryPersistenceResult.create(
                    traceId = traceId,
                    status = MemoryPersistenceStatus.PERSISTABLE,
                    request = requireNotNull(evaluation.request),
                )

            MemoryPersistenceEvaluationStatus.UNAVAILABLE ->
                MemoryPersistenceResult.create(
                    traceId = traceId,
                    status = MemoryPersistenceStatus.DEFERRED,
                )

            MemoryPersistenceEvaluationStatus.FAILED ->
                MemoryPersistenceResult.create(
                    traceId = traceId,
                    status = MemoryPersistenceStatus.FAILED,
                    error = requireNotNull(evaluation.error),
                )
        }
    }
}
