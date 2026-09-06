package com.devil.app.capability

import com.devil.app.accessibility.AndroidAccessibilityCapability
import com.devil.app.accessibility.DevilAccessibilityServiceRegistry
import com.devil.app.device.AndroidDeviceKnowledgeCapability
import com.devil.app.internet.AndroidInternetKnowledgeCapability
import com.devil.app.internet.AndroidInternetKnowledgeSource
import com.devil.app.vision.AndroidCameraInventorySource
import com.devil.app.vision.AndroidVisionCapability
import com.devil.core.model.capability.CapabilityContract
import com.devil.core.model.capability.CapabilityHealthState

/**
 * Default Android capability-health source.
 *
 * Accessibility health is READY only while DevilAccessibilityService is
 * genuinely connected.
 *
 * Device Knowledge is READY because its approved bounded source reads directly
 * available non-sensitive Android Build facts.
 *
 * Vision health is READY only when explicit camera-inventory evidence reports
 * at least one Android camera.
 *
 * Stage 337K keeps Internet Knowledge health UNAVAILABLE until later Internet
 * activation establishes explicit operational health evidence.
 *
 * The Internet retrieval-source constructor seam is preserved for compatibility
 * with the existing Android composition and Stage 42 contracts. Merely supplying
 * that implementation object does not establish current network health.
 *
 * Network permission
 * != retrieval implementation present
 * != network connected
 * != endpoint reachable
 * != Internet capability READY
 * != retrieval success
 * != trusted information.
 *
 * AVAILABLE != HEALTH_READY.
 * HEALTH_READY != EXECUTIVE_READY.
 * HEALTH_READY != AUTHENTICATED.
 * HEALTH_READY != DEVIL_AUTHORIZED.
 * HEALTH_READY != EXECUTION_APPROVED.
 * HEALTH_READY != VERIFIED_SUCCESS.
 */
class DefaultAndroidCapabilityHealthSource(
    private val visionCameraInventorySource:
        AndroidCameraInventorySource? = null,
    @Suppress("UNUSED_PARAMETER")
    internetKnowledgeSource:
        AndroidInternetKnowledgeSource? = null,
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

            AndroidInternetKnowledgeCapability.matches(
                capability,
            ) ->
                CapabilityHealthState.UNAVAILABLE

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
