package com.devil.core.model.memory

import com.devil.core.model.capability.CapabilityCategory
import com.devil.core.model.capability.CapabilityContract
import com.devil.core.model.capability.CapabilityId
import com.devil.core.model.context.ContextEnvelope
import com.devil.core.model.context.ContextSecurityLevel
import com.devil.core.model.context.ContextSource
import com.devil.core.model.context.ContextTrustLevel
import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.common.SchemaVersion
import com.devil.core.model.common.TraceId
import com.devil.core.model.decision.DecisionRecord
import com.devil.core.model.decision.DecisionState
import com.devil.core.model.execution.ExecutionRequest
import com.devil.core.model.identity.IdentityId
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
import kotlin.test.assertFailsWith

class MemoryRepresentationPreparationRequestStage102Test {

    @Test
    fun `preparation request preserves explicitly supplied memory metadata`() {
        val authorityRequest = createAuthorityRequest()

        val request =
            MemoryRepresentationPreparationRequest.create(
                authorityRequest = authorityRequest,
                memoryId = MemoryId.from(" memory-102 "),
                subjectIdentityId = IdentityId.from(" subject-102 "),
                memoryClass = MemoryClass.PERSONAL,
                sensitivity = MemorySensitivity.PRIVATE,
                confidence = MemoryConfidence.from(82),
                retention = MemoryRetention.LONG_TERM,
                source =
                    MemorySource.create(
                        sourceId = " source-102 ",
                        sourceType = " conversation ",
                    ),
                ownerVisibleReason =
                    OwnerVisibleMemoryReason.from(
                        " explicitly supplied reason ",
                    ),
                content = " explicit memory content ",
            )

        assertEquals(authorityRequest, request.authorityRequest)
        assertEquals("memory-102", request.memoryId.value)
        assertEquals("subject-102", request.subjectIdentityId.value)
        assertEquals(MemoryClass.PERSONAL, request.memoryClass)
        assertEquals(MemorySensitivity.PRIVATE, request.sensitivity)
        assertEquals(82, request.confidence.value)
        assertEquals(MemoryRetention.LONG_TERM, request.retention)
        assertEquals("source-102", request.source.sourceId)
        assertEquals("conversation", request.source.sourceType)
        assertEquals(
            "explicitly supplied reason",
            request.ownerVisibleReason.value,
        )
        assertEquals("explicit memory content", request.content)
    }

    @Test
    fun `preparation request rejects blank content`() {
        assertFailsWith<IllegalArgumentException> {
            MemoryRepresentationPreparationRequest.create(
                authorityRequest = createAuthorityRequest(),
                memoryId = MemoryId.from("memory-102"),
                subjectIdentityId = IdentityId.from("subject-102"),
                memoryClass = MemoryClass.PERSONAL,
                sensitivity = MemorySensitivity.PRIVATE,
                confidence = MemoryConfidence.from(82),
                retention = MemoryRetention.LONG_TERM,
                source =
                    MemorySource.create(
                        sourceId = "source-102",
                        sourceType = "conversation",
                    ),
                ownerVisibleReason =
                    OwnerVisibleMemoryReason.from("reason"),
                content = "   ",
            )
        }
    }

    private fun createAuthorityRequest(): MemoryAuthorityRequest {
        return MemoryAuthorityRequest.create(
            proposal =
                MemoryProposalRequest.create(
                    learning =
                        LearningRequest.create(
                            worldModelUpdate =
                                WorldModelUpdateRequest.create(
                                    outcome =
                                        OutcomeRequest.create(
                                            verification =
                                                VerificationRequest.create(
                                                    observation =
                                                        ObservationRequest.create(
                                                            execution =
                                                                ExecutionRequest.create(
                                                                    plan =
                                                                        createPlan(),
                                                                    capability =
                                                                        createCapability(),
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
        val context = createContext()

        val understanding =
            UnderstandingRecord.create(
                context = context,
                state = UnderstandingState.COMPLETE,
                summary = "Bounded understanding.",
            )

        val decision =
            DecisionRecord.create(
                understanding = understanding,
                state = DecisionState.SELECTED,
                summary = "Bounded decision.",
            )

        val task =
            TaskRecord.create(
                taskId = TaskId.from("task-stage-102-request"),
                decision = decision,
                state = TaskState.CREATED,
                summary = "Bounded task.",
            )

        return PlanRecord.create(
            planId = PlanId.from("plan-stage-102-request"),
            task = task,
            state = PlanState.CREATED,
            summary = "Bounded plan.",
        )
    }

    private fun createCapability(): CapabilityContract {
        return CapabilityContract.create(
            capabilityId = CapabilityId.from("capability-stage-102-request"),
            category = CapabilityCategory.ACTION,
            name = "Stage 102 test capability",
            description = "Bounded Stage 102 test capability.",
        )
    }

    private fun createContext(): ContextEnvelope {
        return ContextEnvelope.create(
            traceId = TraceId.from("trace-stage-102-request"),
            schemaVersion = SchemaVersion.from(1),
            source = ContextSource.TEST,
            trustLevel = ContextTrustLevel.VERIFIED,
            securityLevel = ContextSecurityLevel.RESTRICTED,
            observedAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_755_000_102_000L,
                ),
        )
    }
}
