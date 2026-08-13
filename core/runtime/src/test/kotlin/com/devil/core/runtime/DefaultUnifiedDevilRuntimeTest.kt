package com.devil.core.runtime

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
import com.devil.core.model.conversation.ConversationInput
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
import com.devil.core.runtime.authorization.AuthorizationResult
import com.devil.core.runtime.capability.CapabilitySelectionResult
import com.devil.core.runtime.decision.DecisionAuthorityResult
import com.devil.core.runtime.executive.ExecutiveReadinessResult
import com.devil.core.runtime.execution.ExecutionResult
import com.devil.core.runtime.identity.IdentityResult
import com.devil.core.runtime.learning.LearningResult
import com.devil.core.runtime.memory.MemoryAuthority
import com.devil.core.runtime.memory.MemoryAuthorityResult
import com.devil.core.runtime.memory.MemoryAuthorityStatus
import com.devil.core.runtime.memory.MemoryCommitmentAuthority
import com.devil.core.runtime.memory.MemoryCommitmentResult
import com.devil.core.runtime.memory.MemoryCommitmentStatus
import com.devil.core.runtime.memory.MemoryPersistenceAuthority
import com.devil.core.runtime.memory.MemoryPersistenceResult
import com.devil.core.runtime.memory.MemoryPersistenceStatus
import com.devil.core.runtime.memory.MemoryProposalAuthority
import com.devil.core.runtime.memory.MemoryProposalEvidenceResult
import com.devil.core.runtime.memory.MemoryProposalResult
import com.devil.core.runtime.memory.MemoryProposalStatus
import com.devil.core.runtime.observation.ObservationResult
import com.devil.core.runtime.outcome.OutcomeResult
import com.devil.core.runtime.plan.PlanAuthorityResult
import com.devil.core.runtime.task.TaskAuthorityResult
import com.devil.core.runtime.trust.TrustResult
import com.devil.core.runtime.understanding.UnderstandingAuthorityResult
import com.devil.core.runtime.verification.VerificationResult
import com.devil.core.runtime.worldmodel.WorldModelUpdateResult
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class DefaultUnifiedDevilRuntimeTest {

    @Test
    fun `accept coordinates conversation input through one constitutional runtime path`() {
        val input = createInput(
            "trace-runtime-memory-persistence-001",
        )

        val result = DefaultUnifiedDevilRuntime().accept(input)

        assertEquals(input.context.traceId, result.traceId)
        assertEquals(RuntimeStatus.DEFERRED, result.status)
        assertNull(result.error)
    }

    @Test
    fun `accept does not treat committable memory commitment result as persisted logical memory`() {
        val input = createInput(
            "trace-runtime-memory-persistence-002",
        )
        val runtime = DefaultUnifiedDevilRuntime(
            memoryCommitmentAuthority =
                fixedMemoryCommitmentAuthority(
                    status = MemoryCommitmentStatus.COMMITTABLE,
                ),
        )

        val result = runtime.accept(input)

        assertEquals(input.context.traceId, result.traceId)
        assertEquals(RuntimeStatus.DEFERRED, result.status)
        assertNull(result.error)
    }

    @Test
    fun `accept maps persistable memory persistence result to accepted runtime result`() {
        val input = createInput(
            "trace-runtime-memory-persistence-003",
        )
        val runtime = DefaultUnifiedDevilRuntime(
            memoryPersistenceAuthority =
                fixedMemoryPersistenceAuthority(
                    status = MemoryPersistenceStatus.PERSISTABLE,
                ),
        )

        val result = runtime.accept(input)

        assertEquals(input.context.traceId, result.traceId)
        assertEquals(RuntimeStatus.ACCEPTED, result.status)
        assertNull(result.error)
    }

    @Test
    fun `accept maps deferred memory persistence result to deferred runtime result`() {
        val input = createInput(
            "trace-runtime-memory-persistence-004",
        )
        val runtime = DefaultUnifiedDevilRuntime(
            memoryPersistenceAuthority =
                fixedMemoryPersistenceAuthority(
                    status = MemoryPersistenceStatus.DEFERRED,
                ),
        )

        val result = runtime.accept(input)

        assertEquals(input.context.traceId, result.traceId)
        assertEquals(RuntimeStatus.DEFERRED, result.status)
        assertNull(result.error)
    }

    @Test
    fun `accept maps failed memory persistence result to rejected runtime result`() {
        val input = createInput(
            "trace-runtime-memory-persistence-005",
        )
        val error = createError(
            traceId = input.context.traceId,
            code = "UNIFIED_RUNTIME_MEMORY_PERSISTENCE_FAILED",
            summary =
                "Bounded constitutional memory persistence evaluation failed.",
        )
        val runtime = DefaultUnifiedDevilRuntime(
            memoryPersistenceAuthority =
                fixedMemoryPersistenceAuthority(
                    status = MemoryPersistenceStatus.FAILED,
                    error = error,
                ),
        )

        val result = runtime.accept(input)

        assertEquals(input.context.traceId, result.traceId)
        assertEquals(RuntimeStatus.REJECTED, result.status)
        assertEquals(error, result.error)
    }

    @Test
    fun `accept rejects memory persistence result from another trace`() {
        val input = createInput(
            "trace-runtime-memory-persistence-006",
        )
        val runtime = DefaultUnifiedDevilRuntime(
            memoryPersistenceAuthority =
                fixedMemoryPersistenceAuthority(
                    status = MemoryPersistenceStatus.DEFERRED,
                    traceId = TraceId.from(
                        "trace-runtime-memory-persistence-other",
                    ),
                ),
        )

        assertFailsWith<IllegalArgumentException> {
            runtime.accept(input)
        }
    }

    private fun fixedMemoryProposalAuthority(
        status: MemoryProposalStatus,
        traceId: TraceId? = null,
        error: UniversalErrorRecord? = null,
    ): MemoryProposalAuthority {
        return object : MemoryProposalAuthority {
            override fun evaluateProposal(
                context: ContextEnvelope,
                identity: IdentityResult,
                trust: TrustResult,
                authorization: AuthorizationResult,
                understanding: UnderstandingAuthorityResult,
                decision: DecisionAuthorityResult,
                task: TaskAuthorityResult,
                plan: PlanAuthorityResult,
                capability: CapabilitySelectionResult,
                readiness: ExecutiveReadinessResult,
                execution: ExecutionResult,
                observation: ObservationResult,
                verification: VerificationResult,
                outcome: OutcomeResult,
                worldModelUpdate: WorldModelUpdateResult,
                learning: LearningResult,
                memoryProposalEvidence: MemoryProposalEvidenceResult,
            ): MemoryProposalResult {
                val resultTraceId =
                    traceId ?: context.traceId

                return when (status) {
                    MemoryProposalStatus.PROPOSABLE ->
                        MemoryProposalResult.create(
                            traceId = resultTraceId,
                            status =
                                MemoryProposalStatus.PROPOSABLE,
                            request =
                                createMemoryProposalRequest(
                                    context = context,
                                ),
                        )

                    MemoryProposalStatus.DEFERRED ->
                        MemoryProposalResult.create(
                            traceId = resultTraceId,
                            status =
                                MemoryProposalStatus.DEFERRED,
                        )

                    MemoryProposalStatus.FAILED ->
                        MemoryProposalResult.create(
                            traceId = resultTraceId,
                            status =
                                MemoryProposalStatus.FAILED,
                            error = requireNotNull(error),
                        )
                }
            }
        }
    }

    private fun fixedMemoryAuthority(
        status: MemoryAuthorityStatus,
        traceId: TraceId? = null,
        error: UniversalErrorRecord? = null,
    ): MemoryAuthority {
        return object : MemoryAuthority {
            override fun evaluateMemory(
                context: ContextEnvelope,
                identity: IdentityResult,
                trust: TrustResult,
                authorization: AuthorizationResult,
                understanding: UnderstandingAuthorityResult,
                decision: DecisionAuthorityResult,
                task: TaskAuthorityResult,
                plan: PlanAuthorityResult,
                capability: CapabilitySelectionResult,
                readiness: ExecutiveReadinessResult,
                execution: ExecutionResult,
                observation: ObservationResult,
                verification: VerificationResult,
                outcome: OutcomeResult,
                worldModelUpdate: WorldModelUpdateResult,
                learning: LearningResult,
                memoryProposal: MemoryProposalResult,
            ): MemoryAuthorityResult {
                val resultTraceId =
                    traceId ?: context.traceId

                return when (status) {
                    MemoryAuthorityStatus.COMMITTABLE ->
                        MemoryAuthorityResult.create(
                            traceId = resultTraceId,
                            status =
                                MemoryAuthorityStatus.COMMITTABLE,
                            request =
                                MemoryAuthorityRequest.create(
                                    proposal =
                                        createMemoryProposalRequest(
                                            context = context,
                                        ),
                                ),
                        )

                    MemoryAuthorityStatus.DEFERRED ->
                        MemoryAuthorityResult.create(
                            traceId = resultTraceId,
                            status =
                                MemoryAuthorityStatus.DEFERRED,
                        )

                    MemoryAuthorityStatus.FAILED ->
                        MemoryAuthorityResult.create(
                            traceId = resultTraceId,
                            status =
                                MemoryAuthorityStatus.FAILED,
                            error = requireNotNull(error),
                        )
                }
            }
        }
    }

    private fun fixedMemoryCommitmentAuthority(
        status: MemoryCommitmentStatus,
        traceId: TraceId? = null,
        error: UniversalErrorRecord? = null,
    ): MemoryCommitmentAuthority {
        return object : MemoryCommitmentAuthority {
            override fun evaluateCommitment(
                context: ContextEnvelope,
                identity: IdentityResult,
                trust: TrustResult,
                authorization: AuthorizationResult,
                understanding: UnderstandingAuthorityResult,
                decision: DecisionAuthorityResult,
                task: TaskAuthorityResult,
                plan: PlanAuthorityResult,
                capability: CapabilitySelectionResult,
                readiness: ExecutiveReadinessResult,
                execution: ExecutionResult,
                observation: ObservationResult,
                verification: VerificationResult,
                outcome: OutcomeResult,
                worldModelUpdate: WorldModelUpdateResult,
                learning: LearningResult,
                memoryProposal: MemoryProposalResult,
                memory: MemoryAuthorityResult,
            ): MemoryCommitmentResult {
                val resultTraceId =
                    traceId ?: context.traceId

                return when (status) {
                    MemoryCommitmentStatus.COMMITTABLE ->
                        MemoryCommitmentResult.create(
                            traceId = resultTraceId,
                            status =
                                MemoryCommitmentStatus.COMMITTABLE,
                            request =
                                createMemoryCommitmentRequest(
                                    context = context,
                                ),
                        )

                    MemoryCommitmentStatus.DEFERRED ->
                        MemoryCommitmentResult.create(
                            traceId = resultTraceId,
                            status =
                                MemoryCommitmentStatus.DEFERRED,
                        )

                    MemoryCommitmentStatus.FAILED ->
                        MemoryCommitmentResult.create(
                            traceId = resultTraceId,
                            status =
                                MemoryCommitmentStatus.FAILED,
                            error = requireNotNull(error),
                        )
                }
            }
        }
    }

    private fun fixedMemoryPersistenceAuthority(
        status: MemoryPersistenceStatus,
        traceId: TraceId? = null,
        error: UniversalErrorRecord? = null,
    ): MemoryPersistenceAuthority {
        return object : MemoryPersistenceAuthority {
            override fun evaluatePersistence(
                context: ContextEnvelope,
                identity: IdentityResult,
                trust: TrustResult,
                authorization: AuthorizationResult,
                understanding: UnderstandingAuthorityResult,
                decision: DecisionAuthorityResult,
                task: TaskAuthorityResult,
                plan: PlanAuthorityResult,
                capability: CapabilitySelectionResult,
                readiness: ExecutiveReadinessResult,
                execution: ExecutionResult,
                observation: ObservationResult,
                verification: VerificationResult,
                outcome: OutcomeResult,
                worldModelUpdate: WorldModelUpdateResult,
                learning: LearningResult,
                memoryProposal: MemoryProposalResult,
                memory: MemoryAuthorityResult,
                memoryCommitment: MemoryCommitmentResult,
            ): MemoryPersistenceResult {
                val resultTraceId =
                    traceId ?: context.traceId

                return when (status) {
                    MemoryPersistenceStatus.PERSISTABLE ->
                        MemoryPersistenceResult.create(
                            traceId = resultTraceId,
                            status =
                                MemoryPersistenceStatus.PERSISTABLE,
                            request =
                                MemoryPersistenceRequest.create(
                                    commitmentRequest =
                                        createMemoryCommitmentRequest(
                                            context = context,
                                        ),
                                ),
                        )

                    MemoryPersistenceStatus.DEFERRED ->
                        MemoryPersistenceResult.create(
                            traceId = resultTraceId,
                            status =
                                MemoryPersistenceStatus.DEFERRED,
                        )

                    MemoryPersistenceStatus.FAILED ->
                        MemoryPersistenceResult.create(
                            traceId = resultTraceId,
                            status =
                                MemoryPersistenceStatus.FAILED,
                            error = requireNotNull(error),
                        )
                }
            }
        }
    }

    private fun createMemoryCommitmentRequest(
        context: ContextEnvelope,
    ): MemoryCommitmentRequest {
        return MemoryCommitmentRequest.create(
            authorityRequest =
                MemoryAuthorityRequest.create(
                    proposal =
                        createMemoryProposalRequest(
                            context = context,
                        ),
                ),
        )
    }

    private fun createMemoryProposalRequest(
        context: ContextEnvelope,
    ): MemoryProposalRequest {
        return MemoryProposalRequest.create(
            learning = LearningRequest.create(
                worldModelUpdate =
                    WorldModelUpdateRequest.create(
                        outcome =
                            OutcomeRequest.create(
                                verification =
                                    VerificationRequest.create(
                                        observation =
                                            ObservationRequest.create(
                                                execution =
                                                    createExecutionRequest(
                                                        context = context,
                                                    ),
                                            ),
                                    ),
                            ),
                    ),
            ),
        )
    }

    private fun createExecutionRequest(
        context: ContextEnvelope,
    ): ExecutionRequest {
        val understanding =
            UnderstandingRecord.create(
                context = context,
                state = UnderstandingState.COMPLETE,
                summary =
                    "Bounded understanding was produced.",
            )

        val decision =
            DecisionRecord.create(
                understanding = understanding,
                state = DecisionState.SELECTED,
                summary =
                    "A constitutional decision was selected.",
            )

        val task =
            TaskRecord.create(
                taskId =
                    TaskId.from(
                        "task-runtime-memory-persistence",
                    ),
                decision = decision,
                state = TaskState.CREATED,
                summary =
                    "A bounded constitutional task was created.",
            )

        val plan =
            PlanRecord.create(
                planId =
                    PlanId.from(
                        "plan-runtime-memory-persistence",
                    ),
                task = task,
                state = PlanState.CREATED,
                summary =
                    "Use the constitutionally approved capability path.",
            )

        val capability =
            CapabilityContract.create(
                capabilityId =
                    CapabilityId.from(
                        "capability-runtime-memory-persistence",
                    ),
                category = CapabilityCategory.ACTION,
                name =
                    "Runtime Memory Persistence Test Capability",
                description =
                    "Represents one bounded test capability without platform execution or logical-memory persistence.",
            )

        return ExecutionRequest.create(
            plan = plan,
            capability = capability,
        )
    }

    private fun createError(
        traceId: TraceId,
        code: String,
        summary: String,
    ): UniversalErrorRecord {
        return UniversalErrorRecord.create(
            errorCode = ErrorCode.from(code),
            traceId = traceId,
            occurredAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_000_186_500L,
                ),
            summary = summary,
        )
    }

    private fun createInput(
        traceValue: String,
    ): ConversationInput {
        return ConversationInput.create(
            context =
                ContextEnvelope.create(
                    traceId = TraceId.from(traceValue),
                    schemaVersion = SchemaVersion.from(1),
                    source = ContextSource.TEST,
                    trustLevel =
                        ContextTrustLevel.VERIFIED,
                    securityLevel =
                        ContextSecurityLevel.RESTRICTED,
                    observedAt =
                        DevilTimestamp.fromEpochMilliseconds(
                            1_754_000_186_000L,
                        ),
                ),
            content =
                "Please tell me the current phone status.",
        )
    }
}
