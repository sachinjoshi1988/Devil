package com.devil.core.runtime.capability

import com.devil.core.model.capability.CapabilityContract
import com.devil.core.model.capability.CapabilitySelectionRequest
import com.devil.core.model.common.TraceId
import com.devil.core.model.understanding.UnderstandingActionability
import com.devil.core.model.understanding.UnderstandingIntent

/**
 * Default bounded constitutional capability-selection resolver.
 *
 * Stage 60 introduces a deliberately small deterministic selection policy over
 * registered capability contracts.
 *
 * Selection is based only on structured semantic meaning already established by
 * Understanding and preserved through Decision, Task, and Plan.
 *
 * A resolved capability remains only a selected registered contract.
 *
 * Selection does not establish:
 * - capability availability;
 * - capability health;
 * - constitutional authorization;
 * - operating-system permission;
 * - Executive readiness;
 * - execution;
 * - observation;
 * - verification;
 * - Outcome.
 *
 * Unsupported intents, targets, missing semantics, or missing registrations
 * remain unavailable rather than being guessed or fabricated.
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
                resolveAvailableRegistry(
                    traceId = traceId,
                    request = request,
                    capabilities = registry.capabilities,
                )

            CapabilityRegistryStatus.UNAVAILABLE ->
                unavailable(traceId)

            CapabilityRegistryStatus.FAILED ->
                CapabilitySelectionResolutionResult.create(
                    traceId = traceId,
                    status =
                        CapabilitySelectionResolutionStatus.FAILED,
                    error = requireNotNull(registry.error),
                )
        }
    }

    private fun resolveAvailableRegistry(
        traceId: TraceId,
        request: CapabilitySelectionRequest,
        capabilities: List<CapabilityContract>,
    ): CapabilitySelectionResolutionResult {
        val semantics =
            request.plan
                .task
                .decision
                .understanding
                .semantics
                ?: return unavailable(traceId)

        if (
            semantics.intent != UnderstandingIntent.OPEN_TARGET ||
            semantics.actionability !=
                UnderstandingActionability.ACTIONABLE
        ) {
            return unavailable(traceId)
        }

        val target =
            semantics.target
                ?.trim()
                ?.lowercase()
                ?: return unavailable(traceId)

        val requiredCapabilityId =
            when (target) {
                "camera",
                "the camera",
                -> "capability-camera"

                else -> return unavailable(traceId)
            }

        val matches =
            capabilities.filter {
                it.capabilityId.value == requiredCapabilityId
            }

        if (matches.size != 1) {
            return unavailable(traceId)
        }

        return CapabilitySelectionResolutionResult.create(
            traceId = traceId,
            status = CapabilitySelectionResolutionStatus.RESOLVED,
            capability = matches.single(),
        )
    }

    private fun unavailable(
        traceId: TraceId,
    ): CapabilitySelectionResolutionResult {
        return CapabilitySelectionResolutionResult.create(
            traceId = traceId,
            status =
                CapabilitySelectionResolutionStatus.UNAVAILABLE,
        )
    }
}
