package com.devil.app.execution

import com.devil.app.accessibility.AndroidAccessibilityActionRequest
import com.devil.app.accessibility.AndroidAccessibilityActionType
import com.devil.app.accessibility.AndroidAccessibilityCapability
import com.devil.app.accessibility.AndroidAccessibilityTarget
import com.devil.app.accessibility.AndroidScreenElementRecord
import com.devil.app.accessibility.Stage314AndroidAccessibilityChangeReadinessStore
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
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.test.assertNull

class Stage314AndroidPostActionExpectationBindingTest {

    @Test
    fun `settings directive binds destination expectation only after genuine execution identity exists`() {
        val traceId =
            TraceId.from(
                "trace-stage-314-settings-expectation",
            )

        val expectationStore =
            Stage314AndroidPostActionExpectationStore()

        val readinessStore =
            Stage314AndroidAccessibilityChangeReadinessStore()

        val directiveStore =
            AndroidRealExecutionDirectiveStore(
                postActionExpectationStore =
                    expectationStore,
            accessibilityChangeReadinessStore =
                readinessStore,
            )

        assertNull(
            expectationStore.current(
                traceId = traceId,
                capabilityId =
                    AndroidAccessibilityCapability.capabilityId,
            ),
        )

        directiveStore.arm(
            accessibilityRequest =
                AndroidAccessibilityActionRequest(
                    actionType =
                        AndroidAccessibilityActionType
                            .CLICK_VISIBLE_TEXT,
                    target =
                        AndroidAccessibilityTarget.fromText(
                            "Settings",
                        ),
                ),
        )

        assertNull(
            expectationStore.current(
                traceId = traceId,
                capabilityId =
                    AndroidAccessibilityCapability.capabilityId,
            ),
        )

        val request =
            createExecutionRequest(
                traceId = traceId,
            )

        directiveStore.provide(
            traceId = traceId,
            request = request,
        )

        val expectation =
            expectationStore.current(
                traceId = traceId,
                capabilityId =
                    AndroidAccessibilityCapability.capabilityId,
            )

        assertEquals(
            traceId,
            expectation?.traceId,
        )

        assertEquals(
            AndroidAccessibilityCapability.capabilityId,
            expectation?.capabilityId,
        )

        assertEquals(
            "Settings, privacy, and permissions presentation",
            expectation?.expectedVisibleText,
        )

        assertTrue(
            readinessStore.markExecutionAttempted(
                traceId = traceId,
                capabilityId =
                    AndroidAccessibilityCapability.capabilityId,
            ),
        )

        val snapshot =
            listOf(
                AndroidScreenElementRecord.create(
                    position = 0,
                    text = "DEVIL",
                    contentDescription = null,
                ),
            )

        readinessStore.signalAccessibilitySnapshot(snapshot)
        readinessStore.signalAccessibilitySnapshot(snapshot)

        assertEquals(
            snapshot,
            readinessStore.awaitStableAccessibilitySnapshot(
                traceId = traceId,
                capabilityId =
                    AndroidAccessibilityCapability.capabilityId,
                timeoutMilliseconds = 10L,
            ),
        )
    }

    @Test
    fun `non settings accessibility directive does not fabricate destination expectation`() {
        val traceId =
            TraceId.from(
                "trace-stage-314-non-settings",
            )

        val expectationStore =
            Stage314AndroidPostActionExpectationStore()

        val readinessStore =
            Stage314AndroidAccessibilityChangeReadinessStore()

        val directiveStore =
            AndroidRealExecutionDirectiveStore(
                postActionExpectationStore =
                    expectationStore,
            accessibilityChangeReadinessStore =
                readinessStore,
            )

        directiveStore.arm(
            accessibilityRequest =
                AndroidAccessibilityActionRequest(
                    actionType =
                        AndroidAccessibilityActionType
                            .CLICK_VISIBLE_TEXT,
                    target =
                        AndroidAccessibilityTarget.fromText(
                            "Research",
                        ),
                ),
        )

        directiveStore.provide(
            traceId = traceId,
            request =
                createExecutionRequest(
                    traceId = traceId,
                ),
        )

        assertNull(
            expectationStore.current(
                traceId = traceId,
                capabilityId =
                    AndroidAccessibilityCapability.capabilityId,
            ),
        )

        assertFalse(
            readinessStore.markExecutionAttempted(
                traceId = traceId,
                capabilityId =
                    AndroidAccessibilityCapability.capabilityId,
            ),
        )
    }

    private fun createExecutionRequest(
        traceId: TraceId,
    ): ExecutionRequest {
        val context =
            ContextEnvelope.create(
                traceId = traceId,
                schemaVersion =
                    SchemaVersion.from(1),
                source =
                    ContextSource.TEST,
                trustLevel =
                    ContextTrustLevel.VERIFIED,
                securityLevel =
                    ContextSecurityLevel.RESTRICTED,
                observedAt =
                    DevilTimestamp
                        .fromEpochMilliseconds(
                            1_756_000_314_000L,
                        ),
            )

        val understanding =
            UnderstandingRecord.create(
                context = context,
                state =
                    UnderstandingState.COMPLETE,
                summary =
                    "Stage 314 bounded expectation binding test.",
            )

        val decision =
            DecisionRecord.create(
                understanding = understanding,
                state =
                    DecisionState.SELECTED,
                summary =
                    "Use explicit Stage 314 accessibility execution.",
            )

        val task =
            TaskRecord.create(
                taskId =
                    TaskId.from(
                        "task-stage-314-expectation-binding",
                    ),
                decision = decision,
                state =
                    TaskState.CREATED,
                summary =
                    "Bind one post-action expectation.",
            )

        val plan =
            PlanRecord.create(
                planId =
                    PlanId.from(
                        "plan-stage-314-expectation-binding",
                    ),
                task = task,
                state =
                    PlanState.CREATED,
                summary =
                    "Use the existing governed Android execution path.",
            )

        return ExecutionRequest.create(
            plan = plan,
            capability =
                AndroidAccessibilityCapability.contract,
        )
    }
}
