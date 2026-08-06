package com.devil.core.runtime.execution

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

class DefaultExecutionAuthorityTest {

    @Test
    fun `evaluate defers when execution policy is unavailable`() {
        val context = createContext(
            "trace-execution-authority-001",
        )

        val result = createEvaluation(
            authority = DefaultExecutionAuthority(),
            context = context,
        )

        assertEquals(context.traceId, result.traceId)
        assertEquals(ExecutionStatus.DEFERRED, result.status)
        assertNull(result.request)
        assertNull(result.error)
    }

    @Test
    fun `evaluate coordinates request evaluator and mapper`() {
        val context = createContext(
            "trace-execution-authority-002",
        )

        val authority = DefaultExecutionAuthority(
            evaluator = object : ExecutionEvaluator {
                override fun evaluate(
                    traceId: TraceId,
                    request: ExecutionRequest,
                ): ExecutionEvaluationResult {
                    return ExecutionEvaluationResult.create(
                        traceId = traceId,
                        status = ExecutionEvaluationStatus.APPROVED,
                        request = request,
                    )
                }
            },
        )

        val result = createEvaluation(
            authority = authority,
            context = context,
        )

        assertEquals(ExecutionStatus.APPROVED, result.status)
        assertEquals(
            "capability-camera",
            result.request?.capability?.capabilityId?.value,
        )
        assertNull(result.error)
    }

    @Test
    fun `evaluate defers when execution request is unavailable`() {
        val context = createContext(
            "trace-execution-authority-003",
        )

        val authority = DefaultExecutionAuthority(
            requestProvider = object : ExecutionRequestProvider {
                override fun provide(
                    plan: PlanAuthorityResult,
                    capability: CapabilitySelectionResult,
                    readiness: ExecutiveReadinessResult,
                ): ExecutionRequestResult {
                    return ExecutionRequestResult.create(
                        traceId = plan.traceId,
                        status = ExecutionRequestStatus.UNAVAILABLE,
                    )
                }
            },
        )

        val result = createEvaluation(
            authority = authority,
            context = context,
        )

        assertEquals(ExecutionStatus.DEFERRED, result.status)
        assertNull(result.request)
        assertNull(result.error)
    }

    @Test
    fun `evaluate preserves failed execution request error`() {
        val context = createContext(
            "trace-execution-authority-004",
        )
        val error = createError(
            traceId = context.traceId,
            code = "EXECUTION_REQUEST_FAILED",
        )

        val authority = DefaultExecutionAuthority(
            requestProvider = object : ExecutionRequestProvider {
                override fun provide(
                    plan: PlanAuthorityResult,
                    capability: CapabilitySelectionResult,
                    readiness: ExecutiveReadinessResult,
                ): ExecutionRequestResult {
                    return ExecutionRequestResult.create(
                        traceId = plan.traceId,
                        status = ExecutionRequestStatus.FAILED,
                        error = error,
                    )
                }
            },
        )

        val result = createEvaluation(
            authority = authority,
            context = context,
        )

        assertEquals(ExecutionStatus.FAILED, result.status)
        assertNull(result.request)
        assertEquals(error, result.error)
    }

    @Test
    fun `evaluate preserves failed execution evaluation error`() {
        val context = createContext(
            "trace-execution-authority-005",
        )
        val error = createError(
            traceId = context.traceId,
            code = "EXECUTION_EVALUATION_FAILED",
        )

        val authority = DefaultExecutionAuthority(
            evaluator = object : ExecutionEvaluator {
                override fun evaluate(
                    traceId: TraceId,
                    request: ExecutionRequest,
                ): ExecutionEvaluationResult {
                    return ExecutionEvaluationResult.create(
                        traceId = traceId,
                        status = ExecutionEvaluationStatus.FAILED,
                        error = error,
                    )
                }
            },
        )

        val result = createEvaluation(
            authority = authority,
            context = context,
        )

        assertEquals(ExecutionStatus.FAILED, result.status)
        assertNull(result.request)
        assertEquals(error, result.error)
    }

    @Test
    fun `evaluate rejects identity result from a different trace`() {
        val context = createContext(
            "trace-execution-authority-006",
        )

        assertFailsWith<IllegalArgumentException> {
            DefaultExecutionAuthority().evaluate(
                context = context,
                identity = createIdentity(
                    TraceId.from(
                        "trace-execution-identity-other",
                    ),
                ),
                trust = createTrust(context.traceId),
                authorization =
                    createAuthorization(context.traceId),
                understanding =
                    createUnderstanding(context),
                decision = createDecision(context),
                task = createTask(context),
                plan = createPlan(context),
                capability = createCapability(context),
                readiness = createReadiness(context.traceId),
            )
        }
    }

    @Test
    fun `evaluate rejects plan result from a different trace`() {
        val context = createContext(
            "trace-execution-authority-007",
        )

        assertFailsWith<IllegalArgumentException> {
            DefaultExecutionAuthority().evaluate(
                context = context,
                identity = createIdentity(context.traceId),
                trust = createTrust(context.traceId),
                authorization =
                    createAuthorization(context.traceId),
                understanding =
                    createUnderstanding(context),
                decision = createDecision(context),
                task = createTask(context),
                plan = PlanAuthorityResult.create(
                    traceId = TraceId.from(
                        "trace-execution-plan-other",
                    ),
                    status = PlanAuthorityStatus.DEFERRED,
                ),
                capability = createCapability(context),
                readiness = createReadiness(context.traceId),
            )
        }
    }

    @Test
    fun `evaluate rejects readiness result from a different trace`() {
        val context = createContext(
            "trace-execution-authority-008",
        )

        assertFailsWith<IllegalArgumentException> {
            DefaultExecutionAuthority().evaluate(
                context = context,
                identity = createIdentity(context.traceId),
                trust = createTrust(context.traceId),
                authorization =
                    createAuthorization(context.traceId),
                understanding =
                    createUnderstanding(context),
                decision = createDecision(context),
                task = createTask(context),
                plan = createPlan(context),
                capability = createCapability(context),
                readiness = ExecutiveReadinessResult.create(
                    traceId = TraceId.from(
                        "trace-execution-readiness-other",
                    ),
                    status = ExecutiveReadinessStatus.DEFERRED,
                ),
            )
        }
    }

    @Test
    fun `evaluate rejects request result from a different trace`() {
        val context = createContext(
            "trace-execution-authority-009",
        )

        val authority = DefaultExecutionAuthority(
            requestProvider = object : ExecutionRequestProvider {
                override fun provide(
                    plan: PlanAuthorityResult,
                    capability: CapabilitySelectionResult,
                    readiness: ExecutiveReadinessResult,
                ): ExecutionRequestResult {
                    return ExecutionRequestResult.create(
                        traceId = TraceId.from(
                            "trace-execution-request-other",
                        ),
                        status = ExecutionRequestStatus.UNAVAILABLE,
                    )
                }
            },
        )

        assertFailsWith<IllegalArgumentException> {
            createEvaluation(
                authority = authority,
                context = context,
            )
        }
    }

    @Test
    fun `evaluate rejects evaluation result from a different trace`() {
        val context = createContext(
            "trace-execution-authority-010",
        )

        val authority = DefaultExecutionAuthority(
            evaluator = object : ExecutionEvaluator {
                override fun evaluate(
                    traceId: TraceId,
                    request: ExecutionRequest,
                ): ExecutionEvaluationResult {
                    return ExecutionEvaluationResult.create(
                        traceId = TraceId.from(
                            "trace-execution-evaluation-other",
                        ),
                        status = ExecutionEvaluationStatus.UNAVAILABLE,
                    )
                }
            },
        )

        assertFailsWith<IllegalArgumentException> {
            createEvaluation(
                authority = authority,
                context = context,
            )
        }
    }

    @Test
    fun `evaluate rejects mapped result from a different trace`() {
        val context = createContext(
            "trace-execution-authority-011",
        )

        val authority = DefaultExecutionAuthority(
            evaluator = object : ExecutionEvaluator {
                override fun evaluate(
                    traceId: TraceId,
                    request: ExecutionRequest,
                ): ExecutionEvaluationResult {
                    return ExecutionEvaluationResult.create(
                        traceId = traceId,
                        status = ExecutionEvaluationStatus.APPROVED,
                        request = request,
                    )
                }
            },
            resultMapper = object : ExecutionResultMapper {
                override fun map(
                    traceId: TraceId,
                    evaluation: ExecutionEvaluationResult,
                ): ExecutionResult {
                    return ExecutionResult.create(
                        traceId = TraceId.from(
                            "trace-execution-mapper-other",
                        ),
                        status = ExecutionStatus.DEFERRED,
                    )
                }
            },
        )

        assertFailsWith<IllegalArgumentException> {
            createEvaluation(
                authority = authority,
                context = context,
            )
        }
    }

    private fun createEvaluation(
        authority: ExecutionAuthority,
        context: ContextEnvelope,
    ): ExecutionResult {
        return authority.evaluate(
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
                    "task-execution-authority",
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
                    "plan-execution-authority",
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

    private fun createError(
        traceId: TraceId,
        code: String,
    ): UniversalErrorRecord {
        return UniversalErrorRecord.create(
            errorCode = ErrorCode.from(code),
            traceId = traceId,
            occurredAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_000_108_500L,
                ),
            summary =
                "Bounded execution dependency failed.",
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
                    1_754_000_108_000L,
                ),
        )
    }
}
