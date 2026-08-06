package com.devil.core.runtime.memory

import com.devil.core.model.common.TraceId
import com.devil.core.model.memory.MemoryProposalRequest

/**
 * Default Stage 18 constitutional memory-proposal evaluator.
 *
 * No approved constitutional memory-proposal policy, proposal-evidence source,
 * proposal-generation mechanism, sensitivity classification process, retention
 * policy evaluation, or Memory Authority review path exists yet.
 *
 * This evaluator therefore preserves trace continuity and returns UNAVAILABLE
 * rather than treating learnability as proof that a memory proposal should be
 * created.
 *
 * It does not create a memory proposal, approve or commit logical memory,
 * mutate world state, change task or plan state, communicate externally,
 * bypass the single Memory Authority, or produce a runtime result.
 */
class DefaultMemoryProposalEvaluator :
    MemoryProposalEvaluator {

    override fun evaluate(
        traceId: TraceId,
        request: MemoryProposalRequest,
    ): MemoryProposalEvaluationResult {
        require(
            request.learning
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
            "Memory proposal evaluator trace and request must use the same trace identity."
        }

        return MemoryProposalEvaluationResult.create(
            traceId = traceId,
            status = MemoryProposalEvaluationStatus.UNAVAILABLE,
        )
    }
}
