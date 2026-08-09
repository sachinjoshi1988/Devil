package com.devil.app.capability

import com.devil.core.model.capability.CapabilityAvailabilityState
import com.devil.core.model.capability.CapabilityContract
import com.devil.core.model.capability.CapabilityHealthState

/**
 * Preserves one Android embodiment's bounded availability and health view of one
 * already registered CapabilityContract.
 *
 * This state does not register a capability and does not establish Devil
 * authorization, Executive readiness, Android permission, execution permission,
 * execution success, observation, verification, or outcome.
 *
 * CapabilityHealthState.READY means health only. It must never be interpreted
 * as Executive readiness.
 */
@ConsistentCopyVisibility
data class AndroidCapabilityState private constructor(
    val capability: CapabilityContract,
    val availability: CapabilityAvailabilityState,
    val health: CapabilityHealthState,
) {
    companion object {
        fun create(
            capability: CapabilityContract,
            availability: CapabilityAvailabilityState,
            health: CapabilityHealthState,
        ): AndroidCapabilityState {
            return AndroidCapabilityState(
                capability = capability,
                availability = availability,
                health = health,
            )
        }
    }
}
