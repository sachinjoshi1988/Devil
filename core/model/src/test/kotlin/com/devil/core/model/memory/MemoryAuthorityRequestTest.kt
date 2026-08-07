package com.devil.core.model.memory

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
import com.devil.core.model.learning.LearningRequest
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
import com.devil.core.model.worldmodel.WorldModelUpdateRequest
import kotlin.test.Test
import kotlin.test.assertEquals

class MemoryAuthorityRequestTest {

    @Test
    fun `create preserves one bounded memory proposal request`() {
        val proposal = createMemoryProposalRequest()

        val request = MemoryAuthorityRequest.create(
            proposal = proposal,
        )

        assertEquals(proposal, request.proposal)
    }

    @Test
    fun `create does not reinterpret constitutional dependencies`() {
        val request = MemoryAuthorityRequest.create(
            proposal = createMemoryProposalRequest(),
        )

        assertEquals(
            PlanState.CREATED,
            request.proposal
                .learning
                .worldModelUpdate
                .outcome
                .verification
                .observation
                .execution
                .plan
                .state,
        )
        assertEquals(
            TaskState.CREATED,
            request.proposal
                .learning
                .worldModelUpdate
                .outcome
                .verification
                .observation
                .execution
                .plan
                .task
                .state,
        )
        assertEquals(
            "capability-camera",
            request.proposal
                .learning
                .worldModelUpdate
                .outcome
                .verification
                .observation
                .execution
                .capability
                .capabilityId
                .value,
        )
        assertEquals(
            CapabilityCategory.ACTION,
            request.proposal
                .learning
                .worldModelUpdate
                .outcome
                .verification
                .observation
                .execution
                .capability
                .category,
        )
    }

    private fun createMemoryProposalRequest(): MemoryProposalRequest {
        return MemoryProposalRequest.create(
            learning = LearningRequest.create(
                worldModelUpdate =
                    WorldModelUpdateRequest.create(
                        outcome = OutcomeRequest.create(
                            verification =
                                VerificationRequest.create(
                                    observation =
                                        ObservationRequest.create(
                                            execution =
                                                ExecutionRequest.create(
                                                    plan = createPlan(),
                                                    capability =
                                                        CapabilityContract.create(
                                                            capabilityId =
                                                                CapabilityId.from(
                                                                    "capability-camera",
                                                                ),
                                                            category =
                                                                CapabilityCategory.ACTION,
                                                            name = "Camera",
                                                            description =
                                                                "Performs one bounded registered camera action.",
                                                        ),
                                                ),
                                        ),
                                ),
                        ),
                    ),
            ),
        )
    }

    private fun createPlan(): PlanRecord {
        val context = ContextEnvelope.create(
            traceId = TraceId.from(
                "trace-memory-authority-request-001",
            ),
            schemaVersion = SchemaVersion.from(1),
            source = ContextSource.TEXT,
            trustLevel = ContextTrustLevel.VERIFIED,
            securityLevel = ContextSecurityLevel.RESTRICTED,
            observedAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_000_164_000L,
                ),
        )

        val understanding = UnderstandingRecord.create(
            context = context,
            state = UnderstandingState.COMPLETE,
            summary =
                "Open the camera application.",
        )

        val decision = DecisionRecord.create(
            understanding = understanding,
            state = DecisionState.SELECTED,
            summary =
                "Open the camera application.",
        )

        val task = TaskRecord.create(
            taskId = TaskId.from(
                "task-memory-authority-request-001",
            ),
            decision = decision,
            state = TaskState.CREATED,
            summary =
                "Open the camera application.",
        )

        return PlanRecord.create(
            planId = PlanId.from(
                "plan-memory-authority-request-001",
            ),
            task = task,
            state = PlanState.CREATED,
            summary =
                "Use the constitutionally approved capability path.",
        )
    }
}
