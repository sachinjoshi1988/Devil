package com.devil.app.outcome

import com.devil.core.model.capability.CapabilityId

/**
 * Preserves one bounded piece of genuine Android outcome evidence.
 *
 * capabilityId identifies the capability whose independently verified Android
 * effect contributed to this bounded outcome determination.
 *
 * description preserves only what an approved Android outcome mechanism
 * genuinely established.
 *
 * This evidence does not claim task or plan completion, mutate world state,
 * create learning or memory, or report broader success.
 */
@ConsistentCopyVisibility
data class AndroidOutcomeEvidence private constructor(
    val capabilityId: CapabilityId,
    val description: String,
) {
    companion object {
        fun create(
            capabilityId: CapabilityId,
            description: String,
        ): AndroidOutcomeEvidence {
            val normalizedDescription = description.trim()

            require(normalizedDescription.isNotEmpty()) {
                "Android outcome evidence description must not be blank."
            }

            return AndroidOutcomeEvidence(
                capabilityId = capabilityId,
                description = normalizedDescription,
            )
        }
    }
}
