package com.devil.core.runtime.verification

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
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class DefaultVerificationAuthorityTest {

    @Test
    fun `verify defers when verification evidence is unavailable`() {
        val context = createContext(
            "trace-verification-authority-001",
        )

        val result = createVerification(
            authority = DefaultVerificationAuthority(),
            context = context,
        )

        assertEquals(context.traceId, result.traceId)
        assertEquals(VerificationStatus.DEFERRED, result.status)
        assertNull(result.request)
        assertNull(result.error)
    }

    @Test
    fun `verify coordinates request evaluator and mapper`() {
        val context = createContext(
            "trace-verification-authority-002",
        )

        val authority = DefaultVerificationAuthority(
            evaluator = object : VerificationEvaluator {
                override fun evaluate(
                    traceId: TraceId,
                    request: VerificationRequest,
                ): VerificationEvaluationResult {
                    return VerificationEvaluationResult.create(
                        traceId = traceId,
                        status =
                            VerificationEvaluationStatus.VERIFIED,
                        request = request,
                    )
                }
            },
        )

        val result = createVerification(
            authority = authority,
            context = context,
        )

        assertEquals(VerificationStatus.VERIFIED, result.status)
        assertEquals(
            "capability-camera",
            result.request
                ?.observation
                ?.execution
                ?.capability
                ?.capabilityId
                ?.value,
        )
        assertNull(result.error)
    }

    @Test
    fun `verify defers when verification request is unavailable`() {
        val context = createContext(
            "trace-verification-authority-003",
        )

        val authority = DefaultVerificationAuthority(
            requestProvider =
                object : VerificationRequestProvider {
                    override fun provide(
                        observation: ObservationResult,
                    ): VerificationRequestResult {
                        return VerificationRequestResult.create(
                            traceId = observation.traceId,
                            status =
                                VerificationRequestStatus.UNAVAILABLE,
                        )
                    }
                },
        )

        val result = createVerification(
            authority = authority,
            context = context,
        )

        assertEquals(VerificationStatus.DEFERRED, result.status)
        assertNull(result.request)
        assertNull(result.error)
    }

    @Test
    fun `verify preserves failed verification request error`() {
        val context = createContext(
            "trace-verification-authority-004",
        )
        val error = createError(
            traceId = context.traceId,
            code = "VERIFICATION_REQUEST_FAILED",
        )

        val authority = DefaultVerificationAuthority(
            requestProvider =
                object : VerificationRequestProvider {
                    override fun provide(
                        observation: ObservationResult,
                    ): VerificationRequestResult {
                        return VerificationRequestResult.create(
                            traceId = observation.traceId,
                            status = VerificationRequestStatus.FAILED,
                            error = error,
                        )
                    }
                },
        )

        val result = createVerification(
            authority = authority,
            context = context,
        )

        assertEquals(VerificationStatus.FAILED, result.status)
        assertNull(result.request)
        assertEquals(error, result.error)
    }

    @Test
    fun `verify preserves failed verification evaluation error`() {
        val context = createContext(
            "trace-verification-authority-005",
        )
        val error = createError(
            traceId = context.traceId,
            code = "VERIFICATION_EVALUATION_FAILED",
        )

        val authority = DefaultVerificationAuthority(
            evaluator = object : VerificationEvaluator {
                override fun evaluate(
                    traceId: TraceId,
                    request: VerificationRequest,
                ): VerificationEvaluationResult {
                    return VerificationEvaluationResult.create(
                        traceId = traceId,
                        status = VerificationEvaluationStatus.FAILED,
                        error = error,
                    )
                }
            },
        )

        val result = createVerification(
            authority = authority,
            context = context,
        )

        assertEquals(VerificationStatus.FAILED, result.status)
        assertNull(result.request)
        assertEquals(error, result.error)
    }

    @Test
    fun `verify rejects identity result from a different trace`() {
        val context = createContext(
            "trace-verification-authority-006",
        )

        assertFailsWith<IllegalArgumentException> {
            DefaultVerificationAuthority().verify(
                context = context,
                identity = createIdentity(
                    TraceId.from(
                        "trace-verification-identity-other",
                    ),
                ),
                trust = createTrust(context.traceId),
                authorization =
                    createAuthorization(context.traceId),
                understanding = createUnderstanding(context),
                decision = createDecision(context),
                task = createTask(context),
                plan = createPlan(context),
                capability = createCapability(context),
                readiness = createReadiness(context.traceId),
                execution = createExecution(context),
                observation = createObservation(context),
            )
        }
    }

    @Test
    fun `verify rejects observation result from a different trace`() {
        val context = createContext(
            "trace-verification-authority-007",
        )

        assertFailsWith<IllegalArgumentException> {
            DefaultVerificationAuthority().verify(
                context = context,
                identity = createIdentity(context.traceId),
                trust = createTrust(context.traceId),
                authorization =
                    createAuthorization(context.traceId),
                understanding = createUnderstanding(context),
                decision = createDecision(context),
                task = createTask(context),
                plan = createPlan(context),
                capability = createCapability(context),
                readiness = createReadiness(context.traceId),
                execution = createExecution(context),
                observation = ObservationResult.create(
                    traceId = TraceId.from(
                        "trace-verification-observation-other",
                    ),
                    status = ObservationStatus.DEFERRED,
                ),
            )
        }
    }

    @Test
    fun `verify rejects request result from a different trace`() {
        val context = createContext(
            "trace-verification-authority-008",
        )

        val authority = DefaultVerificationAuthority(
            requestProvider =
                object : VerificationRequestProvider {
                    override fun provide(
                        observation: ObservationResult,
                    ): VerificationRequestResult {
                        return VerificationRequestResult.create(
                            traceId = TraceId.from(
                                "trace-verification-request-other",
                            ),
                            status =
                                VerificationRequestStatus.UNAVAILABLE,
                        )
                    }
                },
        )

        assertFailsWith<IllegalArgumentException> {
            createVerification(
                authority = authority,
                context = context,
            )
        }
    }

    @Test
    fun `verify rejects evaluation result from a different trace`() {
        val context = createContext(
            "trace-verification-authority-009",
        )

        val authority = DefaultVerificationAuthority(
            evaluator = object : VerificationEvaluator {
                override fun evaluate(
                    traceId: TraceId,
                    request: VerificationRequest,
                ): VerificationEvaluationResult {
                    return VerificationEvaluationResult.create(
                        traceId = TraceId.from(
                            "trace-verification-evaluation-other",
                        ),
                        status =
                            VerificationEvaluationStatus.UNAVAILABLE,
                    )
                }
            },
        )

        assertFailsWith<IllegalArgumentException> {
            createVerification(
                authority = authority,
                context = context,
            )
        }
    }

    @Test
    fun `verify rejects mapped result from a different trace`() {
        val context = createContext(
            "trace-verification-authority-010",
        )

        val authority = DefaultVerificationAuthority(
            evaluator = object : VerificationEvaluator {
                override fun evaluate(
                    traceId: TraceId,
                    request: VerificationRequest,
                ): VerificationEvaluationResult {
                    return VerificationEvaluationResult.create(
                        traceId = traceId,
                        status =
                            VerificationEvaluationStatus.VERIFIED,
                        request = request,
                    )
                }
            },
            resultMapper = object : VerificationResultMapper {
                override fun map(
                    traceId: TraceId,
                    evaluation: VerificationEvaluationResult,
                ): VerificationResult {
                    return VerificationResult.create(
                        traceId = TraceId.from(
                            "trace-verification-mapper-other",
                        ),
                        status = VerificationStatus.DEFERRED,
                    )
                }
            },
        )

        assertFailsWith<IllegalArgumentException> {
            createVerification(
                authority = authority,
                context = context,
            )
        }
    }

    private fun createVerification(
        authority: VerificationAuthority,
        context: ContextEnvelope,
    ): VerificationResult {
        return authority.verify(
            context = context,
            identity = createIdentity(context.traceId),
            trust = createTrust(context.traceId),
            authorization =
                createAuthorization(context.traceId),
            understanding = createUnderstanding(context),
            decision = createDecision(context),
            task = createTask(context),
            plan = createPlan(context),
            capability = createCapability(context),
            readiness = createReadiness(context.traceId),
            execution = createExecution(context),
            observation = createObservation(context),
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
            task = TaskRecord.create(
                taskId = TaskId.from(
                    "task-verification-authority",
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

    private fun createPlan(
        context: ContextEnvelope,
    ): PlanAuthorityResult {
        return PlanAuthorityResult.create(
            traceId = context.traceId,
            status = PlanAuthorityStatus.CREATED,
            plan = PlanRecord.create(
                planId = PlanId.from(
                    "plan-verification-authority",
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

    private fun createCapability(
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
                    createPlan(context).plan,
                ),
                capability = requireNotNull(
                    createCapability(context).capability,
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

    private fun createError(
        traceId: TraceId,
        code: String,
    ): UniversalErrorRecord {
        return UniversalErrorRecord.create(
            errorCode = ErrorCode.from(code),
            traceId = traceId,
            occurredAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_000_126_500L,
                ),
            summary =
                "Bounded verification dependency failed.",
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
                    1_754_000_126_000L,
                ),
        )
    }
}
