package com.devil.app.execution

import com.devil.app.accessibility.AndroidAccessibilityActionRequest
import com.devil.app.accessibility.AndroidAccessibilityActionResult
import com.devil.app.accessibility.AndroidAccessibilityActionSource
import com.devil.app.accessibility.AndroidAccessibilityActionType
import com.devil.app.accessibility.AndroidAccessibilityCapability
import com.devil.app.accessibility.AndroidAccessibilityTarget
import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.common.SchemaVersion
import com.devil.core.model.common.TraceId
import com.devil.core.model.context.ContextEnvelope
import com.devil.core.model.context.ContextSecurityLevel
import com.devil.core.model.context.ContextSource
import com.devil.core.model.context.ContextTrustLevel
import com.devil.core.model.decision.DecisionRecord
import com.devil.core.model.decision.DecisionState
import com.devil.core.model.execution.ExecutionRequest
import com.devil.core.model.plan.PlanId
import com.devil.core.model.plan.PlanRecord
import com.devil.core.model.plan.PlanState
import com.devil.core.model.task.TaskId
import com.devil.core.model.task.TaskRecord
import com.devil.core.model.task.TaskState
import com.devil.core.model.understanding.UnderstandingRecord
import com.devil.core.model.understanding.UnderstandingState
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class Stage38AccessibilityExecutionGovernanceTest {

    @Test
    fun `accessibility capability without explicit directive remains deferred`() {
        val traceId =
            TraceId.from(
                "trace-stage-38-accessibility-execution-001",
            )

        var sourceCalls = 0

        val performer =
            AndroidAccessibilityExecutionPerformer(
                directiveProvider =
                    AndroidExecutionDirectiveProvider { _, _ ->
                        null
                    },
                accessibilitySource =
                    AndroidAccessibilityActionSource {
                        sourceCalls += 1
                        AndroidAccessibilityActionResult.attempted()
                    },
            )

        val result =
            performer.perform(
                traceId = traceId,
                request = createRequest(traceId),
            )

        assertEquals(
            AndroidExecutionAttemptStatus.DEFERRED,
            result.status,
        )
        assertEquals(0, sourceCalls)
        assertNull(result.capabilityId)
        assertNull(result.error)
    }

    @Test
    fun `matching explicit accessibility directive may approach bounded source`() {
        val traceId =
            TraceId.from(
                "trace-stage-38-accessibility-execution-002",
            )

        val request =
            createRequest(traceId)

        val accessibilityRequest =
            AndroidAccessibilityActionRequest(
                actionType =
                    AndroidAccessibilityActionType
                        .CLICK_VISIBLE_TEXT,
                target =
                    AndroidAccessibilityTarget.fromText(
                        "Send",
                    ),
            )

        var sourceCalls = 0

        val performer =
            AndroidAccessibilityExecutionPerformer(
                directiveProvider =
                    AndroidExecutionDirectiveProvider {
                            providerTrace,
                            providerRequest,
                        ->
                        assertEquals(
                            traceId,
                            providerTrace,
                        )
                        assertEquals(
                            request,
                            providerRequest,
                        )

                        AndroidExecutionDirective(
                            traceId = traceId,
                            capabilityId =
                                request.capability
                                    .capabilityId,
                            accessibilityRequest =
                                accessibilityRequest,
                        )
                    },
                accessibilitySource =
                    AndroidAccessibilityActionSource {
                            receivedRequest,
                        ->
                        sourceCalls += 1

                        assertEquals(
                            accessibilityRequest,
                            receivedRequest,
                        )

                        AndroidAccessibilityActionResult
                            .attempted()
                    },
            )

        val result =
            performer.perform(
                traceId = traceId,
                request = request,
            )

        assertEquals(1, sourceCalls)
        assertEquals(
            AndroidExecutionAttemptStatus.ATTEMPTED,
            result.status,
        )
        assertEquals(
            AndroidAccessibilityCapability.capabilityId,
            result.capabilityId,
        )
        assertNull(result.error)
    }

    @Test
    fun `target not found does not become attempted execution`() {
        val traceId =
            TraceId.from(
                "trace-stage-38-accessibility-execution-003",
            )

        val request =
            createRequest(traceId)

        val performer =
            AndroidAccessibilityExecutionPerformer(
                directiveProvider =
                    directiveProvider(
                        traceId = traceId,
                        request = request,
                        targetText = "Missing target",
                    ),
                accessibilitySource =
                    AndroidAccessibilityActionSource {
                        AndroidAccessibilityActionResult
                            .targetNotFound()
                    },
            )

        val result =
            performer.perform(
                traceId = traceId,
                request = request,
            )

        assertEquals(
            AndroidExecutionAttemptStatus.DEFERRED,
            result.status,
        )
        assertNull(result.capabilityId)
        assertNull(result.error)
    }

    @Test
    fun `unavailable accessibility service does not become attempted execution`() {
        val traceId =
            TraceId.from(
                "trace-stage-38-accessibility-execution-004",
            )

        val request =
            createRequest(traceId)

        val performer =
            AndroidAccessibilityExecutionPerformer(
                directiveProvider =
                    directiveProvider(
                        traceId = traceId,
                        request = request,
                        targetText = "Send",
                    ),
                accessibilitySource =
                    AndroidAccessibilityActionSource {
                        AndroidAccessibilityActionResult
                            .serviceUnavailable()
                    },
            )

        val result =
            performer.perform(
                traceId = traceId,
                request = request,
            )

        assertEquals(
            AndroidExecutionAttemptStatus.DEFERRED,
            result.status,
        )
    }

    @Test
    fun `accessibility operational failure becomes bounded execution failure`() {
        val traceId =
            TraceId.from(
                "trace-stage-38-accessibility-execution-005",
            )

        val request =
            createRequest(traceId)

        val performer =
            AndroidAccessibilityExecutionPerformer(
                directiveProvider =
                    directiveProvider(
                        traceId = traceId,
                        request = request,
                        targetText = "Send",
                    ),
                accessibilitySource =
                    AndroidAccessibilityActionSource {
                        AndroidAccessibilityActionResult
                            .failed(
                                "ANDROID_ACCESSIBILITY_TEST_FAILURE",
                            )
                    },
                failureTimeProvider = {
                    DevilTimestamp.fromEpochMilliseconds(
                        1_754_000_380_000L,
                    )
                },
            )

        val result =
            performer.perform(
                traceId = traceId,
                request = request,
            )

        assertEquals(
            AndroidExecutionAttemptStatus.FAILED,
            result.status,
        )
        assertEquals(
            "ANDROID_ACCESSIBILITY_TEST_FAILURE",
            result.error?.errorCode?.value,
        )
        assertEquals(
            traceId,
            result.error?.traceId,
        )
    }

    @Test
    fun `directive from another capability is rejected`() {
        val traceId =
            TraceId.from(
                "trace-stage-38-accessibility-execution-006",
            )

        val request =
            createRequest(traceId)

        val performer =
            AndroidAccessibilityExecutionPerformer(
                directiveProvider =
                    AndroidExecutionDirectiveProvider {
                            _,
                            _,
                        ->
                        AndroidExecutionDirective(
                            traceId = traceId,
                            capabilityId =
                                com.devil.core.model.capability.CapabilityId
                                    .from(
                                        "different-capability",
                                    ),
                            accessibilityRequest =
                                AndroidAccessibilityActionRequest(
                                    actionType =
                                        AndroidAccessibilityActionType
                                            .CLICK_VISIBLE_TEXT,
                                    target =
                                        AndroidAccessibilityTarget
                                            .fromText(
                                                "Send",
                                            ),
                                ),
                        )
                    },
            )

        kotlin.test.assertFailsWith<
            IllegalArgumentException,
        > {
            performer.perform(
                traceId = traceId,
                request = request,
            )
        }
    }

    private fun directiveProvider(
        traceId: TraceId,
        request: ExecutionRequest,
        targetText: String,
    ): AndroidExecutionDirectiveProvider {
        return AndroidExecutionDirectiveProvider {
                providerTrace,
                providerRequest,
            ->
            assertEquals(
                traceId,
                providerTrace,
            )
            assertEquals(
                request,
                providerRequest,
            )

            AndroidExecutionDirective(
                traceId = traceId,
                capabilityId =
                    request.capability.capabilityId,
                accessibilityRequest =
                    AndroidAccessibilityActionRequest(
                        actionType =
                            AndroidAccessibilityActionType
                                .CLICK_VISIBLE_TEXT,
                        target =
                            AndroidAccessibilityTarget
                                .fromText(
                                    targetText,
                                ),
                    ),
            )
        }
    }

    private fun createRequest(
        traceId: TraceId,
    ): ExecutionRequest {
        return ExecutionRequest.create(
            plan =
                PlanRecord.create(
                    planId =
                        PlanId.from(
                            "plan-stage-38-accessibility",
                        ),
                    task =
                        TaskRecord.create(
                            taskId =
                                TaskId.from(
                                    "task-stage-38-accessibility",
                                ),
                            decision =
                                DecisionRecord.create(
                                    understanding =
                                        UnderstandingRecord.create(
                                            context =
                                                ContextEnvelope.create(
                                                    traceId =
                                                        traceId,
                                                    schemaVersion =
                                                        SchemaVersion.from(1),
                                                    source =
                                                        ContextSource.TEST,
                                                    trustLevel =
                                                        ContextTrustLevel
                                                            .VERIFIED,
                                                    securityLevel =
                                                        ContextSecurityLevel
                                                            .RESTRICTED,
                                                    observedAt =
                                                        DevilTimestamp
                                                            .fromEpochMilliseconds(
                                                                1_754_000_380_000L,
                                                            ),
                                                ),
                                            state =
                                                UnderstandingState
                                                    .COMPLETE,
                                            summary =
                                                "Bounded Stage 38 test understanding.",
                                        ),
                                    state =
                                        DecisionState.SELECTED,
                                    summary =
                                        "Use the already-selected bounded accessibility capability.",
                                ),
                            state =
                                TaskState.CREATED,
                            summary =
                                "Attempt one explicitly supplied bounded accessibility action.",
                        ),
                    state =
                        PlanState.CREATED,
                    summary =
                        "Approach the existing constitutional execution boundary.",
                ),
            capability =
                AndroidAccessibilityCapability.contract,
        )
    }
}
