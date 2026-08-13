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

class DefaultMemoryProposalEvidencePortTest {

    @Test
    fun `learnable learning remains deferred without genuine proposal evidence`() {
        val traceId =
            TraceId.from(
                "trace-default-memory-proposal-evidence-port-001",
            )
        val port: MemoryProposalEvidencePort =
            DefaultMemoryProposalEvidencePort()

        val result =
            port.establish(
                learning =
                    createLearnableLearning(
                        traceId,
                    ),
            )

        assertEquals(traceId, result.traceId)
        assertEquals(
            MemoryProposalEvidenceStatus.DEFERRED,
            result.status,
        )
        assertNull(result.capabilityId)
        assertNull(result.description)
        assertNull(result.error)
    }

    @Test
    fun `deferred learning remains deferred`() {
        val traceId =
            TraceId.from(
                "trace-default-memory-proposal-evidence-port-002",
            )

        val result =
            DefaultMemoryProposalEvidencePort()
                .establish(
                    learning =
                        LearningResult.create(
                            traceId = traceId,
                            status =
                                LearningStatus.DEFERRED,
                        ),
                )

        assertEquals(traceId, result.traceId)
        assertEquals(
            MemoryProposalEvidenceStatus.DEFERRED,
            result.status,
        )
        assertNull(result.capabilityId)
        assertNull(result.description)
        assertNull(result.error)
    }

    @Test
    fun `failed learning preserves matching operational error`() {
        val traceId =
            TraceId.from(
                "trace-default-memory-proposal-evidence-port-003",
            )
        val error = createError(traceId)

        val result =
            DefaultMemoryProposalEvidencePort()
                .establish(
                    learning =
                        LearningResult.create(
                            traceId = traceId,
                            status =
                                LearningStatus.FAILED,
                            error = error,
                        ),
                )

        assertEquals(traceId, result.traceId)
        assertEquals(
            MemoryProposalEvidenceStatus.FAILED,
            result.status,
        )
        assertNull(result.capabilityId)
        assertNull(result.description)
        assertEquals(error, result.error)
    }

    @Test
    fun `default port never manufactures established proposal evidence`() {
        val traceId =
            TraceId.from(
                "trace-default-memory-proposal-evidence-port-004",
            )

        val result =
            DefaultMemoryProposalEvidencePort()
                .establish(
                    learning =
                        createLearnableLearning(
                            traceId,
                        ),
                )

        assertEquals(
            MemoryProposalEvidenceStatus.DEFERRED,
            result.status,
        )
    }

    @Test
    fun `learnable input preserves capability only as prerequisite not evidence`() {
        val traceId =
            TraceId.from(
                "trace-default-memory-proposal-evidence-port-005",
            )
        val learning =
            createLearnableLearning(
                traceId,
            )

        assertEquals(
            "capability-camera",
            learning.request
                ?.worldModelUpdate
                ?.outcome
                ?.verification
                ?.observation
                ?.execution
                ?.capability
                ?.capabilityId
                ?.value,
        )

        val result =
            DefaultMemoryProposalEvidencePort()
                .establish(
                    learning = learning,
                )

        assertEquals(
            MemoryProposalEvidenceStatus.DEFERRED,
            result.status,
        )
        assertNull(result.capabilityId)
        assertNull(result.description)
    }

    private fun createLearnableLearning(
        traceId: TraceId,
    ): LearningResult {
        return LearningResult.create(
            traceId = traceId,
            status = LearningStatus.LEARNABLE,
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
            category = CapabilityCategory.ACTION,
            name = "Camera",
            description =
                "Performs one bounded registered camera action.",
        )
    }

    private fun createPlan(
        traceId: TraceId,
    ): PlanRecord {
        return PlanRecord.create(
            planId =
                PlanId.from(
                    "plan-default-memory-proposal-evidence-port",
                ),
            task =
                TaskRecord.create(
                    taskId =
                        TaskId.from(
                            "task-default-memory-proposal-evidence-port",
                        ),
                    decision =
                        DecisionRecord.create(
                            understanding =
                                UnderstandingRecord.create(
                                    context =
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
                                                DevilTimestamp
                                                    .fromEpochMilliseconds(
                                                        1_754_000_180_000L,
                                                    ),
                                        ),
                                    state =
                                        UnderstandingState.COMPLETE,
                                    summary =
                                        "Bounded understanding was produced.",
                                ),
                            state = DecisionState.SELECTED,
                            summary =
                                "A constitutional decision was selected.",
                        ),
                    state = TaskState.CREATED,
                    summary =
                        "A bounded constitutional task was created.",
                ),
            state = PlanState.CREATED,
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
                    "MEMORY_PROPOSAL_EVIDENCE_DEPENDENCY_FAILED",
                ),
            traceId = traceId,
            occurredAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_000_180_500L,
                ),
            summary =
                "Bounded constitutional Memory Proposal evidence dependency failed.",
        )
    }
}
