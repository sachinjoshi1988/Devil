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
import com.devil.core.model.understanding.UnderstandingRecord
import com.devil.core.model.understanding.UnderstandingState
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertSame

class Stage314AndroidRealExecutionDirectiveStoreTest {

    @Test
    fun `empty Stage 314 directive store fails closed`() {
        val traceId =
            TraceId.from(
                "trace-stage-314-empty",
            )

        val store =
            AndroidRealExecutionDirectiveStore()

        assertNull(
            store.provide(
                traceId = traceId,
                request = createAccessibilityRequest(traceId),
            ),
        )
    }

    @Test
    fun `explicit Stage 314 action binds to genuine execution identity exactly once`() {
        val traceId =
            TraceId.from(
                "trace-stage-314-once",
            )

        val request =
            createAccessibilityRequest(
                traceId,
            )

        val actionRequest =
            AndroidAccessibilityActionRequest(
                actionType =
                    AndroidAccessibilityActionType
                        .CLICK_VISIBLE_TEXT,
                target =
                    AndroidAccessibilityTarget.fromText(
                        "Settings",
                    ),
            )

        val store =
            AndroidRealExecutionDirectiveStore()

        store.arm(
            accessibilityRequest =
                actionRequest,
        )

        val directive =
            store.provide(
                traceId = traceId,
                request = request,
            )

        assertEquals(
            traceId,
            directive?.traceId,
        )

        assertEquals(
            AndroidAccessibilityCapability.capabilityId,
            directive?.capabilityId,
        )

        assertSame(
            actionRequest,
            directive?.accessibilityRequest,
        )

        assertNull(
            store.provide(
                traceId = traceId,
                request = request,
            ),
        )
    }

    @Test
    fun `foreign capability cannot consume armed Stage 314 action`() {
        val traceId =
            TraceId.from(
                "trace-stage-314-foreign-capability",
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
                            "Settings",
                        ),
                ),
        )

        assertNull(
            store.provide(
                traceId = traceId,
                request =
                    createForeignCapabilityRequest(
                        traceId,
                    ),
            ),
        )

        val legitimateDirective =
            store.provide(
                traceId = traceId,
                request =
                    createAccessibilityRequest(
                        traceId,
                    ),
            )

        assertEquals(
            AndroidAccessibilityCapability.capabilityId,
            legitimateDirective?.capabilityId,
        )
    }

    @Test
    fun `directive lookup requires genuine matching trace identity`() {
        val requestTrace =
            TraceId.from(
                "trace-stage-314-request",
            )

        val foreignTrace =
            TraceId.from(
                "trace-stage-314-foreign",
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
                            "Settings",
                        ),
                ),
        )

        assertFailsWith<IllegalArgumentException> {
            store.provide(
                traceId = foreignTrace,
                request =
                    createAccessibilityRequest(
                        requestTrace,
                    ),
            )
        }
    }

    @Test
    fun `clear removes pending Stage 314 action`() {
        val traceId =
            TraceId.from(
                "trace-stage-314-clear",
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
                            "Settings",
                        ),
                ),
        )

        store.clear()

        assertNull(
            store.provide(
                traceId = traceId,
                request =
                    createAccessibilityRequest(
                        traceId,
                    ),
            ),
        )
    }

    private fun createAccessibilityRequest(
        traceId: TraceId,
    ): ExecutionRequest {
        return createRequest(
            traceId = traceId,
            capability =
                AndroidAccessibilityCapability.contract,
        )
    }

    private fun createForeignCapabilityRequest(
        traceId: TraceId,
    ): ExecutionRequest {
        return createRequest(
            traceId = traceId,
            capability =
                CapabilityContract.create(
                    capabilityId =
                        CapabilityId.from(
                            "stage-314-foreign-capability",
                        ),
                    category =
                        CapabilityCategory.ACTION,
                    name =
                        "Foreign Stage 314 Capability",
                    description =
                        "A non-accessibility capability used to validate fail-closed directive binding.",
                ),
        )
    }

    private fun createRequest(
        traceId: TraceId,
        capability: CapabilityContract,
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
                    "Stage 314 bounded real-device execution test.",
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
                        "task-stage-314-real-android",
                    ),
                decision = decision,
                state =
                    TaskState.CREATED,
                summary =
                    "Perform one explicit Stage 314 Android click.",
            )

        val plan =
            PlanRecord.create(
                planId =
                    PlanId.from(
                        "plan-stage-314-real-android",
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
