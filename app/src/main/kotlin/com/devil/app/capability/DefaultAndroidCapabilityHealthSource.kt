package com.devil.app.capability

import com.devil.app.accessibility.AndroidAccessibilityCapability
import com.devil.app.accessibility.DevilAccessibilityServiceRegistry
import com.devil.app.device.AndroidDeviceKnowledgeCapability
import com.devil.core.model.capability.CapabilityContract
import com.devil.core.model.capability.CapabilityHealthState

/**
 * Default Android capability-health source.
 *
 * Stage 38 accessibility health is READY only while
 * DevilAccessibilityService is genuinely connected.
 *
 * Stage 40 Device Knowledge is READY because its approved bounded source reads
 * directly available non-sensitive Android Build facts and requires no
 * additional service lifecycle.
 *
 * READY describes capability health only.
 *
 * READY != Executive readiness.
 * READY != authentication.
 * READY != Devil authorization.
 * READY != Execution APPROVED.
 * READY != action attempted.
 * READY != verified success.
 */
class DefaultAndroidCapabilityHealthSource :
    AndroidCapabilityHealthSource {

    override fun health(
        capability: CapabilityContract,
    ): CapabilityHealthState {
        return when {
            AndroidAccessibilityCapability.matches(
                capability,
            ) ->
                if (
                    DevilAccessibilityServiceRegistry.current() != null
                ) {
                    CapabilityHealthState.READY
                } else {
                    CapabilityHealthState.UNAVAILABLE
                }

            AndroidDeviceKnowledgeCapability.matches(
                capability,
            ) ->
                CapabilityHealthState.READY

            else ->
                CapabilityHealthState.UNAVAILABLE
        }
    }
}
