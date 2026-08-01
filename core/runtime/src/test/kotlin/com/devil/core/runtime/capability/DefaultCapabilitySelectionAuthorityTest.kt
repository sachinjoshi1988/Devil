package com.devil.core.runtime.capability

import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.common.SchemaVersion
import com.devil.core.model.common.TraceId
import com.devil.core.model.context.ContextEnvelope
import com.devil.core.model.context.ContextSecurityLevel
import com.devil.core.model.context.ContextSource
import com.devil.core.model.context.ContextTrustLevel
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
    fun `select defers without inventing a capability`() {
        val context = createContext("trace-capability-default-001")
        val authority: CapabilitySelectionAuthority =
            DefaultCapabilitySelectionAuthority()

        val result = authority.select(
            context = context,
            identity = createIdentity(context.traceId),
            trust = createTrust(context.traceId),
            authorization = createAuthorization(context.traceId),
            understanding = createUnderstanding(context.traceId),
            decision = createDecision(context.traceId),
            task = createTask(context.traceId),
            plan = createPlan(context.traceId),
        )

        assertEquals(context.traceId, result.traceId)
        assertEquals(CapabilitySelectionStatus.DEFERRED, result.status)
        assertNull(result.capability)
        assertNull(result.error)
    }

    @Test
    fun `select rejects identity result from a different trace`() {
        val context = createContext("trace-capability-default-002")
        val authority: CapabilitySelectionAuthority =
            DefaultCapabilitySelectionAuthority()

        assertFailsWith<IllegalArgumentException> {
            authority.select(
                context = context,
                identity = createIdentity(
                    TraceId.from("trace-capability-identity-other"),
                ),
                trust = createTrust(context.traceId),
                authorization = createAuthorization(context.traceId),
                understanding = createUnderstanding(context.traceId),
                decision = createDecision(context.traceId),
                task = createTask(context.traceId),
                plan = createPlan(context.traceId),
            )
        }
    }

    @Test
    fun `select rejects trust result from a different trace`() {
        val context = createContext("trace-capability-default-003")
        val authority: CapabilitySelectionAuthority =
            DefaultCapabilitySelectionAuthority()

        assertFailsWith<IllegalArgumentException> {
            authority.select(
                context = context,
                identity = createIdentity(context.traceId),
                trust = createTrust(
                    TraceId.from("trace-capability-trust-other"),
                ),
                authorization = createAuthorization(context.traceId),
                understanding = createUnderstanding(context.traceId),
                decision = createDecision(context.traceId),
                task = createTask(context.traceId),
                plan = createPlan(context.traceId),
            )
        }
    }

    @Test
    fun `select rejects authorization result from a different trace`() {
        val context = createContext("trace-capability-default-004")
        val authority: CapabilitySelectionAuthority =
            DefaultCapabilitySelectionAuthority()

        assertFailsWith<IllegalArgumentException> {
            authority.select(
                context = context,
                identity = createIdentity(context.traceId),
                trust = createTrust(context.traceId),
                authorization = createAuthorization(
                    TraceId.from("trace-capability-authorization-other"),
                ),
                understanding = createUnderstanding(context.traceId),
                decision = createDecision(context.traceId),
                task = createTask(context.traceId),
                plan = createPlan(context.traceId),
            )
        }
    }

    @Test
    fun `select rejects understanding result from a different trace`() {
        val context = createContext("trace-capability-default-005")
        val authority: CapabilitySelectionAuthority =
            DefaultCapabilitySelectionAuthority()

        assertFailsWith<IllegalArgumentException> {
            authority.select(
                context = context,
                identity = createIdentity(context.traceId),
                trust = createTrust(context.traceId),
                authorization = createAuthorization(context.traceId),
                understanding = createUnderstanding(
                    TraceId.from("trace-capability-understanding-other"),
                ),
                decision = createDecision(context.traceId),
                task = createTask(context.traceId),
                plan = createPlan(context.traceId),
            )
        }
    }

    @Test
    fun `select rejects decision result from a different trace`() {
        val context = createContext("trace-capability-default-006")
        val authority: CapabilitySelectionAuthority =
            DefaultCapabilitySelectionAuthority()

        assertFailsWith<IllegalArgumentException> {
            authority.select(
                context = context,
                identity = createIdentity(context.traceId),
                trust = createTrust(context.traceId),
                authorization = createAuthorization(context.traceId),
                understanding = createUnderstanding(context.traceId),
                decision = createDecision(
                    TraceId.from("trace-capability-decision-other"),
                ),
                task = createTask(context.traceId),
                plan = createPlan(context.traceId),
            )
        }
    }

    @Test
    fun `select rejects task result from a different trace`() {
        val context = createContext("trace-capability-default-007")
        val authority: CapabilitySelectionAuthority =
            DefaultCapabilitySelectionAuthority()

        assertFailsWith<IllegalArgumentException> {
            authority.select(
                context = context,
                identity = createIdentity(context.traceId),
                trust = createTrust(context.traceId),
                authorization = createAuthorization(context.traceId),
                understanding = createUnderstanding(context.traceId),
                decision = createDecision(context.traceId),
                task = createTask(
                    TraceId.from("trace-capability-task-other"),
                ),
                plan = createPlan(context.traceId),
            )
        }
    }

    @Test
    fun `select rejects plan result from a different trace`() {
        val context = createContext("trace-capability-default-008")
        val authority: CapabilitySelectionAuthority =
            DefaultCapabilitySelectionAuthority()

        assertFailsWith<IllegalArgumentException> {
            authority.select(
                context = context,
                identity = createIdentity(context.traceId),
                trust = createTrust(context.traceId),
                authorization = createAuthorization(context.traceId),
                understanding = createUnderstanding(context.traceId),
                decision = createDecision(context.traceId),
                task = createTask(context.traceId),
                plan = createPlan(
                    TraceId.from("trace-capability-plan-other"),
                ),
            )
        }
    }

    private fun createIdentity(traceId: TraceId): IdentityResult {
        return IdentityResult.create(
            traceId = traceId,
            status = IdentityStatus.UNRESOLVED,
        )
    }

    private fun createTrust(traceId: TraceId): TrustResult {
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
        traceId: TraceId,
    ): UnderstandingAuthorityResult {
        return UnderstandingAuthorityResult.create(
            traceId = traceId,
            status = UnderstandingAuthorityStatus.DEFERRED,
        )
    }

    private fun createDecision(
        traceId: TraceId,
    ): DecisionAuthorityResult {
        return DecisionAuthorityResult.create(
            traceId = traceId,
            status = DecisionAuthorityStatus.DEFERRED,
        )
    }

    private fun createTask(
        traceId: TraceId,
    ): TaskAuthorityResult {
        return TaskAuthorityResult.create(
            traceId = traceId,
            status = TaskAuthorityStatus.DEFERRED,
        )
    }

    private fun createPlan(
        traceId: TraceId,
    ): PlanAuthorityResult {
        return PlanAuthorityResult.create(
            traceId = traceId,
            status = PlanAuthorityStatus.DEFERRED,
        )
    }

    private fun createContext(traceValue: String): ContextEnvelope {
        return ContextEnvelope.create(
            traceId = TraceId.from(traceValue),
            schemaVersion = SchemaVersion.from(1),
            source = ContextSource.TEST,
            trustLevel = ContextTrustLevel.VERIFIED,
            securityLevel = ContextSecurityLevel.RESTRICTED,
            observedAt = DevilTimestamp.fromEpochMilliseconds(
                1_754_000_023_000L,
            ),
        )
    }
}
