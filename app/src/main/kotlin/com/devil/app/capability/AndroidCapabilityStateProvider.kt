package com.devil.app.capability

import com.devil.core.model.capability.CapabilityContract

/**
 * Produces one bounded Android availability-and-health state for an already
 * registered CapabilityContract.
 *
 * This provider does not register, select, authorize, prepare, activate,
 * execute, observe, verify, complete, or fail a capability.
 */
fun interface AndroidCapabilityStateProvider {

    fun stateOf(
        capability: CapabilityContract,
    ): AndroidCapabilityState
}
