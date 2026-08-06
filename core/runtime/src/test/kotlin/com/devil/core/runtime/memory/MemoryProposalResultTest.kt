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

class MemoryProposalResultTest {

    @Test
    fun `create preserves proposable result with matching request`() {
        val traceId = TraceId.from(
            "trace-memory-proposal-result-001",
        )
        val request = createRequest(traceId)

        val result = MemoryProposalResult.create(
            traceId = traceId,
            status = MemoryProposalStatus.PROPOSABLE,
            request = request,
        )

        assertEquals(traceId, result.traceId)
        assertEquals(MemoryProposalStatus.PROPOSABLE, result.status)
        assertEquals(request, result.request)
        assertNull(result.error)
    }

    @Test
    fun `create preserves deferred result`() {
        val traceId = TraceId.from(
            "trace-memory-proposal-result-002",
        )

        val result = MemoryProposalResult.create(
            traceId = traceId,
            status = MemoryProposalStatus.DEFERRED,
        )

        assertEquals(traceId, result.traceId)
        assertEquals(MemoryProposalStatus.DEFERRED, result.status)
        assertNull(result.request)
        assertNull(result.error)
    }

    @Test
    fun `create preserves failed result with matching error`() {
        val traceId = TraceId.from(
            "trace-memory-proposal-result-003",
        )
        val error = createError(traceId)

        val result = MemoryProposalResult.create(
            traceId = traceId,
            status = MemoryProposalStatus.FAILED,
            error = error,
        )

        assertEquals(MemoryProposalStatus.FAILED, result.status)
        assertNull(result.request)
        assertEquals(error, result.error)
    }

    @Test
    fun `create rejects proposable result without request`() {
        assertFailsWith<IllegalArgumentException> {
            MemoryProposalResult.create(
                traceId = TraceId.from(
                    "trace-memory-proposal-result-004",
                ),
                status = MemoryProposalStatus.PROPOSABLE,
            )
        }
    }

    @Test
    fun `create rejects proposable request from another trace`() {
        assertFailsWith<IllegalArgumentException> {
            MemoryProposalResult.create(
                traceId = TraceId.from(
                    "trace-memory-proposal-result-005",
                ),
                status = MemoryProposalStatus.PROPOSABLE,
                request = createRequest(
                    TraceId.from(
                        "trace-memory-proposal-request-other",
                    ),
                ),
            )
        }
    }

    @Test
    fun `create rejects deferred result with request`() {
        val traceId = TraceId.from(
            "trace-memory-proposal-result-006",
        )

        assertFailsWith<IllegalArgumentException> {
            MemoryProposalResult.create(
                traceId = traceId,
                status = MemoryProposalStatus.DEFERRED,
                request = createRequest(traceId),
            )
        }
    }

    @Test
    fun `create rejects failed result without error`() {
        assertFailsWith<IllegalArgumentException> {
            MemoryProposalResult.create(
                traceId = TraceId.from(
                    "trace-memory-proposal-result-007",
                ),
                status = MemoryProposalStatus.FAILED,
            )
        }
    }

    @Test
    fun `create rejects failed error from another trace`() {
        assertFailsWith<IllegalArgumentException> {
            MemoryProposalResult.create(
                traceId = TraceId.from(
                    "trace-memory-proposal-result-008",
                ),
                status = MemoryProposalStatus.FAILED,
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
                "plan-memory-proposal-result",
            ),
            task = TaskRecord.create(
                taskId = TaskId.from(
                    "task-memory-proposal-result",
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
                                        1_754_000_160_000L,
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
                "MEMORY_PROPOSAL_FAILED",
            ),
            traceId = traceId,
            occurredAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_000_160_500L,
                ),
            summary =
                "Constitutional memory proposal evaluation failed.",
        )
    }
}
