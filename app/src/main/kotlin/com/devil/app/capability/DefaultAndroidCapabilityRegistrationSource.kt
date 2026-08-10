package com.devil.app.capability

import com.devil.app.accessibility.AndroidAccessibilityCapability
import com.devil.app.device.AndroidDeviceKnowledgeCapability
import com.devil.app.internet.AndroidInternetKnowledgeCapability
import com.devil.app.vision.AndroidVisionCapability
import com.devil.core.model.capability.CapabilityContract

/**
 * Default Android capability-registration source.
 *
 * Currently registered genuine Android embodiment capabilities:
 *
 * Stage 38:
 * Android Accessibility Click Visible Text.
 *
 * Stage 40:
 * Android Device Knowledge.
 *
 * Stage 41:
 * Android Vision Camera Perception.
 *
 * Stage 42:
 * Android Internet Knowledge.
 *
 * Registration declares capability identity and constitutional purpose only.
 *
 * Registration != availability.
 * Registration != health READY.
 * Registration != authentication.
 * Registration != Devil authorization.
 * Registration != Android permission.
 * Registration != network connectivity.
 * Registration != remote source reachability.
 * Registration != retrieved content.
 * Registration != trusted content.
 * Registration != Execution APPROVED.
 * Registration != observed effect.
 * Registration != verified Outcome.
 */
class DefaultAndroidCapabilityRegistrationSource :
    AndroidCapabilityRegistrationSource {

    override fun registrations(): List<CapabilityContract> {
        return listOf(
            AndroidAccessibilityCapability.contract,
            AndroidDeviceKnowledgeCapability.contract,
            AndroidVisionCapability.contract,
            AndroidInternetKnowledgeCapability.contract,
        )
    }
}
