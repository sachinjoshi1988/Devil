package com.devil.app.capability

import com.devil.app.accessibility.AndroidAccessibilityCapability
import com.devil.core.model.capability.CapabilitySelectionRequest
import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.common.SchemaVersion
import com.devil.core.model.common.TraceId
import com.devil.core.model.context.ContextEnvelope
import com.devil.core.model.context.ContextSecurityLevel
import com.devil.core.model.context.ContextSource
import com.devil.core.model.context.ContextTrustLevel
import com.devil.core.model.decision.DecisionRecord
import com.devil.core.model.decision.DecisionState
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
import com.devil.core.runtime.capability.CapabilityRegistryResult
import com.devil.core.runtime.capability.CapabilityRegistryStatus
import com.devil.core.runtime.capability.CapabilitySelectionResolutionStatus
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class Stage314AndroidCapabilitySelectionResolverTest {

    @Test
    fun `open settings resolves exactly to Android accessibility capability`() {
        val traceId =
            TraceId.from(
                "trace-stage-314-settings",
            )

        val result =
            DefaultAndroidCapabilitySelectionResolver()
                .resolve(
                    traceId = traceId,
                    request =
                        request(
                            traceId = traceId,
                            target = "Settings",
                        ),
                    registry =
                        CapabilityRegistryResult.create(
                            traceId = traceId,
                            status =
                                CapabilityRegistryStatus.AVAILABLE,
                            capabilities =
                                listOf(
                                    AndroidAccessibilityCapability.contract,
                                ),
                        ),
                )

        assertEquals(
            CapabilitySelectionResolutionStatus.RESOLVED,
            result.status,
        )

        assertEquals(
            AndroidAccessibilityCapability.capabilityId,
            result.capability?.capabilityId,
        )
    }

    @Test
    fun `open the settings also resolves to same bounded capability`() {
        val traceId =
            TraceId.from(
                "trace-stage-314-the-settings",
            )

        val result =
            DefaultAndroidCapabilitySelectionResolver()
                .resolve(
                    traceId = traceId,
                    request =
                        request(
                            traceId = traceId,
                            target = "the settings",
                        ),
                    registry =
                        availableRegistry(traceId),
                )

        assertEquals(
            CapabilitySelectionResolutionStatus.RESOLVED,
            result.status,
        )

        assertEquals(
            AndroidAccessibilityCapability.capabilityId,
            result.capability?.capabilityId,
        )
    }

    @Test
    fun `unsupported Android open target remains unavailable`() {
        val traceId =
            TraceId.from(
                "trace-stage-314-unsupported",
            )

        val result =
            DefaultAndroidCapabilitySelectionResolver()
                .resolve(
                    traceId = traceId,
                    request =
                        request(
                            traceId = traceId,
                            target = "calculator",
                        ),
                    registry =
                        availableRegistry(traceId),
                )

        assertEquals(
            CapabilitySelectionResolutionStatus.UNAVAILABLE,
            result.status,
        )

        assertNull(result.capability)
        assertNull(result.error)
    }

    @Test
    fun `missing registered accessibility capability remains unavailable`() {
        val traceId =
            TraceId.from(
                "trace-stage-314-no-registration",
            )

        val result =
            DefaultAndroidCapabilitySelectionResolver()
                .resolve(
                    traceId = traceId,
                    request =
                        request(
                            traceId = traceId,
                            target = "Settings",
                        ),
                    registry =
                        CapabilityRegistryResult.create(
                            traceId = traceId,
                            status =
                                CapabilityRegistryStatus.UNAVAILABLE,
                        ),
                )

        assertEquals(
            CapabilitySelectionResolutionStatus.UNAVAILABLE,
            result.status,
        )

        assertNull(result.capability)
    }

    private fun availableRegistry(
        traceId: TraceId,
    ): CapabilityRegistryResult {
        return CapabilityRegistryResult.create(
            traceId = traceId,
            status =
                CapabilityRegistryStatus.AVAILABLE,
            capabilities =
                listOf(
                    AndroidAccessibilityCapability.contract,
                ),
        )
    }

    private fun request(
        traceId: TraceId,
        target: String,
    ): CapabilitySelectionRequest {
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
                        1_756_000_314_100L,
                    ),
            )

        val understanding =
            UnderstandingRecord.create(
                context = context,
                state =
                    UnderstandingState.COMPLETE,
                summary =
                    "User requested opening the target: $target.",
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
                    "Use the bounded Android target capability.",
            )

        val task =
            TaskRecord.create(
                taskId =
                    TaskId.from(
                        "task-stage-314-capability-selection",
                    ),
                decision = decision,
                state =
                    TaskState.CREATED,
                summary =
                    "Prepare bounded Stage 314 Android target execution.",
            )

        val plan =
            PlanRecord.create(
                planId =
                    PlanId.from(
                        "plan-stage-314-capability-selection",
                    ),
                task = task,
                state =
                    PlanState.CREATED,
                summary =
                    "Use the existing constitutional capability-selection path.",
            )

        return CapabilitySelectionRequest.create(
            plan = plan,
        )
    }
}
