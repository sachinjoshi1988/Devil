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
 * Accessibility availability requires a genuinely connected
 * DevilAccessibilityService.
 *
 * Device Knowledge is AVAILABLE because its approved bounded source reads
 * non-sensitive Android Build facts directly from the running platform.
 *
 * Vision availability requires explicit camera-inventory evidence reporting at
 * least one Android camera.
 *
 * Stage 337K keeps Internet Knowledge UNAVAILABLE until later Internet
 * activation establishes explicit operational availability evidence.
 *
 * The Internet retrieval-source constructor seam is preserved for compatibility
 * with the existing Android composition and Stage 42 contracts. Merely supplying
 * that implementation object does not establish current network availability.
 *
 * Registered Internet capability
 * != retrieval implementation present
 * != Android INTERNET permission
 * != network connected
 * != destination reachable
 * != capability AVAILABLE
 * != external content retrieved
 * != external content trusted.
 *
 * REGISTERED != AVAILABLE.
 * AVAILABLE != HEALTH_READY.
 * AVAILABLE != AUTHENTICATED.
 * AVAILABLE != DEVIL_AUTHORIZED.
 * AVAILABLE != EXECUTIVE_READY.
 * AVAILABLE != EXECUTION_APPROVED.
 * AVAILABLE != VERIFIED_OUTCOME.
 */
class DefaultAndroidCapabilityAvailabilitySource(
    private val visionCameraInventorySource:
        AndroidCameraInventorySource? = null,
    @Suppress("UNUSED_PARAMETER")
    internetKnowledgeSource:
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
                CapabilityAvailabilityState.UNAVAILABLE

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
