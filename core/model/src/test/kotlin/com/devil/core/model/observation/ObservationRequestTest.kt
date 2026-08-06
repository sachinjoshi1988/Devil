package com.devil.core.model.observation

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
import com.devil.core.model.execution.ExecutionRequest
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

class ObservationRequestTest {

    @Test
    fun `create preserves approved execution request`() {
        val execution = createExecutionRequest()

        val request = ObservationRequest.create(
            execution = execution,
        )

        assertEquals(execution, request.execution)
    }

    @Test
    fun `create does not reinterpret execution dependencies`() {
        val request = ObservationRequest.create(
            execution = createExecutionRequest(),
        )

        assertEquals(
            PlanState.CREATED,
            request.execution.plan.state,
        )
        assertEquals(
            "capability-camera",
            request.execution.capability.capabilityId.value,
        )
        assertEquals(
            CapabilityCategory.ACTION,
            request.execution.capability.category,
        )
        assertEquals(
            "Camera",
            request.execution.capability.name,
        )
    }

    private fun createExecutionRequest(): ExecutionRequest {
        return ExecutionRequest.create(
            plan = PlanRecord.create(
                planId = PlanId.from(
                    "plan-observation-request-001",
                ),
                task = TaskRecord.create(
                    taskId = TaskId.from(
                        "task-observation-request-001",
                    ),
                    decision = DecisionRecord.create(
                        understanding =
                            UnderstandingRecord.create(
                                context =
                                    ContextEnvelope.create(
                                        traceId =
                                            TraceId.from(
                                                "trace-observation-request-001",
                                            ),
                                        schemaVersion =
                                            SchemaVersion.from(1),
                                        source =
                                            ContextSource.TEXT,
                                        trustLevel =
                                            ContextTrustLevel.VERIFIED,
                                        securityLevel =
                                            ContextSecurityLevel.RESTRICTED,
                                        observedAt =
                                            DevilTimestamp
                                                .fromEpochMilliseconds(
                                                    1_754_000_110_000L,
                                                ),
                                    ),
                                state =
                                    UnderstandingState.COMPLETE,
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
            ),
            capability = CapabilityContract.create(
                capabilityId = CapabilityId.from(
                    "capability-camera",
                ),
                category = CapabilityCategory.ACTION,
                name = "Camera",
                description =
                    "Performs one bounded registered camera action.",
            ),
        )
    }
}
