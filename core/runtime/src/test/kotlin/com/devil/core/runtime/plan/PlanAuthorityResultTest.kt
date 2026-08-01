package com.devil.core.runtime.plan

import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.common.SchemaVersion
import com.devil.core.model.common.TraceId
import com.devil.core.model.context.ContextEnvelope
import com.devil.core.model.context.ContextSecurityLevel
import com.devil.core.model.context.ContextSource
import com.devil.core.model.context.ContextTrustLevel
import com.devil.core.model.decision.DecisionRecord
import com.devil.core.model.decision.DecisionState
import com.devil.core.model.error.ErrorCode
import com.devil.core.model.error.UniversalErrorRecord
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

class PlanAuthorityResultTest {

    @Test
    fun `create preserves created result with matching plan`() {
        val task = createTask("trace-plan-authority-001")
        val plan = createPlan(task)

        val result = PlanAuthorityResult.create(
            traceId = task.decision.understanding.context.traceId,
            status = PlanAuthorityStatus.CREATED,
            plan = plan,
        )

        assertEquals(
            task.decision.understanding.context.traceId,
            result.traceId,
        )
        assertEquals(PlanAuthorityStatus.CREATED, result.status)
        assertEquals(plan, result.plan)
        assertNull(result.error)
    }

    @Test
    fun `create preserves deferred result without plan or error`() {
        val traceId = TraceId.from("trace-plan-authority-002")

        val result = PlanAuthorityResult.create(
            traceId = traceId,
            status = PlanAuthorityStatus.DEFERRED,
        )

        assertEquals(traceId, result.traceId)
        assertEquals(PlanAuthorityStatus.DEFERRED, result.status)
        assertNull(result.plan)
        assertNull(result.error)
    }

    @Test
    fun `create preserves failed result with matching error`() {
        val traceId = TraceId.from("trace-plan-authority-003")
        val error = createError(traceId)

        val result = PlanAuthorityResult.create(
            traceId = traceId,
            status = PlanAuthorityStatus.FAILED,
            error = error,
        )

        assertEquals(traceId, result.traceId)
        assertEquals(PlanAuthorityStatus.FAILED, result.status)
        assertNull(result.plan)
        assertEquals(error, result.error)
    }

    @Test
    fun `create rejects created result without plan`() {
        assertFailsWith<IllegalArgumentException> {
            PlanAuthorityResult.create(
                traceId = TraceId.from("trace-plan-authority-004"),
                status = PlanAuthorityStatus.CREATED,
            )
        }
    }

    @Test
    fun `create rejects deferred result with plan`() {
        val task = createTask("trace-plan-authority-005")

        assertFailsWith<IllegalArgumentException> {
            PlanAuthorityResult.create(
                traceId = task.decision.understanding.context.traceId,
                status = PlanAuthorityStatus.DEFERRED,
                plan = createPlan(task),
            )
        }
    }

    @Test
    fun `create rejects failed result without error`() {
        assertFailsWith<IllegalArgumentException> {
            PlanAuthorityResult.create(
                traceId = TraceId.from("trace-plan-authority-006"),
                status = PlanAuthorityStatus.FAILED,
            )
        }
    }

    @Test
    fun `create rejects plan from a different trace`() {
        val task = createTask("trace-plan-authority-plan-other")

        assertFailsWith<IllegalArgumentException> {
            PlanAuthorityResult.create(
                traceId = TraceId.from("trace-plan-authority-007"),
                status = PlanAuthorityStatus.CREATED,
                plan = createPlan(task),
            )
        }
    }

    @Test
    fun `create rejects error from a different trace`() {
        assertFailsWith<IllegalArgumentException> {
            PlanAuthorityResult.create(
                traceId = TraceId.from("trace-plan-authority-008"),
                status = PlanAuthorityStatus.FAILED,
                error = createError(
                    TraceId.from("trace-plan-authority-error-other"),
                ),
            )
        }
    }

    private fun createPlan(
        task: TaskRecord,
    ): PlanRecord {
        return PlanRecord.create(
            planId = PlanId.from("plan-authority-001"),
            task = task,
            state = PlanState.CREATED,
            summary = "Use the approved capability path.",
        )
    }

    private fun createTask(
        traceValue: String,
    ): TaskRecord {
        return TaskRecord.create(
            taskId = TaskId.from("task-plan-authority-001"),
            decision = createDecision(traceValue),
            state = TaskState.CREATED,
            summary = "Open the camera application.",
        )
    }

    private fun createDecision(
        traceValue: String,
    ): DecisionRecord {
        return DecisionRecord.create(
            understanding = createUnderstanding(traceValue),
            state = DecisionState.SELECTED,
            summary = "Open the camera application.",
        )
    }

    private fun createUnderstanding(
        traceValue: String,
    ): UnderstandingRecord {
        return UnderstandingRecord.create(
            context = createContext(traceValue),
            state = UnderstandingState.COMPLETE,
            summary = "Open the camera application.",
        )
    }

    private fun createContext(traceValue: String): ContextEnvelope {
        return ContextEnvelope.create(
            traceId = TraceId.from(traceValue),
            schemaVersion = SchemaVersion.from(1),
            source = ContextSource.TEST,
            trustLevel = ContextTrustLevel.VERIFIED,
            securityLevel = ContextSecurityLevel.RESTRICTED,
            observedAt = DevilTimestamp.fromEpochMilliseconds(
                1_754_000_020_000L,
            ),
        )
    }

    private fun createError(traceId: TraceId): UniversalErrorRecord {
        return UniversalErrorRecord.create(
            errorCode = ErrorCode.from("PLAN_CREATION_FAILED"),
            traceId = traceId,
            occurredAt = DevilTimestamp.fromEpochMilliseconds(
                1_754_000_020_500L,
            ),
            summary = "Plan creation failed.",
        )
    }
}
