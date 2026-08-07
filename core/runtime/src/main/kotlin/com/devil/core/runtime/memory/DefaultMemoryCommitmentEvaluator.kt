package com.devil.core.runtime.memory

import com.devil.core.model.common.TraceId
import com.devil.core.model.memory.MemoryCommitmentRequest

/**
 * Default Stage 20 constitutional logical-memory commitment evaluator.
 *
 * No approved constitutional commitment policy, completed security-review
 * mechanism, memory-classification process, sensitivity assessment, confidence
 * assessment, retention-policy evaluation, source-attribution process,
 * owner-visible reason generation, storage-destination selection,
 * deletion-policy handling, or persistent logical-memory mechanism exists yet.
 *
 * This evaluator therefore preserves trace continuity and returns UNAVAILABLE
 * rather than treating a MemoryCommitmentRequest as permission to create,
 * persist, store, expose, recall, or commit logical memory.
 *
 * It invokes no database, filesystem, cloud service, Android platform API, or
 * external communication mechanism. It does not mutate world state, change task
 * or plan state, bypass the single Memory Authority, or produce a runtime
 * result.
 */
class DefaultMemoryCommitmentEvaluator :
    MemoryCommitmentEvaluator {

    override fun evaluate(
        traceId: TraceId,
        request: MemoryCommitmentRequest,
    ): MemoryCommitmentEvaluationResult {
        require(
            request.authorityRequest
                .proposal
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
            "Memory commitment evaluator trace and request must use the same trace identity."
        }

        return MemoryCommitmentEvaluationResult.create(
            traceId = traceId,
            status = MemoryCommitmentEvaluationStatus.UNAVAILABLE,
        )
    }
}
