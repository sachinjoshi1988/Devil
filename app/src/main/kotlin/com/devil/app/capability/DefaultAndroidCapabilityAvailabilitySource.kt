package com.devil.app.capability

import com.devil.app.accessibility.AndroidAccessibilityCapability
import com.devil.app.accessibility.DevilAccessibilityServiceRegistry
import com.devil.app.device.AndroidDeviceKnowledgeCapability
import com.devil.app.internet.AndroidInternetKnowledgeCapability
import com.devil.app.internet.AndroidInternetKnowledgeSource
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
 * reads non-sensitive Android Build facts directly from the running platform.
 *
 * Stage 41 Vision availability requires explicit camera-inventory evidence
 * reporting at least one Android camera.
 *
 * Stage 42 Internet Knowledge remains UNAVAILABLE until a genuine bounded
 * production Internet source and explicit reachability evidence exist.
 *
 * Merely declaring Manifest.permission.INTERNET is not network evidence.
 *
 * Registered Internet capability
 * != Android INTERNET permission
 * != network connected
 * != destination reachable
 * != capability AVAILABLE
 * != external content retrieved
 * != external content trusted.
 *
 * Availability describes embodiment availability only.
 *
 * Available != authenticated.
 * Available != Devil authorized.
 * Available != Executive ready.
 * Available != execution approved.
 * Available != verified Outcome.
 */
class DefaultAndroidCapabilityAvailabilitySource(
    private val visionCameraInventorySource:
        AndroidCameraInventorySource? = null,
    private val internetKnowledgeSource:
        AndroidInternetKnowledgeSource? = null,
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

            AndroidInternetKnowledgeCapability.matches(
                capability,
            ) ->
                if (internetKnowledgeSource != null) {
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
