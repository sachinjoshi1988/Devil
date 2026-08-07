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
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class DefaultMemoryCommitmentResultMapperTest {

    @Test
    fun `map converts committable evaluation to committable result`() {
        val traceId = TraceId.from(
            "trace-memory-commitment-mapper-001",
        )
        val request = createRequest(traceId)

        val result = DefaultMemoryCommitmentResultMapper().map(
            traceId = traceId,
            evaluation = MemoryCommitmentEvaluationResult.create(
                traceId = traceId,
                status = MemoryCommitmentEvaluationStatus.COMMITTABLE,
                request = request,
            ),
        )

        assertEquals(traceId, result.traceId)
        assertEquals(MemoryCommitmentStatus.COMMITTABLE, result.status)
        assertEquals(request, result.request)
        assertNull(result.error)
    }

    @Test
    fun `map converts unavailable evaluation to deferred result`() {
        val traceId = TraceId.from(
            "trace-memory-commitment-mapper-002",
        )

        val result = DefaultMemoryCommitmentResultMapper().map(
            traceId = traceId,
            evaluation = MemoryCommitmentEvaluationResult.create(
                traceId = traceId,
                status = MemoryCommitmentEvaluationStatus.UNAVAILABLE,
            ),
        )

        assertEquals(traceId, result.traceId)
        assertEquals(MemoryCommitmentStatus.DEFERRED, result.status)
        assertNull(result.request)
        assertNull(result.error)
    }

    @Test
    fun `map preserves failed evaluation error`() {
        val traceId = TraceId.from(
            "trace-memory-commitment-mapper-003",
        )
        val error = createError(traceId)

        val result = DefaultMemoryCommitmentResultMapper().map(
            traceId = traceId,
            evaluation = MemoryCommitmentEvaluationResult.create(
                traceId = traceId,
                status = MemoryCommitmentEvaluationStatus.FAILED,
                error = error,
            ),
        )

        assertEquals(traceId, result.traceId)
        assertEquals(MemoryCommitmentStatus.FAILED, result.status)
        assertNull(result.request)
        assertEquals(error, result.error)
    }

    @Test
    fun `map does not commit memory or mutate task or plan state`() {
        val traceId = TraceId.from(
            "trace-memory-commitment-mapper-004",
        )
        val request = createRequest(traceId)

        val result = DefaultMemoryCommitmentResultMapper().map(
            traceId = traceId,
            evaluation = MemoryCommitmentEvaluationResult.create(
                traceId = traceId,
                status = MemoryCommitmentEvaluationStatus.COMMITTABLE,
                request = request,
            ),
        )

        assertEquals(MemoryCommitmentStatus.COMMITTABLE, result.status)

        assertEquals(
            PlanState.CREATED,
            result.request
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
            DefaultMemoryCommitmentResultMapper().map(
                traceId = TraceId.from(
                    "trace-memory-commitment-mapper-005",
                ),
                evaluation = MemoryCommitmentEvaluationResult.create(
                    traceId = TraceId.from(
                        "trace-memory-commitment-mapper-other",
                    ),
                    status =
                        MemoryCommitmentEvaluationStatus.UNAVAILABLE,
                ),
            )
        }
    }

    private fun createRequest(
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
                                                                createPlan(traceId),
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
                "capability-memory-commitment-mapper",
            ),
            category = CapabilityCategory.KNOWLEDGE,
            name = "Memory Commitment Mapper",
            description =
                "Represents one bounded mapper test capability without persistence.",
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
            securityLevel = ContextSecurityLevel.RESTRICTED,
            observedAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_000_178_500L,
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
                "task-memory-commitment-mapper",
            ),
            decision = decision,
            state = TaskState.CREATED,
            summary =
                "A bounded constitutional task was created.",
        )

        return PlanRecord.create(
            planId = PlanId.from(
                "plan-memory-commitment-mapper",
            ),
            task = task,
            state = PlanState.CREATED,
            summary =
                "Use only the constitutionally governed Memory Authority path.",
        )
    }

    private fun createError(
        traceId: TraceId,
    ): UniversalErrorRecord {
        return UniversalErrorRecord.create(
            errorCode = ErrorCode.from(
                "MEMORY_COMMITMENT_EVALUATION_FAILED",
            ),
            traceId = traceId,
            occurredAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_000_179_000L,
                ),
            summary =
                "Memory commitment evaluation failed.",
        )
    }
}
