package com.devil.app.verification

import com.devil.core.model.capability.CapabilityId

/**
 * Preserves one bounded piece of genuine Android verification evidence.
 *
 * capabilityId identifies the capability whose observed Android effect was
 * independently verified.
 *
 * description preserves only what the approved Android verification mechanism
 * genuinely established.
 *
 * This evidence does not establish a final constitutional Outcome, claim task
 * or plan completion, update world state, create logical memory, or report
 * broader success beyond the bounded fact actually verified.
 */
@ConsistentCopyVisibility
data class AndroidVerificationEvidence private constructor(
    val capabilityId: CapabilityId,
    val description: String,
) {
    companion object {
        fun create(
            capabilityId: CapabilityId,
            description: String,
        ): AndroidVerificationEvidence {
            val normalizedDescription = description.trim()

            require(normalizedDescription.isNotEmpty()) {
                "Android verification evidence description must not be blank."
            }

            return AndroidVerificationEvidence(
                capabilityId = capabilityId,
                description = normalizedDescription,
            )
        }
    }
}
