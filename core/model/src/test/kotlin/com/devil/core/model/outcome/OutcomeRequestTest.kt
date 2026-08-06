package com.devil.core.model.outcome

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
import com.devil.core.model.verification.VerificationRequest
import kotlin.test.Test
import kotlin.test.assertEquals

class OutcomeRequestTest {

    @Test
    fun `create preserves verified request`() {
        val verification = createVerificationRequest()

        val request = OutcomeRequest.create(
            verification = verification,
        )

        assertEquals(verification, request.verification)
    }

    @Test
    fun `create does not reinterpret verification dependencies`() {
        val request = OutcomeRequest.create(
            verification = createVerificationRequest(),
        )

        assertEquals(
            PlanState.CREATED,
            request.verification
                .observation
                .execution
                .plan
                .state,
        )
        assertEquals(
            "capability-camera",
            request.verification
                .observation
                .execution
                .capability
                .capabilityId
                .value,
        )
        assertEquals(
            CapabilityCategory.ACTION,
            request.verification
                .observation
                .execution
                .capability
                .category,
        )
        assertEquals(
            "Camera",
            request.verification
                .observation
                .execution
                .capability
                .name,
        )
    }

    private fun createVerificationRequest(): VerificationRequest {
        return VerificationRequest.create(
            observation = ObservationRequest.create(
                execution = ExecutionRequest.create(
                    plan = PlanRecord.create(
                        planId = PlanId.from(
                            "plan-outcome-request-001",
                        ),
                        task = TaskRecord.create(
                            taskId = TaskId.from(
                                "task-outcome-request-001",
                            ),
                            decision = DecisionRecord.create(
                                understanding =
                                    UnderstandingRecord.create(
                                        context =
                                            ContextEnvelope.create(
                                                traceId =
                                                    TraceId.from(
                                                        "trace-outcome-request-001",
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
                                                            1_754_000_128_000L,
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
            ),
        )
    }
}
