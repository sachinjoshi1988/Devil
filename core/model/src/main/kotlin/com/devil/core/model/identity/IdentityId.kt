package com.devil.core.model.identity

/**
 * Identifies one subject recognized by Devil.
 *
 * This identity does not prove authentication, trust, ownership, relationship,
 * or authorization. Those determinations belong to later authorities.
 */
@ConsistentCopyVisibility
data class IdentityId private constructor(
    val value: String,
) {
    companion object {
        fun from(rawValue: String): IdentityId {
            val normalizedValue = rawValue.trim()

            require(normalizedValue.isNotEmpty()) {
                "Identity must not be blank."
            }

            return IdentityId(normalizedValue)
        }
    }
}
