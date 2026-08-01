package com.devil.core.model.common

/**
 * Identifies one traceable flow through the Devil runtime.
 *
 * Trace creation belongs to the runtime authority. This type only validates
 * and represents an existing trace identity.
 */
@ConsistentCopyVisibility
data class TraceId private constructor(
    val value: String,
) {
    companion object {
        fun from(rawValue: String): TraceId {
            val normalizedValue = rawValue.trim()

            require(normalizedValue.isNotEmpty()) {
                "Trace identity must not be blank."
            }

            return TraceId(normalizedValue)
        }
    }
}
