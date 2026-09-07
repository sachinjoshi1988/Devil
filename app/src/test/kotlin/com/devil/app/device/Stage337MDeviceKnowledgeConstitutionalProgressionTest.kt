package com.devil.app.device

import com.devil.app.capability.DefaultAndroidCapabilityRegistry
import com.devil.app.capability.DefaultAndroidCapabilitySelectionResolver
import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.common.SchemaVersion
import com.devil.core.model.common.TraceId
import com.devil.core.model.context.ContextEnvelope
import com.devil.core.model.context.ContextSecurityLevel
import com.devil.core.model.context.ContextSource
import com.devil.core.model.context.ContextTrustLevel
import com.devil.core.model.decision.DecisionEvaluationRequest
import com.devil.core.model.decision.DecisionState
import com.devil.core.model.plan.PlanId
import com.devil.core.model.plan.PlanState
import com.devil.core.model.task.TaskId
import com.devil.core.model.task.TaskState
import com.devil.core.model.understanding.UnderstandingActionability
import com.devil.core.model.understanding.UnderstandingIntent
import com.devil.core.model.understanding.UnderstandingRecord
import com.devil.core.model.understanding.UnderstandingSemantics
import com.devil.core.model.understanding.UnderstandingState
import com.devil.core.runtime.capability.CapabilityRegistryStatus
import com.devil.core.runtime.capability.CapabilitySelectionRequestStatus
import com.devil.core.runtime.capability.CapabilitySelectionResolutionResult
import com.devil.core.runtime.capability.CapabilitySelectionResolutionStatus
import com.devil.core.runtime.capability.DefaultCapabilitySelectionRequestProvider
import com.devil.core.runtime.decision.DecisionAuthorityResult
import com.devil.core.runtime.decision.DecisionAuthorityStatus
import com.devil.core.runtime.decision.DefaultDecisionEvaluationResolver
import com.devil.core.runtime.plan.DefaultPlanCreationRequestProvider
import com.devil.core.runtime.plan.DefaultPlanCreationResolver
import com.devil.core.runtime.plan.DefaultPlanningStrategyProvider
import com.devil.core.runtime.plan.PlanAuthorityResult
import com.devil.core.runtime.plan.PlanAuthorityStatus
import com.devil.core.runtime.plan.PlanCreationRequestStatus
import com.devil.core.runtime.plan.PlanningStrategyProvisionStatus
import com.devil.core.runtime.task.DefaultTaskCreationRequestProvider
import com.devil.core.runtime.task.DefaultTaskCreationResolver
import com.devil.core.runtime.task.TaskAuthorityResult
import com.devil.core.runtime.task.TaskAuthorityStatus
import com.devil.core.runtime.task.TaskCreationRequestStatus
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

/**
 * Stage337M regression for the constitutional progression that physically
 * failed on Redmi before the bounded INFORMATION_QUERY policy repair.
 *
 * The test deliberately does not manufacture a CapabilitySelectionRequest.
 * It requires the real bounded gates to progress:
 *
 * structured Understanding
 * -> Decision
 * -> Task request / Task
 * -> Plan request / planning strategy / Plan
 * -> CapabilitySelectionRequest
 * -> Android capability registry
 * -> Android capability-selection resolver.
 *
 * Supported Stage40 Device Knowledge targets must select only the registered
 * Android Device Knowledge capability and establish one trace-bound typed query
 * record.
 *
 * Battery must progress through the same constitutional gates far enough for
 * the Stage337M Device Knowledge domain to be claimed, but it must remain
 * unsupported because Stage40 has no genuine battery source.
 *
 * INFORMATION_QUERY != OPERATIONAL_BY_DEFAULT.
 * DEVICE_KNOWLEDGE_ROUTE != DEVICE_FACT.
 * CAPABILITY_SELECTED != FACT_OBSERVED.
 * QUERY_RECORD != AUTHORIZATION.
 * QUERY_RECORD != DEVICE_FACT.
 * BATTERY_QUERY != BATTERY_FACT.
 * UNSUPPORTED_DEVICE_QUERY != GUESSED_ANSWER.
 */
class Stage337MDeviceKnowledgeConstitutionalProgressionTest {

    @Test
    fun `supported Device Knowledge queries progress through constitutional chain and select exact capability`() {
        listOf(
            Triple(
                "device model",
                AndroidDeviceKnowledgeQueryType.DEVICE_MODEL,
                "trace-stage337m-full-path-device-model",
            ),
            Triple(
                "android version",
                AndroidDeviceKnowledgeQueryType.ANDROID_VERSION,
                "trace-stage337m-full-path-android-version",
            ),
            Triple(
                "device summary",
                AndroidDeviceKnowledgeQueryType.DEVICE_SUMMARY,
                "trace-stage337m-full-path-device-summary",
            ),
        ).forEach { (target, expectedQueryType, traceValue) ->
            val outcome =
                progressToAndroidCapabilitySelection(
                    traceId = TraceId.from(traceValue),
                    target = target,
                )

            assertEquals(
                CapabilitySelectionResolutionStatus.RESOLVED,
                outcome.resolution.status,
            )

            assertEquals(
                AndroidDeviceKnowledgeCapability.capabilityId,
                outcome.resolution.capability?.capabilityId,
            )

            assertEquals(
                expectedQueryType,
                assertNotNull(outcome.queryRecord).queryType,
            )

            assertNull(outcome.resolution.error)
        }
    }

    @Test
    fun `battery progresses to Device Knowledge claim but remains unsupported without a fact`() {
        val outcome =
            progressToAndroidCapabilitySelection(
                traceId =
                    TraceId.from(
                        "trace-stage337m-full-path-battery",
                    ),
                target = "battery level",
            )

        assertEquals(
            CapabilitySelectionResolutionStatus.UNAVAILABLE,
            outcome.resolution.status,
        )

        assertNull(outcome.resolution.capability)
        assertNull(outcome.resolution.error)

        val record =
            assertNotNull(outcome.queryRecord)

        assertNull(
            record.queryType,
            "Battery may claim the Device Knowledge response domain but must not establish a typed Stage40 fact query.",
        )
    }

    private fun progressToAndroidCapabilitySelection(
        traceId: TraceId,
        target: String,
    ): ProgressionOutcome {
        val understanding =
            UnderstandingRecord.create(
                context =
                    ContextEnvelope.create(
                        traceId = traceId,
                        schemaVersion =
                            SchemaVersion.from(1),
                        source = ContextSource.TEST,
                        trustLevel =
                            ContextTrustLevel.VERIFIED,
                        securityLevel =
                            ContextSecurityLevel.RESTRICTED,
                        observedAt =
                            DevilTimestamp.fromEpochMilliseconds(
                                1_756_000_337_600L,
                            ),
                    ),
                state = UnderstandingState.COMPLETE,
                summary =
                    "Stage337M structured Device Knowledge understanding.",
                semantics =
                    UnderstandingSemantics.create(
                        intent =
                            UnderstandingIntent.INFORMATION_QUERY,
                        actionability =
                            UnderstandingActionability.ACTIONABLE,
                        meaning = "query $target",
                        target = target,
                        predicate = "query",
                    ),
            )

        val decision =
            DefaultDecisionEvaluationResolver()
                .evaluate(
                    DecisionEvaluationRequest.create(
                        understanding = understanding,
                    ),
                )

        assertEquals(
            DecisionState.SELECTED,
            decision.state,
            "Stage337M bounded Device Knowledge must survive the Decision gate.",
        )

        val decisionResult =
            DecisionAuthorityResult.create(
                traceId = traceId,
                status =
                    DecisionAuthorityStatus.PRODUCED,
                decision = decision,
            )

        val taskRequestResult =
            DefaultTaskCreationRequestProvider()
                .provide(decisionResult)

        assertEquals(
            TaskCreationRequestStatus.AVAILABLE,
            taskRequestResult.status,
            "A selected Stage337M decision must expose a bounded Task request.",
        )

        val task =
            DefaultTaskCreationResolver()
                .create(
                    request =
                        requireNotNull(
                            taskRequestResult.request,
                        ),
                    taskId =
                        TaskId.from(
                            "task-${traceId.value}",
                        ),
                )

        assertEquals(
            TaskState.CREATED,
            task.state,
        )

        val taskResult =
            TaskAuthorityResult.create(
                traceId = traceId,
                status =
                    TaskAuthorityStatus.CREATED,
                task = task,
            )

        val planRequestResult =
            DefaultPlanCreationRequestProvider()
                .provide(taskResult)

        assertEquals(
            PlanCreationRequestStatus.AVAILABLE,
            planRequestResult.status,
            "A created Stage337M Task must expose a bounded Plan request.",
        )

        val planRequest =
            requireNotNull(
                planRequestResult.request,
            )

        val strategyResult =
            DefaultPlanningStrategyProvider()
                .provide(
                    traceId = traceId,
                    request = planRequest,
                )

        assertEquals(
            PlanningStrategyProvisionStatus.AVAILABLE,
            strategyResult.status,
            "Stage337M bounded Device Knowledge must survive the planning-strategy gate.",
        )

        val plan =
            DefaultPlanCreationResolver()
                .create(
                    request = planRequest,
                    planId =
                        PlanId.from(
                            "plan-${traceId.value}",
                        ),
                    strategy =
                        requireNotNull(
                            strategyResult.strategy,
                        ),
                )

        assertEquals(
            PlanState.CREATED,
            plan.state,
        )

        val planResult =
            PlanAuthorityResult.create(
                traceId = traceId,
                status =
                    PlanAuthorityStatus.CREATED,
                plan = plan,
            )

        val capabilityRequestResult =
            DefaultCapabilitySelectionRequestProvider()
                .provide(planResult)

        assertEquals(
            CapabilitySelectionRequestStatus.AVAILABLE,
            capabilityRequestResult.status,
            "A created Stage337M Plan must reach Capability Selection.",
        )

        val capabilityRequest =
            requireNotNull(
                capabilityRequestResult.request,
            )

        val registryResult =
            DefaultAndroidCapabilityRegistry()
                .obtain(
                    traceId = traceId,
                    request = capabilityRequest,
                )

        assertEquals(
            CapabilityRegistryStatus.AVAILABLE,
            registryResult.status,
        )

        val queryStore =
            Stage337MDeviceKnowledgeQueryStore()

        val resolution =
            DefaultAndroidCapabilitySelectionResolver(
                deviceKnowledgeQueryStore = queryStore,
            ).resolve(
                traceId = traceId,
                request = capabilityRequest,
                registry = registryResult,
            )

        return ProgressionOutcome(
            resolution = resolution,
            queryRecord =
                queryStore.consumeRecord(
                    traceId = traceId,
                ),
        )
    }

    private data class ProgressionOutcome(
        val resolution:
            CapabilitySelectionResolutionResult,
        val queryRecord:
            Stage337MDeviceKnowledgeQueryRecord?,
    )
}
