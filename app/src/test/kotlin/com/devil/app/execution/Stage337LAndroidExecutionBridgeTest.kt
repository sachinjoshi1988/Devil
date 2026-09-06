package com.devil.app.execution

import com.devil.app.accessibility.AndroidAccessibilityActionRequest
import com.devil.app.accessibility.AndroidAccessibilityActionType
import com.devil.app.accessibility.AndroidAccessibilityCapability
import com.devil.app.accessibility.AndroidAccessibilityTarget
import com.devil.core.model.capability.CapabilityCategory
import com.devil.core.model.capability.CapabilityContract
import com.devil.core.model.capability.CapabilityId
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
import com.devil.core.model.understanding.UnderstandingActionability
import com.devil.core.model.understanding.UnderstandingIntent
import com.devil.core.model.understanding.UnderstandingRecord
import com.devil.core.model.understanding.UnderstandingSemantics
import com.devil.core.model.understanding.UnderstandingState
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class Stage337LAndroidExecutionBridgeTest {

    @Test
    fun `structured Settings semantics produce existing bounded Android directive`() {
        val traceId =
            TraceId.from(
                "trace-stage-337l-structured-settings",
            )

        val directive =
            DefaultAndroidExecutionDirectiveProvider()
                .provide(
                    traceId = traceId,
                    request =
                        createRequest(
                            traceId = traceId,
                            target = "settings",
                        ),
                )

        requireNotNull(directive)

        assertEquals(
            traceId,
            directive.traceId,
        )
        assertEquals(
            AndroidAccessibilityCapability.capabilityId,
            directive.capabilityId,
        )
        assertEquals(
            AndroidAccessibilityActionType.CLICK_VISIBLE_TEXT,
            directive.accessibilityRequest.actionType,
        )
        assertEquals(
            "Settings",
            directive.accessibilityRequest.target.text,
        )
    }

    @Test
    fun `unsupported structured target fails closed rather than guessing Android action`() {
        val traceId =
            TraceId.from(
                "trace-stage-337l-unsupported-target",
            )

        val directive =
            DefaultAndroidExecutionDirectiveProvider()
                .provide(
                    traceId = traceId,
                    request =
                        createRequest(
                            traceId = traceId,
                            target = "camera",
                        ),
                )

        assertNull(directive)
    }

    @Test
    fun `Settings route cannot create directive for foreign selected capability`() {
        val traceId =
            TraceId.from(
                "trace-stage-337l-foreign-capability",
            )

        val directive =
            DefaultAndroidExecutionDirectiveProvider()
                .provide(
                    traceId = traceId,
                    request =
                        createRequest(
                            traceId = traceId,
                            target = "settings",
                            capability =
                                CapabilityContract.create(
                                    capabilityId =
                                        CapabilityId.from(
                                            "capability-stage-337l-foreign",
                                        ),
                                    category =
                                        CapabilityCategory.ACTION,
                                    name =
                                        "Stage 337L Foreign Capability",
                                    description =
                                        "Foreign capability used to prove fail-closed Stage 337L identity binding.",
                                ),
                        ),
                )

        assertNull(directive)
    }

    @Test
    fun `explicit Stage 314 directive remains first priority over structured fallback`() {
        val traceId =
            TraceId.from(
                "trace-stage-337l-stage314-priority",
            )

        val store =
            AndroidRealExecutionDirectiveStore()

        store.arm(
            accessibilityRequest =
                AndroidAccessibilityActionRequest(
                    actionType =
                        AndroidAccessibilityActionType
                            .CLICK_VISIBLE_TEXT,
                    target =
                        AndroidAccessibilityTarget.fromText(
                            "Explicit Stage 314 Target",
                        ),
                ),
        )

        val directive =
            store.provide(
                traceId = traceId,
                request =
                    createRequest(
                        traceId = traceId,
                        target = "settings",
                    ),
            )

        requireNotNull(directive)

        assertEquals(
            "Explicit Stage 314 Target",
            directive.accessibilityRequest.target.text,
        )
    }

    @Test
    fun `empty Stage 314 store delegates to structured Settings bridge`() {
        val traceId =
            TraceId.from(
                "trace-stage-337l-structured-fallback",
            )

        val directive =
            AndroidRealExecutionDirectiveStore()
                .provide(
                    traceId = traceId,
                    request =
                        createRequest(
                            traceId = traceId,
                            target = "the settings",
                        ),
                )

        requireNotNull(directive)

        assertEquals(
            AndroidAccessibilityCapability.capabilityId,
            directive.capabilityId,
        )
        assertEquals(
            "Settings",
            directive.accessibilityRequest.target.text,
        )
    }

    private fun createRequest(
        traceId: TraceId,
        target: String,
        capability: CapabilityContract =
            AndroidAccessibilityCapability.contract,
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
                    DevilTimestamp.fromEpochMilliseconds(
                        1_788_000_337_000L,
                    ),
            )

        val understanding =
            UnderstandingRecord.create(
                context = context,
                state =
                    UnderstandingState.COMPLETE,
                summary =
                    "Stage 337L structured execution bridge test.",
                semantics =
                    UnderstandingSemantics.create(
                        intent =
                            UnderstandingIntent.OPEN_TARGET,
                        actionability =
                            UnderstandingActionability.ACTIONABLE,
                        meaning =
                            "open target",
                        target =
                            target,
                    ),
            )

        val decision =
            DecisionRecord.create(
                understanding = understanding,
                state =
                    DecisionState.SELECTED,
                summary =
                    "Use the already-established bounded Android embodiment.",
            )

        val task =
            TaskRecord.create(
                taskId =
                    TaskId.from(
                        "task-stage-337l-android-execution",
                    ),
                decision = decision,
                state =
                    TaskState.CREATED,
                summary =
                    "Preserve the structured Android execution target.",
            )

        val plan =
            PlanRecord.create(
                planId =
                    PlanId.from(
                        "plan-stage-337l-android-execution",
                    ),
                task = task,
                state =
                    PlanState.CREATED,
                summary =
                    "Use the existing governed Android execution path.",
            )

        return ExecutionRequest.create(
            plan = plan,
            capability = capability,
        )
    }
}
