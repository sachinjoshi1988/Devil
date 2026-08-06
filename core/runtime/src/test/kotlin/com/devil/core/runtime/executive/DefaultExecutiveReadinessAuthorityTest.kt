package com.devil.core.runtime.executive

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
import com.devil.core.model.executive.ExecutiveReadinessRequest
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

class DefaultExecutiveReadinessAuthorityTest {

    @Test
    fun `evaluate defers when readiness policy is unavailable`() {
        val context = createContext(
            "trace-executive-authority-001",
        )

        val result = createEvaluation(
            authority = DefaultExecutiveReadinessAuthority(),
            context = context,
        )

        assertEquals(context.traceId, result.traceId)
        assertEquals(
            ExecutiveReadinessStatus.DEFERRED,
            result.status,
        )
        assertNull(result.error)
    }

    @Test
    fun `evaluate coordinates request evaluator and mapper`() {
        val context = createContext(
            "trace-executive-authority-002",
        )

        val authority = DefaultExecutiveReadinessAuthority(
            evaluator = object : ExecutiveReadinessEvaluator {
                override fun evaluate(
                    traceId: TraceId,
                    request: ExecutiveReadinessRequest,
                ): ExecutiveReadinessEvaluationResult {
                    return ExecutiveReadinessEvaluationResult.create(
                        traceId = traceId,
                        status =
                            ExecutiveReadinessEvaluationStatus.READY,
                        request = request,
                    )
                }
            },
        )

        val result = createEvaluation(
            authority = authority,
            context = context,
        )

        assertEquals(
            ExecutiveReadinessStatus.READY,
            result.status,
        )
        assertNull(result.error)
    }

    @Test
    fun `evaluate defers when readiness request is unavailable`() {
        val context = createContext(
            "trace-executive-authority-003",
        )

        val authority = DefaultExecutiveReadinessAuthority(
            requestProvider =
                object : ExecutiveReadinessRequestProvider {
                    override fun provide(
                        plan: PlanAuthorityResult,
                        capability: CapabilitySelectionResult,
                    ): ExecutiveReadinessRequestResult {
                        return ExecutiveReadinessRequestResult.create(
                            traceId = plan.traceId,
                            status =
                                ExecutiveReadinessRequestStatus.UNAVAILABLE,
                        )
                    }
                },
        )

        val result = createEvaluation(
            authority = authority,
            context = context,
        )

        assertEquals(
            ExecutiveReadinessStatus.DEFERRED,
            result.status,
        )
        assertNull(result.error)
    }

    @Test
    fun `evaluate preserves failed readiness request error`() {
        val context = createContext(
            "trace-executive-authority-004",
        )
        val error = createError(
            traceId = context.traceId,
            code = "EXECUTIVE_READINESS_REQUEST_FAILED",
        )

        val authority = DefaultExecutiveReadinessAuthority(
            requestProvider =
                object : ExecutiveReadinessRequestProvider {
                    override fun provide(
                        plan: PlanAuthorityResult,
                        capability: CapabilitySelectionResult,
                    ): ExecutiveReadinessRequestResult {
                        return ExecutiveReadinessRequestResult.create(
                            traceId = plan.traceId,
                            status =
                                ExecutiveReadinessRequestStatus.FAILED,
                            error = error,
                        )
                    }
                },
        )

        val result = createEvaluation(
            authority = authority,
            context = context,
        )

        assertEquals(
            ExecutiveReadinessStatus.FAILED,
            result.status,
        )
        assertEquals(error, result.error)
    }

    @Test
    fun `evaluate preserves failed readiness evaluation error`() {
        val context = createContext(
            "trace-executive-authority-005",
        )
        val error = createError(
            traceId = context.traceId,
            code = "EXECUTIVE_READINESS_EVALUATION_FAILED",
        )

        val authority = DefaultExecutiveReadinessAuthority(
            evaluator = object : ExecutiveReadinessEvaluator {
                override fun evaluate(
                    traceId: TraceId,
                    request: ExecutiveReadinessRequest,
                ): ExecutiveReadinessEvaluationResult {
                    return ExecutiveReadinessEvaluationResult.create(
                        traceId = traceId,
                        status =
                            ExecutiveReadinessEvaluationStatus.FAILED,
                        error = error,
                    )
                }
            },
        )

        val result = createEvaluation(
            authority = authority,
            context = context,
        )

        assertEquals(
            ExecutiveReadinessStatus.FAILED,
            result.status,
        )
        assertEquals(error, result.error)
    }

    @Test
    fun `evaluate rejects identity result from a different trace`() {
        val context = createContext(
            "trace-executive-authority-006",
        )

        assertFailsWith<IllegalArgumentException> {
            DefaultExecutiveReadinessAuthority().evaluate(
                context = context,
                identity = createIdentity(
                    TraceId.from(
                        "trace-executive-identity-other",
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
            )
        }
    }

    @Test
    fun `evaluate rejects plan result from a different trace`() {
        val context = createContext(
            "trace-executive-authority-007",
        )

        assertFailsWith<IllegalArgumentException> {
            DefaultExecutiveReadinessAuthority().evaluate(
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
                        "trace-executive-plan-other",
                    ),
                    status = PlanAuthorityStatus.DEFERRED,
                ),
                capability = createCapability(context),
            )
        }
    }

    @Test
    fun `evaluate rejects capability result from a different trace`() {
        val context = createContext(
            "trace-executive-authority-008",
        )

        assertFailsWith<IllegalArgumentException> {
            DefaultExecutiveReadinessAuthority().evaluate(
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
                capability = CapabilitySelectionResult.create(
                    traceId = TraceId.from(
                        "trace-executive-capability-other",
                    ),
                    status =
                        CapabilitySelectionStatus.DEFERRED,
                ),
            )
        }
    }

    @Test
    fun `evaluate rejects request result from a different trace`() {
        val context = createContext(
            "trace-executive-authority-009",
        )

        val authority = DefaultExecutiveReadinessAuthority(
            requestProvider =
                object : ExecutiveReadinessRequestProvider {
                    override fun provide(
                        plan: PlanAuthorityResult,
                        capability: CapabilitySelectionResult,
                    ): ExecutiveReadinessRequestResult {
                        return ExecutiveReadinessRequestResult.create(
                            traceId = TraceId.from(
                                "trace-executive-request-other",
                            ),
                            status =
                                ExecutiveReadinessRequestStatus.UNAVAILABLE,
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
            "trace-executive-authority-010",
        )

        val authority = DefaultExecutiveReadinessAuthority(
            evaluator = object : ExecutiveReadinessEvaluator {
                override fun evaluate(
                    traceId: TraceId,
                    request: ExecutiveReadinessRequest,
                ): ExecutiveReadinessEvaluationResult {
                    return ExecutiveReadinessEvaluationResult.create(
                        traceId = TraceId.from(
                            "trace-executive-evaluation-other",
                        ),
                        status =
                            ExecutiveReadinessEvaluationStatus.UNAVAILABLE,
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
            "trace-executive-authority-011",
        )

        val authority = DefaultExecutiveReadinessAuthority(
            evaluator = object : ExecutiveReadinessEvaluator {
                override fun evaluate(
                    traceId: TraceId,
                    request: ExecutiveReadinessRequest,
                ): ExecutiveReadinessEvaluationResult {
                    return ExecutiveReadinessEvaluationResult.create(
                        traceId = traceId,
                        status =
                            ExecutiveReadinessEvaluationStatus.READY,
                        request = request,
                    )
                }
            },
            resultMapper =
                object : ExecutiveReadinessResultMapper {
                    override fun map(
                        traceId: TraceId,
                        evaluation:
                            ExecutiveReadinessEvaluationResult,
                    ): ExecutiveReadinessResult {
                        return ExecutiveReadinessResult.create(
                            traceId = TraceId.from(
                                "trace-executive-mapper-other",
                            ),
                            status =
                                ExecutiveReadinessStatus.DEFERRED,
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
        authority: ExecutiveReadinessAuthority,
        context: ContextEnvelope,
    ): ExecutiveReadinessResult {
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
                    "task-executive-authority",
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
                    "plan-executive-authority",
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

    private fun createError(
        traceId: TraceId,
        code: String,
    ): UniversalErrorRecord {
        return UniversalErrorRecord.create(
            errorCode = ErrorCode.from(code),
            traceId = traceId,
            occurredAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_000_100_500L,
                ),
            summary =
                "Bounded Executive readiness dependency failed.",
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
                    1_754_000_100_000L,
                ),
        )
    }
}
