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
import com.devil.core.runtime.authorization.AuthorizationResult
import com.devil.core.runtime.authorization.AuthorizationStatus
import com.devil.core.runtime.capability.CapabilitySelectionResult
import com.devil.core.runtime.capability.CapabilitySelectionStatus
import com.devil.core.runtime.decision.DecisionAuthorityResult
import com.devil.core.runtime.decision.DecisionAuthorityStatus
import com.devil.core.runtime.executive.ExecutiveReadinessResult
import com.devil.core.runtime.executive.ExecutiveReadinessStatus
import com.devil.core.runtime.execution.ExecutionResult
import com.devil.core.runtime.execution.ExecutionStatus
import com.devil.core.runtime.identity.IdentityResult
import com.devil.core.runtime.identity.IdentityStatus
import com.devil.core.runtime.learning.LearningResult
import com.devil.core.runtime.learning.LearningStatus
import com.devil.core.runtime.observation.ObservationResult
import com.devil.core.runtime.observation.ObservationStatus
import com.devil.core.runtime.outcome.OutcomeResult
import com.devil.core.runtime.outcome.OutcomeStatus
import com.devil.core.runtime.plan.PlanAuthorityResult
import com.devil.core.runtime.plan.PlanAuthorityStatus
import com.devil.core.runtime.task.TaskAuthorityResult
import com.devil.core.runtime.task.TaskAuthorityStatus
import com.devil.core.runtime.trust.TrustResult
import com.devil.core.runtime.trust.TrustStatus
import com.devil.core.runtime.understanding.UnderstandingAuthorityResult
import com.devil.core.runtime.understanding.UnderstandingAuthorityStatus
import com.devil.core.runtime.verification.VerificationResult
import com.devil.core.runtime.verification.VerificationStatus
import com.devil.core.runtime.worldmodel.WorldModelUpdateResult
import com.devil.core.runtime.worldmodel.WorldModelUpdateStatus
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class DefaultMemoryPersistenceAuthorityTest {

    @Test
    fun `evaluate persistence defers when persistence evidence is unavailable`() {
        val context = createContext(
            "trace-memory-persistence-authority-001",
        )

        val result = evaluatePersistence(
            authority = DefaultMemoryPersistenceAuthority(),
            context = context,
        )

        assertEquals(context.traceId, result.traceId)
        assertEquals(
            MemoryPersistenceStatus.DEFERRED,
            result.status,
        )
        assertNull(result.request)
        assertNull(result.error)
    }

    @Test
    fun `evaluate persistence coordinates request evaluator and mapper`() {
        val context = createContext(
            "trace-memory-persistence-authority-002",
        )
        val authority = DefaultMemoryPersistenceAuthority(
            evaluator =
                object : MemoryPersistenceEvaluator {
                    override fun evaluate(
                        traceId: TraceId,
                        request:
                            com.devil.core.model.memory.MemoryPersistenceRequest,
                    ): MemoryPersistenceEvaluationResult {
                        return MemoryPersistenceEvaluationResult.create(
                            traceId = traceId,
                            status =
                                MemoryPersistenceEvaluationStatus.PERSISTABLE,
                            request = request,
                        )
                    }
                },
        )

        val result = evaluatePersistence(
            authority = authority,
            context = context,
        )

        assertEquals(
            MemoryPersistenceStatus.PERSISTABLE,
            result.status,
        )
        assertEquals(
            "capability-memory-persistence-authority",
            result.request
                ?.commitmentRequest
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
        assertEquals(
            PlanState.CREATED,
            result.request
                ?.commitmentRequest
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
                ?.commitmentRequest
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
    fun `evaluate persistence defers when request is unavailable`() {
        val context = createContext(
            "trace-memory-persistence-authority-003",
        )
        val authority = DefaultMemoryPersistenceAuthority(
            requestProvider =
                object : MemoryPersistenceRequestProvider {
                    override fun provide(
                        commitment: MemoryCommitmentResult,
                    ): MemoryPersistenceRequestResult {
                        return MemoryPersistenceRequestResult.create(
                            traceId = commitment.traceId,
                            status =
                                MemoryPersistenceRequestStatus.UNAVAILABLE,
                        )
                    }
                },
        )

        val result = evaluatePersistence(
            authority = authority,
            context = context,
        )

        assertEquals(
            MemoryPersistenceStatus.DEFERRED,
            result.status,
        )
        assertNull(result.request)
        assertNull(result.error)
    }

    @Test
    fun `evaluate persistence preserves failed request error`() {
        val context = createContext(
            "trace-memory-persistence-authority-004",
        )
        val error = createError(
            traceId = context.traceId,
            code = "MEMORY_PERSISTENCE_REQUEST_FAILED",
        )
        val authority = DefaultMemoryPersistenceAuthority(
            requestProvider =
                object : MemoryPersistenceRequestProvider {
                    override fun provide(
                        commitment: MemoryCommitmentResult,
                    ): MemoryPersistenceRequestResult {
                        return MemoryPersistenceRequestResult.create(
                            traceId = commitment.traceId,
                            status =
                                MemoryPersistenceRequestStatus.FAILED,
                            error = error,
                        )
                    }
                },
        )

        val result = evaluatePersistence(
            authority = authority,
            context = context,
        )

        assertEquals(
            MemoryPersistenceStatus.FAILED,
            result.status,
        )
        assertNull(result.request)
        assertEquals(error, result.error)
    }

    @Test
    fun `evaluate persistence preserves failed evaluation error`() {
        val context = createContext(
            "trace-memory-persistence-authority-005",
        )
        val error = createError(
            traceId = context.traceId,
            code = "MEMORY_PERSISTENCE_EVALUATION_FAILED",
        )
        val authority = DefaultMemoryPersistenceAuthority(
            evaluator =
                object : MemoryPersistenceEvaluator {
                    override fun evaluate(
                        traceId: TraceId,
                        request:
                            com.devil.core.model.memory.MemoryPersistenceRequest,
                    ): MemoryPersistenceEvaluationResult {
                        return MemoryPersistenceEvaluationResult.create(
                            traceId = traceId,
                            status =
                                MemoryPersistenceEvaluationStatus.FAILED,
                            error = error,
                        )
                    }
                },
        )

        val result = evaluatePersistence(
            authority = authority,
            context = context,
        )

        assertEquals(
            MemoryPersistenceStatus.FAILED,
            result.status,
        )
        assertNull(result.request)
        assertEquals(error, result.error)
    }

    @Test
    fun `evaluate persistence rejects memory commitment result from another trace`() {
        val context = createContext(
            "trace-memory-persistence-authority-006",
        )

        assertFailsWith<IllegalArgumentException> {
            DefaultMemoryPersistenceAuthority().evaluatePersistence(
                context = context,
                identity = createIdentity(context.traceId),
                trust = createTrust(context.traceId),
                authorization =
                    createAuthorization(context.traceId),
                understanding =
                    createUnderstanding(context),
                decision = createDecision(context),
                task = createTask(context),
                plan = createPlanResult(context),
                capability =
                    createCapabilityResult(context),
                readiness =
                    createReadiness(context.traceId),
                execution = createExecution(context),
                observation =
                    createObservation(context),
                verification =
                    createVerification(context),
                outcome = createOutcome(context),
                worldModelUpdate =
                    createWorldModelUpdate(context),
                learning = createLearning(context),
                memoryProposal =
                    createMemoryProposal(context),
                memory =
                    createMemoryAuthorityResult(context),
                memoryCommitment =
                    MemoryCommitmentResult.create(
                        traceId = TraceId.from(
                            "trace-memory-persistence-commitment-other",
                        ),
                        status =
                            MemoryCommitmentStatus.DEFERRED,
                    ),
            )
        }
    }

    private fun evaluatePersistence(
        authority: MemoryPersistenceAuthority,
        context: ContextEnvelope,
    ): MemoryPersistenceResult {
        return authority.evaluatePersistence(
            context = context,
            identity = createIdentity(context.traceId),
            trust = createTrust(context.traceId),
            authorization =
                createAuthorization(context.traceId),
            understanding =
                createUnderstanding(context),
            decision = createDecision(context),
            task = createTask(context),
            plan = createPlanResult(context),
            capability =
                createCapabilityResult(context),
            readiness =
                createReadiness(context.traceId),
            execution = createExecution(context),
            observation =
                createObservation(context),
            verification =
                createVerification(context),
            outcome = createOutcome(context),
            worldModelUpdate =
                createWorldModelUpdate(context),
            learning = createLearning(context),
            memoryProposal =
                createMemoryProposal(context),
            memory =
                createMemoryAuthorityResult(context),
            memoryCommitment =
                createMemoryCommitmentResult(context),
        )
    }

    private fun createIdentity(
        traceId: TraceId,
    ): IdentityResult {
        return IdentityResult.create(
            traceId = traceId,
            status = IdentityStatus.UNRESOLVED,
        )
    }

    private fun createTrust(
        traceId: TraceId,
    ): TrustResult {
        return TrustResult.create(
            traceId = traceId,
            status = TrustStatus.DEFERRED,
        )
    }

    private fun createAuthorization(
        traceId: TraceId,
    ): AuthorizationResult {
        return AuthorizationResult.create(
            traceId = traceId,
            status = AuthorizationStatus.DEFERRED,
        )
    }

    private fun createUnderstanding(
        context: ContextEnvelope,
    ): UnderstandingAuthorityResult {
        return UnderstandingAuthorityResult.create(
            traceId = context.traceId,
            status =
                UnderstandingAuthorityStatus.PRODUCED,
            understanding =
                UnderstandingRecord.create(
                    context = context,
                    state =
                        UnderstandingState.COMPLETE,
                    summary =
                        "Bounded understanding was produced.",
                ),
        )
    }

    private fun createDecision(
        context: ContextEnvelope,
    ): DecisionAuthorityResult {
        return DecisionAuthorityResult.create(
            traceId = context.traceId,
            status = DecisionAuthorityStatus.PRODUCED,
            decision =
                DecisionRecord.create(
                    understanding =
                        requireNotNull(
                            createUnderstanding(context)
                                .understanding,
                        ),
                    state = DecisionState.SELECTED,
                    summary =
                        "A constitutional decision was selected.",
                ),
        )
    }

    private fun createTask(
        context: ContextEnvelope,
    ): TaskAuthorityResult {
        return TaskAuthorityResult.create(
            traceId = context.traceId,
            status = TaskAuthorityStatus.CREATED,
            task =
                TaskRecord.create(
                    taskId = TaskId.from(
                        "task-memory-persistence-authority",
                    ),
                    decision =
                        requireNotNull(
                            createDecision(context)
                                .decision,
                        ),
                    state = TaskState.CREATED,
                    summary =
                        "A bounded constitutional task was created.",
                ),
        )
    }

    private fun createPlanResult(
        context: ContextEnvelope,
    ): PlanAuthorityResult {
        return PlanAuthorityResult.create(
            traceId = context.traceId,
            status = PlanAuthorityStatus.CREATED,
            plan =
                PlanRecord.create(
                    planId = PlanId.from(
                        "plan-memory-persistence-authority",
                    ),
                    task =
                        requireNotNull(
                            createTask(context).task,
                        ),
                    state = PlanState.CREATED,
                    summary =
                        "Use only the constitutionally governed Memory Authority persistence path.",
                ),
        )
    }

    private fun createCapabilityResult(
        context: ContextEnvelope,
    ): CapabilitySelectionResult {
        return CapabilitySelectionResult.create(
            traceId = context.traceId,
            status =
                CapabilitySelectionStatus.SELECTED,
            capability = createCapability(),
        )
    }

    private fun createCapability(): CapabilityContract {
        return CapabilityContract.create(
            capabilityId = CapabilityId.from(
                "capability-memory-persistence-authority",
            ),
            category = CapabilityCategory.KNOWLEDGE,
            name = "Memory Persistence Authority",
            description =
                "Represents one bounded persistence-authority test capability without storage.",
        )
    }

    private fun createReadiness(
        traceId: TraceId,
    ): ExecutiveReadinessResult {
        return ExecutiveReadinessResult.create(
            traceId = traceId,
            status = ExecutiveReadinessStatus.READY,
        )
    }

    private fun createExecution(
        context: ContextEnvelope,
    ): ExecutionResult {
        return ExecutionResult.create(
            traceId = context.traceId,
            status = ExecutionStatus.APPROVED,
            request =
                ExecutionRequest.create(
                    plan =
                        requireNotNull(
                            createPlanResult(context).plan,
                        ),
                    capability =
                        requireNotNull(
                            createCapabilityResult(context)
                                .capability,
                        ),
                ),
        )
    }

    private fun createObservation(
        context: ContextEnvelope,
    ): ObservationResult {
        return ObservationResult.create(
            traceId = context.traceId,
            status = ObservationStatus.OBSERVED,
            request =
                ObservationRequest.create(
                    execution =
                        requireNotNull(
                            createExecution(context)
                                .request,
                        ),
                ),
        )
    }

    private fun createVerification(
        context: ContextEnvelope,
    ): VerificationResult {
        return VerificationResult.create(
            traceId = context.traceId,
            status = VerificationStatus.VERIFIED,
            request =
                VerificationRequest.create(
                    observation =
                        requireNotNull(
                            createObservation(context)
                                .request,
                        ),
                ),
        )
    }

    private fun createOutcome(
        context: ContextEnvelope,
    ): OutcomeResult {
        return OutcomeResult.create(
            traceId = context.traceId,
            status = OutcomeStatus.ESTABLISHED,
            request =
                OutcomeRequest.create(
                    verification =
                        requireNotNull(
                            createVerification(context)
                                .request,
                        ),
                ),
        )
    }

    private fun createWorldModelUpdate(
        context: ContextEnvelope,
    ): WorldModelUpdateResult {
        return WorldModelUpdateResult.create(
            traceId = context.traceId,
            status =
                WorldModelUpdateStatus.APPLICABLE,
            request =
                WorldModelUpdateRequest.create(
                    outcome =
                        requireNotNull(
                            createOutcome(context)
                                .request,
                        ),
                ),
        )
    }

    private fun createLearning(
        context: ContextEnvelope,
    ): LearningResult {
        return LearningResult.create(
            traceId = context.traceId,
            status = LearningStatus.LEARNABLE,
            request =
                LearningRequest.create(
                    worldModelUpdate =
                        requireNotNull(
                            createWorldModelUpdate(context)
                                .request,
                        ),
                ),
        )
    }

    private fun createMemoryProposal(
        context: ContextEnvelope,
    ): MemoryProposalResult {
        return MemoryProposalResult.create(
            traceId = context.traceId,
            status = MemoryProposalStatus.PROPOSABLE,
            request =
                MemoryProposalRequest.create(
                    learning =
                        requireNotNull(
                            createLearning(context).request,
                        ),
                ),
        )
    }

    private fun createMemoryAuthorityResult(
        context: ContextEnvelope,
    ): MemoryAuthorityResult {
        return MemoryAuthorityResult.create(
            traceId = context.traceId,
            status = MemoryAuthorityStatus.COMMITTABLE,
            request =
                MemoryAuthorityRequest.create(
                    proposal =
                        requireNotNull(
                            createMemoryProposal(context)
                                .request,
                        ),
                ),
        )
    }

    private fun createMemoryCommitmentResult(
        context: ContextEnvelope,
    ): MemoryCommitmentResult {
        return MemoryCommitmentResult.create(
            traceId = context.traceId,
            status = MemoryCommitmentStatus.COMMITTABLE,
            request =
                MemoryCommitmentRequest.create(
                    authorityRequest =
                        requireNotNull(
                            createMemoryAuthorityResult(
                                context,
                            ).request,
                        ),
                ),
        )
    }

    private fun createError(
        traceId: TraceId,
        code: String,
    ): UniversalErrorRecord {
        return UniversalErrorRecord.create(
            errorCode = ErrorCode.from(code),
            traceId = traceId,
            occurredAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_000_185_500L,
                ),
            summary =
                "Bounded constitutional memory persistence dependency failed.",
        )
    }

    private fun createContext(
        traceValue: String,
    ): ContextEnvelope {
        return ContextEnvelope.create(
            traceId = TraceId.from(traceValue),
            schemaVersion = SchemaVersion.from(1),
            source = ContextSource.TEST,
            trustLevel = ContextTrustLevel.VERIFIED,
            securityLevel =
                ContextSecurityLevel.RESTRICTED,
            observedAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_000_185_000L,
                ),
        )
    }
}
