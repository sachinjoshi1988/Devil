package com.devil.core.model.error

/**
 * Identifies one stable class of Devil failure.
 *
 * Error code ownership belongs to the authority that defines the failure.
 * This type validates and represents the code without interpreting it.
 */
@ConsistentCopyVisibility
data class ErrorCode private constructor(
    val value: String,
) {
    companion object {
        fun from(rawValue: String): ErrorCode {
            val normalizedValue = rawValue.trim()

            require(normalizedValue.isNotEmpty()) {
                "Error code must not be blank."
            }

            return ErrorCode(normalizedValue)
        }
    }
}
