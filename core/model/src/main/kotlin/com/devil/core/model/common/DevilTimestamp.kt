package com.devil.core.model.common

/**
 * Represents an absolute UTC timestamp as milliseconds since the Unix epoch.
 *
 * Clock access and timestamp creation belong to runtime adapters. This type
 * only validates and represents an already observed timestamp.
 */
@ConsistentCopyVisibility
data class DevilTimestamp private constructor(
    val epochMilliseconds: Long,
) {
    companion object {
        fun fromEpochMilliseconds(rawValue: Long): DevilTimestamp {
            require(rawValue >= 0L) {
                "Timestamp must not be earlier than the Unix epoch."
            }

            return DevilTimestamp(rawValue)
        }
    }
}
