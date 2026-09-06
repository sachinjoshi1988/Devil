package com.devil.app.execution

import com.devil.app.accessibility.AndroidAccessibilityActionRequest
import com.devil.app.accessibility.AndroidAccessibilityActionType
import com.devil.app.accessibility.AndroidAccessibilityCapability
import com.devil.app.accessibility.Stage314AndroidAccessibilityChangeReadinessStore
import com.devil.core.model.common.TraceId
import com.devil.core.model.execution.ExecutionRequest

/**
 * Process-local Android execution-directive bridge.
 *
 * Stage 314 established the explicit one-shot directive store used for bounded
 * real-device Open Settings acceptance.
 *
 * Stage 337L preserves that explicit Stage 314 directive as first priority and
 * adds a fail-closed structured fallback through the existing
 * AndroidExecutionDirectiveProvider boundary.
 *
 * An explicitly armed action remains:
 *
 * - process-local;
 * - explicitly supplied;
 * - unbound before constitutional execution;
 * - bound only to the genuine trace and capability supplied by ExecutionRequest;
 * - and one-shot after a justified match.
 *
 * If no explicit Stage 314 action is armed, the structured provider may resolve
 * only an already-established bounded directive from the genuine
 * ExecutionRequest. The default Stage 337L provider currently supports only the
 * existing SETTINGS accessibility embodiment.
 *
 * This bridge does not:
 *
 * - parse raw conversation text;
 * - infer targets from model output or summaries;
 * - create TraceId;
 * - create ExecutionRequest;
 * - select a capability;
 * - authenticate a subject;
 * - grant authorization;
 * - establish Executive readiness;
 * - approve execution;
 * - grant Android permission;
 * - perform an Android action;
 * - establish Observation, Verification, Outcome, Learning, or Memory;
 * - persist execution directives;
 * - or create a second runtime.
 *
 * Stage 314 post-action expectation/readiness evidence is bound only for the
 * explicit Stage 314 armed Settings directive. A Stage 337L structured fallback
 * does not manufacture Stage 314 Observation, Verification, or Outcome
 * evidence.
 *
 * EXPECTATION_STORED != OBSERVED.
 * ARMED != AUTHORIZED.
 * ARMED != TRACE_BOUND.
 * DIRECTIVE_AVAILABLE != EXECUTION_APPROVED.
 * STRUCTURED_SEMANTICS != EXECUTION_APPROVAL.
 * DIRECTIVE_CREATED != EXECUTED.
 * ANDROID_PERMISSION != DEVIL_AUTHORIZATION.
 * ATTEMPTED != VERIFIED.
 */
class AndroidRealExecutionDirectiveStore(
    private val postActionExpectationStore:
        Stage314AndroidPostActionExpectationStore =
        Stage314AndroidPostActionExpectationStore(),
    private val accessibilityChangeReadinessStore:
        Stage314AndroidAccessibilityChangeReadinessStore =
        Stage314AndroidAccessibilityChangeReadinessStore(),
    private val structuredDirectiveProvider:
        AndroidExecutionDirectiveProvider =
        DefaultAndroidExecutionDirectiveProvider(),
) : AndroidExecutionDirectiveProvider {

    private val lock = Any()

    private var pendingAccessibilityRequest:
        AndroidAccessibilityActionRequest? = null

    /**
     * Arms one explicit Android accessibility action request.
     *
     * No constitutional trace, capability, authorization, or ExecutionRequest is
     * created or inferred here.
     */
    fun arm(
        accessibilityRequest: AndroidAccessibilityActionRequest,
    ) {
        synchronized(lock) {
            pendingAccessibilityRequest =
                accessibilityRequest
        }
    }

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
            "Android directive lookup and constitutional execution request must use the same trace identity."
        }

        if (
            !AndroidAccessibilityCapability.matches(
                request.capability,
            )
        ) {
            return structuredDirectiveProvider.provide(
                traceId = traceId,
                request = request,
            )
        }

        val explicitlyArmedRequest =
            synchronized(lock) {
                val pending =
                    pendingAccessibilityRequest
                        ?: return@synchronized null

                pendingAccessibilityRequest = null
                pending
            }

        if (explicitlyArmedRequest != null) {
            bindStage314PostActionExpectation(
                traceId = traceId,
                request = request,
                accessibilityRequest =
                    explicitlyArmedRequest,
            )

            return AndroidExecutionDirective(
                traceId = traceId,
                capabilityId =
                    request.capability.capabilityId,
                accessibilityRequest =
                    explicitlyArmedRequest,
            )
        }

        return structuredDirectiveProvider.provide(
            traceId = traceId,
            request = request,
        )
    }

    private fun bindStage314PostActionExpectation(
        traceId: TraceId,
        request: ExecutionRequest,
        accessibilityRequest: AndroidAccessibilityActionRequest,
    ) {
        if (
            accessibilityRequest.actionType !=
            AndroidAccessibilityActionType.CLICK_VISIBLE_TEXT ||
            accessibilityRequest.target.normalizedText !=
            STAGE_314_SETTINGS_TARGET_NORMALIZED_TEXT
        ) {
            return
        }

        postActionExpectationStore.bind(
            traceId = traceId,
            capabilityId =
                request.capability.capabilityId,
            expectedVisibleText =
                STAGE_314_SETTINGS_DESTINATION_VISIBLE_TEXT,
        )

        accessibilityChangeReadinessStore.arm(
            traceId = traceId,
            capabilityId =
                request.capability.capabilityId,
        )
    }

    fun clear() {
        synchronized(lock) {
            pendingAccessibilityRequest = null
        }
    }

    private companion object {
        const val STAGE_314_SETTINGS_TARGET_NORMALIZED_TEXT =
            "settings"

        const val STAGE_314_SETTINGS_DESTINATION_VISIBLE_TEXT =
            "Settings, privacy, and permissions presentation"
    }
}
