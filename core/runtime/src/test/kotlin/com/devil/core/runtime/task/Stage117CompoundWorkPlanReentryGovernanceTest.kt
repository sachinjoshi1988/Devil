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
import com.devil.core.model.task.CompoundWorkContinuationRecord
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
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertSame

class Stage117CompoundWorkPlanReentryGovernanceTest {

    private val coordinator =
        CompoundWorkPlanReentryCoordinator()

    @Test
    fun `prepared Task reentry plus exact created Task may prepare Plan reentry only`() {
        val originalTrace =
            TraceId.from("trace-stage-117-original-001")
        val freshTrace =
            TraceId.from("trace-stage-117-fresh-001")

        val taskReentry =
            taskReentryResult(
                originalTrace = originalTrace,
                freshTrace = freshTrace,
            )

        val freshDecision =
            requireNotNull(taskReentry.record)
                .request
                .reconsideration
                .request
                .freshDecision

        val taskRecord =
            TaskRecord.create(
                taskId = TaskId.from("task:${freshTrace.value}"),
                decision = freshDecision,
                state = TaskState.CREATED,
                summary = freshDecision.summary,
            )

        val taskResult =
            TaskAuthorityResult.create(
                traceId = freshTrace,
                status = TaskAuthorityStatus.CREATED,
                task = taskRecord,
            )

        val result =
            coordinator.prepare(
                currentTraceId = freshTrace,
                taskReentry = taskReentry,
                task = taskResult,
            )

        assertEquals(
            CompoundWorkPlanReentryStatus.PREPARED,
            result.status,
        )

        val record = requireNotNull(result.record)

        assertSame(taskReentry.record, record.request.taskReentry)
        assertSame(taskRecord, record.request.task)
        assertSame(freshDecision, record.request.task.decision)

        assertSame(
            requireNotNull(taskReentry.record)
                .request
                .reconsideration,
            record.request
                .taskReentry
                .request
                .reconsideration,
        )

        assertSame(
            requireNotNull(taskReentry.record)
                .request
                .reconsideration
                .request
                .continuation,
            record.request
                .taskReentry
                .request
                .reconsideration
                .request
                .continuation,
        )

        assertSame(
            requireNotNull(taskReentry.record)
                .request
                .reconsideration
                .request
                .continuation
                .step,
            record.request
                .taskReentry
                .request
                .reconsideration
                .request
                .continuation
                .step,
        )
    }

    @Test
    fun `deferred Stage 116 Task reentry cannot prepare Plan reentry`() {
        val trace =
            TraceId.from("trace-stage-117-fresh-002")

        val taskReentry =
            CompoundWorkTaskReentryResult.create(
                traceId = trace,
                status = CompoundWorkTaskReentryStatus.DEFERRED,
            )

        val task =
            TaskAuthorityResult.create(
                traceId = trace,
                status = TaskAuthorityStatus.DEFERRED,
            )

        val result =
            coordinator.prepare(
                currentTraceId = trace,
                taskReentry = taskReentry,
                task = task,
            )

        assertEquals(
            CompoundWorkPlanReentryStatus.DEFERRED,
            result.status,
        )
        assertNull(result.record)
    }

    @Test
    fun `Task Authority must actually create Task before Plan reentry`() {
        val originalTrace =
            TraceId.from("trace-stage-117-original-003")
        val freshTrace =
            TraceId.from("trace-stage-117-fresh-003")

        val taskReentry =
            taskReentryResult(
                originalTrace = originalTrace,
                freshTrace = freshTrace,
            )

        val task =
            TaskAuthorityResult.create(
                traceId = freshTrace,
                status = TaskAuthorityStatus.DEFERRED,
            )

        val result =
            coordinator.prepare(
                currentTraceId = freshTrace,
                taskReentry = taskReentry,
                task = task,
            )

        assertEquals(
            CompoundWorkPlanReentryStatus.DEFERRED,
            result.status,
        )
        assertNull(result.record)
    }

    @Test
    fun `Task from another trace cannot prepare Plan reentry`() {
        val originalTrace =
            TraceId.from("trace-stage-117-original-004")
        val freshTrace =
            TraceId.from("trace-stage-117-fresh-004")

        val taskReentry =
            taskReentryResult(
                originalTrace = originalTrace,
                freshTrace = freshTrace,
            )

        val freshDecision =
            requireNotNull(taskReentry.record)
                .request
                .reconsideration
                .request
                .freshDecision

        val foreignTrace =
            TraceId.from("trace-stage-117-foreign-task-004")

        val foreignDecision =
            decision(
                traceId = foreignTrace,
                summary = freshDecision.summary,
            )

        val taskRecord =
            TaskRecord.create(
                taskId = TaskId.from("task:${foreignTrace.value}"),
                decision = foreignDecision,
                state = TaskState.CREATED,
                summary = foreignDecision.summary,
            )

        val task =
            TaskAuthorityResult.create(
                traceId = foreignTrace,
                status = TaskAuthorityStatus.CREATED,
                task = taskRecord,
            )

        val result =
            coordinator.prepare(
                currentTraceId = freshTrace,
                taskReentry = taskReentry,
                task = task,
            )

        assertEquals(
            CompoundWorkPlanReentryStatus.DEFERRED,
            result.status,
        )
        assertNull(result.record)
    }

    @Test
    fun `created Task must preserve exact fresh Decision identity`() {
        val originalTrace =
            TraceId.from("trace-stage-117-original-005")
        val freshTrace =
            TraceId.from("trace-stage-117-fresh-005")

        val taskReentry =
            taskReentryResult(
                originalTrace = originalTrace,
                freshTrace = freshTrace,
            )

        val foreignDecision =
            decision(
                traceId = freshTrace,
                summary = "Different selected Decision object.",
            )

        val taskRecord =
            TaskRecord.create(
                taskId = TaskId.from("task:${freshTrace.value}"),
                decision = foreignDecision,
                state = TaskState.CREATED,
                summary = foreignDecision.summary,
            )

        val task =
            TaskAuthorityResult.create(
                traceId = freshTrace,
                status = TaskAuthorityStatus.CREATED,
                task = taskRecord,
            )

        val result =
            coordinator.prepare(
                currentTraceId = freshTrace,
                taskReentry = taskReentry,
                task = task,
            )

        assertEquals(
            CompoundWorkPlanReentryStatus.DEFERRED,
            result.status,
        )
        assertNull(result.record)
    }

    @Test
    fun `originating compound-work trace cannot become current Plan reentry trace`() {
        val originalTrace =
            TraceId.from("trace-stage-117-original-006")
        val freshTrace =
            TraceId.from("trace-stage-117-fresh-006")

        val taskReentry =
            taskReentryResult(
                originalTrace = originalTrace,
                freshTrace = freshTrace,
            )

        val task =
            TaskAuthorityResult.create(
                traceId = originalTrace,
                status = TaskAuthorityStatus.DEFERRED,
            )

        val result =
            coordinator.prepare(
                currentTraceId = originalTrace,
                taskReentry = taskReentry,
                task = task,
            )

        assertEquals(
            CompoundWorkPlanReentryStatus.DEFERRED,
            result.status,
        )
        assertNull(result.record)
    }

    private fun taskReentryResult(
        originalTrace: TraceId,
        freshTrace: TraceId,
    ): CompoundWorkTaskReentryResult {
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

        return CompoundWorkTaskReentryResult.create(
            traceId = freshTrace,
            status = CompoundWorkTaskReentryStatus.PREPARED,
            record = taskReentryRecord,
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
                                1_754_000_117_500L,
                            ),
                    ),
                state = UnderstandingState.COMPLETE,
                summary =
                    "Stage 117 constitutional Plan re-entry understanding.",
            )

        return DecisionRecord.create(
            understanding = understanding,
            state = DecisionState.SELECTED,
            summary = summary,
        )
    }
}
