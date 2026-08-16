package com.devil.core.model.memory

/**
 * Describes the bounded retention classification attached to one logical-memory
 * representation.
 *
 * This value records policy context only.
 *
 * It does not:
 *
 * - enforce retention;
 * - calculate expiry;
 * - delete memory;
 * - prevent deletion;
 * - persist memory;
 * - select storage;
 * - authorize storage;
 * - establish durable lifetime;
 * - or prove that retention policy was executed.
 *
 * RETENTION_CLASSIFICATION != RETENTION_ENFORCEMENT.
 * RETENTION_CLASSIFICATION != PERSISTENCE.
 * RETENTION_CLASSIFICATION != DELETION_EXECUTION.
 */
enum class MemoryRetention {
    SESSION,
    SHORT_TERM,
    LONG_TERM,
    UNTIL_DELETED,
}
