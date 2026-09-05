package com.devil.app.capability

import com.devil.app.accessibility.AndroidAccessibilityCapability
import com.devil.app.device.AndroidDeviceKnowledgeCapability
import com.devil.app.internet.AndroidInternetKnowledgeCapability
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

/**
 * Stage 337J Android integration protection.
 *
 * A general route does not activate a registered Android capability.
 *
 * REGISTERED != SELECTED.
 * INTENT_ROUTE != CAPABILITY_SELECTED.
 * ROUTE_CANDIDATE != CAPABILITY_AVAILABLE.
 * CAPABILITY_SELECTED != EXECUTION_APPROVED.
 * ROUTED != EXECUTED.
 * STAGE_337J != STAGE_337K_CAPABILITY_AVAILABILITY_AND_HEALTH.
 * STAGE_337J != STAGE_337L_ANDROID_EXECUTION_BRIDGE.
 */
class Stage337JAndroidGeneralIntentCapabilityRouterTest {

    @Test
    fun `settings route preserves exact Stage314 accessibility selection`() {
        val traceId =
            TraceId.from(
                "trace-stage337j-settings-preserved",
            )

        val result =
            DefaultAndroidCapabilitySelectionResolver().resolve(
                traceId = traceId,
                request =
                    request(
                        traceId = traceId,
                        intent = UnderstandingIntent.OPEN_TARGET,
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
    fun `device knowledge route does not activate registered device knowledge capability`() {
        val traceId =
            TraceId.from(
                "trace-stage337j-device-knowledge-not-activated",
            )

        val result =
            DefaultAndroidCapabilitySelectionResolver().resolve(
                traceId = traceId,
                request =
                    request(
                        traceId = traceId,
                        intent =
                            UnderstandingIntent.INFORMATION_QUERY,
                        target = "battery level",
                        predicate = "query",
                    ),
                registry =
                    CapabilityRegistryResult.create(
                        traceId = traceId,
                        status =
                            CapabilityRegistryStatus.AVAILABLE,
                        capabilities =
                            listOf(
                                AndroidDeviceKnowledgeCapability.contract,
                            ),
                    ),
            )

        assertEquals(
            CapabilitySelectionResolutionStatus.UNAVAILABLE,
            result.status,
        )
        assertNull(result.capability)
        assertNull(result.error)
    }

    @Test
    fun `general information route does not activate registered internet knowledge capability`() {
        val traceId =
            TraceId.from(
                "trace-stage337j-internet-not-activated",
            )

        val result =
            DefaultAndroidCapabilitySelectionResolver().resolve(
                traceId = traceId,
                request =
                    request(
                        traceId = traceId,
                        intent =
                            UnderstandingIntent.INFORMATION_QUERY,
                        target = "Kopargaon",
                        predicate = "query",
                    ),
                registry =
                    CapabilityRegistryResult.create(
                        traceId = traceId,
                        status =
                            CapabilityRegistryStatus.AVAILABLE,
                        capabilities =
                            listOf(
                                AndroidInternetKnowledgeCapability.contract,
                            ),
                    ),
            )

        assertEquals(
            CapabilitySelectionResolutionStatus.UNAVAILABLE,
            result.status,
        )
        assertNull(result.capability)
        assertNull(result.error)
    }

    @Test
    fun `routable action domains remain unavailable without Stage337J capability activation`() {
        val traceId =
            TraceId.from(
                "trace-stage337j-action-not-activated",
            )

        val result =
            DefaultAndroidCapabilitySelectionResolver().resolve(
                traceId = traceId,
                request =
                    request(
                        traceId = traceId,
                        intent =
                            UnderstandingIntent.ACTION_REQUEST,
                        target = "volume",
                        predicate = "increase",
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
            CapabilitySelectionResolutionStatus.UNAVAILABLE,
            result.status,
        )
        assertNull(result.capability)
    }

    private fun request(
        traceId: TraceId,
        intent: UnderstandingIntent,
        target: String,
        predicate: String? = null,
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
                        1_788_599_337_100L,
                    ),
            )

        val understanding =
            UnderstandingRecord.create(
                context = context,
                state =
                    UnderstandingState.COMPLETE,
                summary =
                    "Stage 337J structured understanding.",
                semantics =
                    UnderstandingSemantics.create(
                        intent = intent,
                        actionability =
                            UnderstandingActionability.ACTIONABLE,
                        meaning =
                            when (intent) {
                                UnderstandingIntent.OPEN_TARGET ->
                                    "open target"

                                UnderstandingIntent.ACTION_REQUEST ->
                                    "action request"

                                UnderstandingIntent.INFORMATION_QUERY ->
                                    "query information"

                                UnderstandingIntent.GREETING,
                                UnderstandingIntent.INFORMATIONAL,
                                -> error(
                                    "Stage337J Android helper expects actionable semantics.",
                                )
                            },
                        target = target,
                        predicate = predicate,
                    ),
            )

        val decision =
            DecisionRecord.create(
                understanding = understanding,
                state =
                    DecisionState.SELECTED,
                summary =
                    "Preserve Stage337J routing evidence through Decision.",
            )

        val task =
            TaskRecord.create(
                taskId =
                    TaskId.from(
                        "task-stage337j-general-intent-router",
                    ),
                decision = decision,
                state =
                    TaskState.CREATED,
                summary =
                    "Preserve Stage337J routing evidence through Task.",
            )

        val plan =
            PlanRecord.create(
                planId =
                    PlanId.from(
                        "plan-stage337j-general-intent-router",
                    ),
                task = task,
                state =
                    PlanState.CREATED,
                summary =
                    "Preserve Stage337J routing evidence through Plan.",
            )

        return CapabilitySelectionRequest.create(
            plan = plan,
        )
    }
}
