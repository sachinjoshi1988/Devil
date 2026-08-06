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
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class MemoryProposalRequestResultTest {

    @Test
    fun `create preserves available result with matching request`() {
        val traceId = TraceId.from(
            "trace-memory-proposal-request-result-001",
        )
        val request = createRequest(traceId)

        val result = MemoryProposalRequestResult.create(
            traceId = traceId,
            status = MemoryProposalRequestStatus.AVAILABLE,
            request = request,
        )

        assertEquals(traceId, result.traceId)
        assertEquals(
            MemoryProposalRequestStatus.AVAILABLE,
            result.status,
        )
        assertEquals(request, result.request)
        assertNull(result.error)
    }

    @Test
    fun `create preserves unavailable result`() {
        val traceId = TraceId.from(
            "trace-memory-proposal-request-result-002",
        )

        val result = MemoryProposalRequestResult.create(
            traceId = traceId,
            status = MemoryProposalRequestStatus.UNAVAILABLE,
        )

        assertEquals(
            MemoryProposalRequestStatus.UNAVAILABLE,
            result.status,
        )
        assertNull(result.request)
        assertNull(result.error)
    }

    @Test
    fun `create preserves failed result with matching error`() {
        val traceId = TraceId.from(
            "trace-memory-proposal-request-result-003",
        )
        val error = createError(traceId)

        val result = MemoryProposalRequestResult.create(
            traceId = traceId,
            status = MemoryProposalRequestStatus.FAILED,
            error = error,
        )

        assertEquals(
            MemoryProposalRequestStatus.FAILED,
            result.status,
        )
        assertNull(result.request)
        assertEquals(error, result.error)
    }

    @Test
    fun `create rejects available result without request`() {
        assertFailsWith<IllegalArgumentException> {
            MemoryProposalRequestResult.create(
                traceId = TraceId.from(
                    "trace-memory-proposal-request-result-004",
                ),
                status = MemoryProposalRequestStatus.AVAILABLE,
            )
        }
    }

    @Test
    fun `create rejects request from a different trace`() {
        assertFailsWith<IllegalArgumentException> {
            MemoryProposalRequestResult.create(
                traceId = TraceId.from(
                    "trace-memory-proposal-request-result-005",
                ),
                status = MemoryProposalRequestStatus.AVAILABLE,
                request = createRequest(
                    TraceId.from(
                        "trace-memory-proposal-request-other",
                    ),
                ),
            )
        }
    }

    @Test
    fun `create rejects unavailable result with request`() {
        val traceId = TraceId.from(
            "trace-memory-proposal-request-result-006",
        )

        assertFailsWith<IllegalArgumentException> {
            MemoryProposalRequestResult.create(
                traceId = traceId,
                status = MemoryProposalRequestStatus.UNAVAILABLE,
                request = createRequest(traceId),
            )
        }
    }

    @Test
    fun `create rejects failed result without error`() {
        assertFailsWith<IllegalArgumentException> {
            MemoryProposalRequestResult.create(
                traceId = TraceId.from(
                    "trace-memory-proposal-request-result-007",
                ),
                status = MemoryProposalRequestStatus.FAILED,
            )
        }
    }

    @Test
    fun `create rejects failed error from a different trace`() {
        assertFailsWith<IllegalArgumentException> {
            MemoryProposalRequestResult.create(
                traceId = TraceId.from(
                    "trace-memory-proposal-request-result-008",
                ),
                status = MemoryProposalRequestStatus.FAILED,
                error = createError(
                    TraceId.from(
                        "trace-memory-proposal-error-other",
                    ),
                ),
            )
        }
    }

    private fun createRequest(
        traceId: TraceId,
    ): MemoryProposalRequest {
        return MemoryProposalRequest.create(
            learning = LearningRequest.create(
                worldModelUpdate = WorldModelUpdateRequest.create(
                    outcome = OutcomeRequest.create(
                        verification = VerificationRequest.create(
                            observation = ObservationRequest.create(
                                execution = ExecutionRequest.create(
                                    plan = createPlan(traceId),
                                    capability = createCapability(),
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
            planId = PlanId.from(
                "plan-memory-proposal-request-result",
            ),
            task = TaskRecord.create(
                taskId = TaskId.from(
                    "task-memory-proposal-request-result",
                ),
                decision = DecisionRecord.create(
                    understanding = UnderstandingRecord.create(
                        context = ContextEnvelope.create(
                            traceId = traceId,
                            schemaVersion = SchemaVersion.from(1),
                            source = ContextSource.TEXT,
                            trustLevel =
                                ContextTrustLevel.VERIFIED,
                            securityLevel =
                                ContextSecurityLevel.RESTRICTED,
                            observedAt =
                                DevilTimestamp
                                    .fromEpochMilliseconds(
                                        1_754_000_156_000L,
                                    ),
                        ),
                        state = UnderstandingState.COMPLETE,
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
            errorCode = ErrorCode.from(
                "MEMORY_PROPOSAL_REQUEST_FAILED",
            ),
            traceId = traceId,
            occurredAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_000_156_500L,
                ),
            summary =
                "Memory proposal request preparation failed.",
        )
    }
}
