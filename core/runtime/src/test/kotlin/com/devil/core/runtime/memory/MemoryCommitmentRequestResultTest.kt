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

class MemoryCommitmentRequestResultTest {

    @Test
    fun `create preserves available result with matching request`() {
        val traceId = TraceId.from(
            "trace-memory-commitment-request-result-001",
        )
        val request = createRequest(traceId)

        val result = MemoryCommitmentRequestResult.create(
            traceId = traceId,
            status = MemoryCommitmentRequestStatus.AVAILABLE,
            request = request,
        )

        assertEquals(traceId, result.traceId)
        assertEquals(
            MemoryCommitmentRequestStatus.AVAILABLE,
            result.status,
        )
        assertEquals(request, result.request)
        assertNull(result.error)
    }

    @Test
    fun `create preserves unavailable result`() {
        val traceId = TraceId.from(
            "trace-memory-commitment-request-result-002",
        )

        val result = MemoryCommitmentRequestResult.create(
            traceId = traceId,
            status = MemoryCommitmentRequestStatus.UNAVAILABLE,
        )

        assertEquals(traceId, result.traceId)
        assertEquals(
            MemoryCommitmentRequestStatus.UNAVAILABLE,
            result.status,
        )
        assertNull(result.request)
        assertNull(result.error)
    }

    @Test
    fun `create preserves failed result with matching error`() {
        val traceId = TraceId.from(
            "trace-memory-commitment-request-result-003",
        )
        val error = createError(traceId)

        val result = MemoryCommitmentRequestResult.create(
            traceId = traceId,
            status = MemoryCommitmentRequestStatus.FAILED,
            error = error,
        )

        assertEquals(traceId, result.traceId)
        assertEquals(
            MemoryCommitmentRequestStatus.FAILED,
            result.status,
        )
        assertNull(result.request)
        assertEquals(error, result.error)
    }

    @Test
    fun `create rejects available result without request`() {
        assertFailsWith<IllegalArgumentException> {
            MemoryCommitmentRequestResult.create(
                traceId = TraceId.from(
                    "trace-memory-commitment-request-result-004",
                ),
                status = MemoryCommitmentRequestStatus.AVAILABLE,
            )
        }
    }

    @Test
    fun `create rejects request from another trace`() {
        assertFailsWith<IllegalArgumentException> {
            MemoryCommitmentRequestResult.create(
                traceId = TraceId.from(
                    "trace-memory-commitment-request-result-005",
                ),
                status = MemoryCommitmentRequestStatus.AVAILABLE,
                request = createRequest(
                    TraceId.from(
                        "trace-memory-commitment-request-other",
                    ),
                ),
            )
        }
    }

    @Test
    fun `create rejects unavailable result with request`() {
        val traceId = TraceId.from(
            "trace-memory-commitment-request-result-006",
        )

        assertFailsWith<IllegalArgumentException> {
            MemoryCommitmentRequestResult.create(
                traceId = traceId,
                status = MemoryCommitmentRequestStatus.UNAVAILABLE,
                request = createRequest(traceId),
            )
        }
    }

    @Test
    fun `create rejects unavailable result with error`() {
        val traceId = TraceId.from(
            "trace-memory-commitment-request-result-007",
        )

        assertFailsWith<IllegalArgumentException> {
            MemoryCommitmentRequestResult.create(
                traceId = traceId,
                status = MemoryCommitmentRequestStatus.UNAVAILABLE,
                error = createError(traceId),
            )
        }
    }

    @Test
    fun `create rejects failed result without error`() {
        assertFailsWith<IllegalArgumentException> {
            MemoryCommitmentRequestResult.create(
                traceId = TraceId.from(
                    "trace-memory-commitment-request-result-008",
                ),
                status = MemoryCommitmentRequestStatus.FAILED,
            )
        }
    }

    @Test
    fun `create rejects failed error from another trace`() {
        assertFailsWith<IllegalArgumentException> {
            MemoryCommitmentRequestResult.create(
                traceId = TraceId.from(
                    "trace-memory-commitment-request-result-009",
                ),
                status = MemoryCommitmentRequestStatus.FAILED,
                error = createError(
                    TraceId.from(
                        "trace-memory-commitment-error-other",
                    ),
                ),
            )
        }
    }

    @Test
    fun `create does not reinterpret constitutional dependencies`() {
        val traceId = TraceId.from(
            "trace-memory-commitment-request-result-010",
        )

        val result = MemoryCommitmentRequestResult.create(
            traceId = traceId,
            status = MemoryCommitmentRequestStatus.AVAILABLE,
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
            "capability-memory-commitment",
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
                "capability-memory-commitment",
            ),
            category = CapabilityCategory.KNOWLEDGE,
            name = "Memory Commitment",
            description =
                "Represents one bounded commitment test capability without persistence.",
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
                    1_754_000_174_000L,
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
                "task-memory-commitment-request-result",
            ),
            decision = decision,
            state = TaskState.CREATED,
            summary =
                "A bounded constitutional task was created.",
        )

        return PlanRecord.create(
            planId = PlanId.from(
                "plan-memory-commitment-request-result",
            ),
            task = task,
            state = PlanState.CREATED,
            summary =
                "Use the constitutionally governed Memory Authority path.",
        )
    }

    private fun createError(
        traceId: TraceId,
    ): UniversalErrorRecord {
        return UniversalErrorRecord.create(
            errorCode = ErrorCode.from(
                "MEMORY_COMMITMENT_REQUEST_FAILED",
            ),
            traceId = traceId,
            occurredAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_000_174_500L,
                ),
            summary =
                "Memory commitment request preparation failed.",
        )
    }
}
