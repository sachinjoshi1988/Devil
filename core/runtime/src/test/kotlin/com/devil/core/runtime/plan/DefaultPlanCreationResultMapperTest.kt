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

class DefaultPlanCreationResultMapperTest {

    @Test
    fun `map preserves created plan as operationally created`() {
        val traceId = TraceId.from(
            "trace-plan-result-mapper-001",
        )
        val plan = createPlan(
            traceId = traceId,
            state = PlanState.CREATED,
        )
        val mapper: PlanCreationResultMapper =
            DefaultPlanCreationResultMapper()

        val result = mapper.map(
            traceId = traceId,
            plan = plan,
        )

        assertEquals(traceId, result.traceId)
        assertEquals(
            PlanAuthorityStatus.CREATED,
            result.status,
        )
        assertEquals(plan, result.plan)
        assertEquals(
            PlanState.CREATED,
            result.plan?.state,
        )
        assertNull(result.error)
    }

    @Test
    fun `map preserves waiting plan lifecycle state`() {
        val traceId = TraceId.from(
            "trace-plan-result-mapper-002",
        )

        val result = DefaultPlanCreationResultMapper().map(
            traceId = traceId,
            plan = createPlan(
                traceId = traceId,
                state = PlanState.WAITING,
            ),
        )

        assertEquals(
            PlanAuthorityStatus.CREATED,
            result.status,
        )
        assertEquals(
            PlanState.WAITING,
            result.plan?.state,
        )
        assertNull(result.error)
    }

    @Test
    fun `map preserves ready plan lifecycle state`() {
        val traceId = TraceId.from(
            "trace-plan-result-mapper-003",
        )

        val result = DefaultPlanCreationResultMapper().map(
            traceId = traceId,
            plan = createPlan(
                traceId = traceId,
                state = PlanState.READY,
            ),
        )

        assertEquals(
            PlanAuthorityStatus.CREATED,
            result.status,
        )
        assertEquals(
            PlanState.READY,
            result.plan?.state,
        )
    }

    @Test
    fun `map preserves active plan lifecycle state`() {
        val traceId = TraceId.from(
            "trace-plan-result-mapper-004",
        )

        val result = DefaultPlanCreationResultMapper().map(
            traceId = traceId,
            plan = createPlan(
                traceId = traceId,
                state = PlanState.ACTIVE,
            ),
        )

        assertEquals(
            PlanAuthorityStatus.CREATED,
            result.status,
        )
        assertEquals(
            PlanState.ACTIVE,
            result.plan?.state,
        )
    }

    @Test
    fun `map rejects plan from a different trace`() {
        assertFailsWith<IllegalArgumentException> {
            DefaultPlanCreationResultMapper().map(
                traceId = TraceId.from(
                    "trace-plan-result-mapper-005",
                ),
                plan = createPlan(
                    traceId = TraceId.from(
                        "trace-plan-record-other",
                    ),
                    state = PlanState.CREATED,
                ),
            )
        }
    }

    private fun createPlan(
        traceId: TraceId,
        state: PlanState,
    ): PlanRecord {
        return PlanRecord.create(
            planId = PlanId.from("plan-result-mapper"),
            task = TaskRecord.create(
                taskId = TaskId.from("task-plan-result-mapper"),
                decision = DecisionRecord.create(
                    understanding = UnderstandingRecord.create(
                        context = ContextEnvelope.create(
                            traceId = traceId,
                            schemaVersion = SchemaVersion.from(1),
                            source = ContextSource.TEST,
                            trustLevel =
                                ContextTrustLevel.VERIFIED,
                            securityLevel =
                                ContextSecurityLevel.RESTRICTED,
                            observedAt =
                                DevilTimestamp.fromEpochMilliseconds(
                                    1_754_000_085_000L,
                                ),
                        ),
                        state = UnderstandingState.COMPLETE,
                        summary =
                            "Bounded understanding was produced.",
                    ),
                    state = DecisionState.SELECTED,
                    summary =
                        "Bounded constitutional decision was selected.",
                ),
                state = TaskState.CREATED,
                summary =
                    "Bounded constitutional task was created.",
            ),
            state = state,
            summary =
                "Use the constitutionally approved capability path.",
        )
    }
}
