package com.devil.core.runtime.capability

import com.devil.core.model.capability.CapabilityContract
import com.devil.core.model.capability.CapabilitySelectionRequest
import com.devil.core.model.common.TraceId

/**
 * Default bounded constitutional capability-selection resolver.
 *
 * Stage 337J delegates semantic-domain classification to the shared General
 * Intent & Capability Router while preserving the existing constitutional
 * Capability Selection Authority.
 *
 * Selection remains based only on structured semantic meaning already
 * established by Understanding and preserved through Decision, Task, and Plan.
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
 * Unsupported routes, missing semantics, or missing registrations remain
 * unavailable rather than being guessed or fabricated.
 *
 * GENERAL_INTENT_ROUTER != CAPABILITY_SELECTION_AUTHORITY.
 * INTENT_ROUTE != CAPABILITY_SELECTED.
 * CAPABILITY_SELECTED != EXECUTION_APPROVED.
 */
class DefaultCapabilitySelectionResolver(
    private val generalIntentCapabilityRouter:
        GeneralIntentCapabilityRouter =
        DefaultGeneralIntentCapabilityRouter(),
) : CapabilitySelectionResolver {

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
            request
                .plan
                .task
                .decision
                .understanding
                .semantics

        val route =
            generalIntentCapabilityRouter.route(semantics)

        val requiredCapabilityId =
            when (route) {
                GeneralIntentCapabilityRoute.CAMERA ->
                    "capability-camera"

                GeneralIntentCapabilityRoute.NO_CAPABILITY_REQUIRED,
                GeneralIntentCapabilityRoute.SETTINGS,
                GeneralIntentCapabilityRoute.DEVICE_CONTROL,
                GeneralIntentCapabilityRoute.ALARM,
                GeneralIntentCapabilityRoute.MESSAGING,
                GeneralIntentCapabilityRoute.CALL,
                GeneralIntentCapabilityRoute.MEDIA,
                GeneralIntentCapabilityRoute.DEVICE_KNOWLEDGE,
                GeneralIntentCapabilityRoute.NOTIFICATIONS,
                GeneralIntentCapabilityRoute.GENERAL_INFORMATION,
                GeneralIntentCapabilityRoute.UNSUPPORTED,
                -> return unavailable(traceId)
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
            status =
                CapabilitySelectionResolutionStatus.RESOLVED,
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
