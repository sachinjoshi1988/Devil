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

class MemoryCommitmentEvaluationResultTest {

    @Test
    fun `create preserves committable result with matching request`() {
        val traceId = TraceId.from(
            "trace-memory-commitment-evaluation-result-001",
        )
        val request = createRequest(traceId)

        val result = MemoryCommitmentEvaluationResult.create(
            traceId = traceId,
            status = MemoryCommitmentEvaluationStatus.COMMITTABLE,
            request = request,
        )

        assertEquals(traceId, result.traceId)
        assertEquals(
            MemoryCommitmentEvaluationStatus.COMMITTABLE,
            result.status,
        )
        assertEquals(request, result.request)
        assertNull(result.error)
    }

    @Test
    fun `create preserves unavailable result`() {
        val traceId = TraceId.from(
            "trace-memory-commitment-evaluation-result-002",
        )

        val result = MemoryCommitmentEvaluationResult.create(
            traceId = traceId,
            status = MemoryCommitmentEvaluationStatus.UNAVAILABLE,
        )

        assertEquals(traceId, result.traceId)
        assertEquals(
            MemoryCommitmentEvaluationStatus.UNAVAILABLE,
            result.status,
        )
        assertNull(result.request)
        assertNull(result.error)
    }

    @Test
    fun `create preserves failed result with matching error`() {
        val traceId = TraceId.from(
            "trace-memory-commitment-evaluation-result-003",
        )
        val error = createError(traceId)

        val result = MemoryCommitmentEvaluationResult.create(
            traceId = traceId,
            status = MemoryCommitmentEvaluationStatus.FAILED,
            error = error,
        )

        assertEquals(traceId, result.traceId)
        assertEquals(
            MemoryCommitmentEvaluationStatus.FAILED,
            result.status,
        )
        assertNull(result.request)
        assertEquals(error, result.error)
    }

    @Test
    fun `create rejects committable result without request`() {
        assertFailsWith<IllegalArgumentException> {
            MemoryCommitmentEvaluationResult.create(
                traceId = TraceId.from(
                    "trace-memory-commitment-evaluation-result-004",
                ),
                status =
                    MemoryCommitmentEvaluationStatus.COMMITTABLE,
            )
        }
    }

    @Test
    fun `create rejects committable request from another trace`() {
        assertFailsWith<IllegalArgumentException> {
            MemoryCommitmentEvaluationResult.create(
                traceId = TraceId.from(
                    "trace-memory-commitment-evaluation-result-005",
                ),
                status =
                    MemoryCommitmentEvaluationStatus.COMMITTABLE,
                request = createRequest(
                    TraceId.from(
                        "trace-memory-commitment-evaluation-other",
                    ),
                ),
            )
        }
    }

    @Test
    fun `create rejects unavailable result with request`() {
        val traceId = TraceId.from(
            "trace-memory-commitment-evaluation-result-006",
        )

        assertFailsWith<IllegalArgumentException> {
            MemoryCommitmentEvaluationResult.create(
                traceId = traceId,
                status =
                    MemoryCommitmentEvaluationStatus.UNAVAILABLE,
                request = createRequest(traceId),
            )
        }
    }

    @Test
    fun `create rejects unavailable result with error`() {
        val traceId = TraceId.from(
            "trace-memory-commitment-evaluation-result-007",
        )

        assertFailsWith<IllegalArgumentException> {
            MemoryCommitmentEvaluationResult.create(
                traceId = traceId,
                status =
                    MemoryCommitmentEvaluationStatus.UNAVAILABLE,
                error = createError(traceId),
            )
        }
    }

    @Test
    fun `create rejects failed result without error`() {
        assertFailsWith<IllegalArgumentException> {
            MemoryCommitmentEvaluationResult.create(
                traceId = TraceId.from(
                    "trace-memory-commitment-evaluation-result-008",
                ),
                status = MemoryCommitmentEvaluationStatus.FAILED,
            )
        }
    }

    @Test
    fun `create rejects failed error from another trace`() {
        assertFailsWith<IllegalArgumentException> {
            MemoryCommitmentEvaluationResult.create(
                traceId = TraceId.from(
                    "trace-memory-commitment-evaluation-result-009",
                ),
                status = MemoryCommitmentEvaluationStatus.FAILED,
                error = createError(
                    TraceId.from(
                        "trace-memory-commitment-evaluation-error-other",
                    ),
                ),
            )
        }
    }

    @Test
    fun `create does not reinterpret constitutional dependencies`() {
        val traceId = TraceId.from(
            "trace-memory-commitment-evaluation-result-010",
        )

        val result = MemoryCommitmentEvaluationResult.create(
            traceId = traceId,
            status = MemoryCommitmentEvaluationStatus.COMMITTABLE,
            request = createRequest(traceId),
        )

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

        assertEquals(
            "capability-memory-commitment-evaluation",
            result.request
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
                "capability-memory-commitment-evaluation",
            ),
            category = CapabilityCategory.KNOWLEDGE,
            name = "Memory Commitment Evaluation",
            description =
                "Represents one bounded evaluation test capability without persistence.",
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
                    1_754_000_176_500L,
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
                "task-memory-commitment-evaluation-result",
            ),
            decision = decision,
            state = TaskState.CREATED,
            summary =
                "A bounded constitutional task was created.",
        )

        return PlanRecord.create(
            planId = PlanId.from(
                "plan-memory-commitment-evaluation-result",
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
                    1_754_000_177_000L,
                ),
            summary =
                "Memory commitment evaluation failed.",
        )
    }
}
