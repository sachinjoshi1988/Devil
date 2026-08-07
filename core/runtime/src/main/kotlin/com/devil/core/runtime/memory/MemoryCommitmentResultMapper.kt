package com.devil.core.runtime.memory

import com.devil.core.model.common.TraceId

/**
 * Translates one bounded MemoryCommitmentEvaluationResult into the stable
 * operational MemoryCommitmentResult contract.
 *
 * This mapper does not create commitment evidence, create, persist, store,
 * expose, recall, or commit logical memory, mutate world state, change task or
 * plan state, communicate externally, or bypass the single Memory Authority.
 */
interface MemoryCommitmentResultMapper {

    fun map(
        traceId: TraceId,
        evaluation: MemoryCommitmentEvaluationResult,
    ): MemoryCommitmentResult
}
