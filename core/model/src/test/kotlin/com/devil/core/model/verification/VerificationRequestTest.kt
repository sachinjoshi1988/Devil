package com.devil.core.model.verification

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
import com.devil.core.model.observation.ObservationRequest
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

class VerificationRequestTest {

    @Test
    fun `create preserves observed request`() {
        val observation = createObservationRequest()

        val request = VerificationRequest.create(
            observation = observation,
        )

        assertEquals(observation, request.observation)
    }

    @Test
    fun `create does not reinterpret observation dependencies`() {
        val request = VerificationRequest.create(
            observation = createObservationRequest(),
        )

        assertEquals(
            PlanState.CREATED,
            request.observation.execution.plan.state,
        )
        assertEquals(
            "capability-camera",
            request.observation
                .execution
                .capability
                .capabilityId
                .value,
        )
        assertEquals(
            CapabilityCategory.ACTION,
            request.observation
                .execution
                .capability
                .category,
        )
        assertEquals(
            "Camera",
            request.observation
                .execution
                .capability
                .name,
        )
    }

    private fun createObservationRequest(): ObservationRequest {
        return ObservationRequest.create(
            execution = ExecutionRequest.create(
                plan = PlanRecord.create(
                    planId = PlanId.from(
                        "plan-verification-request-001",
                    ),
                    task = TaskRecord.create(
                        taskId = TaskId.from(
                            "task-verification-request-001",
                        ),
                        decision = DecisionRecord.create(
                            understanding =
                                UnderstandingRecord.create(
                                    context =
                                        ContextEnvelope.create(
                                            traceId =
                                                TraceId.from(
                                                    "trace-verification-request-001",
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
                                                        1_754_000_119_000L,
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
            ),
        )
    }
}
