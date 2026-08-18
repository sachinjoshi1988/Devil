package com.devil.core.runtime.task

import com.devil.core.model.authorization.AuthorizationEvaluationState
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
import com.devil.core.model.plan.PlanId
import com.devil.core.model.plan.PlanRecord
import com.devil.core.model.plan.PlanState
import com.devil.core.model.task.CompoundWorkCapabilityReentryRecord
import com.devil.core.model.task.CompoundWorkCapabilityReentryRequest
import com.devil.core.model.task.CompoundWorkContinuationRecord
import com.devil.core.model.task.CompoundWorkPlanReentryRecord
import com.devil.core.model.task.CompoundWorkPlanReentryRequest
import com.devil.core.model.task.CompoundWorkReconsiderationRecord
import com.devil.core.model.task.CompoundWorkReconsiderationRequest
import com.devil.core.model.task.CompoundWorkRequest
import com.devil.core.model.task.CompoundWorkStep
import com.devil.core.model.task.CompoundWorkTaskReentryRecord
import com.devil.core.model.task.CompoundWorkTaskReentryRequest
import com.devil.core.model.task.TaskId
import com.devil.core.model.task.TaskRecord
import com.devil.core.model.task.TaskState
import com.devil.core.model.understanding.UnderstandingRecord
import com.devil.core.model.understanding.UnderstandingState
import com.devil.core.runtime.capability.CapabilitySelectionResult
import com.devil.core.runtime.capability.CapabilitySelectionStatus
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertSame

class Stage119CompoundWorkExecutiveReentryGovernanceTest {

    private val coordinator =
        CompoundWorkExecutiveReentryCoordinator()

    @Test
    fun `selected current capability prepares executive reentry preserving provenance`() {
        val fixture = fixture()

        val result =
            coordinator.prepare(
                currentTraceId = fixture.currentTrace,
                capabilityReentry = fixture.capabilityReentry,
                capability = fixture.capabilitySelection,
            )

        assertEquals(
            CompoundWorkExecutiveReentryStatus.PREPARED,
            result.status,
        )

        val record = requireNotNull(result.record)

        assertSame(
            fixture.capabilityReentry.record,
            record.request.capabilityReentry,
        )

        assertSame(
            fixture.capability,
            record.request.capability,
        )

        assertSame(
            fixture.plan,
            record.request.capabilityReentry.request.plan,
        )

        assertSame(
            fixture.task,
            record.request.capabilityReentry.request.plan.task,
        )

        assertSame(
            fixture.freshDecision,
            record.request.capabilityReentry.request.plan.task.decision,
        )
    }

    @Test
    fun `non prepared capability reentry defers`() {
        val fixture = fixture()

        val result =
            coordinator.prepare(
                currentTraceId = fixture.currentTrace,
                capabilityReentry =
                    CompoundWorkCapabilityReentryResult.create(
                        traceId = fixture.currentTrace,
                        status =
                            CompoundWorkCapabilityReentryStatus.DEFERRED,
                    ),
                capability = fixture.capabilitySelection,
            )

        assertEquals(
            CompoundWorkExecutiveReentryStatus.DEFERRED,
            result.status,
        )

        assertNull(result.record)
    }

    @Test
    fun `mismatched capability trace defers`() {
        val fixture = fixture()

        val result =
            coordinator.prepare(
                currentTraceId = fixture.currentTrace,
                capabilityReentry = fixture.capabilityReentry,
                capability =
                    CapabilitySelectionResult.create(
                        traceId = TraceId.from("stage119-other"),
                        status = CapabilitySelectionStatus.SELECTED,
                        capability = fixture.capability,
                    ),
            )

        assertEquals(
            CompoundWorkExecutiveReentryStatus.DEFERRED,
            result.status,
        )

        assertNull(result.record)
    }

    @Test
    fun `non selected capability defers`() {
        val fixture = fixture()

        val result =
            coordinator.prepare(
                currentTraceId = fixture.currentTrace,
                capabilityReentry = fixture.capabilityReentry,
                capability =
                    CapabilitySelectionResult.create(
                        traceId = fixture.currentTrace,
                        status = CapabilitySelectionStatus.DEFERRED,
                    ),
            )

        assertEquals(
            CompoundWorkExecutiveReentryStatus.DEFERRED,
            result.status,
        )

        assertNull(result.record)
    }

    @Test
    fun `originating compound work trace cannot be reused`() {
        val fixture = fixture()

        val capability =
            CapabilitySelectionResult.create(
                traceId = fixture.originalTrace,
                status = CapabilitySelectionStatus.SELECTED,
                capability = fixture.capability,
            )

        val result =
            coordinator.prepare(
                currentTraceId = fixture.originalTrace,
                capabilityReentry = fixture.capabilityReentry,
                capability = capability,
            )

        assertEquals(
            CompoundWorkExecutiveReentryStatus.DEFERRED,
            result.status,
        )

        assertNull(result.record)
    }

    @Test
    fun `exact selected current capability may prepare executive reentry only`() {
        val fixture = fixture()

        val result =
            coordinator.prepare(
                currentTraceId = fixture.currentTrace,
                capabilityReentry = fixture.capabilityReentry,
                capability = fixture.capabilitySelection,
            )

        assertEquals(
            CompoundWorkExecutiveReentryStatus.PREPARED,
            result.status,
        )

        requireNotNull(result.record)
    }

    private data class Fixture(
        val currentTrace: TraceId,
        val originalTrace: TraceId,
        val freshDecision: DecisionRecord,
        val task: TaskRecord,
        val plan: PlanRecord,
        val capability: CapabilityContract,
        val capabilitySelection: CapabilitySelectionResult,
        val capabilityReentry: CompoundWorkCapabilityReentryResult,
    )

    private fun fixture(): Fixture {
        val currentTrace =
            TraceId.from("stage119-current")

        val originalTrace =
            TraceId.from("stage119-original")

        val originalDecision =
            decision(
                traceId = originalTrace,
                summary = "Original Decision",
            )

        val freshDecision =
            decision(
                traceId = currentTrace,
                summary = "Fresh Decision",
            )

        val predecessor =
            CompoundWorkStep.create(
                position = 1,
                summary = "Completed predecessor.",
            )

        val step =
            CompoundWorkStep.create(
                position = 2,
                summary = "Stage 119 step",
            )

        val originalRequest =
            CompoundWorkRequest.create(
                decision = originalDecision,
                steps = listOf(predecessor, step),
            )

        val continuation =
            CompoundWorkContinuationRecord.create(
                request = originalRequest,
                step = step,
            )

        val reconsideration =
            CompoundWorkReconsiderationRecord.create(
                request =
                    CompoundWorkReconsiderationRequest.create(
                        continuation = continuation,
                        freshDecision = freshDecision,
                    ),
            )

        val taskReentry =
            CompoundWorkTaskReentryRecord.create(
                request =
                    CompoundWorkTaskReentryRequest.create(
                        reconsideration = reconsideration,
                        authorizationState =
                            AuthorizationEvaluationState.AUTHORIZED,
                    ),
            )

        val task =
            TaskRecord.create(
                taskId = TaskId.from("stage119-task"),
                decision = freshDecision,
                state = TaskState.CREATED,
                summary = "Stage 119 task",
            )

        val planReentry =
            CompoundWorkPlanReentryRecord.create(
                request =
                    CompoundWorkPlanReentryRequest.create(
                        taskReentry = taskReentry,
                        task = task,
                    ),
            )

        val plan =
            PlanRecord.create(
                planId = PlanId.from("stage119-plan"),
                task = task,
                state = PlanState.CREATED,
                summary = "Stage 119 plan",
            )

        val capabilityReentryRecord =
            CompoundWorkCapabilityReentryRecord.create(
                request =
                    CompoundWorkCapabilityReentryRequest.create(
                        planReentry = planReentry,
                        plan = plan,
                    ),
            )

        val capabilityReentry =
            CompoundWorkCapabilityReentryResult.create(
                traceId = currentTrace,
                status =
                    CompoundWorkCapabilityReentryStatus.PREPARED,
                record = capabilityReentryRecord,
            )

        val capability =
            CapabilityContract.create(
                capabilityId =
                    CapabilityId.from("stage119-capability"),
                category = CapabilityCategory.ACTION,
                name = "Stage 119 capability",
                description =
                    "Selected capability for Executive re-entry.",
            )

        val capabilitySelection =
            CapabilitySelectionResult.create(
                traceId = currentTrace,
                status = CapabilitySelectionStatus.SELECTED,
                capability = capability,
            )

        return Fixture(
            currentTrace = currentTrace,
            originalTrace = originalTrace,
            freshDecision = freshDecision,
            task = task,
            plan = plan,
            capability = capability,
            capabilitySelection = capabilitySelection,
            capabilityReentry = capabilityReentry,
        )
    }

    private fun decision(
        traceId: TraceId,
        summary: String,
    ): DecisionRecord {
        val understanding =
            UnderstandingRecord.create(
                context =
                    ContextEnvelope.create(
                        traceId = traceId,
                        schemaVersion = SchemaVersion.from(1),
                        source = ContextSource.TEXT,
                        trustLevel = ContextTrustLevel.VERIFIED,
                        securityLevel =
                            ContextSecurityLevel.RESTRICTED,
                        observedAt =
                            DevilTimestamp.fromEpochMilliseconds(
                                1_754_000_119_500L,
                            ),
                    ),
                state = UnderstandingState.COMPLETE,
                summary =
                    "Stage 119 constitutional Executive re-entry understanding.",
            )

        return DecisionRecord.create(
            understanding = understanding,
            state = DecisionState.SELECTED,
            summary = summary,
        )
    }
}
