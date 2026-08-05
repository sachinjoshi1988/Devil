package com.devil.core.runtime.capability

import com.devil.core.model.capability.CapabilitySelectionRequest
import com.devil.core.model.common.TraceId

/**
 * Default Stage 10 constitutional capability-selection resolver.
 *
 * No constitutional capability-selection policy exists yet. Therefore this
 * resolver preserves trace continuity and returns UNAVAILABLE rather than
 * choosing a registered capability without justified policy evidence.
 *
 * Registry unavailability remains unavailable. Registry failure propagates its
 * matching error.
 *
 * This implementation does not establish capability availability, health,
 * authorization, operating-system permission, readiness, execution,
 * observation, verification, or final outcome.
 */
class DefaultCapabilitySelectionResolver :
    CapabilitySelectionResolver {

    override fun resolve(
        traceId: TraceId,
        request: CapabilitySelectionRequest,
        registry: CapabilityRegistryResult,
    ): CapabilitySelectionResolutionResult {
        require(
            request.plan.task.decision.understanding.context.traceId ==
                traceId,
        ) {
            "Capability selection resolver trace and request must use the same trace identity."
        }

        require(registry.traceId == traceId) {
            "Capability selection resolver trace and registry result must use the same trace identity."
        }

        return when (registry.status) {
            CapabilityRegistryStatus.AVAILABLE ->
                CapabilitySelectionResolutionResult.create(
                    traceId = traceId,
                    status =
                        CapabilitySelectionResolutionStatus.UNAVAILABLE,
                )

            CapabilityRegistryStatus.UNAVAILABLE ->
                CapabilitySelectionResolutionResult.create(
                    traceId = traceId,
                    status =
                        CapabilitySelectionResolutionStatus.UNAVAILABLE,
                )

            CapabilityRegistryStatus.FAILED ->
                CapabilitySelectionResolutionResult.create(
                    traceId = traceId,
                    status = CapabilitySelectionResolutionStatus.FAILED,
                    error = requireNotNull(registry.error),
                )
        }
    }
}
