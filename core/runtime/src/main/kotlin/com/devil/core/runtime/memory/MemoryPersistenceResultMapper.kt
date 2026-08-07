package com.devil.core.runtime.memory

import com.devil.core.model.common.TraceId

/**
 * Translates one bounded MemoryPersistenceEvaluationResult into the stable
 * operational MemoryPersistenceResult contract.
 *
 * This mapper does not create persistence evidence, create, persist, store,
 * expose, recall, delete, or commit logical memory.
 *
 * It does not assign or alter logical-memory metadata, invoke storage, mutate
 * world state, change task or plan state, communicate externally, bypass the
 * single Memory Authority, or produce persistence side effects.
 */
interface MemoryPersistenceResultMapper {

    fun map(
        traceId: TraceId,
        evaluation: MemoryPersistenceEvaluationResult,
    ): MemoryPersistenceResult
}
