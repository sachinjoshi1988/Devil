package com.devil.core.runtime.memory

import com.devil.core.model.common.TraceId
import com.devil.core.model.memory.MemoryAuthorityRequest

/**
 * Default Stage 19 constitutional Memory Authority evaluator.
 *
 * No approved constitutional memory-commitment policy, security-review path,
 * memory-classification process, sensitivity assessment, confidence assessment,
 * retention-policy evaluation, source-attribution process, owner-visible reason
 * generation, or persistent logical-memory mechanism exists yet.
 *
 * This evaluator therefore preserves trace continuity and returns UNAVAILABLE
 * rather than treating a proposable memory request as permission to create,
 * persist, or commit logical memory.
 *
 * It does not mutate world state, change task or plan state, communicate
 * externally, bypass constitutional security review, or produce a runtime
 * result.
 */
class DefaultMemoryAuthorityEvaluator :
    MemoryAuthorityEvaluator {

    override fun evaluate(
        traceId: TraceId,
        request: MemoryAuthorityRequest,
    ): MemoryAuthorityEvaluationResult {
        require(
            request.proposal
                .learning
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
            "Memory Authority evaluator trace and request must use the same trace identity."
        }

        return MemoryAuthorityEvaluationResult.create(
            traceId = traceId,
            status = MemoryAuthorityEvaluationStatus.UNAVAILABLE,
        )
    }
}
