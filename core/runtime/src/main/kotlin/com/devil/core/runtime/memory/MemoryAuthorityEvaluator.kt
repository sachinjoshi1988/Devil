package com.devil.core.runtime.memory

import com.devil.core.model.common.TraceId
import com.devil.core.model.memory.MemoryAuthorityRequest

/**
 * Evaluates one bounded request submitted to the single constitutional Memory
 * Authority.
 *
 * An evaluator must not approve logical-memory commitment without genuine
 * evidence, approved constitutional memory policy, security review, memory
 * classification, sensitivity assessment, confidence assessment, retention
 * policy, source attribution, and an owner-visible reason.
 *
 * It must never persist or commit logical memory directly.
 */
interface MemoryAuthorityEvaluator {

    fun evaluate(
        traceId: TraceId,
        request: MemoryAuthorityRequest,
    ): MemoryAuthorityEvaluationResult
}
