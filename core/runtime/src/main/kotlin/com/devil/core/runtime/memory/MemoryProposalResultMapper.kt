package com.devil.core.runtime.memory

import com.devil.core.model.common.TraceId

/**
 * Translates one bounded constitutional memory-proposal evaluation result into
 * the stable operational MemoryProposalResult contract.
 *
 * This mapper does not create proposal evidence, create or commit logical
 * memory, mutate world state, change task or plan state, communicate externally,
 * or bypass the single Memory Authority.
 */
interface MemoryProposalResultMapper {

    fun map(
        traceId: TraceId,
        evaluation: MemoryProposalEvaluationResult,
    ): MemoryProposalResult
}
