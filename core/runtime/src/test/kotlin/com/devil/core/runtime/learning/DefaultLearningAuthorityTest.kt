package com.devil.core.runtime.learning

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

class DefaultLearningAuthorityTest {

    @Test
    fun `evaluate learning defers when learning evidence is unavailable`() {
        val context = createContext(
            "trace-learning-authority-001",
        )

        val result = evaluateLearning(
            authority = DefaultLearningAuthority(),
            context = context,
        )

        assertEquals(context.traceId, result.traceId)
        assertEquals(LearningStatus.DEFERRED, result.status)
        assertNull(result.request)
        assertNull(result.error)
    }

    @Test
    fun `evaluate learning coordinates request evaluator and mapper`() {
        val context = createContext(
            "trace-learning-authority-002",
        )
        val authority = DefaultLearningAuthority(
            evaluator = object : LearningEvaluator {
                override fun evaluate(
                    traceId: TraceId,
                    request: LearningRequest,
                ): LearningEvaluationResult {
                    return LearningEvaluationResult.create(
                        traceId = traceId,
                        status = LearningEvaluationStatus.LEARNABLE,
                        request = request,
                    )
                }
            },
        )

        val result = evaluateLearning(
            authority = authority,
            context = context,
        )

        assertEquals(LearningStatus.LEARNABLE, result.status)
        assertEquals(
            "capability-camera",
            result.request
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
    fun `evaluate learning defers when request is unavailable`() {
        val context = createContext(
            "trace-learning-authority-003",
        )
        val authority = DefaultLearningAuthority(
            requestProvider = object : LearningRequestProvider {
                override fun provide(
                    worldModelUpdate: WorldModelUpdateResult,
                ): LearningRequestResult {
                    return LearningRequestResult.create(
                        traceId = worldModelUpdate.traceId,
                        status = LearningRequestStatus.UNAVAILABLE,
                    )
                }
            },
        )

        val result = evaluateLearning(
            authority = authority,
            context = context,
        )

        assertEquals(LearningStatus.DEFERRED, result.status)
        assertNull(result.request)
        assertNull(result.error)
    }

    @Test
    fun `evaluate learning preserves failed request error`() {
        val context = createContext(
            "trace-learning-authority-004",
        )
        val error = createError(
            traceId = context.traceId,
            code = "LEARNING_REQUEST_FAILED",
        )
        val authority = DefaultLearningAuthority(
            requestProvider = object : LearningRequestProvider {
                override fun provide(
                    worldModelUpdate: WorldModelUpdateResult,
                ): LearningRequestResult {
                    return LearningRequestResult.create(
                        traceId = worldModelUpdate.traceId,
                        status = LearningRequestStatus.FAILED,
                        error = error,
                    )
                }
            },
        )

        val result = evaluateLearning(
            authority = authority,
            context = context,
        )

        assertEquals(LearningStatus.FAILED, result.status)
        assertNull(result.request)
        assertEquals(error, result.error)
    }

    @Test
    fun `evaluate learning preserves failed evaluation error`() {
        val context = createContext(
            "trace-learning-authority-005",
        )
        val error = createError(
            traceId = context.traceId,
            code = "LEARNING_EVALUATION_FAILED",
        )
        val authority = DefaultLearningAuthority(
            evaluator = object : LearningEvaluator {
                override fun evaluate(
                    traceId: TraceId,
                    request: LearningRequest,
                ): LearningEvaluationResult {
                    return LearningEvaluationResult.create(
                        traceId = traceId,
                        status = LearningEvaluationStatus.FAILED,
                        error = error,
                    )
                }
            },
        )

        val result = evaluateLearning(
            authority = authority,
            context = context,
        )

        assertEquals(LearningStatus.FAILED, result.status)
        assertNull(result.request)
        assertEquals(error, result.error)
    }

    @Test
    fun `evaluate learning rejects identity result from another trace`() {
        val context = createContext(
            "trace-learning-authority-006",
        )

        assertFailsWith<IllegalArgumentException> {
            DefaultLearningAuthority().evaluateLearning(
                context = context,
                identity = createIdentity(
                    TraceId.from(
                        "trace-learning-identity-other",
                    ),
                ),
                trust = createTrust(context.traceId),
                authorization = createAuthorization(context.traceId),
                understanding = createUnderstanding(context),
                decision = createDecision(context),
                task = createTask(context),
                plan = createPlanResult(context),
                capability = createCapabilityResult(context),
                readiness = createReadiness(context.traceId),
                execution = createExecution(context),
                observation = createObservation(context),
                verification = createVerification(context),
                outcome = createOutcome(context),
                worldModelUpdate = createWorldModelUpdate(context),
            )
        }
    }

    @Test
    fun `evaluate learning rejects World Model update from another trace`() {
        val context = createContext(
            "trace-learning-authority-007",
        )

        assertFailsWith<IllegalArgumentException> {
            DefaultLearningAuthority().evaluateLearning(
                context = context,
                identity = createIdentity(context.traceId),
                trust = createTrust(context.traceId),
                authorization = createAuthorization(context.traceId),
                understanding = createUnderstanding(context),
                decision = createDecision(context),
                task = createTask(context),
                plan = createPlanResult(context),
                capability = createCapabilityResult(context),
                readiness = createReadiness(context.traceId),
                execution = createExecution(context),
                observation = createObservation(context),
                verification = createVerification(context),
                outcome = createOutcome(context),
                worldModelUpdate = WorldModelUpdateResult.create(
                    traceId = TraceId.from(
                        "trace-learning-world-model-other",
                    ),
                    status = WorldModelUpdateStatus.DEFERRED,
                ),
            )
        }
    }

    @Test
    fun `evaluate learning rejects request result from another trace`() {
        val context = createContext(
            "trace-learning-authority-008",
        )
        val authority = DefaultLearningAuthority(
            requestProvider = object : LearningRequestProvider {
                override fun provide(
                    worldModelUpdate: WorldModelUpdateResult,
                ): LearningRequestResult {
                    return LearningRequestResult.create(
                        traceId = TraceId.from(
                            "trace-learning-request-other",
                        ),
                        status = LearningRequestStatus.UNAVAILABLE,
                    )
                }
            },
        )

        assertFailsWith<IllegalArgumentException> {
            evaluateLearning(
                authority = authority,
                context = context,
            )
        }
    }

    @Test
    fun `evaluate learning rejects evaluation result from another trace`() {
        val context = createContext(
            "trace-learning-authority-009",
        )
        val authority = DefaultLearningAuthority(
            evaluator = object : LearningEvaluator {
                override fun evaluate(
                    traceId: TraceId,
                    request: LearningRequest,
                ): LearningEvaluationResult {
                    return LearningEvaluationResult.create(
                        traceId = TraceId.from(
                            "trace-learning-evaluation-other",
                        ),
                        status = LearningEvaluationStatus.UNAVAILABLE,
                    )
                }
            },
        )

        assertFailsWith<IllegalArgumentException> {
            evaluateLearning(
                authority = authority,
                context = context,
            )
        }
    }

    @Test
    fun `evaluate learning rejects mapped result from another trace`() {
        val context = createContext(
            "trace-learning-authority-010",
        )
        val authority = DefaultLearningAuthority(
            evaluator = object : LearningEvaluator {
                override fun evaluate(
                    traceId: TraceId,
                    request: LearningRequest,
                ): LearningEvaluationResult {
                    return LearningEvaluationResult.create(
                        traceId = traceId,
                        status = LearningEvaluationStatus.LEARNABLE,
                        request = request,
                    )
                }
            },
            resultMapper = object : LearningResultMapper {
                override fun map(
                    traceId: TraceId,
                    evaluation: LearningEvaluationResult,
                ): LearningResult {
                    return LearningResult.create(
                        traceId = TraceId.from(
                            "trace-learning-mapper-other",
                        ),
                        status = LearningStatus.DEFERRED,
                    )
                }
            },
        )

        assertFailsWith<IllegalArgumentException> {
            evaluateLearning(
                authority = authority,
                context = context,
            )
        }
    }

    private fun evaluateLearning(
        authority: LearningAuthority,
        context: ContextEnvelope,
    ): LearningResult {
        return authority.evaluateLearning(
            context = context,
            identity = createIdentity(context.traceId),
            trust = createTrust(context.traceId),
            authorization = createAuthorization(context.traceId),
            understanding = createUnderstanding(context),
            decision = createDecision(context),
            task = createTask(context),
            plan = createPlanResult(context),
            capability = createCapabilityResult(context),
            readiness = createReadiness(context.traceId),
            execution = createExecution(context),
            observation = createObservation(context),
            verification = createVerification(context),
            outcome = createOutcome(context),
            worldModelUpdate = createWorldModelUpdate(context),
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
            status = UnderstandingAuthorityStatus.PRODUCED,
            understanding = UnderstandingRecord.create(
                context = context,
                state = UnderstandingState.COMPLETE,
                summary = "Bounded understanding was produced.",
            ),
        )
    }

    private fun createDecision(
        context: ContextEnvelope,
    ): DecisionAuthorityResult {
        return DecisionAuthorityResult.create(
            traceId = context.traceId,
            status = DecisionAuthorityStatus.PRODUCED,
            decision = DecisionRecord.create(
                understanding = requireNotNull(
                    createUnderstanding(context).understanding,
                ),
                state = DecisionState.SELECTED,
                summary = "A constitutional decision was selected.",
            ),
        )
    }

    private fun createTask(
        context: ContextEnvelope,
    ): TaskAuthorityResult {
        return TaskAuthorityResult.create(
            traceId = context.traceId,
            status = TaskAuthorityStatus.CREATED,
            task = TaskRecord.create(
                taskId = TaskId.from(
                    "task-learning-authority",
                ),
                decision = requireNotNull(
                    createDecision(context).decision,
                ),
                state = TaskState.CREATED,
                summary = "A bounded constitutional task was created.",
            ),
        )
    }

    private fun createPlanResult(
        context: ContextEnvelope,
    ): PlanAuthorityResult {
        return PlanAuthorityResult.create(
            traceId = context.traceId,
            status = PlanAuthorityStatus.CREATED,
            plan = PlanRecord.create(
                planId = PlanId.from(
                    "plan-learning-authority",
                ),
                task = requireNotNull(
                    createTask(context).task,
                ),
                state = PlanState.CREATED,
                summary =
                    "Use the constitutionally approved capability path.",
            ),
        )
    }

    private fun createCapabilityResult(
        context: ContextEnvelope,
    ): CapabilitySelectionResult {
        return CapabilitySelectionResult.create(
            traceId = context.traceId,
            status = CapabilitySelectionStatus.SELECTED,
            capability = createCapability(),
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
            request = ExecutionRequest.create(
                plan = requireNotNull(
                    createPlanResult(context).plan,
                ),
                capability = requireNotNull(
                    createCapabilityResult(context).capability,
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
            request = ObservationRequest.create(
                execution = requireNotNull(
                    createExecution(context).request,
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
            request = VerificationRequest.create(
                observation = requireNotNull(
                    createObservation(context).request,
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
            request = OutcomeRequest.create(
                verification = requireNotNull(
                    createVerification(context).request,
                ),
            ),
        )
    }

    private fun createWorldModelUpdate(
        context: ContextEnvelope,
    ): WorldModelUpdateResult {
        return WorldModelUpdateResult.create(
            traceId = context.traceId,
            status = WorldModelUpdateStatus.APPLICABLE,
            request = WorldModelUpdateRequest.create(
                outcome = requireNotNull(
                    createOutcome(context).request,
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
                    1_754_000_153_500L,
                ),
            summary =
                "Bounded constitutional learning dependency failed.",
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
                    1_754_000_153_000L,
                ),
        )
    }
}
