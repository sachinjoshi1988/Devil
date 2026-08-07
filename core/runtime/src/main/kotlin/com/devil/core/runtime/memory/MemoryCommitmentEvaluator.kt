package com.devil.core.runtime.memory

import com.devil.core.model.common.TraceId
import com.devil.core.model.memory.MemoryCommitmentRequest

/**
 * Evaluates one bounded logical-memory commitment request under the single
 * constitutional Memory Authority.
 *
 * An evaluator must not establish commitment eligibility without genuine
 * evidence, approved constitutional policy, completed security review, memory
 * classification, sensitivity assessment, confidence assessment, retention
 * policy, source attribution, owner-visible reason, storage-destination
 * selection, and deletion-policy handling.
 *
 * It must never persist or commit logical memory directly.
 */
interface MemoryCommitmentEvaluator {

    fun evaluate(
        traceId: TraceId,
        request: MemoryCommitmentRequest,
    ): MemoryCommitmentEvaluationResult
}
