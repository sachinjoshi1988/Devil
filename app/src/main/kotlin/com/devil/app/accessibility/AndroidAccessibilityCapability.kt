package com.devil.app.accessibility

import com.devil.core.model.capability.CapabilityCategory
import com.devil.core.model.capability.CapabilityContract
import com.devil.core.model.capability.CapabilityId

/**
 * Canonical Stage 38 Android accessibility-action capability.
 *
 * This capability declares that the Android embodiment contains one bounded
 * accessibility-action implementation capable of approaching an explicitly
 * supplied accessibility action request.
 *
 * Registration does not mean:
 *
 * - the Android accessibility service is connected;
 * - the capability is available;
 * - capability health is READY;
 * - the user or owner is authenticated;
 * - Devil authorization exists;
 * - constitutional Execution is APPROVED;
 * - any accessibility target was selected;
 * - an Android action was attempted;
 * - an effect was observed;
 * - an outcome was verified;
 * - or the requested task completed.
 *
 * Dynamic AndroidAccessibilityActionRequest data is intentionally not encoded
 * into CapabilityContract. Capability identity and per-action target data remain
 * separate constitutional facts.
 */
object AndroidAccessibilityCapability {

    val capabilityId: CapabilityId =
        CapabilityId.from(
            "android-accessibility-click-visible-text",
        )

    val contract: CapabilityContract =
        CapabilityContract.create(
            capabilityId = capabilityId,
            category = CapabilityCategory.ACTION,
            name = "Android Accessibility Click Visible Text",
            description =
                "Performs one explicitly authorized bounded Android accessibility click on an explicitly supplied visible-text target.",
        )

    fun matches(
        capability: CapabilityContract,
    ): Boolean {
        return capability.capabilityId == capabilityId
    }
}
