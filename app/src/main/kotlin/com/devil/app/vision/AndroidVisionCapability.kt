package com.devil.app.vision

import com.devil.core.model.capability.CapabilityCategory
import com.devil.core.model.capability.CapabilityContract
import com.devil.core.model.capability.CapabilityId

/**
 * Canonical Stage 41 Android Vision and Camera capability.
 *
 * This INPUT capability represents bounded access to Android camera perception.
 *
 * Registration means only that Devil contains an approved Android embodiment
 * for approaching visual perception.
 *
 * Registration does not mean:
 *
 * - camera hardware exists;
 * - Android CAMERA permission is granted;
 * - a camera is open;
 * - an image has been captured;
 * - visual content is truthful;
 * - a person has been identified;
 * - owner identity is established;
 * - authentication exists;
 * - Devil authorization exists;
 * - constitutional understanding has occurred;
 * - Execution is APPROVED;
 * - memory persistence is authorized;
 * - or an Outcome has been verified.
 *
 * Camera != Brain.
 * Image != truth.
 * Perception != understanding.
 * Perception != authorization.
 */
object AndroidVisionCapability {

    val capabilityId: CapabilityId =
        CapabilityId.from(
            "android-vision-camera-perception",
        )

    val contract: CapabilityContract =
        CapabilityContract.create(
            capabilityId = capabilityId,
            category = CapabilityCategory.INPUT,
            name = "Android Vision Camera Perception",
            description =
                "Provides bounded Android camera perception without granting interpretation, identity, authorization, memory, or execution authority.",
        )

    fun matches(
        capability: CapabilityContract,
    ): Boolean {
        return capability.capabilityId == capabilityId
    }
}
