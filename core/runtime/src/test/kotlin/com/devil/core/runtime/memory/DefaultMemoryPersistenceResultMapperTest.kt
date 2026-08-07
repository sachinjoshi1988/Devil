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
import com.devil.core.model.memory.MemoryPersistenceRequest
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
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class DefaultMemoryPersistenceResultMapperTest {

    @Test
    fun `map converts persistable evaluation to persistable result`() {
        val traceId = TraceId.from(
            "trace-memory-persistence-mapper-001",
        )
        val request = createRequest(traceId)

        val result = DefaultMemoryPersistenceResultMapper().map(
            traceId = traceId,
            evaluation = MemoryPersistenceEvaluationResult.create(
                traceId = traceId,
                status =
                    MemoryPersistenceEvaluationStatus.PERSISTABLE,
                request = request,
            ),
        )

        assertEquals(traceId, result.traceId)
        assertEquals(
            MemoryPersistenceStatus.PERSISTABLE,
            result.status,
        )
        assertEquals(request, result.request)
        assertNull(result.error)
    }

    @Test
    fun `map converts unavailable evaluation to deferred result`() {
        val traceId = TraceId.from(
            "trace-memory-persistence-mapper-002",
        )

        val result = DefaultMemoryPersistenceResultMapper().map(
            traceId = traceId,
            evaluation = MemoryPersistenceEvaluationResult.create(
                traceId = traceId,
                status =
                    MemoryPersistenceEvaluationStatus.UNAVAILABLE,
            ),
        )

        assertEquals(traceId, result.traceId)
        assertEquals(
            MemoryPersistenceStatus.DEFERRED,
            result.status,
        )
        assertNull(result.request)
        assertNull(result.error)
    }

    @Test
    fun `map preserves failed evaluation error`() {
        val traceId = TraceId.from(
            "trace-memory-persistence-mapper-003",
        )
        val error = createError(traceId)

        val result = DefaultMemoryPersistenceResultMapper().map(
            traceId = traceId,
            evaluation = MemoryPersistenceEvaluationResult.create(
                traceId = traceId,
                status =
                    MemoryPersistenceEvaluationStatus.FAILED,
                error = error,
            ),
        )

        assertEquals(traceId, result.traceId)
        assertEquals(
            MemoryPersistenceStatus.FAILED,
            result.status,
        )
        assertNull(result.request)
        assertEquals(error, result.error)
    }

    @Test
    fun `map does not persist memory or mutate task or plan state`() {
        val traceId = TraceId.from(
            "trace-memory-persistence-mapper-004",
        )
        val request = createRequest(traceId)

        val result = DefaultMemoryPersistenceResultMapper().map(
            traceId = traceId,
            evaluation = MemoryPersistenceEvaluationResult.create(
                traceId = traceId,
                status =
                    MemoryPersistenceEvaluationStatus.PERSISTABLE,
                request = request,
            ),
        )

        assertEquals(
            MemoryPersistenceStatus.PERSISTABLE,
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

        assertNull(result.error)
    }

    @Test
    fun `map rejects evaluation result from another trace`() {
        assertFailsWith<IllegalArgumentException> {
            DefaultMemoryPersistenceResultMapper().map(
                traceId = TraceId.from(
                    "trace-memory-persistence-mapper-005",
                ),
                evaluation = MemoryPersistenceEvaluationResult.create(
                    traceId = TraceId.from(
                        "trace-memory-persistence-mapper-other",
                    ),
                    status =
                        MemoryPersistenceEvaluationStatus.UNAVAILABLE,
                ),
            )
        }
    }

    private fun createRequest(
        traceId: TraceId,
    ): MemoryPersistenceRequest {
        return MemoryPersistenceRequest.create(
            commitmentRequest = MemoryCommitmentRequest.create(
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
            ),
        )
    }

    private fun createCapability(): CapabilityContract {
        return CapabilityContract.create(
            capabilityId = CapabilityId.from(
                "capability-memory-persistence-mapper",
            ),
            category = CapabilityCategory.KNOWLEDGE,
            name = "Memory Persistence Mapper",
            description =
                "Represents one bounded persistence-mapper test capability without storage.",
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
                    1_754_000_186_000L,
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
                "task-memory-persistence-mapper",
            ),
            decision = decision,
            state = TaskState.CREATED,
            summary =
                "A bounded constitutional task was created.",
        )

        return PlanRecord.create(
            planId = PlanId.from(
                "plan-memory-persistence-mapper",
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
                "MEMORY_PERSISTENCE_EVALUATION_FAILED",
            ),
            traceId = traceId,
            occurredAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_000_186_500L,
                ),
            summary =
                "Memory persistence evaluation failed.",
        )
    }
}
