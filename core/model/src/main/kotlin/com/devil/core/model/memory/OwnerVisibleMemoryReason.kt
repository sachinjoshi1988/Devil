package com.devil.core.model.memory

/**
 * Preserves one bounded owner-visible explanation associated with a logical
 * memory representation.
 *
 * The reason is transparency metadata only.
 *
 * It does not establish:
 *
 * - owner identity;
 * - owner authentication;
 * - owner consent;
 * - Owner Mode;
 * - authorization;
 * - Memory Authority approval;
 * - commitment;
 * - persistence;
 * - disclosure permission;
 * - or deletion authority.
 *
 * OWNER_VISIBLE_REASON != OWNER_CONSENT.
 * OWNER_VISIBLE_REASON != AUTHORIZATION.
 * OWNER_VISIBLE_REASON != MEMORY_AUTHORITY_APPROVAL.
 */
@ConsistentCopyVisibility
data class OwnerVisibleMemoryReason private constructor(
    val value: String,
) {
    companion object {

        fun from(rawValue: String): OwnerVisibleMemoryReason {
            val normalizedValue =
                rawValue.trim()

            require(normalizedValue.isNotEmpty()) {
                "Owner-visible memory reason must not be blank."
            }

            return OwnerVisibleMemoryReason(
                value = normalizedValue,
            )
        }
    }
}
