package com.devil.core.runtime.memory

import com.devil.core.model.common.TraceId
import com.devil.core.model.memory.MemoryPersistenceRequest

/**
 * Evaluates one bounded logical-memory persistence request under the single
 * constitutional Memory Authority.
 *
 * An evaluator must not establish persistence eligibility without genuine
 * evidence, approved constitutional policy, completed security review,
 * complete memory classification, sensitivity and confidence assessment,
 * retention handling, source attribution, owner-visible reasoning,
 * storage-destination approval, deletion-policy handling, encryption-policy
 * handling, replication-policy handling, and an explicitly authorized
 * persistence mechanism.
 *
 * It must never create, persist, store, expose, recall, delete, or commit
 * logical memory directly.
 */
interface MemoryPersistenceEvaluator {

    fun evaluate(
        traceId: TraceId,
        request: MemoryPersistenceRequest,
    ): MemoryPersistenceEvaluationResult
}
