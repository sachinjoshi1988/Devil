package com.devil.core.model.capability

/**
 * Declares one registered Devil capability.
 *
 * This contract identifies a capability and its constitutional purpose.
 * It does not represent authorization, availability, health, permissions,
 * execution, observations, or outcomes.
 */
@ConsistentCopyVisibility
data class CapabilityContract private constructor(
    val capabilityId: CapabilityId,
    val category: CapabilityCategory,
    val name: String,
    val description: String,
) {
    companion object {
        fun create(
            capabilityId: CapabilityId,
            category: CapabilityCategory,
            name: String,
            description: String,
        ): CapabilityContract {
            val normalizedName = name.trim()
            val normalizedDescription = description.trim()

            require(normalizedName.isNotEmpty()) {
                "Capability name must not be blank."
            }

            require(normalizedDescription.isNotEmpty()) {
                "Capability description must not be blank."
            }

            return CapabilityContract(
                capabilityId = capabilityId,
                category = category,
                name = normalizedName,
                description = normalizedDescription,
            )
        }
    }
}
