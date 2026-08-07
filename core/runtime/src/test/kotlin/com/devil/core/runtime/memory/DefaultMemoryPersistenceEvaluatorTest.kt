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

class DefaultMemoryPersistenceEvaluatorTest {

    @Test
    fun `evaluate returns unavailable without persisting logical memory`() {
        val traceId = TraceId.from(
            "trace-memory-persistence-evaluator-001",
        )
        val request = createRequest(traceId)
        val evaluator: MemoryPersistenceEvaluator =
            DefaultMemoryPersistenceEvaluator()

        val result = evaluator.evaluate(
            traceId = traceId,
            request = request,
        )

        assertEquals(traceId, result.traceId)
        assertEquals(
            MemoryPersistenceEvaluationStatus.UNAVAILABLE,
            result.status,
        )
        assertNull(result.request)
        assertNull(result.error)
    }

    @Test
    fun `evaluate does not mutate task or plan state`() {
        val traceId = TraceId.from(
            "trace-memory-persistence-evaluator-002",
        )
        val request = createRequest(traceId)

        val result =
            DefaultMemoryPersistenceEvaluator().evaluate(
                traceId = traceId,
                request = request,
            )

        assertEquals(
            PlanState.CREATED,
            request.commitmentRequest
                .authorityRequest
                .proposal
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
            request.commitmentRequest
                .authorityRequest
                .proposal
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
            MemoryPersistenceEvaluationStatus.UNAVAILABLE,
            result.status,
        )
        assertNull(result.request)
        assertNull(result.error)
    }

    @Test
    fun `evaluate rejects request from another trace`() {
        assertFailsWith<IllegalArgumentException> {
            DefaultMemoryPersistenceEvaluator().evaluate(
                traceId = TraceId.from(
                    "trace-memory-persistence-evaluator-003",
                ),
                request = createRequest(
                    TraceId.from(
                        "trace-memory-persistence-evaluator-other",
                    ),
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
                "capability-memory-persistence-evaluator",
            ),
            category = CapabilityCategory.KNOWLEDGE,
            name = "Memory Persistence Evaluator",
            description =
                "Represents one bounded persistence evaluator test capability without storage.",
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
                    1_754_000_184_000L,
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
                "task-memory-persistence-evaluator",
            ),
            decision = decision,
            state = TaskState.CREATED,
            summary =
                "A bounded constitutional task was created.",
        )

        return PlanRecord.create(
            planId = PlanId.from(
                "plan-memory-persistence-evaluator",
            ),
            task = task,
            state = PlanState.CREATED,
            summary =
                "Use only the constitutionally governed Memory Authority persistence path.",
        )
    }
}
