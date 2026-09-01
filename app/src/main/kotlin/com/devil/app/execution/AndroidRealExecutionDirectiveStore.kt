package com.devil.app.execution

import com.devil.app.accessibility.AndroidAccessibilityActionRequest
import com.devil.app.accessibility.AndroidAccessibilityActionType
import com.devil.app.accessibility.AndroidAccessibilityCapability
import com.devil.app.accessibility.Stage314AndroidAccessibilityChangeReadinessStore
import com.devil.core.model.common.TraceId
import com.devil.core.model.execution.ExecutionRequest

/**
 * Stage 314 process-local one-shot Android execution-directive store.
 *
 * This store exists only to support explicit real-device alpha execution testing
 * through the already-established constitutional execution chain.
 *
 * Explicit Android embodiment action data may be armed before runtime submission.
 *
 * Trace identity and constitutional execution identity are never fabricated or
 * supplied by the arming caller. They are bound only when the existing execution
 * path later presents one genuine runtime-created ExecutionRequest.
 *
 * This store does not:
 *
 * - infer targets from conversation text;
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
 * A stored action request is:
 *
 * - process-local;
 * - explicitly supplied;
 * - unbound before constitutional execution;
 * - bound only to the genuine trace and capability supplied by ExecutionRequest;
 * - and one-shot after a justified match.
 *
 * Stage 314 may additionally preserve one bounded post-action expectation only
 * after that genuine trace/capability binding has occurred.
 *
 * EXPECTATION_STORED != OBSERVED.
 * ARMED != AUTHORIZED.
 * ARMED != TRACE_BOUND.
 * DIRECTIVE_AVAILABLE != EXECUTION_APPROVED.
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
            "Stage 314 directive lookup and constitutional execution request must use the same trace identity."
        }

        if (
            !AndroidAccessibilityCapability.matches(
                request.capability,
            )
        ) {
            return null
        }

        synchronized(lock) {
            val accessibilityRequest =
                pendingAccessibilityRequest
                    ?: return null

            pendingAccessibilityRequest = null

            bindStage314PostActionExpectation(
                traceId = traceId,
                request = request,
                accessibilityRequest =
                    accessibilityRequest,
            )

            return AndroidExecutionDirective(
                traceId = traceId,
                capabilityId =
                    request.capability.capabilityId,
                accessibilityRequest =
                    accessibilityRequest,
            )
        }
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
