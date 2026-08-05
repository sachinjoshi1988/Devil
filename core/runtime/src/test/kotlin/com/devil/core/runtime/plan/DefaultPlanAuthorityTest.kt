package com.devil.core.runtime.plan

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
import com.devil.core.model.plan.PlanCreationRequest
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
import com.devil.core.runtime.decision.DecisionAuthorityResult
import com.devil.core.runtime.decision.DecisionAuthorityStatus
import com.devil.core.runtime.identity.IdentityResult
import com.devil.core.runtime.identity.IdentityStatus
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

class DefaultPlanAuthorityTest {

    @Test
    fun `createPlan defers when planning strategy is unavailable`() {
        val context = createContext("trace-plan-default-001")

        val result = DefaultPlanAuthority().createPlan(
            context = context,
            identity = createIdentity(context.traceId),
            trust = createTrust(context.traceId),
            authorization = createAuthorization(context.traceId),
            understanding = createUnderstanding(context),
            decision = createDecision(context),
            task = createTask(context),
        )

        assertEquals(context.traceId, result.traceId)
        assertEquals(PlanAuthorityStatus.DEFERRED, result.status)
        assertNull(result.plan)
        assertNull(result.error)
    }

    @Test
    fun `createPlan coordinates request strategy identity resolver and mapper`() {
        val context = createContext("trace-plan-default-002")
        val planId = PlanId.from("plan-default-authority-002")
        val strategy =
            "Use the constitutionally approved capability path."

        val authority = DefaultPlanAuthority(
            strategyProvider = availableStrategyProvider(strategy),
            planIdentityProvider = availableIdentityProvider(planId),
        )

        val result = authority.createPlan(
            context = context,
            identity = createIdentity(context.traceId),
            trust = createTrust(context.traceId),
            authorization = createAuthorization(context.traceId),
            understanding = createUnderstanding(context),
            decision = createDecision(context),
            task = createTask(context),
        )

        assertEquals(PlanAuthorityStatus.CREATED, result.status)
        assertEquals(planId, result.plan?.planId)
        assertEquals(PlanState.CREATED, result.plan?.state)
        assertEquals(strategy, result.plan?.summary)
        assertEquals(TaskState.CREATED, result.plan?.task?.state)
        assertNull(result.error)
    }

    @Test
    fun `createPlan defers when plan creation request is unavailable`() {
        val context = createContext("trace-plan-default-003")
        val authority = DefaultPlanAuthority(
            requestProvider = object : PlanCreationRequestProvider {
                override fun provide(
                    task: TaskAuthorityResult,
                ): PlanCreationRequestResult {
                    return PlanCreationRequestResult.create(
                        traceId = task.traceId,
                        status = PlanCreationRequestStatus.UNAVAILABLE,
                    )
                }
            },
        )

        val result = createPlan(authority, context)

        assertEquals(PlanAuthorityStatus.DEFERRED, result.status)
        assertNull(result.plan)
        assertNull(result.error)
    }

    @Test
    fun `createPlan preserves failed plan creation request error`() {
        val context = createContext("trace-plan-default-004")
        val error = createError(
            context.traceId,
            "PLAN_CREATION_REQUEST_FAILED",
        )
        val authority = DefaultPlanAuthority(
            requestProvider = object : PlanCreationRequestProvider {
                override fun provide(
                    task: TaskAuthorityResult,
                ): PlanCreationRequestResult {
                    return PlanCreationRequestResult.create(
                        traceId = task.traceId,
                        status = PlanCreationRequestStatus.FAILED,
                        error = error,
                    )
                }
            },
        )

        val result = createPlan(authority, context)

        assertEquals(PlanAuthorityStatus.FAILED, result.status)
        assertNull(result.plan)
        assertEquals(error, result.error)
    }

    @Test
    fun `createPlan preserves failed planning strategy error`() {
        val context = createContext("trace-plan-default-005")
        val error = createError(
            context.traceId,
            "PLANNING_STRATEGY_PROVISION_FAILED",
        )
        val authority = DefaultPlanAuthority(
            strategyProvider = object : PlanningStrategyProvider {
                override fun provide(
                    traceId: TraceId,
                    request: PlanCreationRequest,
                ): PlanningStrategyProvisionResult {
                    return PlanningStrategyProvisionResult.create(
                        traceId = traceId,
                        status = PlanningStrategyProvisionStatus.FAILED,
                        error = error,
                    )
                }
            },
        )

        val result = createPlan(authority, context)

        assertEquals(PlanAuthorityStatus.FAILED, result.status)
        assertNull(result.plan)
        assertEquals(error, result.error)
    }

    @Test
    fun `createPlan defers when plan identity is unavailable`() {
        val context = createContext("trace-plan-default-006")
        val authority = DefaultPlanAuthority(
            strategyProvider =
                availableStrategyProvider(
                    "Use the approved capability path.",
                ),
        )

        val result = createPlan(authority, context)

        assertEquals(PlanAuthorityStatus.DEFERRED, result.status)
        assertNull(result.plan)
        assertNull(result.error)
    }

    @Test
    fun `createPlan preserves failed plan identity error`() {
        val context = createContext("trace-plan-default-007")
        val error = createError(
            context.traceId,
            "PLAN_IDENTITY_PROVISION_FAILED",
        )
        val authority = DefaultPlanAuthority(
            strategyProvider =
                availableStrategyProvider(
                    "Use the approved capability path.",
                ),
            planIdentityProvider = object : PlanIdentityProvider {
                override fun provide(
                    traceId: TraceId,
                    request: PlanCreationRequest,
                ): PlanIdentityProvisionResult {
                    return PlanIdentityProvisionResult.create(
                        traceId = traceId,
                        status = PlanIdentityProvisionStatus.FAILED,
                        error = error,
                    )
                }
            },
        )

        val result = createPlan(authority, context)

        assertEquals(PlanAuthorityStatus.FAILED, result.status)
        assertNull(result.plan)
        assertEquals(error, result.error)
    }

    @Test
    fun `createPlan rejects identity result from a different trace`() {
        val context = createContext("trace-plan-default-008")

        assertFailsWith<IllegalArgumentException> {
            DefaultPlanAuthority().createPlan(
                context = context,
                identity = createIdentity(
                    TraceId.from("trace-plan-identity-other"),
                ),
                trust = createTrust(context.traceId),
                authorization = createAuthorization(context.traceId),
                understanding = createUnderstanding(context),
                decision = createDecision(context),
                task = createTask(context),
            )
        }
    }

    @Test
    fun `createPlan rejects trust result from a different trace`() {
        val context = createContext("trace-plan-default-009")

        assertFailsWith<IllegalArgumentException> {
            DefaultPlanAuthority().createPlan(
                context = context,
                identity = createIdentity(context.traceId),
                trust = createTrust(
                    TraceId.from("trace-plan-trust-other"),
                ),
                authorization = createAuthorization(context.traceId),
                understanding = createUnderstanding(context),
                decision = createDecision(context),
                task = createTask(context),
            )
        }
    }

    @Test
    fun `createPlan rejects authorization result from a different trace`() {
        val context = createContext("trace-plan-default-010")

        assertFailsWith<IllegalArgumentException> {
            DefaultPlanAuthority().createPlan(
                context = context,
                identity = createIdentity(context.traceId),
                trust = createTrust(context.traceId),
                authorization = createAuthorization(
                    TraceId.from("trace-plan-authorization-other"),
                ),
                understanding = createUnderstanding(context),
                decision = createDecision(context),
                task = createTask(context),
            )
        }
    }

    @Test
    fun `createPlan rejects understanding result from a different trace`() {
        val context = createContext("trace-plan-default-011")

        assertFailsWith<IllegalArgumentException> {
            DefaultPlanAuthority().createPlan(
                context = context,
                identity = createIdentity(context.traceId),
                trust = createTrust(context.traceId),
                authorization = createAuthorization(context.traceId),
                understanding =
                    UnderstandingAuthorityResult.create(
                        traceId = TraceId.from(
                            "trace-plan-understanding-other",
                        ),
                        status =
                            UnderstandingAuthorityStatus.DEFERRED,
                    ),
                decision = createDecision(context),
                task = createTask(context),
            )
        }
    }

    @Test
    fun `createPlan rejects decision result from a different trace`() {
        val context = createContext("trace-plan-default-012")

        assertFailsWith<IllegalArgumentException> {
            DefaultPlanAuthority().createPlan(
                context = context,
                identity = createIdentity(context.traceId),
                trust = createTrust(context.traceId),
                authorization = createAuthorization(context.traceId),
                understanding = createUnderstanding(context),
                decision = DecisionAuthorityResult.create(
                    traceId = TraceId.from(
                        "trace-plan-decision-other",
                    ),
                    status = DecisionAuthorityStatus.DEFERRED,
                ),
                task = createTask(context),
            )
        }
    }

    @Test
    fun `createPlan rejects task result from a different trace`() {
        val context = createContext("trace-plan-default-013")

        assertFailsWith<IllegalArgumentException> {
            DefaultPlanAuthority().createPlan(
                context = context,
                identity = createIdentity(context.traceId),
                trust = createTrust(context.traceId),
                authorization = createAuthorization(context.traceId),
                understanding = createUnderstanding(context),
                decision = createDecision(context),
                task = TaskAuthorityResult.create(
                    traceId = TraceId.from("trace-plan-task-other"),
                    status = TaskAuthorityStatus.DEFERRED,
                ),
            )
        }
    }

    @Test
    fun `createPlan rejects request result from a different trace`() {
        val context = createContext("trace-plan-default-014")
        val authority = DefaultPlanAuthority(
            requestProvider = object : PlanCreationRequestProvider {
                override fun provide(
                    task: TaskAuthorityResult,
                ): PlanCreationRequestResult {
                    return PlanCreationRequestResult.create(
                        traceId = TraceId.from(
                            "trace-plan-request-result-other",
                        ),
                        status = PlanCreationRequestStatus.UNAVAILABLE,
                    )
                }
            },
        )

        assertFailsWith<IllegalArgumentException> {
            createPlan(authority, context)
        }
    }

    @Test
    fun `createPlan rejects strategy result from a different trace`() {
        val context = createContext("trace-plan-default-015")
        val authority = DefaultPlanAuthority(
            strategyProvider = object : PlanningStrategyProvider {
                override fun provide(
                    traceId: TraceId,
                    request: PlanCreationRequest,
                ): PlanningStrategyProvisionResult {
                    return PlanningStrategyProvisionResult.create(
                        traceId = TraceId.from(
                            "trace-plan-strategy-result-other",
                        ),
                        status =
                            PlanningStrategyProvisionStatus.UNAVAILABLE,
                    )
                }
            },
        )

        assertFailsWith<IllegalArgumentException> {
            createPlan(authority, context)
        }
    }

    @Test
    fun `createPlan rejects plan identity result from a different trace`() {
        val context = createContext("trace-plan-default-016")
        val authority = DefaultPlanAuthority(
            strategyProvider =
                availableStrategyProvider(
                    "Use the approved capability path.",
                ),
            planIdentityProvider = object : PlanIdentityProvider {
                override fun provide(
                    traceId: TraceId,
                    request: PlanCreationRequest,
                ): PlanIdentityProvisionResult {
                    return PlanIdentityProvisionResult.create(
                        traceId = TraceId.from(
                            "trace-plan-identity-result-other",
                        ),
                        status = PlanIdentityProvisionStatus.UNAVAILABLE,
                    )
                }
            },
        )

        assertFailsWith<IllegalArgumentException> {
            createPlan(authority, context)
        }
    }

    @Test
    fun `createPlan rejects mapped result from a different trace`() {
        val context = createContext("trace-plan-default-017")
        val authority = DefaultPlanAuthority(
            strategyProvider =
                availableStrategyProvider(
                    "Use the approved capability path.",
                ),
            planIdentityProvider =
                availableIdentityProvider(
                    PlanId.from("plan-default-authority-017"),
                ),
            resultMapper = object : PlanCreationResultMapper {
                override fun map(
                    traceId: TraceId,
                    plan: PlanRecord,
                ): PlanAuthorityResult {
                    return PlanAuthorityResult.create(
                        traceId = TraceId.from(
                            "trace-plan-mapper-other",
                        ),
                        status = PlanAuthorityStatus.DEFERRED,
                    )
                }
            },
        )

        assertFailsWith<IllegalArgumentException> {
            createPlan(authority, context)
        }
    }

    private fun availableStrategyProvider(
        strategy: String,
    ): PlanningStrategyProvider {
        return object : PlanningStrategyProvider {
            override fun provide(
                traceId: TraceId,
                request: PlanCreationRequest,
            ): PlanningStrategyProvisionResult {
                return PlanningStrategyProvisionResult.create(
                    traceId = traceId,
                    status = PlanningStrategyProvisionStatus.AVAILABLE,
                    strategy = strategy,
                )
            }
        }
    }

    private fun availableIdentityProvider(
        planId: PlanId,
    ): PlanIdentityProvider {
        return object : PlanIdentityProvider {
            override fun provide(
                traceId: TraceId,
                request: PlanCreationRequest,
            ): PlanIdentityProvisionResult {
                return PlanIdentityProvisionResult.create(
                    traceId = traceId,
                    status = PlanIdentityProvisionStatus.AVAILABLE,
                    planId = planId,
                )
            }
        }
    }

    private fun createPlan(
        authority: PlanAuthority,
        context: ContextEnvelope,
    ): PlanAuthorityResult {
        return authority.createPlan(
            context = context,
            identity = createIdentity(context.traceId),
            trust = createTrust(context.traceId),
            authorization = createAuthorization(context.traceId),
            understanding = createUnderstanding(context),
            decision = createDecision(context),
            task = createTask(context),
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
                understanding = UnderstandingRecord.create(
                    context = context,
                    state = UnderstandingState.COMPLETE,
                    summary = "Bounded understanding was produced.",
                ),
                state = DecisionState.SELECTED,
                summary =
                    "Bounded constitutional decision was selected.",
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
                taskId = TaskId.from("task-plan-authority"),
                decision = requireNotNull(
                    createDecision(context).decision,
                ),
                state = TaskState.CREATED,
                summary = "Bounded constitutional task was created.",
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
                    1_754_000_086_500L,
                ),
            summary =
                "Bounded plan authority dependency failed.",
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
            securityLevel = ContextSecurityLevel.RESTRICTED,
            observedAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_000_086_000L,
                ),
        )
    }
}
