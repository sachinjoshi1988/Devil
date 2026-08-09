package com.devil.app.observation

import com.devil.core.model.capability.CapabilityId

/**
 * Preserves one bounded piece of genuine Android execution-observation evidence.
 *
 * capabilityId identifies the capability whose Stage 30 Android execution
 * attempt was actually observed.
 *
 * description preserves only what the approved Android observation mechanism
 * genuinely established.
 *
 * This evidence does not verify that the intended outcome was achieved, report
 * task success, update world state, change task or plan state, or establish a
 * final Outcome.
 */
@ConsistentCopyVisibility
data class AndroidObservationEvidence private constructor(
    val capabilityId: CapabilityId,
    val description: String,
) {
    companion object {
        fun create(
            capabilityId: CapabilityId,
            description: String,
        ): AndroidObservationEvidence {
            val normalizedDescription = description.trim()

            require(normalizedDescription.isNotEmpty()) {
                "Android observation evidence description must not be blank."
            }

            return AndroidObservationEvidence(
                capabilityId = capabilityId,
                description = normalizedDescription,
            )
        }
    }
}
