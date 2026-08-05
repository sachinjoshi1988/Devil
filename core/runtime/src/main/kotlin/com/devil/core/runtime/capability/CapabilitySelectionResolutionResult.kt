package com.devil.core.runtime.capability

import com.devil.core.model.capability.CapabilityContract
import com.devil.core.model.common.TraceId
import com.devil.core.model.error.UniversalErrorRecord

/**
 * Represents the bounded result of attempting to resolve one registered
 * capability for constitutional capability selection.
 *
 * A resolved result contains one registered CapabilityContract. An unavailable
 * result contains neither capability nor error. A failed result contains one
 * matching error.
 *
 * This result does not establish capability availability, health,
 * authorization, operating-system permission, readiness, execution,
 * observation, verification, or final outcome.
 */
@ConsistentCopyVisibility
data class CapabilitySelectionResolutionResult private constructor(
    val traceId: TraceId,
    val status: CapabilitySelectionResolutionStatus,
    val capability: CapabilityContract?,
    val error: UniversalErrorRecord?,
) {
    companion object {
        fun create(
            traceId: TraceId,
            status: CapabilitySelectionResolutionStatus,
            capability: CapabilityContract? = null,
            error: UniversalErrorRecord? = null,
        ): CapabilitySelectionResolutionResult {
            when (status) {
                CapabilitySelectionResolutionStatus.RESOLVED -> {
                    require(capability != null && error == null) {
                        "Resolved capability selection results require a capability and must not contain an error."
                    }
                }

                CapabilitySelectionResolutionStatus.UNAVAILABLE -> {
                    require(capability == null && error == null) {
                        "Unavailable capability selection results must not contain a capability or error."
                    }
                }

                CapabilitySelectionResolutionStatus.FAILED -> {
                    require(capability == null && error != null) {
                        "Failed capability selection results require an error and must not contain a capability."
                    }
                }
            }

            require(error == null || error.traceId == traceId) {
                "Capability selection resolution result and error must use the same trace identity."
            }

            return CapabilitySelectionResolutionResult(
                traceId = traceId,
                status = status,
                capability = capability,
                error = error,
            )
        }
    }
}
