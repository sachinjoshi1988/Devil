package com.devil.core.model.task

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
import com.devil.core.model.understanding.UnderstandingRecord
import com.devil.core.model.understanding.UnderstandingState
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertSame

class CompoundWorkCapabilityReentryStage118Test {

    @Test
    fun `Capability reentry request preserves exact Stage 117 provenance and Plan`() {
        val planReentry =
            planReentryRecord(
                originalTrace = "trace-stage-118-original-001",
                freshTrace = "trace-stage-118-fresh-001",
            )

        val task =
            planReentry.request.task

        val plan =
            PlanRecord.create(
                planId = PlanId.from("plan-stage-118-001"),
                task = task,
                state = PlanState.CREATED,
                summary = "Bounded constitutional Plan.",
            )

        val request =
            CompoundWorkCapabilityReentryRequest.create(
                planReentry = planReentry,
                plan = plan,
            )

        val record =
            CompoundWorkCapabilityReentryRecord.create(
                request = request,
            )

        assertSame(planReentry, record.request.planReentry)
        assertSame(plan, record.request.plan)
        assertSame(task, record.request.plan.task)

        assertSame(
            planReentry.request.taskReentry,
            record.request.planReentry.request.taskReentry,
        )

        assertSame(
            planReentry.request.taskReentry.request.reconsideration,
            record.request.planReentry
                .request
                .taskReentry
                .request
                .reconsideration,
        )

        assertSame(
            planReentry.request.taskReentry
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
            planReentry.request.taskReentry
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
            planReentry.request.taskReentry
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

        assertSame(
            planReentry.request.taskReentry
                .request
                .reconsideration
                .request
                .freshDecision,
            record.request.plan.task.decision,
        )
    }

    @Test
    fun `Capability reentry request rejects non created Plan`() {
        val planReentry =
            planReentryRecord(
                originalTrace = "trace-stage-118-original-002",
                freshTrace = "trace-stage-118-fresh-002",
            )

        val plan =
            PlanRecord.create(
                planId = PlanId.from("plan-stage-118-002"),
                task = planReentry.request.task,
                state = PlanState.READY,
                summary = "Plan not in created state.",
            )

        assertFailsWith<IllegalArgumentException> {
            CompoundWorkCapabilityReentryRequest.create(
                planReentry = planReentry,
                plan = plan,
            )
        }
    }

    @Test
    fun `Capability reentry request rejects Plan bound to another Task`() {
        val planReentry =
            planReentryRecord(
                originalTrace = "trace-stage-118-original-003",
                freshTrace = "trace-stage-118-fresh-003",
            )

        val freshDecision =
            planReentry.request.taskReentry
                .request
                .reconsideration
                .request
                .freshDecision

        val otherTask =
            TaskRecord.create(
                taskId = TaskId.from("task-stage-118-other-003"),
                decision = freshDecision,
                state = TaskState.CREATED,
                summary = "Different Task object.",
            )

        val plan =
            PlanRecord.create(
                planId = PlanId.from("plan-stage-118-003"),
                task = otherTask,
                state = PlanState.CREATED,
                summary = "Plan bound to another Task object.",
            )

        assertFailsWith<IllegalArgumentException> {
            CompoundWorkCapabilityReentryRequest.create(
                planReentry = planReentry,
                plan = plan,
            )
        }
    }

    private fun planReentryRecord(
        originalTrace: String,
        freshTrace: String,
    ): CompoundWorkPlanReentryRecord {
        val originalDecision =
            decision(
                trace = originalTrace,
                summary = "Original bounded compound-work Decision.",
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
                            summary = "Exact eligible Stage 77 step.",
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
                trace = freshTrace,
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
                taskId = TaskId.from("task:$freshTrace"),
                decision = freshDecision,
                state = TaskState.CREATED,
                summary = freshDecision.summary,
            )

        return CompoundWorkPlanReentryRecord.create(
            request =
                CompoundWorkPlanReentryRequest.create(
                    taskReentry = taskReentry,
                    task = task,
                ),
        )
    }

    private fun decision(
        trace: String,
        summary: String,
    ): DecisionRecord {
        val traceId = TraceId.from(trace)

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
                                1_754_000_118_000L,
                            ),
                    ),
                state = UnderstandingState.COMPLETE,
                summary = "Stage 118 bounded understanding.",
            )

        return DecisionRecord.create(
            understanding = understanding,
            state = DecisionState.SELECTED,
            summary = summary,
        )
    }
}
