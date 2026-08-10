package com.devil.app.capability

import com.devil.app.accessibility.AndroidAccessibilityCapability
import com.devil.app.device.AndroidDeviceKnowledgeCapability
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
 * Registration declares capability identity and constitutional purpose only.
 *
 * Registration != availability.
 * Registration != health READY.
 * Registration != authentication.
 * Registration != Devil authorization.
 * Registration != Android permission.
 * Registration != Execution APPROVED.
 * Registration != Android action attempted.
 * Registration != observed effect.
 * Registration != verified outcome.
 */
class DefaultAndroidCapabilityRegistrationSource :
    AndroidCapabilityRegistrationSource {

    override fun registrations(): List<CapabilityContract> {
        return listOf(
            AndroidAccessibilityCapability.contract,
            AndroidDeviceKnowledgeCapability.contract,
        )
    }
}
