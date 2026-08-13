package com.devil.core.runtime.memory

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
import com.devil.core.model.error.ErrorCode
import com.devil.core.model.error.UniversalErrorRecord
import com.devil.core.model.execution.ExecutionRequest
import com.devil.core.model.learning.LearningRequest
import com.devil.core.model.memory.MemoryProposalRequest
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
import com.devil.core.runtime.learning.LearningResult
import com.devil.core.runtime.learning.LearningStatus
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class DefaultMemoryAuthorityEvidencePortTest {

    @Test
    fun `proposable Memory Proposal remains deferred without authorized Memory Authority evidence mechanism`() {
        val traceId =
            TraceId.from(
                "trace-default-memory-authority-evidence-port-001",
            )

        val result =
            DefaultMemoryAuthorityEvidencePort().establish(
                memoryProposal =
                    createMemoryProposal(
                        traceId,
                    ),
            )

        assertEquals(traceId, result.traceId)
        assertEquals(
            MemoryAuthorityEvidenceStatus.DEFERRED,
            result.status,
        )
        assertNull(result.capabilityId)
        assertNull(result.description)
        assertNull(result.error)
    }

    @Test
    fun `deferred Memory Proposal remains deferred`() {
        val traceId =
            TraceId.from(
                "trace-default-memory-authority-evidence-port-002",
            )

        val result =
            DefaultMemoryAuthorityEvidencePort().establish(
                memoryProposal =
                    MemoryProposalResult.create(
                        traceId = traceId,
                        status =
                            MemoryProposalStatus.DEFERRED,
                    ),
            )

        assertEquals(
            MemoryAuthorityEvidenceStatus.DEFERRED,
            result.status,
        )
        assertNull(result.capabilityId)
        assertNull(result.description)
        assertNull(result.error)
    }

    @Test
    fun `failed Memory Proposal preserves matching operational error`() {
        val traceId =
            TraceId.from(
                "trace-default-memory-authority-evidence-port-003",
            )
        val error = createError(traceId)

        val result =
            DefaultMemoryAuthorityEvidencePort().establish(
                memoryProposal =
                    MemoryProposalResult.create(
                        traceId = traceId,
                        status =
                            MemoryProposalStatus.FAILED,
                        error = error,
                    ),
            )

        assertEquals(
            MemoryAuthorityEvidenceStatus.FAILED,
            result.status,
        )
        assertNull(result.capabilityId)
        assertNull(result.description)
        assertEquals(error, result.error)
    }

    @Test
    fun `proposable Memory Proposal does not manufacture Memory Authority approval`() {
        val traceId =
            TraceId.from(
                "trace-default-memory-authority-evidence-port-004",
            )
        val memoryProposal =
            createMemoryProposal(
                traceId,
            )

        val result =
            DefaultMemoryAuthorityEvidencePort().establish(
                memoryProposal =
                    memoryProposal,
            )

        assertEquals(
            MemoryProposalStatus.PROPOSABLE,
            memoryProposal.status,
        )
        assertEquals(
            MemoryAuthorityEvidenceStatus.DEFERRED,
            result.status,
        )
        assertNull(result.capabilityId)
        assertNull(result.description)
        assertNull(result.error)
    }

    private fun createMemoryProposal(
        traceId: TraceId,
    ): MemoryProposalResult {
        return MemoryProposalResult.create(
            traceId = traceId,
            status =
                MemoryProposalStatus.PROPOSABLE,
            request =
                MemoryProposalRequest.create(
                    learning =
                        requireNotNull(
                            createLearning(
                                traceId,
                            ).request,
                        ),
                ),
        )
    }

    private fun createLearning(
        traceId: TraceId,
    ): LearningResult {
        return LearningResult.create(
            traceId = traceId,
            status =
                LearningStatus.LEARNABLE,
            request =
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
                                                                createPlan(
                                                                    traceId,
                                                                ),
                                                            capability =
                                                                createCapability(),
                                                        ),
                                                ),
                                        ),
                                ),
                        ),
                ),
        )
    }

    private fun createCapability(): CapabilityContract {
        return CapabilityContract.create(
            capabilityId =
                CapabilityId.from(
                    "capability-camera",
                ),
            category =
                CapabilityCategory.ACTION,
            name = "Camera",
            description =
                "Performs one bounded registered camera action.",
        )
    }

    private fun createPlan(
        traceId: TraceId,
    ): PlanRecord {
        val context =
            ContextEnvelope.create(
                traceId = traceId,
                schemaVersion =
                    SchemaVersion.from(1),
                source =
                    ContextSource.TEST,
                trustLevel =
                    ContextTrustLevel.VERIFIED,
                securityLevel =
                    ContextSecurityLevel.RESTRICTED,
                observedAt =
                    DevilTimestamp.fromEpochMilliseconds(
                        1_754_000_272_000L,
                    ),
            )

        val understanding =
            UnderstandingRecord.create(
                context = context,
                state =
                    UnderstandingState.COMPLETE,
                summary =
                    "Bounded understanding was produced.",
            )

        val decision =
            DecisionRecord.create(
                understanding =
                    understanding,
                state =
                    DecisionState.SELECTED,
                summary =
                    "A constitutional decision was selected.",
            )

        val task =
            TaskRecord.create(
                taskId =
                    TaskId.from(
                        "task-default-memory-authority-evidence-port",
                    ),
                decision = decision,
                state =
                    TaskState.CREATED,
                summary =
                    "A bounded constitutional task was created.",
            )

        return PlanRecord.create(
            planId =
                PlanId.from(
                    "plan-default-memory-authority-evidence-port",
                ),
            task = task,
            state =
                PlanState.CREATED,
            summary =
                "Use the constitutionally approved capability path.",
        )
    }

    private fun createError(
        traceId: TraceId,
    ): UniversalErrorRecord {
        return UniversalErrorRecord.create(
            errorCode =
                ErrorCode.from(
                    "MEMORY_PROPOSAL_FAILED",
                ),
            traceId = traceId,
            occurredAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_000_272_500L,
                ),
            summary =
                "Bounded constitutional Memory Proposal failed.",
        )
    }
}
