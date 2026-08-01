package com.devil.core.model.common

/**
 * Identifies the schema version of a Devil contract.
 *
 * This value describes contract structure only. It does not grant authority,
 * select capabilities, or change constitutional behavior.
 */
@ConsistentCopyVisibility
data class SchemaVersion private constructor(
    val value: Int,
) {
    companion object {
        fun from(rawValue: Int): SchemaVersion {
            require(rawValue > 0) {
                "Schema version must be greater than zero."
            }

            return SchemaVersion(rawValue)
        }
    }
}
