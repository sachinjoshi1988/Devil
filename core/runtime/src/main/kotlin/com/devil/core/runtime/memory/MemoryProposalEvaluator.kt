package com.devil.core.runtime.memory

import com.devil.core.model.common.TraceId
import com.devil.core.model.memory.MemoryProposalRequest

/**
 * Evaluates one bounded constitutional memory-proposal request.
 *
 * An evaluator must not create or approve a memory proposal without genuine
 * supporting evidence and approved constitutional policy. It must never commit
 * logical memory directly or bypass the single Memory Authority.
 */
interface MemoryProposalEvaluator {

    fun evaluate(
        traceId: TraceId,
        request: MemoryProposalRequest,
    ): MemoryProposalEvaluationResult
}
