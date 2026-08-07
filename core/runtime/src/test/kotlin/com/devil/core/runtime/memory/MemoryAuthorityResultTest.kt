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

class MemoryAuthorityResultTest {

    @Test
    fun `create preserves committable result with matching request`() {
        val traceId = TraceId.from(
            "trace-memory-authority-result-001",
        )
        val request = createRequest(traceId)

        val result = MemoryAuthorityResult.create(
            traceId = traceId,
            status = MemoryAuthorityStatus.COMMITTABLE,
            request = request,
        )

        assertEquals(traceId, result.traceId)
        assertEquals(MemoryAuthorityStatus.COMMITTABLE, result.status)
        assertEquals(request, result.request)
        assertNull(result.error)
    }

    @Test
    fun `create preserves deferred result`() {
        val traceId = TraceId.from(
            "trace-memory-authority-result-002",
        )

        val result = MemoryAuthorityResult.create(
            traceId = traceId,
            status = MemoryAuthorityStatus.DEFERRED,
        )

        assertEquals(traceId, result.traceId)
        assertEquals(MemoryAuthorityStatus.DEFERRED, result.status)
        assertNull(result.request)
        assertNull(result.error)
    }

    @Test
    fun `create preserves failed result with matching error`() {
        val traceId = TraceId.from(
            "trace-memory-authority-result-003",
        )
        val error = createError(traceId)

        val result = MemoryAuthorityResult.create(
            traceId = traceId,
            status = MemoryAuthorityStatus.FAILED,
            error = error,
        )

        assertEquals(MemoryAuthorityStatus.FAILED, result.status)
        assertNull(result.request)
        assertEquals(error, result.error)
    }

    @Test
    fun `create rejects committable result without request`() {
        assertFailsWith<IllegalArgumentException> {
            MemoryAuthorityResult.create(
                traceId = TraceId.from(
                    "trace-memory-authority-result-004",
                ),
                status = MemoryAuthorityStatus.COMMITTABLE,
            )
        }
    }

    @Test
    fun `create rejects committable request from another trace`() {
        assertFailsWith<IllegalArgumentException> {
            MemoryAuthorityResult.create(
                traceId = TraceId.from(
                    "trace-memory-authority-result-005",
                ),
                status = MemoryAuthorityStatus.COMMITTABLE,
                request = createRequest(
                    TraceId.from(
                        "trace-memory-authority-request-other",
                    ),
                ),
            )
        }
    }

    @Test
    fun `create rejects deferred result with request`() {
        val traceId = TraceId.from(
            "trace-memory-authority-result-006",
        )

        assertFailsWith<IllegalArgumentException> {
            MemoryAuthorityResult.create(
                traceId = traceId,
                status = MemoryAuthorityStatus.DEFERRED,
                request = createRequest(traceId),
            )
        }
    }

    @Test
    fun `create rejects failed result without error`() {
        assertFailsWith<IllegalArgumentException> {
            MemoryAuthorityResult.create(
                traceId = TraceId.from(
                    "trace-memory-authority-result-007",
                ),
                status = MemoryAuthorityStatus.FAILED,
            )
        }
    }

    @Test
    fun `create rejects failed error from another trace`() {
        assertFailsWith<IllegalArgumentException> {
            MemoryAuthorityResult.create(
                traceId = TraceId.from(
                    "trace-memory-authority-result-008",
                ),
                status = MemoryAuthorityStatus.FAILED,
                error = createError(
                    TraceId.from(
                        "trace-memory-authority-error-other",
                    ),
                ),
            )
        }
    }

    @Test
    fun `create does not commit memory or mutate task or plan state`() {
        val traceId = TraceId.from(
            "trace-memory-authority-result-009",
        )

        val result = MemoryAuthorityResult.create(
            traceId = traceId,
            status = MemoryAuthorityStatus.COMMITTABLE,
            request = createRequest(traceId),
        )

        assertEquals(
            TaskState.CREATED,
            result.request
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
            PlanState.CREATED,
            result.request
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
        assertNull(result.error)
    }

    private fun createRequest(
        traceId: TraceId,
    ): MemoryAuthorityRequest {
        return MemoryAuthorityRequest.create(
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
        val context = ContextEnvelope.create(
            traceId = traceId,
            schemaVersion = SchemaVersion.from(1),
            source = ContextSource.TEXT,
            trustLevel = ContextTrustLevel.VERIFIED,
            securityLevel = ContextSecurityLevel.RESTRICTED,
            observedAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_000_169_000L,
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
                "task-memory-authority-result",
            ),
            decision = decision,
            state = TaskState.CREATED,
            summary =
                "A bounded constitutional task was created.",
        )

        return PlanRecord.create(
            planId = PlanId.from(
                "plan-memory-authority-result",
            ),
            task = task,
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
                "MEMORY_AUTHORITY_FAILED",
            ),
            traceId = traceId,
            occurredAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_000_169_500L,
                ),
            summary =
                "Constitutional Memory Authority evaluation failed.",
        )
    }
}
