package com.devil.core.model.worldmodel

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
import com.devil.core.model.outcome.OutcomeRequest
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

class WorldModelUpdateRequestTest {

    @Test
    fun `create preserves established outcome request`() {
        val outcome = createOutcomeRequest()

        val request = WorldModelUpdateRequest.create(
            outcome = outcome,
        )

        assertEquals(outcome, request.outcome)
    }

    @Test
    fun `create does not reinterpret constitutional dependencies`() {
        val request = WorldModelUpdateRequest.create(
            outcome = createOutcomeRequest(),
        )

        assertEquals(
            PlanState.CREATED,
            request.outcome
                .verification
                .observation
                .execution
                .plan
                .state,
        )
        assertEquals(
            TaskState.CREATED,
            request.outcome
                .verification
                .observation
                .execution
                .plan
                .task
                .state,
        )
        assertEquals(
            "capability-camera",
            request.outcome
                .verification
                .observation
                .execution
                .capability
                .capabilityId
                .value,
        )
        assertEquals(
            CapabilityCategory.ACTION,
            request.outcome
                .verification
                .observation
                .execution
                .capability
                .category,
        )
    }

    private fun createOutcomeRequest(): OutcomeRequest {
        return OutcomeRequest.create(
            verification = VerificationRequest.create(
                observation = ObservationRequest.create(
                    execution = ExecutionRequest.create(
                        plan = PlanRecord.create(
                            planId = PlanId.from(
                                "plan-world-model-update-request-001",
                            ),
                            task = TaskRecord.create(
                                taskId = TaskId.from(
                                    "task-world-model-update-request-001",
                                ),
                                decision = DecisionRecord.create(
                                    understanding =
                                        UnderstandingRecord.create(
                                            context =
                                                ContextEnvelope.create(
                                                    traceId =
                                                        TraceId.from(
                                                            "trace-world-model-update-request-001",
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
                                                                1_754_000_137_000L,
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
            ),
        )
    }
}
