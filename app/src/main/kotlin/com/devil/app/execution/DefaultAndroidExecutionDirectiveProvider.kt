package com.devil.app.execution

import com.devil.app.accessibility.AndroidAccessibilityActionRequest
import com.devil.app.accessibility.AndroidAccessibilityActionType
import com.devil.app.accessibility.AndroidAccessibilityCapability
import com.devil.app.accessibility.AndroidAccessibilityTarget
import com.devil.core.model.common.TraceId
import com.devil.core.model.execution.ExecutionRequest
import com.devil.core.runtime.capability.DefaultGeneralIntentCapabilityRouter
import com.devil.core.runtime.capability.GeneralIntentCapabilityRoute
import com.devil.core.runtime.capability.GeneralIntentCapabilityRouter

/**
 * Default Android execution-directive provider.
 *
 * Stage 337L establishes the reusable bridge between one genuine constitutional
 * ExecutionRequest and the already-existing bounded Android Settings
 * accessibility embodiment.
 *
 * The provider consumes only structured Understanding semantics already
 * preserved through:
 *
 * Understanding -> Decision -> Task -> Plan -> Capability -> ExecutionRequest.
 *
 * It does not parse raw conversation text, model output, recognition text,
 * plan/task/decision summaries, capability descriptions, or unrelated runtime
 * state.
 *
 * Stage 337L intentionally supports only the already-established SETTINGS
 * domain whose constitutionally selected capability is exactly the existing
 * Android Accessibility Click Visible Text capability.
 *
 * All other semantic routes or capability identities fail closed with null.
 *
 * STRUCTURED_SEMANTICS != EXECUTION_APPROVAL.
 * INTENT_ROUTE != CAPABILITY_SELECTED.
 * CAPABILITY_SELECTED != CAPABILITY_AVAILABLE.
 * DIRECTIVE_CREATED != EXECUTED.
 * DIRECTIVE_CREATED != ATTEMPTED.
 * ANDROID_PERMISSION != DEVIL_AUTHORIZATION.
 * SETTINGS_ROUTE != SETTINGS_EXECUTED.
 * ATTEMPTED != OBSERVED.
 * OBSERVED != VERIFIED.
 * VERIFIED != OUTCOME.
 */
class DefaultAndroidExecutionDirectiveProvider(
    private val generalIntentCapabilityRouter:
        GeneralIntentCapabilityRouter =
        DefaultGeneralIntentCapabilityRouter(),
) : AndroidExecutionDirectiveProvider {

    override fun provide(
        traceId: TraceId,
        request: ExecutionRequest,
    ): AndroidExecutionDirective? {
        require(
            request
                .plan
                .task
                .decision
                .understanding
                .context
                .traceId == traceId,
        ) {
            "Android execution directive request and constitutional execution request must use the same trace identity."
        }

        if (
            !AndroidAccessibilityCapability.matches(
                request.capability,
            )
        ) {
            return null
        }

        val route =
            generalIntentCapabilityRouter.route(
                request
                    .plan
                    .task
                    .decision
                    .understanding
                    .semantics,
            )

        if (route != GeneralIntentCapabilityRoute.SETTINGS) {
            return null
        }

        return AndroidExecutionDirective(
            traceId = traceId,
            capabilityId =
                request.capability.capabilityId,
            accessibilityRequest =
                AndroidAccessibilityActionRequest(
                    actionType =
                        AndroidAccessibilityActionType
                            .CLICK_VISIBLE_TEXT,
                    target =
                        AndroidAccessibilityTarget.fromText(
                            SETTINGS_VISIBLE_TEXT,
                        ),
                ),
        )
    }

    private companion object {
        const val SETTINGS_VISIBLE_TEXT =
            "Settings"
    }
}
