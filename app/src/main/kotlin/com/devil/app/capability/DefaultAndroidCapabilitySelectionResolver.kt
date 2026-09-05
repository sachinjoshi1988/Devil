package com.devil.app.capability

import com.devil.app.accessibility.AndroidAccessibilityCapability
import com.devil.core.model.capability.CapabilityContract
import com.devil.core.model.capability.CapabilitySelectionRequest
import com.devil.core.model.common.TraceId
import com.devil.core.runtime.capability.CapabilityRegistryResult
import com.devil.core.runtime.capability.CapabilityRegistryStatus
import com.devil.core.runtime.capability.CapabilitySelectionResolutionResult
import com.devil.core.runtime.capability.CapabilitySelectionResolutionStatus
import com.devil.core.runtime.capability.CapabilitySelectionResolver
import com.devil.core.runtime.capability.DefaultGeneralIntentCapabilityRouter
import com.devil.core.runtime.capability.GeneralIntentCapabilityRoute
import com.devil.core.runtime.capability.GeneralIntentCapabilityRouter

/**
 * Android embodiment capability-selection resolver.
 *
 * Stage 337J delegates semantic-domain classification to the shared
 * provider-neutral General Intent & Capability Router while remaining inside
 * the existing constitutional Capability Selection boundary.
 *
 * Stage 314's exact bounded concrete mapping remains preserved:
 *
 * OPEN_TARGET + settings / the settings
 * -> Android Accessibility Click Visible Text capability.
 *
 * The dynamic visible-text action request itself is not created here. That
 * remains a separate explicit Android embodiment fact supplied through the
 * existing Stage 314 execution-directive boundary.
 *
 * Stage 337J may classify other capability domains, but this resolver does not
 * activate or select Android Device Knowledge, Internet Knowledge, Vision,
 * notifications, alarms, messaging, calls, media, or volume-control
 * capabilities merely because a route or registration exists.
 *
 * This resolver does not create a capability, establish availability or
 * health, authorize work, grant Android permission, establish Executive
 * readiness, create an ExecutionRequest, perform an Android action, invent an
 * observation, verify an effect, or establish an Outcome.
 *
 * STRUCTURED_INTENT != AUTHORIZATION.
 * GENERAL_INTENT_ROUTER != CAPABILITY_SELECTION_AUTHORITY.
 * INTENT_ROUTE != CAPABILITY_SELECTED.
 * REGISTERED != SELECTED.
 * CAPABILITY_SELECTED != EXECUTION_APPROVED.
 * ANDROID_PERMISSION != DEVIL_AUTHORIZATION.
 * ROUTED != EXECUTED.
 * ATTEMPTED != VERIFIED.
 */
class DefaultAndroidCapabilitySelectionResolver(
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
            "Android capability resolver trace and request must use the same trace identity."
        }

        require(registry.traceId == traceId) {
            "Android capability resolver trace and registry result must use the same trace identity."
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
                GeneralIntentCapabilityRoute.SETTINGS ->
                    AndroidAccessibilityCapability.capabilityId

                GeneralIntentCapabilityRoute.NO_CAPABILITY_REQUIRED,
                GeneralIntentCapabilityRoute.CAMERA,
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
            capabilities.filter { capability ->
                capability.capabilityId == requiredCapabilityId
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
