package com.devil.core.model.executive

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
import com.devil.core.model.task.TaskId
import com.devil.core.model.task.TaskRecord
import com.devil.core.model.task.TaskState
import com.devil.core.model.understanding.UnderstandingRecord
import com.devil.core.model.understanding.UnderstandingState
import kotlin.test.Test
import kotlin.test.assertEquals

class ExecutiveReadinessRequestTest {

    @Test
    fun `create preserves originating plan and selected capability`() {
        val plan = createPlan()
        val capability = createCapability()

        val request = ExecutiveReadinessRequest.create(
            plan = plan,
            capability = capability,
        )

        assertEquals(plan, request.plan)
        assertEquals(capability, request.capability)
    }

    @Test
    fun `create does not reinterpret plan lifecycle or capability contract`() {
        val plan = createPlan()
        val capability = createCapability()

        val request = ExecutiveReadinessRequest.create(
            plan = plan,
            capability = capability,
        )

        assertEquals(PlanState.CREATED, request.plan.state)
        assertEquals(
            "Use the constitutionally approved capability path.",
            request.plan.summary,
        )
        assertEquals(
            "capability-camera",
            request.capability.capabilityId.value,
        )
        assertEquals(
            CapabilityCategory.ACTION,
            request.capability.category,
        )
        assertEquals("Camera", request.capability.name)
    }

    private fun createCapability(): CapabilityContract {
        return CapabilityContract.create(
            capabilityId = CapabilityId.from(
                "capability-camera",
            ),
            category = CapabilityCategory.ACTION,
            name = "Camera",
            description =
                "Performs one bounded registered camera action.",
        )
    }

    private fun createPlan(): PlanRecord {
        return PlanRecord.create(
            planId = PlanId.from(
                "plan-executive-readiness-request-001",
            ),
            task = TaskRecord.create(
                taskId = TaskId.from(
                    "task-executive-readiness-request-001",
                ),
                decision = DecisionRecord.create(
                    understanding = UnderstandingRecord.create(
                        context = ContextEnvelope.create(
                            traceId = TraceId.from(
                                "trace-executive-readiness-request-001",
                            ),
                            schemaVersion = SchemaVersion.from(1),
                            source = ContextSource.TEXT,
                            trustLevel =
                                ContextTrustLevel.VERIFIED,
                            securityLevel =
                                ContextSecurityLevel.RESTRICTED,
                            observedAt =
                                DevilTimestamp.fromEpochMilliseconds(
                                    1_754_000_094_000L,
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
