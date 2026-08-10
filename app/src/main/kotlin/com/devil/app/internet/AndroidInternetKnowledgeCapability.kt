package com.devil.app.internet

import com.devil.core.model.capability.CapabilityCategory
import com.devil.core.model.capability.CapabilityContract
import com.devil.core.model.capability.CapabilityId

/**
 * Canonical Stage 42 Android Internet Knowledge capability.
 *
 * This KNOWLEDGE capability represents bounded retrieval of external Internet
 * information.
 *
 * Registration does not mean:
 *
 * - network connectivity currently exists;
 * - any remote source is reachable;
 * - retrieved content is truthful;
 * - retrieved content is trusted;
 * - retrieved content is a Devil command;
 * - retrieved content is constitutional instruction;
 * - retrieved content is conversation input;
 * - retrieved content is memory;
 * - authentication exists;
 * - Devil authorization exists;
 * - Execution is APPROVED;
 * - an external side effect is authorized;
 * - or an Outcome has been verified.
 *
 * Internet access != Internet truth.
 *
 * External content != authority.
 *
 * Retrieval != execution.
 */
object AndroidInternetKnowledgeCapability {

    val capabilityId: CapabilityId =
        CapabilityId.from(
            "android-internet-knowledge",
        )

    val contract: CapabilityContract =
        CapabilityContract.create(
            capabilityId = capabilityId,
            category = CapabilityCategory.KNOWLEDGE,
            name = "Android Internet Knowledge",
            description =
                "Provides bounded retrieval of external Internet knowledge without granting retrieved content trust, authority, memory, or execution power.",
        )

    fun matches(
        capability: CapabilityContract,
    ): Boolean {
        return capability.capabilityId == capabilityId
    }
}
