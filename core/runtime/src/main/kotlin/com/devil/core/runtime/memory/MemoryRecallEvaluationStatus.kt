package com.devil.core.runtime.memory

/**
 * Stage 106 bounded status for constitutional logical-memory recall evaluation.
 *
 * RECALLABLE means genuine governed evidence established that one existing
 * MemoryRecallRequest may approach a later authorized logical-memory retrieval
 * mechanism.
 *
 * UNAVAILABLE means the current architecture cannot safely justify actual recall.
 *
 * FAILED means recall evaluation encountered one genuine matching operational
 * failure.
 *
 * RECALLABLE does not itself mean logical memory was read, retrieved, restored,
 * decrypted, exposed, disclosed, presented, or recalled.
 *
 * Stage 106 introduces no logical-memory storage-read mechanism.
 *
 * RECALLABLE != MEMORY_RECALL.
 * RECALLABLE != STORAGE_READ.
 * RECALLABLE != RETRIEVAL_SUCCESS.
 * RECALLABLE != DISCLOSURE_PERMISSION.
 * RECALLABLE != VERIFIED_SUCCESS.
 */
enum class MemoryRecallEvaluationStatus {
    RECALLABLE,
    UNAVAILABLE,
    FAILED,
}
