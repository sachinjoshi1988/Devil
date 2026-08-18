package com.devil.core.runtime.memory

/**
 * Stage 105 bounded status describing whether one constitutional logical-memory
 * recall request is available.
 *
 * AVAILABLE means one MemoryRecallRequest was formed only from an existing Stage 104
 * ELIGIBLE result.
 *
 * UNAVAILABLE means no justified recall request can currently be established.
 *
 * FAILED preserves one genuine matching upstream Stage 104 failure.
 *
 * AVAILABLE does not mean logical memory was read, retrieved, exposed, disclosed,
 * recalled, decrypted, restored, or otherwise supplied to any caller.
 *
 * RECALL_REQUEST_AVAILABLE != MEMORY_RECALL.
 * RECALL_REQUEST_AVAILABLE != STORAGE_READ.
 * RECALL_REQUEST_AVAILABLE != RETRIEVAL_SUCCESS.
 * RECALL_REQUEST_AVAILABLE != DISCLOSURE_PERMISSION.
 */
enum class MemoryRecallRequestStatus {
    AVAILABLE,
    UNAVAILABLE,
    FAILED,
}
