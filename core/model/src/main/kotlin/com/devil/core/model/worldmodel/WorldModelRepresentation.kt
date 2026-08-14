package com.devil.core.model.worldmodel

import com.devil.core.model.capability.CapabilityId
import com.devil.core.model.common.TraceId

/**
 * Represents one immutable, evidence-backed piece of constitutional World Model
 * representation.
 *
 * A representation may be created only from information already established by
 * the constitutional World Model update-evidence path. It preserves:
 *
 * - the trace identity of the reasoning cycle;
 * - the capability identity associated with the established evidence; and
 * - one nonblank description of that bounded evidence-backed representation.
 *
 * This type does not establish evidence, evaluate a World Model update, mutate
 * global state, change task or plan state, perform Learning, create or persist
 * Memory, communicate externally, or create authority.
 *
 * A WorldModelRepresentation is not a speculative assertion that arbitrary
 * world state is true. The authority and evidence boundaries that precede its
 * creation remain constitutionally authoritative.
 *
 * WORLD_MODEL_UPDATE_EVIDENCE != WORLD_MODEL_REPRESENTATION.
 * WORLD_MODEL_REPRESENTATION != WORLD_STATE_MUTATION.
 * WORLD_MODEL_REPRESENTATION != MEMORY.
 */
@ConsistentCopyVisibility
data class WorldModelRepresentation private constructor(
    val traceId: TraceId,
    val capabilityId: CapabilityId,
    val description: String,
) {
    companion object {
        fun create(
            traceId: TraceId,
            capabilityId: CapabilityId,
            description: String,
        ): WorldModelRepresentation {
            val normalizedDescription = description.trim()

            require(capabilityId.value.isNotBlank()) {
                "World Model representation requires a nonblank capability identity."
            }

            require(normalizedDescription.isNotEmpty()) {
                "World Model representation requires a nonblank evidence-backed description."
            }

            return WorldModelRepresentation(
                traceId = traceId,
                capabilityId = capabilityId,
                description = normalizedDescription,
            )
        }
    }
}
