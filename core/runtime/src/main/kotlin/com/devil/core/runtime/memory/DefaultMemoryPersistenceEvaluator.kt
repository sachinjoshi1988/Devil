package com.devil.core.runtime.memory

import com.devil.core.model.common.TraceId
import com.devil.core.model.memory.MemoryPersistenceRequest

/**
 * Default Stage 21 constitutional logical-memory persistence evaluator.
 *
 * No approved constitutional persistence policy, completed security-review
 * mechanism, complete memory-classification process, sensitivity assessment,
 * confidence assessment, retention-policy evaluation, source-attribution
 * process, owner-visible reason generation, storage-destination approval,
 * deletion-policy handling, encryption-policy handling, replication-policy
 * handling, persistence evidence source, or authorized logical-memory storage
 * mechanism exists yet.
 *
 * This evaluator therefore preserves trace continuity and returns UNAVAILABLE
 * rather than treating a MemoryPersistenceRequest as permission to create,
 * persist, store, expose, recall, delete, or commit logical memory.
 *
 * It invokes no database, filesystem, cloud service, Android platform API,
 * network service, or external communication mechanism. It does not mutate
 * world state, change task or plan state, bypass the single Memory Authority,
 * or produce a runtime result.
 */
class DefaultMemoryPersistenceEvaluator :
    MemoryPersistenceEvaluator {

    override fun evaluate(
        traceId: TraceId,
        request: MemoryPersistenceRequest,
    ): MemoryPersistenceEvaluationResult {
        require(
            request.commitmentRequest
                .authorityRequest
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
            "Memory persistence evaluator trace and request must use the same trace identity."
        }

        return MemoryPersistenceEvaluationResult.create(
            traceId = traceId,
            status = MemoryPersistenceEvaluationStatus.UNAVAILABLE,
        )
    }
}
