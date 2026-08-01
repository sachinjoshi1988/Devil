package com.devil.core.model.capability

/**
 * Identifies one registered capability throughout its lifecycle.
 *
 * Capability registration belongs to the Capability Authority. This type only
 * validates and represents an existing capability identity.
 */
@ConsistentCopyVisibility
data class CapabilityId private constructor(
    val value: String,
) {
    companion object {
        fun from(rawValue: String): CapabilityId {
            val normalizedValue = rawValue.trim()

            require(normalizedValue.isNotEmpty()) {
                "Capability identity must not be blank."
            }

            return CapabilityId(normalizedValue)
        }
    }
}
