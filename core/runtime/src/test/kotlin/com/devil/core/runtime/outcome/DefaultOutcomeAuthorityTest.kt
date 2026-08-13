package com.devil.core.runtime.outcome

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

class DefaultOutcomeAuthorityTest {

    @Test
    fun `establish defers when outcome evidence is unavailable`() {
        val context = createContext(
            "trace-outcome-authority-001",
        )

        val result = createOutcome(
            authority = DefaultOutcomeAuthority(),
            context = context,
        )

        assertEquals(context.traceId, result.traceId)
        assertEquals(OutcomeStatus.DEFERRED, result.status)
        assertNull(result.request)
        assertNull(result.error)
    }

    @Test
    fun `establish coordinates request evaluator and mapper`() {
        val context = createContext(
            "trace-outcome-authority-002",
        )

        val authority = DefaultOutcomeAuthority(
            evaluator = object : OutcomeEvaluator {
                override fun evaluate(
                    traceId: TraceId,
                    request: OutcomeRequest,
                    evidence: OutcomeEvidenceResult,
                ): OutcomeEvaluationResult {
                    return OutcomeEvaluationResult.create(
                        traceId = traceId,
                        status = OutcomeEvaluationStatus.ESTABLISHED,
                        request = request,
                    )
                }
            },
        )

        val result = createOutcome(
            authority = authority,
            context = context,
            outcomeEvidence = createEstablishedOutcomeEvidence(context),
        )

        assertEquals(OutcomeStatus.ESTABLISHED, result.status)
        assertEquals(
            "capability-camera",
            result.request
                ?.verification
                ?.observation
                ?.execution
                ?.capability
                ?.capabilityId
                ?.value,
        )
        assertNull(result.error)
    }

    @Test
    fun `establish defers when outcome request is unavailable`() {
        val context = createContext(
            "trace-outcome-authority-003",
        )

        val authority = DefaultOutcomeAuthority(
            requestProvider =
                object : OutcomeRequestProvider {
                    override fun provide(
                        verification: VerificationResult,
                    ): OutcomeRequestResult {
                        return OutcomeRequestResult.create(
                            traceId = verification.traceId,
                            status = OutcomeRequestStatus.UNAVAILABLE,
                        )
                    }
                },
        )

        val result = createOutcome(
            authority = authority,
            context = context,
        )

        assertEquals(OutcomeStatus.DEFERRED, result.status)
        assertNull(result.request)
        assertNull(result.error)
    }

    @Test
    fun `establish preserves failed outcome request error`() {
        val context = createContext(
            "trace-outcome-authority-004",
        )
        val error = createError(
            traceId = context.traceId,
            code = "OUTCOME_REQUEST_FAILED",
        )

        val authority = DefaultOutcomeAuthority(
            requestProvider =
                object : OutcomeRequestProvider {
                    override fun provide(
                        verification: VerificationResult,
                    ): OutcomeRequestResult {
                        return OutcomeRequestResult.create(
                            traceId = verification.traceId,
                            status = OutcomeRequestStatus.FAILED,
                            error = error,
                        )
                    }
                },
        )

        val result = createOutcome(
            authority = authority,
            context = context,
        )

        assertEquals(OutcomeStatus.FAILED, result.status)
        assertNull(result.request)
        assertEquals(error, result.error)
    }

    @Test
    fun `establish preserves failed outcome evaluation error`() {
        val context = createContext(
            "trace-outcome-authority-005",
        )
        val error = createError(
            traceId = context.traceId,
            code = "OUTCOME_EVALUATION_FAILED",
        )

        val authority = DefaultOutcomeAuthority(
            evaluator = object : OutcomeEvaluator {
                override fun evaluate(
                    traceId: TraceId,
                    request: OutcomeRequest,
                    evidence: OutcomeEvidenceResult,
                ): OutcomeEvaluationResult {
                    return OutcomeEvaluationResult.create(
                        traceId = traceId,
                        status = OutcomeEvaluationStatus.FAILED,
                        error = error,
                    )
                }
            },
        )

        val result = createOutcome(
            authority = authority,
            context = context,
            outcomeEvidence = createEstablishedOutcomeEvidence(context),
        )

        assertEquals(OutcomeStatus.FAILED, result.status)
        assertNull(result.request)
        assertEquals(error, result.error)
    }

    @Test
    fun `establish rejects identity result from a different trace`() {
        val context = createContext(
            "trace-outcome-authority-006",
        )

        assertFailsWith<IllegalArgumentException> {
            DefaultOutcomeAuthority().establish(
                context = context,
                identity = createIdentity(
                    TraceId.from(
                        "trace-outcome-identity-other",
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
                outcomeEvidence = createDeferredOutcomeEvidence(context.traceId),
            )
        }
    }

    @Test
    fun `establish rejects verification result from a different trace`() {
        val context = createContext(
            "trace-outcome-authority-007",
        )

        assertFailsWith<IllegalArgumentException> {
            DefaultOutcomeAuthority().establish(
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
                verification = VerificationResult.create(
                    traceId = TraceId.from(
                        "trace-outcome-verification-other",
                    ),
                    status = VerificationStatus.DEFERRED,
                ),
                outcomeEvidence = createDeferredOutcomeEvidence(context.traceId),
            )
        }
    }

    @Test
    fun `establish rejects request result from a different trace`() {
        val context = createContext(
            "trace-outcome-authority-008",
        )

        val authority = DefaultOutcomeAuthority(
            requestProvider =
                object : OutcomeRequestProvider {
                    override fun provide(
                        verification: VerificationResult,
                    ): OutcomeRequestResult {
                        return OutcomeRequestResult.create(
                            traceId = TraceId.from(
                                "trace-outcome-request-other",
                            ),
                            status = OutcomeRequestStatus.UNAVAILABLE,
                        )
                    }
                },
        )

        assertFailsWith<IllegalArgumentException> {
            createOutcome(
                authority = authority,
                context = context,
            )
        }
    }

    @Test
    fun `establish rejects evaluation result from a different trace`() {
        val context = createContext(
            "trace-outcome-authority-009",
        )

        val authority = DefaultOutcomeAuthority(
            evaluator = object : OutcomeEvaluator {
                override fun evaluate(
                    traceId: TraceId,
                    request: OutcomeRequest,
                    evidence: OutcomeEvidenceResult,
                ): OutcomeEvaluationResult {
                    return OutcomeEvaluationResult.create(
                        traceId = TraceId.from(
                            "trace-outcome-evaluation-other",
                        ),
                        status = OutcomeEvaluationStatus.UNAVAILABLE,
                    )
                }
            },
        )

        assertFailsWith<IllegalArgumentException> {
            createOutcome(
                authority = authority,
                context = context,
                outcomeEvidence = createEstablishedOutcomeEvidence(context),
            )
        }
    }

    @Test
    fun `establish rejects mapped result from a different trace`() {
        val context = createContext(
            "trace-outcome-authority-010",
        )

        val authority = DefaultOutcomeAuthority(
            evaluator = object : OutcomeEvaluator {
                override fun evaluate(
                    traceId: TraceId,
                    request: OutcomeRequest,
                    evidence: OutcomeEvidenceResult,
                ): OutcomeEvaluationResult {
                    return OutcomeEvaluationResult.create(
                        traceId = traceId,
                        status = OutcomeEvaluationStatus.ESTABLISHED,
                        request = request,
                    )
                }
            },
            resultMapper = object : OutcomeResultMapper {
                override fun map(
                    traceId: TraceId,
                    evaluation: OutcomeEvaluationResult,
                ): OutcomeResult {
                    return OutcomeResult.create(
                        traceId = TraceId.from(
                            "trace-outcome-mapper-other",
                        ),
                        status = OutcomeStatus.DEFERRED,
                    )
                }
            },
        )

        assertFailsWith<IllegalArgumentException> {
            createOutcome(
                authority = authority,
                context = context,
                outcomeEvidence = createEstablishedOutcomeEvidence(context),
            )
        }
    }

    private fun createOutcome(
        authority: OutcomeAuthority,
        context: ContextEnvelope,
        outcomeEvidence: OutcomeEvidenceResult =
            createDeferredOutcomeEvidence(context.traceId),
    ): OutcomeResult {
        return authority.establish(
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
            outcomeEvidence = outcomeEvidence,
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
                    "task-outcome-authority",
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
                    "plan-outcome-authority",
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

    private fun createEstablishedOutcomeEvidence(
        context: ContextEnvelope,
    ): OutcomeEvidenceResult {
        return OutcomeEvidenceResult.create(
            traceId = context.traceId,
            status = OutcomeEvidenceStatus.ESTABLISHED,
            capabilityId = CapabilityId.from(
                "capability-camera",
            ),
            description =
                "Bounded constitutional outcome evidence was independently established.",
        )
    }

    private fun createDeferredOutcomeEvidence(
        traceId: TraceId,
    ): OutcomeEvidenceResult {
        return OutcomeEvidenceResult.create(
            traceId = traceId,
            status = OutcomeEvidenceStatus.DEFERRED,
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
                    1_754_000_135_500L,
                ),
            summary =
                "Bounded outcome dependency failed.",
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
                    1_754_000_135_000L,
                ),
        )
    }
}
