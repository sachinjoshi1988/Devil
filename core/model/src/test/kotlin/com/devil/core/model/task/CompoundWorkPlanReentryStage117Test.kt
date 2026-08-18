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
import com.devil.core.model.understanding.UnderstandingRecord
import com.devil.core.model.understanding.UnderstandingState
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertSame

class CompoundWorkPlanReentryStage117Test {

    @Test
    fun `Plan reentry request preserves exact Stage 116 provenance and created Task`() {
        val taskReentry =
            taskReentryRecord(
                originalTrace = "trace-stage-117-original-001",
                freshTrace = "trace-stage-117-fresh-001",
            )

        val freshDecision =
            taskReentry.request.reconsideration.request.freshDecision

        val task =
            TaskRecord.create(
                taskId = TaskId.from("task:trace-stage-117-fresh-001"),
                decision = freshDecision,
                state = TaskState.CREATED,
                summary = freshDecision.summary,
            )

        val request =
            CompoundWorkPlanReentryRequest.create(
                taskReentry = taskReentry,
                task = task,
            )

        val record =
            CompoundWorkPlanReentryRecord.create(
                request = request,
            )

        assertSame(taskReentry, record.request.taskReentry)
        assertSame(task, record.request.task)

        assertSame(
            taskReentry.request.reconsideration,
            record.request.taskReentry.request.reconsideration,
        )

        assertSame(
            taskReentry.request.reconsideration.request.continuation,
            record.request.taskReentry.request.reconsideration.request.continuation,
        )

        assertSame(
            taskReentry.request.reconsideration.request.continuation.request,
            record.request.taskReentry.request.reconsideration.request.continuation.request,
        )

        assertSame(
            taskReentry.request.reconsideration.request.continuation.step,
            record.request.taskReentry.request.reconsideration.request.continuation.step,
        )

        assertSame(
            freshDecision,
            record.request.task.decision,
        )
    }

    @Test
    fun `Plan reentry request rejects Task carrying foreign Decision`() {
        val taskReentry =
            taskReentryRecord(
                originalTrace = "trace-stage-117-original-002",
                freshTrace = "trace-stage-117-fresh-002",
            )

        val foreignDecision =
            decision(
                trace = "trace-stage-117-fresh-002",
                summary = "Different selected Decision object.",
            )

        val task =
            TaskRecord.create(
                taskId = TaskId.from("task:trace-stage-117-fresh-002"),
                decision = foreignDecision,
                state = TaskState.CREATED,
                summary = foreignDecision.summary,
            )

        assertFailsWith<IllegalArgumentException> {
            CompoundWorkPlanReentryRequest.create(
                taskReentry = taskReentry,
                task = task,
            )
        }
    }

    @Test
    fun `Plan reentry request requires created Task state`() {
        val taskReentry =
            taskReentryRecord(
                originalTrace = "trace-stage-117-original-003",
                freshTrace = "trace-stage-117-fresh-003",
            )

        val freshDecision =
            taskReentry.request.reconsideration.request.freshDecision

        val task =
            TaskRecord.create(
                taskId = TaskId.from("task:trace-stage-117-fresh-003"),
                decision = freshDecision,
                state = TaskState.ACTIVE,
                summary = freshDecision.summary,
            )

        assertFailsWith<IllegalArgumentException> {
            CompoundWorkPlanReentryRequest.create(
                taskReentry = taskReentry,
                task = task,
            )
        }
    }

    private fun taskReentryRecord(
        originalTrace: String,
        freshTrace: String,
    ): CompoundWorkTaskReentryRecord {
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
                            summary = "Previously completed exact step.",
                        ),
                        CompoundWorkStep.create(
                            position = 2,
                            summary = "Exact eligible step.",
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

        return CompoundWorkTaskReentryRecord.create(
            request =
                CompoundWorkTaskReentryRequest.create(
                    reconsideration = reconsideration,
                    authorizationState =
                        AuthorizationEvaluationState.AUTHORIZED,
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
                                1_754_000_117_000L,
                            ),
                    ),
                state = UnderstandingState.COMPLETE,
                summary = "Stage 117 bounded Plan re-entry understanding.",
            )

        return DecisionRecord.create(
            understanding = understanding,
            state = DecisionState.SELECTED,
            summary = summary,
        )
    }
}
