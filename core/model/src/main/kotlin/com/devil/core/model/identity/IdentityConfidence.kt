package com.devil.core.model.identity

/**
 * Represents a normalized confidence value associated with identity reasoning.
 *
 * Confidence ranges from 0 to 100 inclusive. This value does not authenticate
 * a subject, establish evidence sufficiency, resolve identity, prove ownership,
 * evaluate trust, or grant authorization.
 */
@ConsistentCopyVisibility
data class IdentityConfidence private constructor(
    val value: Int,
) {
    companion object {
        fun from(rawValue: Int): IdentityConfidence {
            require(rawValue in 0..100) {
                "Identity confidence must be between 0 and 100 inclusive."
            }

            return IdentityConfidence(rawValue)
        }
    }
}
