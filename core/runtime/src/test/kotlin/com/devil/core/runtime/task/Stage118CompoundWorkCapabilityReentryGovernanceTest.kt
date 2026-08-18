package com.devil.core.runtime.task

import com.devil.core.model.authorization.AuthorizationEvaluationState
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
import com.devil.core.runtime.plan.PlanAuthorityResult
import com.devil.core.runtime.plan.PlanAuthorityStatus
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertSame

class Stage118CompoundWorkCapabilityReentryGovernanceTest {

    private val coordinator =
        CompoundWorkCapabilityReentryCoordinator()

    @Test
    fun `prepared Plan reentry plus created current Plan may prepare Capability reentry only`() {
        val originalTrace =
            TraceId.from("trace-stage-118-original-001")

        val freshTrace =
            TraceId.from("trace-stage-118-fresh-001")

        val planReentry =
            planReentryResult(
                originalTrace = originalTrace,
                freshTrace = freshTrace,
            )

        val planReentryRecord =
            requireNotNull(planReentry.record)

        val task =
            planReentryRecord.request.task

        val freshDecision =
            planReentryRecord
                .request
                .taskReentry
                .request
                .reconsideration
                .request
                .freshDecision

        val planRecord =
            PlanRecord.create(
                planId = PlanId.from("plan-stage-118-001"),
                task = task,
                state = PlanState.CREATED,
                summary = "Current created constitutional Plan.",
            )

        val plan =
            PlanAuthorityResult.create(
                traceId = freshTrace,
                status = PlanAuthorityStatus.CREATED,
                plan = planRecord,
            )

        val result =
            coordinator.prepare(
                currentTraceId = freshTrace,
                planReentry = planReentry,
                plan = plan,
            )

        assertEquals(
            CompoundWorkCapabilityReentryStatus.PREPARED,
            result.status,
        )

        val record =
            requireNotNull(result.record)

        assertSame(planReentry.record, record.request.planReentry)
        assertSame(planRecord, record.request.plan)
        assertSame(task, record.request.plan.task)
        assertSame(freshDecision, record.request.plan.task.decision)

        assertSame(
            planReentryRecord.request.taskReentry,
            record.request.planReentry.request.taskReentry,
        )

        assertSame(
            planReentryRecord
                .request
                .taskReentry
                .request
                .reconsideration,
            record.request.planReentry
                .request
                .taskReentry
                .request
                .reconsideration,
        )

        assertSame(
            planReentryRecord
                .request
                .taskReentry
                .request
                .reconsideration
                .request
                .continuation,
            record.request.planReentry
                .request
                .taskReentry
                .request
                .reconsideration
                .request
                .continuation,
        )

        assertSame(
            planReentryRecord
                .request
                .taskReentry
                .request
                .reconsideration
                .request
                .continuation
                .request,
            record.request.planReentry
                .request
                .taskReentry
                .request
                .reconsideration
                .request
                .continuation
                .request,
        )

        assertSame(
            planReentryRecord
                .request
                .taskReentry
                .request
                .reconsideration
                .request
                .continuation
                .step,
            record.request.planReentry
                .request
                .taskReentry
                .request
                .reconsideration
                .request
                .continuation
                .step,
        )
    }

    @Test
    fun `Stage 117 deferred Plan reentry cannot prepare Capability reentry`() {
        val trace =
            TraceId.from("trace-stage-118-fresh-002")

        val planReentry =
            CompoundWorkPlanReentryResult.create(
                traceId = trace,
                status = CompoundWorkPlanReentryStatus.DEFERRED,
            )

        val plan =
            PlanAuthorityResult.create(
                traceId = trace,
                status = PlanAuthorityStatus.DEFERRED,
            )

        val result =
            coordinator.prepare(
                currentTraceId = trace,
                planReentry = planReentry,
                plan = plan,
            )

        assertEquals(
            CompoundWorkCapabilityReentryStatus.DEFERRED,
            result.status,
        )
        assertNull(result.record)
    }

    @Test
    fun `current created Plan is mandatory for Capability reentry preparation`() {
        val originalTrace =
            TraceId.from("trace-stage-118-original-003")

        val freshTrace =
            TraceId.from("trace-stage-118-fresh-003")

        val planReentry =
            planReentryResult(
                originalTrace = originalTrace,
                freshTrace = freshTrace,
            )

        val plan =
            PlanAuthorityResult.create(
                traceId = freshTrace,
                status = PlanAuthorityStatus.DEFERRED,
            )

        val result =
            coordinator.prepare(
                currentTraceId = freshTrace,
                planReentry = planReentry,
                plan = plan,
            )

        assertEquals(
            CompoundWorkCapabilityReentryStatus.DEFERRED,
            result.status,
        )
        assertNull(result.record)
    }

    @Test
    fun `Plan from another trace cannot prepare Capability reentry`() {
        val originalTrace =
            TraceId.from("trace-stage-118-original-004")

        val freshTrace =
            TraceId.from("trace-stage-118-fresh-004")

        val planReentry =
            planReentryResult(
                originalTrace = originalTrace,
                freshTrace = freshTrace,
            )

        val task =
            requireNotNull(planReentry.record)
                .request
                .task

        val foreignTrace =
            TraceId.from("trace-stage-118-foreign-plan-004")

        val foreignDecision =
            decision(
                traceId = foreignTrace,
                summary = "Foreign selected Decision.",
            )

        val foreignTask =
            TaskRecord.create(
                taskId = TaskId.from("task-stage-118-foreign-004"),
                decision = foreignDecision,
                state = TaskState.CREATED,
                summary = foreignDecision.summary,
            )

        val foreignPlanRecord =
            PlanRecord.create(
                planId = PlanId.from("plan-stage-118-foreign-004"),
                task = foreignTask,
                state = PlanState.CREATED,
                summary = "Foreign Plan.",
            )

        val plan =
            PlanAuthorityResult.create(
                traceId = foreignTrace,
                status = PlanAuthorityStatus.CREATED,
                plan = foreignPlanRecord,
            )

        val result =
            coordinator.prepare(
                currentTraceId = freshTrace,
                planReentry = planReentry,
                plan = plan,
            )

        assertEquals(
            CompoundWorkCapabilityReentryStatus.DEFERRED,
            result.status,
        )
        assertNull(result.record)

        require(task.state == TaskState.CREATED)
    }

    @Test
    fun `created Plan must preserve exact Stage 117 Task`() {
        val originalTrace =
            TraceId.from("trace-stage-118-original-005")

        val freshTrace =
            TraceId.from("trace-stage-118-fresh-005")

        val planReentry =
            planReentryResult(
                originalTrace = originalTrace,
                freshTrace = freshTrace,
            )

        val record =
            requireNotNull(planReentry.record)

        val freshDecision =
            record
                .request
                .taskReentry
                .request
                .reconsideration
                .request
                .freshDecision

        val differentTask =
            TaskRecord.create(
                taskId = TaskId.from("task-stage-118-different-005"),
                decision = freshDecision,
                state = TaskState.CREATED,
                summary = "Different Task object preserving same Decision.",
            )

        val planRecord =
            PlanRecord.create(
                planId = PlanId.from("plan-stage-118-005"),
                task = differentTask,
                state = PlanState.CREATED,
                summary = "Plan attached to different Task.",
            )

        val plan =
            PlanAuthorityResult.create(
                traceId = freshTrace,
                status = PlanAuthorityStatus.CREATED,
                plan = planRecord,
            )

        val result =
            coordinator.prepare(
                currentTraceId = freshTrace,
                planReentry = planReentry,
                plan = plan,
            )

        assertEquals(
            CompoundWorkCapabilityReentryStatus.DEFERRED,
            result.status,
        )
        assertNull(result.record)
    }

    @Test
    fun `Stage 117 fresh trace must equal current Capability reentry trace`() {
        val originalTrace =
            TraceId.from("trace-stage-118-original-006")

        val freshTrace =
            TraceId.from("trace-stage-118-fresh-006")

        val currentTrace =
            TraceId.from("trace-stage-118-current-006")

        val planReentry =
            planReentryResult(
                originalTrace = originalTrace,
                freshTrace = freshTrace,
            )

        val currentDecision =
            decision(
                traceId = currentTrace,
                summary = "Current unrelated selected Decision.",
            )

        val currentTask =
            TaskRecord.create(
                taskId = TaskId.from("task-stage-118-current-006"),
                decision = currentDecision,
                state = TaskState.CREATED,
                summary = currentDecision.summary,
            )

        val currentPlan =
            PlanRecord.create(
                planId = PlanId.from("plan-stage-118-current-006"),
                task = currentTask,
                state = PlanState.CREATED,
                summary = "Current unrelated Plan.",
            )

        val plan =
            PlanAuthorityResult.create(
                traceId = currentTrace,
                status = PlanAuthorityStatus.CREATED,
                plan = currentPlan,
            )

        val result =
            coordinator.prepare(
                currentTraceId = currentTrace,
                planReentry = planReentry,
                plan = plan,
            )

        assertEquals(
            CompoundWorkCapabilityReentryStatus.DEFERRED,
            result.status,
        )
        assertNull(result.record)
    }

    private fun planReentryResult(
        originalTrace: TraceId,
        freshTrace: TraceId,
    ): CompoundWorkPlanReentryResult {
        val originalDecision =
            decision(
                traceId = originalTrace,
                summary = "Original governed compound-work Decision.",
            )

        val compoundRequest =
            CompoundWorkRequest.create(
                decision = originalDecision,
                steps =
                    listOf(
                        CompoundWorkStep.create(
                            position = 1,
                            summary = "Completed predecessor.",
                        ),
                        CompoundWorkStep.create(
                            position = 2,
                            summary = "Exact Stage 77 eligible step.",
                        ),
                    ),
            )

        val continuation =
            CompoundWorkContinuationRecord.create(
                request = compoundRequest,
                step = compoundRequest.steps[1],
            )

        val freshDecision =
            decision(
                traceId = freshTrace,
                summary = "Fresh selected reconsideration Decision.",
            )

        val reconsideration =
            CompoundWorkReconsiderationRecord.create(
                request =
                    CompoundWorkReconsiderationRequest.create(
                        continuation = continuation,
                        freshDecision = freshDecision,
                    ),
            )

        val taskReentryRecord =
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
                taskId = TaskId.from("task:${freshTrace.value}"),
                decision = freshDecision,
                state = TaskState.CREATED,
                summary = freshDecision.summary,
            )

        val planReentryRecord =
            CompoundWorkPlanReentryRecord.create(
                request =
                    CompoundWorkPlanReentryRequest.create(
                        taskReentry = taskReentryRecord,
                        task = task,
                    ),
            )

        return CompoundWorkPlanReentryResult.create(
            traceId = freshTrace,
            status = CompoundWorkPlanReentryStatus.PREPARED,
            record = planReentryRecord,
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
                        securityLevel = ContextSecurityLevel.RESTRICTED,
                        observedAt =
                            DevilTimestamp.fromEpochMilliseconds(
                                1_754_000_118_500L,
                            ),
                    ),
                state = UnderstandingState.COMPLETE,
                summary =
                    "Stage 118 constitutional Capability re-entry understanding.",
            )

        return DecisionRecord.create(
            understanding = understanding,
            state = DecisionState.SELECTED,
            summary = summary,
        )
    }
}
