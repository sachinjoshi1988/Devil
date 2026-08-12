package com.devil.core.runtime.observation

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
import com.devil.core.runtime.authorization.AuthorizationResult
import com.devil.core.runtime.authorization.AuthorizationStatus
import com.devil.core.runtime.capability.CapabilitySelectionResult
import com.devil.core.runtime.capability.CapabilitySelectionStatus
import com.devil.core.runtime.decision.DecisionAuthorityResult
import com.devil.core.runtime.decision.DecisionAuthorityStatus
import com.devil.core.runtime.executive.ExecutiveReadinessResult
import com.devil.core.runtime.executive.ExecutiveReadinessStatus
import com.devil.core.runtime.execution.ExecutionAttemptResult
import com.devil.core.runtime.execution.ExecutionAttemptStatus
import com.devil.core.runtime.execution.ExecutionResult
import com.devil.core.runtime.execution.ExecutionStatus
import com.devil.core.runtime.identity.IdentityResult
import com.devil.core.runtime.identity.IdentityStatus
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

class DefaultObservationAuthorityTest {

    @Test
    fun `observe defers when observation evidence is unavailable`() {
        val context = createContext(
            "trace-observation-authority-001",
        )

        val result = createObservation(
            authority = DefaultObservationAuthority(),
            context = context,
        )

        assertEquals(context.traceId, result.traceId)
        assertEquals(ObservationStatus.DEFERRED, result.status)
        assertNull(result.request)
        assertNull(result.error)
    }

    @Test
    fun `observe coordinates request evaluator and mapper`() {
        val context = createContext(
            "trace-observation-authority-002",
        )

        val authority = DefaultObservationAuthority(
            evaluator = object : ObservationEvaluator {
                override fun evaluate(
                    traceId: TraceId,
                    request: ObservationRequest,
                ): ObservationEvaluationResult {
                    return ObservationEvaluationResult.create(
                        traceId = traceId,
                        status = ObservationEvaluationStatus.OBSERVED,
                        request = request,
                    )
                }
            },
        )

        val result = createObservation(
            authority = authority,
            context = context,
        )

        assertEquals(ObservationStatus.OBSERVED, result.status)
        assertEquals(
            "capability-camera",
            result.request
                ?.execution
                ?.capability
                ?.capabilityId
                ?.value,
        )
        assertNull(result.error)
    }

    @Test
    fun `observe defers when observation request is unavailable`() {
        val context = createContext(
            "trace-observation-authority-003",
        )

        val authority = DefaultObservationAuthority(
            requestProvider = object : ObservationRequestProvider {
                override fun provide(
                    executionAttempt: ExecutionAttemptResult,
                ): ObservationRequestResult {
                    return ObservationRequestResult.create(
                        traceId = executionAttempt.traceId,
                        status = ObservationRequestStatus.UNAVAILABLE,
                    )
                }
            },
        )

        val result = createObservation(
            authority = authority,
            context = context,
        )

        assertEquals(ObservationStatus.DEFERRED, result.status)
        assertNull(result.request)
        assertNull(result.error)
    }

    @Test
    fun `observe preserves failed observation request error`() {
        val context = createContext(
            "trace-observation-authority-004",
        )
        val error = createError(
            traceId = context.traceId,
            code = "OBSERVATION_REQUEST_FAILED",
        )

        val authority = DefaultObservationAuthority(
            requestProvider = object : ObservationRequestProvider {
                override fun provide(
                    executionAttempt: ExecutionAttemptResult,
                ): ObservationRequestResult {
                    return ObservationRequestResult.create(
                        traceId = executionAttempt.traceId,
                        status = ObservationRequestStatus.FAILED,
                        error = error,
                    )
                }
            },
        )

        val result = createObservation(
            authority = authority,
            context = context,
        )

        assertEquals(ObservationStatus.FAILED, result.status)
        assertNull(result.request)
        assertEquals(error, result.error)
    }

    @Test
    fun `observe preserves failed observation evaluation error`() {
        val context = createContext(
            "trace-observation-authority-005",
        )
        val error = createError(
            traceId = context.traceId,
            code = "OBSERVATION_EVALUATION_FAILED",
        )

        val authority = DefaultObservationAuthority(
            evaluator = object : ObservationEvaluator {
                override fun evaluate(
                    traceId: TraceId,
                    request: ObservationRequest,
                ): ObservationEvaluationResult {
                    return ObservationEvaluationResult.create(
                        traceId = traceId,
                        status = ObservationEvaluationStatus.FAILED,
                        error = error,
                    )
                }
            },
        )

        val result = createObservation(
            authority = authority,
            context = context,
        )

        assertEquals(ObservationStatus.FAILED, result.status)
        assertNull(result.request)
        assertEquals(error, result.error)
    }

    @Test
    fun `observe rejects identity result from a different trace`() {
        val context = createContext(
            "trace-observation-authority-006",
        )

        assertFailsWith<IllegalArgumentException> {
            DefaultObservationAuthority().observe(
                context = context,
                identity = createIdentity(
                    TraceId.from(
                        "trace-observation-identity-other",
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
                executionAttempt = createExecutionAttempt(context),
            )
        }
    }

    @Test
    fun `observe rejects execution result from a different trace`() {
        val context = createContext(
            "trace-observation-authority-007",
        )

        assertFailsWith<IllegalArgumentException> {
            DefaultObservationAuthority().observe(
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
                execution = ExecutionResult.create(
                    traceId = TraceId.from(
                        "trace-observation-execution-other",
                    ),
                    status = ExecutionStatus.DEFERRED,
                ),
                executionAttempt = createExecutionAttempt(context),
            )
        }
    }

    @Test
    fun `observe rejects request result from a different trace`() {
        val context = createContext(
            "trace-observation-authority-008",
        )

        val authority = DefaultObservationAuthority(
            requestProvider = object : ObservationRequestProvider {
                override fun provide(
                    executionAttempt: ExecutionAttemptResult,
                ): ObservationRequestResult {
                    return ObservationRequestResult.create(
                        traceId = TraceId.from(
                            "trace-observation-request-other",
                        ),
                        status = ObservationRequestStatus.UNAVAILABLE,
                    )
                }
            },
        )

        assertFailsWith<IllegalArgumentException> {
            createObservation(
                authority = authority,
                context = context,
            )
        }
    }

    @Test
    fun `observe rejects evaluation result from a different trace`() {
        val context = createContext(
            "trace-observation-authority-009",
        )

        val authority = DefaultObservationAuthority(
            evaluator = object : ObservationEvaluator {
                override fun evaluate(
                    traceId: TraceId,
                    request: ObservationRequest,
                ): ObservationEvaluationResult {
                    return ObservationEvaluationResult.create(
                        traceId = TraceId.from(
                            "trace-observation-evaluation-other",
                        ),
                        status = ObservationEvaluationStatus.UNAVAILABLE,
                    )
                }
            },
        )

        assertFailsWith<IllegalArgumentException> {
            createObservation(
                authority = authority,
                context = context,
            )
        }
    }

    @Test
    fun `observe rejects mapped result from a different trace`() {
        val context = createContext(
            "trace-observation-authority-010",
        )

        val authority = DefaultObservationAuthority(
            evaluator = object : ObservationEvaluator {
                override fun evaluate(
                    traceId: TraceId,
                    request: ObservationRequest,
                ): ObservationEvaluationResult {
                    return ObservationEvaluationResult.create(
                        traceId = traceId,
                        status = ObservationEvaluationStatus.OBSERVED,
                        request = request,
                    )
                }
            },
            resultMapper = object : ObservationResultMapper {
                override fun map(
                    traceId: TraceId,
                    evaluation: ObservationEvaluationResult,
                ): ObservationResult {
                    return ObservationResult.create(
                        traceId = TraceId.from(
                            "trace-observation-mapper-other",
                        ),
                        status = ObservationStatus.DEFERRED,
                    )
                }
            },
        )

        assertFailsWith<IllegalArgumentException> {
            createObservation(
                authority = authority,
                context = context,
            )
        }
    }

    private fun createObservation(
        authority: ObservationAuthority,
        context: ContextEnvelope,
    ): ObservationResult {
        return authority.observe(
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
            executionAttempt = createExecutionAttempt(context),
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
                    "task-observation-authority",
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
                    "plan-observation-authority",
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

    private fun createExecutionAttempt(
        context: ContextEnvelope,
    ): ExecutionAttemptResult {
        val execution = createExecution(context)

        return ExecutionAttemptResult.create(
            traceId = context.traceId,
            status = ExecutionAttemptStatus.ATTEMPTED,
            request = requireNotNull(execution.request),
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
                    1_754_000_117_500L,
                ),
            summary =
                "Bounded observation dependency failed.",
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
                    1_754_000_117_000L,
                ),
        )
    }
}
