package com.devil.app.capability

import com.devil.app.accessibility.AndroidAccessibilityCapability
import com.devil.app.device.AndroidDeviceKnowledgeCapability
import com.devil.app.device.AndroidDeviceKnowledgeQueryType
import com.devil.app.device.Stage337MDeviceKnowledgeQueryStore
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
import java.util.Locale

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
 * Stage 337M additionally activates only three bounded read-only Device
 * Knowledge queries that already have genuine Stage 40 Android sources:
 *
 * device model
 * android version
 * device summary
 *
 * The Stage 337M query store preserves only the already-structured query type
 * under the genuine runtime TraceId after exactly one registered Device
 * Knowledge capability has matched.
 *
 * Battery may still classify to the DEVICE_KNOWLEDGE domain, but Stage 337M
 * deliberately has no battery query mapping because no genuine Stage 40 battery
 * evidence source exists.
 *
 * The dynamic Android action request itself is not created here. That remains a
 * separate explicit Android embodiment fact supplied through the existing
 * Stage 314 / Stage 337L execution-directive boundary.
 *
 * Other routed domains remain unavailable here until their own roadmap stages
 * activate them.
 *
 * This resolver does not create a capability, establish availability or health,
 * authorize work, grant Android permission, establish Executive readiness,
 * create an ExecutionRequest, perform an Android action, read a device fact,
 * invent an observation, verify an effect, or establish an Outcome.
 *
 * STRUCTURED_INTENT != AUTHORIZATION.
 * GENERAL_INTENT_ROUTER != CAPABILITY_SELECTION_AUTHORITY.
 * INTENT_ROUTE != CAPABILITY_SELECTED.
 * REGISTERED != SELECTED.
 * CAPABILITY_SELECTED != FACT_OBSERVED.
 * STRUCTURED_TARGET != RAW_TEXT_PARSE.
 * STRUCTURED_TARGET != MODEL_INFERENCE.
 * QUERY_RECORD != DEVICE_FACT.
 * QUERY_RECORD != AUTHORIZATION.
 * CAPABILITY_SELECTED != EXECUTION_APPROVED.
 * ANDROID_PERMISSION != DEVIL_AUTHORIZATION.
 * ATTEMPTED != VERIFIED.
 * BATTERY_QUERY != BATTERY_FACT.
 * UNSUPPORTED_DEVICE_QUERY != GUESSED_ANSWER.
 */
class DefaultAndroidCapabilitySelectionResolver(
    private val generalIntentCapabilityRouter:
        GeneralIntentCapabilityRouter =
        DefaultGeneralIntentCapabilityRouter(),
    private val deviceKnowledgeQueryStore:
        Stage337MDeviceKnowledgeQueryStore =
        Stage337MDeviceKnowledgeQueryStore(),
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

        val structuredRoute =
            generalIntentCapabilityRouter.route(
                request
                    .plan
                    .task
                    .decision
                    .understanding
                    .semantics,
            )

        if (
            structuredRoute ==
            GeneralIntentCapabilityRoute.DEVICE_KNOWLEDGE
        ) {
            /*
             * Claim only the response domain for this genuine trace.
             *
             * DEVICE_KNOWLEDGE_CLAIM != CAPABILITY_SELECTED.
             * DEVICE_KNOWLEDGE_CLAIM != DEVICE_FACT.
             * DEVICE_KNOWLEDGE_CLAIM != AUTHORIZATION.
             */
            deviceKnowledgeQueryStore.claim(
                traceId = traceId,
            )
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
            generalIntentCapabilityRouter.route(
                semantics,
            )

        val deviceKnowledgeQueryType =
            if (
                route ==
                GeneralIntentCapabilityRoute.DEVICE_KNOWLEDGE
            ) {
                resolveDeviceKnowledgeQueryType(
                    target = semantics?.target,
                    predicate = semantics?.predicate,
                ) ?: return unavailable(traceId)
            } else {
                null
            }

        val requiredCapabilityId =
            when (route) {
                GeneralIntentCapabilityRoute.SETTINGS ->
                    AndroidAccessibilityCapability.capabilityId

                GeneralIntentCapabilityRoute.DEVICE_KNOWLEDGE ->
                    AndroidDeviceKnowledgeCapability.capabilityId

                GeneralIntentCapabilityRoute.NO_CAPABILITY_REQUIRED,
                GeneralIntentCapabilityRoute.CAMERA,
                GeneralIntentCapabilityRoute.DEVICE_CONTROL,
                GeneralIntentCapabilityRoute.ALARM,
                GeneralIntentCapabilityRoute.MESSAGING,
                GeneralIntentCapabilityRoute.CALL,
                GeneralIntentCapabilityRoute.MEDIA,
                GeneralIntentCapabilityRoute.NOTIFICATIONS,
                GeneralIntentCapabilityRoute.GENERAL_INFORMATION,
                GeneralIntentCapabilityRoute.UNSUPPORTED,
                -> return unavailable(traceId)
            }

        val matches =
            capabilities.filter { capability ->
                capability.capabilityId ==
                    requiredCapabilityId
            }

        if (matches.size != 1) {
            return unavailable(traceId)
        }

        if (deviceKnowledgeQueryType != null) {
            deviceKnowledgeQueryStore.record(
                traceId = traceId,
                queryType = deviceKnowledgeQueryType,
            )
        }

        return CapabilitySelectionResolutionResult.create(
            traceId = traceId,
            status =
                CapabilitySelectionResolutionStatus.RESOLVED,
            capability = matches.single(),
        )
    }

    private fun resolveDeviceKnowledgeQueryType(
        target: String?,
        predicate: String?,
    ): AndroidDeviceKnowledgeQueryType? {
        val normalizedPredicate =
            predicate
                ?.trim()
                ?.lowercase(Locale.ROOT)
                ?: return null

        if (normalizedPredicate != "query") {
            return null
        }

        val normalizedTarget =
            target
                ?.trim()
                ?.lowercase(Locale.ROOT)
                ?: return null

        return when (normalizedTarget) {
            "device model" ->
                AndroidDeviceKnowledgeQueryType.DEVICE_MODEL

            "android version" ->
                AndroidDeviceKnowledgeQueryType.ANDROID_VERSION

            "device summary" ->
                AndroidDeviceKnowledgeQueryType.DEVICE_SUMMARY

            else ->
                null
        }
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
