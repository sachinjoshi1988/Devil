package com.devil.app.device

import com.devil.core.model.capability.CapabilityCategory
import com.devil.core.model.capability.CapabilityContract
import com.devil.core.model.capability.CapabilityId

/**
 * Canonical Stage 40 Android Device Knowledge capability.
 *
 * This KNOWLEDGE capability represents bounded access to directly observable,
 * non-sensitive Android platform and device facts.
 *
 * Registration does not mean:
 *
 * - device knowledge has been collected;
 * - every possible device fact is available;
 * - collected facts are permanently current;
 * - owner identity is established;
 * - authentication exists;
 * - Devil authorization exists;
 * - Execution is APPROVED;
 * - an Android action may be performed;
 * - memory persistence is authorized;
 * - or an Outcome has been verified.
 *
 * Device knowledge
 * != owner identity
 * != authentication
 * != authorization
 * != execution.
 */
object AndroidDeviceKnowledgeCapability {

    val capabilityId: CapabilityId =
        CapabilityId.from(
            "android-device-knowledge",
        )

    val contract: CapabilityContract =
        CapabilityContract.create(
            capabilityId = capabilityId,
            category = CapabilityCategory.KNOWLEDGE,
            name = "Android Device Knowledge",
            description =
                "Provides bounded directly observed non-sensitive Android device and platform facts.",
        )

    fun matches(
        capability: CapabilityContract,
    ): Boolean {
        return capability.capabilityId == capabilityId
    }
}
