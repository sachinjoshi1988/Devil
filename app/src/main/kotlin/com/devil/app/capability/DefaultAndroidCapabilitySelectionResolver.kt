package com.devil.app.capability

import com.devil.app.accessibility.AndroidAccessibilityCapability
import com.devil.core.model.capability.CapabilityContract
import com.devil.core.model.capability.CapabilitySelectionRequest
import com.devil.core.model.common.TraceId
import com.devil.core.model.understanding.UnderstandingActionability
import com.devil.core.model.understanding.UnderstandingIntent
import com.devil.core.runtime.capability.CapabilityRegistryResult
import com.devil.core.runtime.capability.CapabilityRegistryStatus
import com.devil.core.runtime.capability.CapabilitySelectionResolutionResult
import com.devil.core.runtime.capability.CapabilitySelectionResolutionStatus
import com.devil.core.runtime.capability.CapabilitySelectionResolver

/**
 * Stage 314 Android embodiment capability-selection resolver.
 *
 * This resolver remains inside the existing constitutional Capability Selection
 * boundary. It does not create a capability, authorize anything, establish
 * availability or health, grant Android permission, establish Executive
 * readiness, create an ExecutionRequest, perform an Android action, observe an
 * effect, verify an outcome, or establish task completion.
 *
 * Selection is based only on structured semantic meaning already established by
 * Understanding and preserved through Decision -> Task -> Plan.
 *
 * Stage 314 adds one deliberately bounded Android real-device mapping:
 *
 * OPEN_TARGET + "settings" / "the settings"
 * -> Android Accessibility Click Visible Text capability.
 *
 * The dynamic visible-text action request itself is NOT created here. That
 * remains a separate explicit Android embodiment fact supplied through the
 * Stage 314 one-shot execution-directive store.
 *
 * STRUCTURED_INTENT != AUTHORIZATION.
 * CAPABILITY_SELECTED != EXECUTION_APPROVED.
 * ANDROID_PERMISSION != DEVIL_AUTHORIZATION.
 * ATTEMPTED != VERIFIED.
 */
class DefaultAndroidCapabilitySelectionResolver :
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
            "Stage 314 Android capability resolver trace and request must use the same trace identity."
        }

        require(registry.traceId == traceId) {
            "Stage 314 Android capability resolver trace and registry result must use the same trace identity."
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
                ?: return unavailable(traceId)

        if (
            semantics.intent != UnderstandingIntent.OPEN_TARGET ||
            semantics.actionability !=
            UnderstandingActionability.ACTIONABLE
        ) {
            return unavailable(traceId)
        }

        val normalizedTarget =
            semantics.target
                ?.trim()
                ?.lowercase()
                ?: return unavailable(traceId)

        val requiredCapabilityId =
            when (normalizedTarget) {
                "settings",
                "the settings",
                -> AndroidAccessibilityCapability.capabilityId

                else -> return unavailable(traceId)
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
