package com.devil.core.runtime.capability

import com.devil.core.model.common.TraceId

/**
 * Default Stage 10 mapping from bounded capability-selection resolution results
 * into the stable CapabilitySelectionResult contract.
 *
 * A resolved capability is mapped as SELECTED. Resolution unavailability becomes
 * operational deferral. Resolution failure preserves its matching error.
 *
 * This mapper performs no capability registration or resolution, availability or
 * health evaluation, authorization, operating-system permission checking,
 * execution, observation, verification, or outcome reporting.
 */
class DefaultCapabilitySelectionResultMapper :
    CapabilitySelectionResultMapper {

    override fun map(
        traceId: TraceId,
        resolution: CapabilitySelectionResolutionResult,
    ): CapabilitySelectionResult {
        require(resolution.traceId == traceId) {
            "Capability selection mapper trace and resolution result must use the same trace identity."
        }

        return when (resolution.status) {
            CapabilitySelectionResolutionStatus.RESOLVED ->
                CapabilitySelectionResult.create(
                    traceId = traceId,
                    status = CapabilitySelectionStatus.SELECTED,
                    capability = requireNotNull(
                        resolution.capability,
                    ),
                )

            CapabilitySelectionResolutionStatus.UNAVAILABLE ->
                CapabilitySelectionResult.create(
                    traceId = traceId,
                    status = CapabilitySelectionStatus.DEFERRED,
                )

            CapabilitySelectionResolutionStatus.FAILED ->
                CapabilitySelectionResult.create(
                    traceId = traceId,
                    status = CapabilitySelectionStatus.FAILED,
                    error = requireNotNull(
                        resolution.error,
                    ),
                )
        }
    }
}
