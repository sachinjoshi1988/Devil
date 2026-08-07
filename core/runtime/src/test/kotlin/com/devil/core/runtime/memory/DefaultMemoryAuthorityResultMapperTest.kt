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

class DefaultMemoryAuthorityResultMapperTest {

    @Test
    fun `map translates committable evaluation into committable result`() {
        val traceId = TraceId.from(
            "trace-memory-authority-result-mapper-001",
        )
        val request = createRequest(traceId)
        val mapper: MemoryAuthorityResultMapper =
            DefaultMemoryAuthorityResultMapper()

        val result = mapper.map(
            traceId = traceId,
            evaluation = MemoryAuthorityEvaluationResult.create(
                traceId = traceId,
                status = MemoryAuthorityEvaluationStatus.COMMITTABLE,
                request = request,
            ),
        )

        assertEquals(traceId, result.traceId)
        assertEquals(MemoryAuthorityStatus.COMMITTABLE, result.status)
        assertEquals(request, result.request)
        assertNull(result.error)
    }

    @Test
    fun `map translates unavailable evaluation into deferred result`() {
        val traceId = TraceId.from(
            "trace-memory-authority-result-mapper-002",
        )

        val result = DefaultMemoryAuthorityResultMapper().map(
            traceId = traceId,
            evaluation = MemoryAuthorityEvaluationResult.create(
                traceId = traceId,
                status = MemoryAuthorityEvaluationStatus.UNAVAILABLE,
            ),
        )

        assertEquals(traceId, result.traceId)
        assertEquals(MemoryAuthorityStatus.DEFERRED, result.status)
        assertNull(result.request)
        assertNull(result.error)
    }

    @Test
    fun `map preserves failed evaluation error`() {
        val traceId = TraceId.from(
            "trace-memory-authority-result-mapper-003",
        )
        val error = createError(traceId)

        val result = DefaultMemoryAuthorityResultMapper().map(
            traceId = traceId,
            evaluation = MemoryAuthorityEvaluationResult.create(
                traceId = traceId,
                status = MemoryAuthorityEvaluationStatus.FAILED,
                error = error,
            ),
        )

        assertEquals(MemoryAuthorityStatus.FAILED, result.status)
        assertNull(result.request)
        assertEquals(error, result.error)
    }

    @Test
    fun `map does not commit memory or mutate task or plan state`() {
        val traceId = TraceId.from(
            "trace-memory-authority-result-mapper-004",
        )

        val result = DefaultMemoryAuthorityResultMapper().map(
            traceId = traceId,
            evaluation = MemoryAuthorityEvaluationResult.create(
                traceId = traceId,
                status = MemoryAuthorityEvaluationStatus.COMMITTABLE,
                request = createRequest(traceId),
            ),
        )

        assertEquals(MemoryAuthorityStatus.COMMITTABLE, result.status)
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

    @Test
    fun `map rejects evaluation result from another trace`() {
        assertFailsWith<IllegalArgumentException> {
            DefaultMemoryAuthorityResultMapper().map(
                traceId = TraceId.from(
                    "trace-memory-authority-result-mapper-005",
                ),
                evaluation = MemoryAuthorityEvaluationResult.create(
                    traceId = TraceId.from(
                        "trace-memory-authority-evaluation-other",
                    ),
                    status = MemoryAuthorityEvaluationStatus.UNAVAILABLE,
                ),
            )
        }
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
                    1_754_000_170_000L,
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
                "task-memory-authority-result-mapper",
            ),
            decision = decision,
            state = TaskState.CREATED,
            summary =
                "A bounded constitutional task was created.",
        )

        return PlanRecord.create(
            planId = PlanId.from(
                "plan-memory-authority-result-mapper",
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
                "MEMORY_AUTHORITY_EVALUATION_FAILED",
            ),
            traceId = traceId,
            occurredAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_000_170_500L,
                ),
            summary =
                "Constitutional Memory Authority evaluation failed.",
        )
    }
}
