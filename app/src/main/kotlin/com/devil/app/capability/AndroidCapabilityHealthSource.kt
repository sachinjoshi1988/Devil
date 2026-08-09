package com.devil.app.capability

import com.devil.core.model.capability.CapabilityContract
import com.devil.core.model.capability.CapabilityHealthState

/**
 * Supplies bounded Android health evidence for one already registered
 * capability.
 *
 * Capability health is not Executive readiness and does not establish Android
 * permission, Devil authorization, execution permission, execution success,
 * observation, verification, or outcome.
 */
fun interface AndroidCapabilityHealthSource {

    fun health(
        capability: CapabilityContract,
    ): CapabilityHealthState
}
