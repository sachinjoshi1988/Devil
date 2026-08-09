package com.devil.app.capability

import com.devil.app.accessibility.AndroidAccessibilityCapability
import com.devil.app.accessibility.DevilAccessibilityServiceRegistry
import com.devil.core.model.capability.CapabilityContract
import com.devil.core.model.capability.CapabilityHealthState

/**
 * Default Android capability-health source.
 *
 * Stage 38 reports READY for the registered Android accessibility capability only
 * while DevilAccessibilityService is genuinely connected.
 *
 * READY describes capability health only.
 *
 * READY != Executive readiness.
 * READY != authentication.
 * READY != Devil authorization.
 * READY != Execution APPROVED.
 * READY != action attempted.
 * READY != verified success.
 *
 * Capabilities without approved health evidence remain UNAVAILABLE.
 */
class DefaultAndroidCapabilityHealthSource :
    AndroidCapabilityHealthSource {

    override fun health(
        capability: CapabilityContract,
    ): CapabilityHealthState {
        if (!AndroidAccessibilityCapability.matches(capability)) {
            return CapabilityHealthState.UNAVAILABLE
        }

        return if (
            DevilAccessibilityServiceRegistry.current() != null
        ) {
            CapabilityHealthState.READY
        } else {
            CapabilityHealthState.UNAVAILABLE
        }
    }
}
