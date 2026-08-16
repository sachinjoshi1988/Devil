package com.devil.core.model.memory

/**
 * Immutable provenance descriptor for one logical-memory representation.
 *
 * sourceId identifies the supplied source instance or provenance identity.
 *
 * sourceType preserves an extensible descriptive source category.
 *
 * Constructing MemorySource does not establish:
 *
 * - that the source is authentic;
 * - that the source is trusted;
 * - that supplied information is true;
 * - that evidence is sufficient;
 * - that the current subject owns the source;
 * - that Memory Authority approved the source;
 * - that memory may be committed;
 * - that memory may be persisted;
 * - or that any capability is authorized.
 *
 * MEMORY_SOURCE != TRUSTED_SOURCE.
 * MEMORY_SOURCE != VERIFIED_EVIDENCE.
 * MEMORY_SOURCE != MEMORY_AUTHORITY_APPROVAL.
 */
@ConsistentCopyVisibility
data class MemorySource private constructor(
    val sourceId: String,
    val sourceType: String,
) {
    companion object {

        fun create(
            sourceId: String,
            sourceType: String,
        ): MemorySource {
            val normalizedSourceId =
                sourceId.trim()

            val normalizedSourceType =
                sourceType.trim()

            require(normalizedSourceId.isNotEmpty()) {
                "Memory source identity must not be blank."
            }

            require(normalizedSourceType.isNotEmpty()) {
                "Memory source type must not be blank."
            }

            return MemorySource(
                sourceId = normalizedSourceId,
                sourceType = normalizedSourceType,
            )
        }
    }
}
