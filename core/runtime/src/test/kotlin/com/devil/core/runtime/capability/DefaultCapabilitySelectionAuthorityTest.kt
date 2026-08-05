package com.devil.core.runtime.capability

import com.devil.core.model.capability.CapabilityCategory
import com.devil.core.model.capability.CapabilityContract
import com.devil.core.model.capability.CapabilityId
import com.devil.core.model.capability.CapabilitySelectionRequest
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

class DefaultCapabilitySelectionAuthorityTest {

    @Test
    fun `select defers when capability registry is unavailable`() {
        val context = createContext(
            "trace-capability-default-001",
        )

        val result = createSelection(
            authority = DefaultCapabilitySelectionAuthority(),
            context = context,
        )

        assertEquals(context.traceId, result.traceId)
        assertEquals(
            CapabilitySelectionStatus.DEFERRED,
            result.status,
        )
        assertNull(result.capability)
        assertNull(result.error)
    }

    @Test
    fun `select coordinates request registry resolver and mapper`() {
        val context = createContext(
            "trace-capability-default-002",
        )
        val capability = createCapability()

        val authority = DefaultCapabilitySelectionAuthority(
            registry = object : CapabilityRegistry {
                override fun obtain(
                    traceId: TraceId,
                    request: CapabilitySelectionRequest,
                ): CapabilityRegistryResult {
                    return CapabilityRegistryResult.create(
                        traceId = traceId,
                        status = CapabilityRegistryStatus.AVAILABLE,
                        capabilities = listOf(capability),
                    )
                }
            },
            resolver = object : CapabilitySelectionResolver {
                override fun resolve(
                    traceId: TraceId,
                    request: CapabilitySelectionRequest,
                    registry: CapabilityRegistryResult,
                ): CapabilitySelectionResolutionResult {
                    return CapabilitySelectionResolutionResult.create(
                        traceId = traceId,
                        status =
                            CapabilitySelectionResolutionStatus.RESOLVED,
                        capability = capability,
                    )
                }
            },
        )

        val result = createSelection(
            authority = authority,
            context = context,
        )

        assertEquals(
            CapabilitySelectionStatus.SELECTED,
            result.status,
        )
        assertEquals(capability, result.capability)
        assertNull(result.error)
    }

    @Test
    fun `select defers when capability selection request is unavailable`() {
        val context = createContext(
            "trace-capability-default-003",
        )
        val authority = DefaultCapabilitySelectionAuthority(
            requestProvider =
                object : CapabilitySelectionRequestProvider {
                    override fun provide(
                        plan: PlanAuthorityResult,
                    ): CapabilitySelectionRequestResult {
                        return CapabilitySelectionRequestResult.create(
                            traceId = plan.traceId,
                            status =
                                CapabilitySelectionRequestStatus.UNAVAILABLE,
                        )
                    }
                },
        )

        val result = createSelection(
            authority = authority,
            context = context,
        )

        assertEquals(
            CapabilitySelectionStatus.DEFERRED,
            result.status,
        )
        assertNull(result.capability)
        assertNull(result.error)
    }

    @Test
    fun `select preserves failed request error`() {
        val context = createContext(
            "trace-capability-default-004",
        )
        val error = createError(
            traceId = context.traceId,
            code = "CAPABILITY_SELECTION_REQUEST_FAILED",
        )
        val authority = DefaultCapabilitySelectionAuthority(
            requestProvider =
                object : CapabilitySelectionRequestProvider {
                    override fun provide(
                        plan: PlanAuthorityResult,
                    ): CapabilitySelectionRequestResult {
                        return CapabilitySelectionRequestResult.create(
                            traceId = plan.traceId,
                            status =
                                CapabilitySelectionRequestStatus.FAILED,
                            error = error,
                        )
                    }
                },
        )

        val result = createSelection(
            authority = authority,
            context = context,
        )

        assertEquals(
            CapabilitySelectionStatus.FAILED,
            result.status,
        )
        assertNull(result.capability)
        assertEquals(error, result.error)
    }

    @Test
    fun `select preserves failed registry error`() {
        val context = createContext(
            "trace-capability-default-005",
        )
        val error = createError(
            traceId = context.traceId,
            code = "CAPABILITY_REGISTRY_FAILED",
        )
        val authority = DefaultCapabilitySelectionAuthority(
            registry = object : CapabilityRegistry {
                override fun obtain(
                    traceId: TraceId,
                    request: CapabilitySelectionRequest,
                ): CapabilityRegistryResult {
                    return CapabilityRegistryResult.create(
                        traceId = traceId,
                        status = CapabilityRegistryStatus.FAILED,
                        error = error,
                    )
                }
            },
        )

        val result = createSelection(
            authority = authority,
            context = context,
        )

        assertEquals(
            CapabilitySelectionStatus.FAILED,
            result.status,
        )
        assertNull(result.capability)
        assertEquals(error, result.error)
    }

    @Test
    fun `select preserves failed resolution error`() {
        val context = createContext(
            "trace-capability-default-006",
        )
        val error = createError(
            traceId = context.traceId,
            code = "CAPABILITY_SELECTION_RESOLUTION_FAILED",
        )
        val capability = createCapability()

        val authority = DefaultCapabilitySelectionAuthority(
            registry = availableRegistry(capability),
            resolver = object : CapabilitySelectionResolver {
                override fun resolve(
                    traceId: TraceId,
                    request: CapabilitySelectionRequest,
                    registry: CapabilityRegistryResult,
                ): CapabilitySelectionResolutionResult {
                    return CapabilitySelectionResolutionResult.create(
                        traceId = traceId,
                        status =
                            CapabilitySelectionResolutionStatus.FAILED,
                        error = error,
                    )
                }
            },
        )

        val result = createSelection(
            authority = authority,
            context = context,
        )

        assertEquals(
            CapabilitySelectionStatus.FAILED,
            result.status,
        )
        assertNull(result.capability)
        assertEquals(error, result.error)
    }

    @Test
    fun `select rejects identity result from a different trace`() {
        val context = createContext(
            "trace-capability-default-007",
        )

        assertFailsWith<IllegalArgumentException> {
            DefaultCapabilitySelectionAuthority().select(
                context = context,
                identity = createIdentity(
                    TraceId.from(
                        "trace-capability-identity-other",
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
            )
        }
    }

    @Test
    fun `select rejects plan result from a different trace`() {
        val context = createContext(
            "trace-capability-default-008",
        )

        assertFailsWith<IllegalArgumentException> {
            DefaultCapabilitySelectionAuthority().select(
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
                        "trace-capability-plan-other",
                    ),
                    status = PlanAuthorityStatus.DEFERRED,
                ),
            )
        }
    }

    @Test
    fun `select rejects request result from a different trace`() {
        val context = createContext(
            "trace-capability-default-009",
        )
        val authority = DefaultCapabilitySelectionAuthority(
            requestProvider =
                object : CapabilitySelectionRequestProvider {
                    override fun provide(
                        plan: PlanAuthorityResult,
                    ): CapabilitySelectionRequestResult {
                        return CapabilitySelectionRequestResult.create(
                            traceId = TraceId.from(
                                "trace-capability-request-other",
                            ),
                            status =
                                CapabilitySelectionRequestStatus.UNAVAILABLE,
                        )
                    }
                },
        )

        assertFailsWith<IllegalArgumentException> {
            createSelection(
                authority = authority,
                context = context,
            )
        }
    }

    @Test
    fun `select rejects registry result from a different trace`() {
        val context = createContext(
            "trace-capability-default-010",
        )
        val authority = DefaultCapabilitySelectionAuthority(
            registry = object : CapabilityRegistry {
                override fun obtain(
                    traceId: TraceId,
                    request: CapabilitySelectionRequest,
                ): CapabilityRegistryResult {
                    return CapabilityRegistryResult.create(
                        traceId = TraceId.from(
                            "trace-capability-registry-other",
                        ),
                        status =
                            CapabilityRegistryStatus.UNAVAILABLE,
                    )
                }
            },
        )

        assertFailsWith<IllegalArgumentException> {
            createSelection(
                authority = authority,
                context = context,
            )
        }
    }

    @Test
    fun `select rejects resolution result from a different trace`() {
        val context = createContext(
            "trace-capability-default-011",
        )
        val capability = createCapability()
        val authority = DefaultCapabilitySelectionAuthority(
            registry = availableRegistry(capability),
            resolver = object : CapabilitySelectionResolver {
                override fun resolve(
                    traceId: TraceId,
                    request: CapabilitySelectionRequest,
                    registry: CapabilityRegistryResult,
                ): CapabilitySelectionResolutionResult {
                    return CapabilitySelectionResolutionResult.create(
                        traceId = TraceId.from(
                            "trace-capability-resolution-other",
                        ),
                        status =
                            CapabilitySelectionResolutionStatus.UNAVAILABLE,
                    )
                }
            },
        )

        assertFailsWith<IllegalArgumentException> {
            createSelection(
                authority = authority,
                context = context,
            )
        }
    }

    @Test
    fun `select rejects mapped result from a different trace`() {
        val context = createContext(
            "trace-capability-default-012",
        )
        val capability = createCapability()
        val authority = DefaultCapabilitySelectionAuthority(
            registry = availableRegistry(capability),
            resolver = resolvedResolver(capability),
            resultMapper =
                object : CapabilitySelectionResultMapper {
                    override fun map(
                        traceId: TraceId,
                        resolution:
                            CapabilitySelectionResolutionResult,
                    ): CapabilitySelectionResult {
                        return CapabilitySelectionResult.create(
                            traceId = TraceId.from(
                                "trace-capability-mapper-other",
                            ),
                            status =
                                CapabilitySelectionStatus.DEFERRED,
                        )
                    }
                },
        )

        assertFailsWith<IllegalArgumentException> {
            createSelection(
                authority = authority,
                context = context,
            )
        }
    }

    private fun availableRegistry(
        capability: CapabilityContract,
    ): CapabilityRegistry {
        return object : CapabilityRegistry {
            override fun obtain(
                traceId: TraceId,
                request: CapabilitySelectionRequest,
            ): CapabilityRegistryResult {
                return CapabilityRegistryResult.create(
                    traceId = traceId,
                    status = CapabilityRegistryStatus.AVAILABLE,
                    capabilities = listOf(capability),
                )
            }
        }
    }

    private fun resolvedResolver(
        capability: CapabilityContract,
    ): CapabilitySelectionResolver {
        return object : CapabilitySelectionResolver {
            override fun resolve(
                traceId: TraceId,
                request: CapabilitySelectionRequest,
                registry: CapabilityRegistryResult,
            ): CapabilitySelectionResolutionResult {
                return CapabilitySelectionResolutionResult.create(
                    traceId = traceId,
                    status =
                        CapabilitySelectionResolutionStatus.RESOLVED,
                    capability = capability,
                )
            }
        }
    }

    private fun createSelection(
        authority: CapabilitySelectionAuthority,
        context: ContextEnvelope,
    ): CapabilitySelectionResult {
        return authority.select(
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
                    "task-capability-authority",
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
                    "plan-capability-authority",
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

    private fun createError(
        traceId: TraceId,
        code: String,
    ): UniversalErrorRecord {
        return UniversalErrorRecord.create(
            errorCode = ErrorCode.from(code),
            traceId = traceId,
            occurredAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_000_093_500L,
                ),
            summary =
                "Bounded capability selection dependency failed.",
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
                    1_754_000_093_000L,
                ),
        )
    }
}
