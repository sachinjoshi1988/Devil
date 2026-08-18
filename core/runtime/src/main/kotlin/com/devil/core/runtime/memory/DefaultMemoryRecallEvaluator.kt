package com.devil.core.runtime.memory

import com.devil.core.model.common.TraceId
import com.devil.core.model.memory.MemoryRecallRequest

/**
 * Default Stage 106 constitutional logical-memory recall evaluator.
 *
 * Stage 105 establishes only one bounded recall request.
 *
 * The current repository has no approved production logical-memory read source,
 * storage-read port, durable logical-memory retrieval mechanism, governed
 * decryption/restoration mechanism, or constitutional bridge from memory
 * sensitivity into privacy-disclosure permission.
 *
 * Therefore this evaluator fails closed as UNAVAILABLE.
 *
 * It preserves constitutional trace continuity but does not preserve the request
 * inside an affirmative RECALLABLE result because the missing retrieval and
 * disclosure prerequisites do not justify that state.
 *
 * This evaluator does not:
 *
 * - read a database;
 * - read a filesystem;
 * - read Android storage;
 * - read cloud storage;
 * - invoke a network service;
 * - retrieve logical memory;
 * - restore logical memory;
 * - decrypt logical memory;
 * - recall logical memory;
 * - expose or disclose logical-memory content;
 * - establish privacy-disclosure permission;
 * - map MemorySensitivity to privacy-disclosure policy;
 * - create, commit, persist, delete, or mutate logical memory;
 * - introduce another MemoryId;
 * - modify the Unified Devil Runtime;
 * - modify Stage 49 constitutional ordering;
 * - execute an action;
 * - or establish verified success.
 *
 * RECALL_EVALUATION_UNAVAILABLE != MEMORY_RECALL.
 * RECALL_REQUEST != STORAGE_READ_AUTHORIZATION.
 * AUTHORIZATION != PRIVACY_DISCLOSURE_PERMISSION.
 */
class DefaultMemoryRecallEvaluator :
    MemoryRecallEvaluator {

    override fun evaluate(
        traceId: TraceId,
        request: MemoryRecallRequest,
    ): MemoryRecallEvaluationResult {
        require(request.traceId == traceId) {
            "Memory recall evaluator trace and request must use the same trace identity."
        }

        return MemoryRecallEvaluationResult.create(
            traceId = traceId,
            status = MemoryRecallEvaluationStatus.UNAVAILABLE,
        )
    }
}
