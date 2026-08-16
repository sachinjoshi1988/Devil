package com.devil.core.runtime.memory

/**
 * Stage 102 bounded status for logical-memory representation preparation.
 *
 * PREPARED
 * means one LogicalMemoryRepresentation was formed only after:
 *
 * - the existing single Memory Authority returned COMMITTABLE;
 * - the exact approved MemoryAuthorityRequest was preserved;
 * - Stage 100 owner / multi-user subject context was ESTABLISHED;
 * - the supplied subject identity matched the current subject identity;
 * - and every Stage 101 memory metadata value was explicitly supplied.
 *
 * DEFERRED
 * means those bounded prerequisites are not currently established.
 *
 * FAILED
 * preserves a genuine matching upstream failure.
 *
 * PREPARED does not mean logical memory was committed, persisted, stored,
 * exposed, recalled, deleted, synchronized, replicated, encrypted, or made
 * available for future recall.
 *
 * PREPARED != MEMORY_COMMITMENT.
 * PREPARED != MEMORY_PERSISTENCE.
 * PREPARED != STORAGE_SUCCESS.
 * PREPARED != RECALL_AVAILABILITY.
 */
enum class MemoryRepresentationPreparationStatus {
    PREPARED,
    DEFERRED,
    FAILED,
}
