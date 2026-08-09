package com.devil.app.capability

import com.devil.core.model.capability.CapabilityContract
import com.devil.core.model.capability.CapabilityHealthState

/**
 * Default Stage 28 Android capability-health source.
 *
 * No production Android capability implementation currently supplies genuine
 * capability-health evidence.
 *
 * Therefore this source returns UNAVAILABLE rather than fabricating READY,
 * DEGRADED, BUSY, or any other health state.
 *
 * READY would describe capability health only and would still not establish
 * constitutional Executive readiness.
 *
 * This implementation invokes no Android platform API and performs no action.
 */
class DefaultAndroidCapabilityHealthSource :
    AndroidCapabilityHealthSource {

    override fun health(
        capability: CapabilityContract,
    ): CapabilityHealthState {
        return CapabilityHealthState.UNAVAILABLE
    }
}
