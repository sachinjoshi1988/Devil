package com.devil.core.runtime.worldmodel

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
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class DefaultWorldModelUpdateAuthorityTest {

    @Test
    fun `evaluate update defers when update evidence is unavailable`() {
        val context = createContext(
            "trace-world-model-update-authority-001",
        )

        val result = evaluateUpdate(
            authority = DefaultWorldModelUpdateAuthority(),
            context = context,
        )

        assertEquals(context.traceId, result.traceId)
        assertEquals(WorldModelUpdateStatus.DEFERRED, result.status)
        assertNull(result.request)
        assertNull(result.error)
    }

    @Test
    fun `evaluate update coordinates request evaluator and mapper`() {
        val context = createContext(
            "trace-world-model-update-authority-002",
        )
        val authority = DefaultWorldModelUpdateAuthority(
            evaluator = object : WorldModelUpdateEvaluator {
                override fun evaluate(
                    traceId: TraceId,
                    request: WorldModelUpdateRequest,
                ): WorldModelUpdateEvaluationResult {
                    return WorldModelUpdateEvaluationResult.create(
                        traceId = traceId,
                        status =
                            WorldModelUpdateEvaluationStatus.APPLICABLE,
                        request = request,
                    )
                }
            },
        )

        val result = evaluateUpdate(
            authority = authority,
            context = context,
        )

        assertEquals(
            WorldModelUpdateStatus.APPLICABLE,
            result.status,
        )
        assertEquals(
            "capability-camera",
            result.request
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
    fun `evaluate update defers when request is unavailable`() {
        val context = createContext(
            "trace-world-model-update-authority-003",
        )
        val authority = DefaultWorldModelUpdateAuthority(
            requestProvider =
                object : WorldModelUpdateRequestProvider {
                    override fun provide(
                        outcome: OutcomeResult,
                    ): WorldModelUpdateRequestResult {
                        return WorldModelUpdateRequestResult.create(
                            traceId = outcome.traceId,
                            status =
                                WorldModelUpdateRequestStatus.UNAVAILABLE,
                        )
                    }
                },
        )

        val result = evaluateUpdate(
            authority = authority,
            context = context,
        )

        assertEquals(WorldModelUpdateStatus.DEFERRED, result.status)
        assertNull(result.request)
        assertNull(result.error)
    }

    @Test
    fun `evaluate update preserves failed request error`() {
        val context = createContext(
            "trace-world-model-update-authority-004",
        )
        val error = createError(
            traceId = context.traceId,
            code = "WORLD_MODEL_UPDATE_REQUEST_FAILED",
        )
        val authority = DefaultWorldModelUpdateAuthority(
            requestProvider =
                object : WorldModelUpdateRequestProvider {
                    override fun provide(
                        outcome: OutcomeResult,
                    ): WorldModelUpdateRequestResult {
                        return WorldModelUpdateRequestResult.create(
                            traceId = outcome.traceId,
                            status =
                                WorldModelUpdateRequestStatus.FAILED,
                            error = error,
                        )
                    }
                },
        )

        val result = evaluateUpdate(
            authority = authority,
            context = context,
        )

        assertEquals(WorldModelUpdateStatus.FAILED, result.status)
        assertNull(result.request)
        assertEquals(error, result.error)
    }

    @Test
    fun `evaluate update preserves failed evaluation error`() {
        val context = createContext(
            "trace-world-model-update-authority-005",
        )
        val error = createError(
            traceId = context.traceId,
            code = "WORLD_MODEL_UPDATE_EVALUATION_FAILED",
        )
        val authority = DefaultWorldModelUpdateAuthority(
            evaluator = object : WorldModelUpdateEvaluator {
                override fun evaluate(
                    traceId: TraceId,
                    request: WorldModelUpdateRequest,
                ): WorldModelUpdateEvaluationResult {
                    return WorldModelUpdateEvaluationResult.create(
                        traceId = traceId,
                        status =
                            WorldModelUpdateEvaluationStatus.FAILED,
                        error = error,
                    )
                }
            },
        )

        val result = evaluateUpdate(
            authority = authority,
            context = context,
        )

        assertEquals(WorldModelUpdateStatus.FAILED, result.status)
        assertNull(result.request)
        assertEquals(error, result.error)
    }

    @Test
    fun `evaluate update rejects identity result from another trace`() {
        val context = createContext(
            "trace-world-model-update-authority-006",
        )

        assertFailsWith<IllegalArgumentException> {
            DefaultWorldModelUpdateAuthority().evaluateUpdate(
                context = context,
                identity = createIdentity(
                    TraceId.from(
                        "trace-world-model-update-identity-other",
                    ),
                ),
                trust = createTrust(context.traceId),
                authorization =
                    createAuthorization(context.traceId),
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
            )
        }
    }

    @Test
    fun `evaluate update rejects outcome result from another trace`() {
        val context = createContext(
            "trace-world-model-update-authority-007",
        )

        assertFailsWith<IllegalArgumentException> {
            DefaultWorldModelUpdateAuthority().evaluateUpdate(
                context = context,
                identity = createIdentity(context.traceId),
                trust = createTrust(context.traceId),
                authorization =
                    createAuthorization(context.traceId),
                understanding = createUnderstanding(context),
                decision = createDecision(context),
                task = createTask(context),
                plan = createPlanResult(context),
                capability = createCapabilityResult(context),
                readiness = createReadiness(context.traceId),
                execution = createExecution(context),
                observation = createObservation(context),
                verification = createVerification(context),
                outcome = OutcomeResult.create(
                    traceId = TraceId.from(
                        "trace-world-model-update-outcome-other",
                    ),
                    status = OutcomeStatus.DEFERRED,
                ),
            )
        }
    }

    @Test
    fun `evaluate update rejects request result from another trace`() {
        val context = createContext(
            "trace-world-model-update-authority-008",
        )
        val authority = DefaultWorldModelUpdateAuthority(
            requestProvider =
                object : WorldModelUpdateRequestProvider {
                    override fun provide(
                        outcome: OutcomeResult,
                    ): WorldModelUpdateRequestResult {
                        return WorldModelUpdateRequestResult.create(
                            traceId = TraceId.from(
                                "trace-world-model-update-request-other",
                            ),
                            status =
                                WorldModelUpdateRequestStatus.UNAVAILABLE,
                        )
                    }
                },
        )

        assertFailsWith<IllegalArgumentException> {
            evaluateUpdate(
                authority = authority,
                context = context,
            )
        }
    }

    @Test
    fun `evaluate update rejects evaluation result from another trace`() {
        val context = createContext(
            "trace-world-model-update-authority-009",
        )
        val authority = DefaultWorldModelUpdateAuthority(
            evaluator = object : WorldModelUpdateEvaluator {
                override fun evaluate(
                    traceId: TraceId,
                    request: WorldModelUpdateRequest,
                ): WorldModelUpdateEvaluationResult {
                    return WorldModelUpdateEvaluationResult.create(
                        traceId = TraceId.from(
                            "trace-world-model-update-evaluation-other",
                        ),
                        status =
                            WorldModelUpdateEvaluationStatus.UNAVAILABLE,
                    )
                }
            },
        )

        assertFailsWith<IllegalArgumentException> {
            evaluateUpdate(
                authority = authority,
                context = context,
            )
        }
    }

    @Test
    fun `evaluate update rejects mapped result from another trace`() {
        val context = createContext(
            "trace-world-model-update-authority-010",
        )
        val authority = DefaultWorldModelUpdateAuthority(
            evaluator = object : WorldModelUpdateEvaluator {
                override fun evaluate(
                    traceId: TraceId,
                    request: WorldModelUpdateRequest,
                ): WorldModelUpdateEvaluationResult {
                    return WorldModelUpdateEvaluationResult.create(
                        traceId = traceId,
                        status =
                            WorldModelUpdateEvaluationStatus.APPLICABLE,
                        request = request,
                    )
                }
            },
            resultMapper = object : WorldModelUpdateResultMapper {
                override fun map(
                    traceId: TraceId,
                    evaluation: WorldModelUpdateEvaluationResult,
                ): WorldModelUpdateResult {
                    return WorldModelUpdateResult.create(
                        traceId = TraceId.from(
                            "trace-world-model-update-mapper-other",
                        ),
                        status = WorldModelUpdateStatus.DEFERRED,
                    )
                }
            },
        )

        assertFailsWith<IllegalArgumentException> {
            evaluateUpdate(
                authority = authority,
                context = context,
            )
        }
    }

    private fun evaluateUpdate(
        authority: WorldModelUpdateAuthority,
        context: ContextEnvelope,
    ): WorldModelUpdateResult {
        return authority.evaluateUpdate(
            context = context,
            identity = createIdentity(context.traceId),
            trust = createTrust(context.traceId),
            authorization =
                createAuthorization(context.traceId),
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
            understanding = UnderstandingRecord.create(
                context = context,
                state = UnderstandingState.COMPLETE,
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
            decision = DecisionRecord.create(
                understanding = requireNotNull(
                    createUnderstanding(context).understanding,
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
            task = TaskRecord.create(
                taskId = TaskId.from(
                    "task-world-model-update-authority",
                ),
                decision = requireNotNull(
                    createDecision(context).decision,
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
            plan = PlanRecord.create(
                planId = PlanId.from(
                    "plan-world-model-update-authority",
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
            capability = CapabilityContract.create(
                capabilityId = CapabilityId.from(
                    "capability-camera",
                ),
                category = CapabilityCategory.ACTION,
                name = "Camera",
                description =
                    "Performs one bounded registered camera action.",
            ),
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

    private fun createError(
        traceId: TraceId,
        code: String,
    ): UniversalErrorRecord {
        return UniversalErrorRecord.create(
            errorCode = ErrorCode.from(code),
            traceId = traceId,
            occurredAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_000_144_500L,
                ),
            summary =
                "Bounded World Model update dependency failed.",
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
                    1_754_000_144_000L,
                ),
        )
    }
}
