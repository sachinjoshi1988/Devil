package com.devil.app.capability

import com.devil.app.accessibility.AndroidAccessibilityCapability
import com.devil.app.accessibility.DevilAccessibilityServiceRegistry
import com.devil.app.device.AndroidDeviceKnowledgeCapability
import com.devil.app.vision.AndroidCameraInventorySource
import com.devil.app.vision.AndroidVisionCapability
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
 * Stage 41 Vision health is READY only when explicit camera-inventory evidence
 * reports at least one Android camera.
 *
 * No inventory evidence, no camera hardware, or inventory inspection failure
 * remains fail-closed as UNAVAILABLE.
 *
 * READY describes capability health only.
 *
 * Camera READY != CAMERA permission granted.
 * Camera READY != camera opened.
 * Camera READY != frame captured.
 * Camera READY != visual understanding.
 * READY != Executive readiness.
 * READY != authentication.
 * READY != Devil authorization.
 * READY != Execution APPROVED.
 * READY != action attempted.
 * READY != verified success.
 */
class DefaultAndroidCapabilityHealthSource(
    private val visionCameraInventorySource:
        AndroidCameraInventorySource? = null,
) : AndroidCapabilityHealthSource {

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

            AndroidVisionCapability.matches(
                capability,
            ) ->
                if (hasGenuineCameraEvidence()) {
                    CapabilityHealthState.READY
                } else {
                    CapabilityHealthState.UNAVAILABLE
                }

            else ->
                CapabilityHealthState.UNAVAILABLE
        }
    }

    private fun hasGenuineCameraEvidence(): Boolean {
        val source =
            visionCameraInventorySource
                ?: return false

        return runCatching {
            source.inventory().hasCamera
        }.getOrDefault(false)
    }
}
