package com.devil.app.capability

import com.devil.app.accessibility.AndroidAccessibilityCapability
import com.devil.core.model.capability.CapabilityContract

/**
 * Default Android capability-registration source.
 *
 * Stage 38 registers the first genuine Android action capability whose bounded
 * platform implementation now exists:
 *
 * Android Accessibility Click Visible Text.
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
        )
    }
}
