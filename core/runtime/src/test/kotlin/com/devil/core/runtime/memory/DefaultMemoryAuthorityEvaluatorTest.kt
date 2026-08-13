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
import com.devil.core.model.execution.ExecutionRequest
import com.devil.core.model.learning.LearningRequest
import com.devil.core.model.memory.MemoryAuthorityRequest
import com.devil.core.runtime.memory.MemoryAuthorityEvidenceResult
import com.devil.core.runtime.memory.MemoryAuthorityEvidenceStatus
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

class DefaultMemoryAuthorityEvaluatorTest {

    @Test
    fun `evaluate returns unavailable without committing logical memory`() {
        val traceId = TraceId.from(
            "trace-default-memory-authority-evaluator-001",
        )
        val evaluator: MemoryAuthorityEvaluator =
            DefaultMemoryAuthorityEvaluator()

        val result = evaluator.evaluate(
            traceId = traceId,
            evidence = createDeferredMemoryAuthorityEvidence(traceId),
            request = createRequest(traceId),
        )

        assertEquals(traceId, result.traceId)
        assertEquals(
            MemoryAuthorityEvaluationStatus.UNAVAILABLE,
            result.status,
        )
        assertNull(result.request)
        assertNull(result.error)
    }

    @Test
    fun `evaluate does not treat proposable memory as committed memory`() {
        val traceId = TraceId.from(
            "trace-default-memory-authority-evaluator-002",
        )
        val request = createRequest(traceId)

        val result = DefaultMemoryAuthorityEvaluator().evaluate(
            traceId = traceId,
            evidence = createDeferredMemoryAuthorityEvidence(traceId),
            request = request,
        )

        assertEquals(
            PlanState.CREATED,
            request.proposal
                .learning
                .worldModelUpdate
                .outcome
                .verification
                .observation
                .execution
                .plan
                .state,
        )
        assertEquals(
            TaskState.CREATED,
            request.proposal
                .learning
                .worldModelUpdate
                .outcome
                .verification
                .observation
                .execution
                .plan
                .task
                .state,
        )
        assertEquals(
            MemoryAuthorityEvaluationStatus.UNAVAILABLE,
            result.status,
        )
        assertNull(result.request)
        assertNull(result.error)
    }

    @Test
    fun `evaluate rejects request from another trace`() {
        assertFailsWith<IllegalArgumentException> {
            DefaultMemoryAuthorityEvaluator().evaluate(
                traceId = TraceId.from(
                    "trace-default-memory-authority-evaluator-003",
                ),
                evidence = createDeferredMemoryAuthorityEvidence(
                    TraceId.from(
                        "trace-default-memory-authority-evaluator-003",
                    ),
                ),
                request = createRequest(
                    TraceId.from(
                        "trace-default-memory-authority-request-other",
                    ),
                ),
            )
        }
    }

    private fun createDeferredMemoryAuthorityEvidence(
        traceId: TraceId,
    ): MemoryAuthorityEvidenceResult {
        return MemoryAuthorityEvidenceResult.create(
            traceId = traceId,
            status = MemoryAuthorityEvidenceStatus.DEFERRED,
        )
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
                    1_754_000_168_000L,
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
                "task-default-memory-authority-evaluator",
            ),
            decision = decision,
            state = TaskState.CREATED,
            summary =
                "A bounded constitutional task was created.",
        )

        return PlanRecord.create(
            planId = PlanId.from(
                "plan-default-memory-authority-evaluator",
            ),
            task = task,
            state = PlanState.CREATED,
            summary =
                "Use the constitutionally approved capability path.",
        )
    }
}
