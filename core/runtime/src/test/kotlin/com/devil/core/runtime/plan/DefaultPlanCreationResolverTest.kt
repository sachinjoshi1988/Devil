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
import com.devil.core.model.plan.PlanCreationRequest
import com.devil.core.model.plan.PlanId
import com.devil.core.model.plan.PlanState
import com.devil.core.model.task.TaskId
import com.devil.core.model.task.TaskRecord
import com.devil.core.model.task.TaskState
import com.devil.core.model.understanding.UnderstandingRecord
import com.devil.core.model.understanding.UnderstandingState
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class DefaultPlanCreationResolverTest {

    @Test
    fun `create preserves plan identity task and strategy`() {
        val request = createRequest()
        val planId = PlanId.from(
            "plan-default-creation-resolver-001",
        )
        val resolver: PlanCreationResolver =
            DefaultPlanCreationResolver()

        val plan = resolver.create(
            request = request,
            planId = planId,
            strategy =
                "Use the constitutionally approved capability path.",
        )

        assertEquals(planId, plan.planId)
        assertEquals(request.task, plan.task)
        assertEquals(PlanState.CREATED, plan.state)
        assertEquals(
            "Use the constitutionally approved capability path.",
            plan.summary,
        )
    }

    @Test
    fun `create preserves and normalizes supplied strategy`() {
        val plan = DefaultPlanCreationResolver().create(
            request = createRequest(),
            planId = PlanId.from(
                "plan-default-creation-resolver-002",
            ),
            strategy =
                "  Request the approved camera-opening capability.  ",
        )

        assertEquals(
            "Request the approved camera-opening capability.",
            plan.summary,
        )
    }

    @Test
    fun `create does not copy task summary over supplied strategy`() {
        val request = createRequest()

        val plan = DefaultPlanCreationResolver().create(
            request = request,
            planId = PlanId.from(
                "plan-default-creation-resolver-003",
            ),
            strategy =
                "Use the approved capability-selection route.",
        )

        assertEquals(
            "Open the camera application.",
            request.task.summary,
        )
        assertEquals(
            "Use the approved capability-selection route.",
            plan.summary,
        )
    }

    @Test
    fun `create always begins in created lifecycle state`() {
        val plan = DefaultPlanCreationResolver().create(
            request = createRequest(),
            planId = PlanId.from(
                "plan-default-creation-resolver-004",
            ),
            strategy = "Use the approved capability path.",
        )

        assertEquals(PlanState.CREATED, plan.state)
    }

    @Test
    fun `create rejects blank supplied strategy`() {
        assertFailsWith<IllegalArgumentException> {
            DefaultPlanCreationResolver().create(
                request = createRequest(),
                planId = PlanId.from(
                    "plan-default-creation-resolver-005",
                ),
                strategy = "   ",
            )
        }
    }

    private fun createRequest(): PlanCreationRequest {
        return PlanCreationRequest.create(
            task = TaskRecord.create(
                taskId = TaskId.from(
                    "task-default-plan-creation-resolver-001",
                ),
                decision = DecisionRecord.create(
                    understanding = UnderstandingRecord.create(
                        context = ContextEnvelope.create(
                            traceId = TraceId.from(
                                "trace-default-plan-creation-resolver-001",
                            ),
                            schemaVersion = SchemaVersion.from(1),
                            source = ContextSource.TEXT,
                            trustLevel =
                                ContextTrustLevel.VERIFIED,
                            securityLevel =
                                ContextSecurityLevel.RESTRICTED,
                            observedAt =
                                DevilTimestamp.fromEpochMilliseconds(
                                    1_754_000_084_000L,
                                ),
                        ),
                        state = UnderstandingState.COMPLETE,
                        summary =
                            "Open the camera application.",
                    ),
                    state = DecisionState.SELECTED,
                    summary = "Open the camera application.",
                ),
                state = TaskState.CREATED,
                summary = "Open the camera application.",
            ),
        )
    }
}
