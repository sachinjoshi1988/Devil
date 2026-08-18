package com.devil.core.runtime.memory

import com.devil.core.model.common.TraceId
import com.devil.core.model.memory.MemoryRecallRequest

/**
 * Stage 106 bounded constitutional logical-memory recall evaluator.
 *
 * The evaluator receives one already-established Stage 105 MemoryRecallRequest.
 *
 * It must not create recall eligibility, manufacture another memory identity,
 * read storage, retrieve memory, expose content, derive privacy-disclosure
 * permission, execute an action, or claim successful recall.
 */
interface MemoryRecallEvaluator {

    fun evaluate(
        traceId: TraceId,
        request: MemoryRecallRequest,
    ): MemoryRecallEvaluationResult
}
