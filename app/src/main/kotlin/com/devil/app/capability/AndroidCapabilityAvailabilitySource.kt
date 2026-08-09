package com.devil.app.capability

import com.devil.core.model.capability.CapabilityAvailabilityState
import com.devil.core.model.capability.CapabilityContract

/**
 * Supplies bounded Android availability evidence for one already registered
 * capability.
 *
 * Implementations must not infer availability merely from registration, an
 * Android API, hardware feature, component, service, or permission.
 *
 * This source grants no authority and performs no capability execution.
 */
fun interface AndroidCapabilityAvailabilitySource {

    fun availability(
        capability: CapabilityContract,
    ): CapabilityAvailabilityState
}
