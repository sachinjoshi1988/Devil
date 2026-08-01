package com.devil.core.runtime.capability

import com.devil.core.model.capability.CapabilityContract
import com.devil.core.model.common.TraceId
import com.devil.core.model.error.UniversalErrorRecord

/**
 * Represents the structured operational result of capability selection.
 *
 * A selected result contains one CapabilityContract. A deferred result contains
 * neither capability nor error. A failed result contains a matching error.
 */
@ConsistentCopyVisibility
data class CapabilitySelectionResult private constructor(
    val traceId: TraceId,
    val status: CapabilitySelectionStatus,
    val capability: CapabilityContract?,
    val error: UniversalErrorRecord?,
) {
    companion object {
        fun create(
            traceId: TraceId,
            status: CapabilitySelectionStatus,
            capability: CapabilityContract? = null,
            error: UniversalErrorRecord? = null,
        ): CapabilitySelectionResult {
            when (status) {
                CapabilitySelectionStatus.SELECTED -> {
                    require(capability != null && error == null) {
                        "Selected capability results require a capability and must not contain an error."
                    }
                }

                CapabilitySelectionStatus.DEFERRED -> {
                    require(capability == null && error == null) {
                        "Deferred capability results must not contain a capability or error."
                    }
                }

                CapabilitySelectionStatus.FAILED -> {
                    require(capability == null && error != null) {
                        "Failed capability results require an error and must not contain a capability."
                    }
                }
            }

            require(error == null || error.traceId == traceId) {
                "Capability selection result and error must use the same trace identity."
            }

            return CapabilitySelectionResult(
                traceId = traceId,
                status = status,
                capability = capability,
                error = error,
            )
        }
    }
}
