package com.devil.core.model.capability

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

class CapabilitySelectionRequestTest {

    @Test
    fun `create preserves originating plan`() {
        val plan = createPlan()

        val request = CapabilitySelectionRequest.create(
            plan = plan,
        )

        assertEquals(plan, request.plan)
    }

    private fun createPlan(): PlanRecord {
        return PlanRecord.create(
            planId = PlanId.from(
                "plan-capability-selection-request-001",
            ),
            task = TaskRecord.create(
                taskId = TaskId.from(
                    "task-capability-selection-request-001",
                ),
                decision = DecisionRecord.create(
                    understanding = UnderstandingRecord.create(
                        context = ContextEnvelope.create(
                            traceId = TraceId.from(
                                "trace-capability-selection-request-001",
                            ),
                            schemaVersion = SchemaVersion.from(1),
                            source = ContextSource.TEST,
                            trustLevel =
                                ContextTrustLevel.VERIFIED,
                            securityLevel =
                                ContextSecurityLevel.RESTRICTED,
                            observedAt =
                                DevilTimestamp
                                    .fromEpochMilliseconds(
                                        1_754_000_087_000L,
                                    ),
                        ),
                        state = UnderstandingState.COMPLETE,
                        summary =
                            "Open the camera application.",
                    ),
                    state = DecisionState.SELECTED,
                    summary =
                        "Open the camera application.",
                ),
                state = TaskState.CREATED,
                summary =
                    "Open the camera application.",
            ),
            state = PlanState.CREATED,
            summary =
                "Use the constitutionally approved capability path.",
        )
    }
}
