package com.devil.core.runtime.worldmodel

import com.devil.core.model.capability.CapabilityId
import com.devil.core.model.common.TraceId
import com.devil.core.model.worldmodel.WorldModelRepresentation

/**
 * Creates one bounded synthetic World Model representation for unit tests that
 * intentionally construct an APPLICABLE World Model evaluation directly.
 *
 * This helper is test-only. It does not establish production evidence and does
 * not participate in the Unified Devil Runtime.
 */
internal fun createStage73TestWorldModelRepresentation(
    traceId: TraceId,
): WorldModelRepresentation {
    return WorldModelRepresentation.create(
        traceId = traceId,
        capabilityId =
            CapabilityId.from(
                "capability-camera",
            ),
        description =
            "Synthetic bounded World Model representation for structural unit testing.",
    )
}
