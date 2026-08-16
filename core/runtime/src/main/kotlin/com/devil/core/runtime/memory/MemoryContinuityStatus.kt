package com.devil.core.runtime.memory

/**
 * Stage 103 bounded status for logical-memory continuity.
 *
 * ESTABLISHED means one MemoryContinuityRecord was formed only from:
 *
 * - one existing Stage 102 PREPARED memory representation result;
 * - the exact prepared LogicalMemoryRepresentation;
 * - an ESTABLISHED Stage 100 owner / multi-user context;
 * - the same constitutional trace identity;
 * - and the same current subject identity.
 *
 * DEFERRED means those bounded prerequisites are not currently established.
 *
 * FAILED preserves one genuine matching upstream failure.
 *
 * ESTABLISHED does not mean logical memory was committed, persisted, stored,
 * indexed, exposed, made available for future recall, recalled, deleted,
 * synchronized, replicated, encrypted, or restored across sessions.
 *
 * MEMORY_CONTINUITY_ESTABLISHED != MEMORY_COMMITMENT.
 * MEMORY_CONTINUITY_ESTABLISHED != MEMORY_PERSISTENCE.
 * MEMORY_CONTINUITY_ESTABLISHED != STORAGE_SUCCESS.
 * MEMORY_CONTINUITY_ESTABLISHED != RECALL_AVAILABILITY.
 * MEMORY_CONTINUITY_ESTABLISHED != MEMORY_RECALL.
 */
enum class MemoryContinuityStatus {
    ESTABLISHED,
    DEFERRED,
    FAILED,
}
