package com.devil.app.capability

import com.devil.app.accessibility.AndroidAccessibilityCapability
import com.devil.app.accessibility.DevilAccessibilityServiceRegistry
import com.devil.app.device.AndroidDeviceKnowledgeCapability
import com.devil.app.vision.AndroidCameraInventorySource
import com.devil.app.vision.AndroidVisionCapability
import com.devil.core.model.capability.CapabilityAvailabilityState
import com.devil.core.model.capability.CapabilityContract

/**
 * Default Android capability-availability source.
 *
 * Stage 38 accessibility availability requires a genuinely connected
 * DevilAccessibilityService.
 *
 * Stage 40 Device Knowledge is AVAILABLE because its approved bounded source
 * reads non-sensitive Android Build facts directly from the running platform
 * and requires no separate service connection.
 *
 * Stage 41 Vision availability requires explicit camera-inventory evidence.
 *
 * A camera inventory source being present does not itself make Vision AVAILABLE.
 * At least one Android camera must actually be reported by that source.
 *
 * When no Stage 41 camera inventory source is supplied, Vision remains
 * fail-closed as UNAVAILABLE.
 *
 * Availability describes embodiment availability only.
 *
 * Camera hardware available != CAMERA permission granted.
 * Camera hardware available != camera opened.
 * Camera hardware available != image captured.
 * Camera hardware available != visual understanding.
 * Available != authenticated.
 * Available != Devil authorized.
 * Available != Executive ready.
 * Available != execution approved.
 * Available != verified outcome.
 */
class DefaultAndroidCapabilityAvailabilitySource(
    private val visionCameraInventorySource:
        AndroidCameraInventorySource? = null,
) : AndroidCapabilityAvailabilitySource {

    override fun availability(
        capability: CapabilityContract,
    ): CapabilityAvailabilityState {
        return when {
            AndroidAccessibilityCapability.matches(
                capability,
            ) ->
                if (
                    DevilAccessibilityServiceRegistry.current() != null
                ) {
                    CapabilityAvailabilityState.AVAILABLE
                } else {
                    CapabilityAvailabilityState.UNAVAILABLE
                }

            AndroidDeviceKnowledgeCapability.matches(
                capability,
            ) ->
                CapabilityAvailabilityState.AVAILABLE

            AndroidVisionCapability.matches(
                capability,
            ) ->
                if (hasGenuineCameraEvidence()) {
                    CapabilityAvailabilityState.AVAILABLE
                } else {
                    CapabilityAvailabilityState.UNAVAILABLE
                }

            else ->
                CapabilityAvailabilityState.UNAVAILABLE
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
