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

class DefaultAndroidExecutionPerformerStage38Test {

    @Test
    fun `default production performer stays deferred because production directive provider is fail closed`() {
        val traceId =
            TraceId.from(
                "trace-stage-38-default-performer-001",
            )

        val result =
            DefaultAndroidExecutionPerformer()
                .perform(
                    traceId = traceId,
                    request = createRequest(traceId),
                )

        assertEquals(
            AndroidExecutionAttemptStatus.DEFERRED,
            result.status,
        )
    }

    @Test
    fun `explicit test directive reaches accessibility source through default router`() {
        val traceId =
            TraceId.from(
                "trace-stage-38-default-performer-002",
            )

        val request =
            createRequest(traceId)

        var calls = 0

        val performer =
            DefaultAndroidExecutionPerformer(
                directiveProvider =
                    AndroidExecutionDirectiveProvider {
                            providerTrace,
                            providerRequest,
                        ->
                        AndroidExecutionDirective(
                            traceId = providerTrace,
                            capabilityId =
                                providerRequest.capability
                                    .capabilityId,
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
                accessibilitySource =
                    AndroidAccessibilityActionSource {
                        calls += 1
                        AndroidAccessibilityActionResult
                            .attempted()
                    },
            )

        val result =
            performer.perform(
                traceId = traceId,
                request = request,
            )

        assertEquals(1, calls)
        assertEquals(
            AndroidExecutionAttemptStatus.ATTEMPTED,
            result.status,
        )
    }

    private fun createRequest(
        traceId: TraceId,
    ): ExecutionRequest {
        return ExecutionRequest.create(
            plan =
                PlanRecord.create(
                    planId =
                        PlanId.from(
                            "plan-stage-38-default-performer",
                        ),
                    task =
                        TaskRecord.create(
                            taskId =
                                TaskId.from(
                                    "task-stage-38-default-performer",
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
                                                                1_754_000_381_000L,
                                                            ),
                                                ),
                                            state =
                                                UnderstandingState
                                                    .COMPLETE,
                                            summary =
                                                "Stage 38 execution router test.",
                                        ),
                                    state =
                                        DecisionState.SELECTED,
                                    summary =
                                        "Use accessibility capability.",
                                ),
                            state =
                                TaskState.CREATED,
                            summary =
                                "Perform explicit bounded accessibility test.",
                        ),
                    state =
                        PlanState.CREATED,
                    summary =
                        "Use existing execution governance.",
                ),
            capability =
                AndroidAccessibilityCapability.contract,
        )
    }
}
