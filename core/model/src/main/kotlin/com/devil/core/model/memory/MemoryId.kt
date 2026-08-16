package com.devil.core.model.memory

/**
 * Identifies one bounded logical-memory representation.
 *
 * Memory identity is descriptive continuity only.
 *
 * Possessing a MemoryId does not establish:
 *
 * - that logical memory was approved;
 * - that Memory Authority reviewed it;
 * - that memory was committed;
 * - that memory was persisted;
 * - that durable storage exists;
 * - that memory can be recalled;
 * - that memory is true;
 * - that memory is current;
 * - that memory belongs to an authenticated owner;
 * - or that any action is authorized.
 *
 * Memory identity creation belongs to an approved constitutional mechanism.
 * This type only validates and represents an already-created identifier.
 *
 * MEMORY_ID != MEMORY_AUTHORITY_APPROVAL.
 * MEMORY_ID != MEMORY_COMMITMENT.
 * MEMORY_ID != MEMORY_PERSISTENCE.
 */
@ConsistentCopyVisibility
data class MemoryId private constructor(
    val value: String,
) {
    companion object {

        fun from(rawValue: String): MemoryId {
            val normalizedValue =
                rawValue.trim()

            require(normalizedValue.isNotEmpty()) {
                "Memory identity must not be blank."
            }

            return MemoryId(
                value = normalizedValue,
            )
        }
    }
}
