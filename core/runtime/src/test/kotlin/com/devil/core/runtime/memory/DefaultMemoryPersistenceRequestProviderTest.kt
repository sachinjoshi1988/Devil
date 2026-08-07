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
import com.devil.core.model.memory.MemoryAuthorityRequest
import com.devil.core.model.memory.MemoryCommitmentRequest
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
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class DefaultMemoryPersistenceRequestProviderTest {

    @Test
    fun `provide returns available request for committable memory commitment result`() {
        val traceId = TraceId.from(
            "trace-memory-persistence-provider-001",
        )
        val commitmentRequest =
            createMemoryCommitmentRequest(traceId)

        val provider: MemoryPersistenceRequestProvider =
            DefaultMemoryPersistenceRequestProvider()

        val result = provider.provide(
            commitment = MemoryCommitmentResult.create(
                traceId = traceId,
                status = MemoryCommitmentStatus.COMMITTABLE,
                request = commitmentRequest,
            ),
        )

        assertEquals(traceId, result.traceId)
        assertEquals(
            MemoryPersistenceRequestStatus.AVAILABLE,
            result.status,
        )
        assertEquals(
            commitmentRequest,
            result.request?.commitmentRequest,
        )
        assertNull(result.error)
    }

    @Test
    fun `provide returns unavailable for deferred memory commitment result`() {
        val traceId = TraceId.from(
            "trace-memory-persistence-provider-002",
        )

        val result =
            DefaultMemoryPersistenceRequestProvider().provide(
                commitment = MemoryCommitmentResult.create(
                    traceId = traceId,
                    status = MemoryCommitmentStatus.DEFERRED,
                ),
            )

        assertEquals(traceId, result.traceId)
        assertEquals(
            MemoryPersistenceRequestStatus.UNAVAILABLE,
            result.status,
        )
        assertNull(result.request)
        assertNull(result.error)
    }

    @Test
    fun `provide preserves failed memory commitment error`() {
        val traceId = TraceId.from(
            "trace-memory-persistence-provider-003",
        )
        val error = createError(traceId)

        val result =
            DefaultMemoryPersistenceRequestProvider().provide(
                commitment = MemoryCommitmentResult.create(
                    traceId = traceId,
                    status = MemoryCommitmentStatus.FAILED,
                    error = error,
                ),
            )

        assertEquals(traceId, result.traceId)
        assertEquals(
            MemoryPersistenceRequestStatus.FAILED,
            result.status,
        )
        assertNull(result.request)
        assertEquals(error, result.error)
    }

    @Test
    fun `provide does not treat committable result as persisted memory`() {
        val traceId = TraceId.from(
            "trace-memory-persistence-provider-004",
        )

        val result =
            DefaultMemoryPersistenceRequestProvider().provide(
                commitment = MemoryCommitmentResult.create(
                    traceId = traceId,
                    status = MemoryCommitmentStatus.COMMITTABLE,
                    request =
                        createMemoryCommitmentRequest(traceId),
                ),
            )

        assertEquals(
            MemoryPersistenceRequestStatus.AVAILABLE,
            result.status,
        )

        assertEquals(
            PlanState.CREATED,
            result.request
                ?.commitmentRequest
                ?.authorityRequest
                ?.proposal
                ?.learning
                ?.worldModelUpdate
                ?.outcome
                ?.verification
                ?.observation
                ?.execution
                ?.plan
                ?.state,
        )

        assertEquals(
            TaskState.CREATED,
            result.request
                ?.commitmentRequest
                ?.authorityRequest
                ?.proposal
                ?.learning
                ?.worldModelUpdate
                ?.outcome
                ?.verification
                ?.observation
                ?.execution
                ?.plan
                ?.task
                ?.state,
        )

        assertEquals(
            "capability-memory-persistence-provider",
            result.request
                ?.commitmentRequest
                ?.authorityRequest
                ?.proposal
                ?.learning
                ?.worldModelUpdate
                ?.outcome
                ?.verification
                ?.observation
                ?.execution
                ?.capability
                ?.capabilityId
                ?.value,
        )

        assertNull(result.error)
    }

    private fun createMemoryCommitmentRequest(
        traceId: TraceId,
    ): MemoryCommitmentRequest {
        return MemoryCommitmentRequest.create(
            authorityRequest = MemoryAuthorityRequest.create(
                proposal = MemoryProposalRequest.create(
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
                ),
            ),
        )
    }

    private fun createCapability(): CapabilityContract {
        return CapabilityContract.create(
            capabilityId = CapabilityId.from(
                "capability-memory-persistence-provider",
            ),
            category = CapabilityCategory.KNOWLEDGE,
            name = "Memory Persistence Provider",
            description =
                "Represents one bounded persistence-provider test capability without storage.",
        )
    }

    private fun createPlan(
        traceId: TraceId,
    ): PlanRecord {
        val context = ContextEnvelope.create(
            traceId = traceId,
            schemaVersion = SchemaVersion.from(1),
            source = ContextSource.TEST,
            trustLevel = ContextTrustLevel.VERIFIED,
            securityLevel =
                ContextSecurityLevel.RESTRICTED,
            observedAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_000_183_000L,
                ),
        )

        val understanding = UnderstandingRecord.create(
            context = context,
            state = UnderstandingState.COMPLETE,
            summary =
                "Bounded understanding was produced.",
        )

        val decision = DecisionRecord.create(
            understanding = understanding,
            state = DecisionState.SELECTED,
            summary =
                "A constitutional decision was selected.",
        )

        val task = TaskRecord.create(
            taskId = TaskId.from(
                "task-memory-persistence-provider",
            ),
            decision = decision,
            state = TaskState.CREATED,
            summary =
                "A bounded constitutional task was created.",
        )

        return PlanRecord.create(
            planId = PlanId.from(
                "plan-memory-persistence-provider",
            ),
            task = task,
            state = PlanState.CREATED,
            summary =
                "Use only the constitutionally governed Memory Authority persistence path.",
        )
    }

    private fun createError(
        traceId: TraceId,
    ): UniversalErrorRecord {
        return UniversalErrorRecord.create(
            errorCode = ErrorCode.from(
                "MEMORY_COMMITMENT_FAILED",
            ),
            traceId = traceId,
            occurredAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_000_183_500L,
                ),
            summary =
                "Constitutional memory commitment evaluation failed.",
        )
    }
}
