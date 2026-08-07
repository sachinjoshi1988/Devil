package com.devil.core.runtime.memory

import com.devil.core.model.common.TraceId

/**
 * Translates one bounded MemoryAuthorityEvaluationResult into the stable
 * operational MemoryAuthorityResult contract.
 *
 * This mapper does not create evaluation evidence, create, persist, or commit
 * logical memory, mutate world state, change task or plan state, communicate
 * externally, or bypass constitutional security review.
 */
interface MemoryAuthorityResultMapper {

    fun map(
        traceId: TraceId,
        evaluation: MemoryAuthorityEvaluationResult,
    ): MemoryAuthorityResult
}
