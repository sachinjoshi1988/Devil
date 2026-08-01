package com.devil.core.runtime.plan

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
    fun `createPlan defers without inventing a plan`() {
        val context = createContext("trace-plan-default-001")
        val authority: PlanAuthority = DefaultPlanAuthority()

        val result = authority.createPlan(
            context = context,
            identity = createIdentity(context.traceId),
            trust = createTrust(context.traceId),
            authorization = createAuthorization(context.traceId),
            understanding = createUnderstanding(context.traceId),
            decision = createDecision(context.traceId),
            task = createTask(context.traceId),
        )

        assertEquals(context.traceId, result.traceId)
        assertEquals(PlanAuthorityStatus.DEFERRED, result.status)
        assertNull(result.plan)
        assertNull(result.error)
    }

    @Test
    fun `createPlan rejects identity result from a different trace`() {
        val context = createContext("trace-plan-default-002")
        val authority: PlanAuthority = DefaultPlanAuthority()

        assertFailsWith<IllegalArgumentException> {
            authority.createPlan(
                context = context,
                identity = createIdentity(
                    TraceId.from("trace-plan-identity-other"),
                ),
                trust = createTrust(context.traceId),
                authorization = createAuthorization(context.traceId),
                understanding = createUnderstanding(context.traceId),
                decision = createDecision(context.traceId),
                task = createTask(context.traceId),
            )
        }
    }

    @Test
    fun `createPlan rejects trust result from a different trace`() {
        val context = createContext("trace-plan-default-003")
        val authority: PlanAuthority = DefaultPlanAuthority()

        assertFailsWith<IllegalArgumentException> {
            authority.createPlan(
                context = context,
                identity = createIdentity(context.traceId),
                trust = createTrust(
                    TraceId.from("trace-plan-trust-other"),
                ),
                authorization = createAuthorization(context.traceId),
                understanding = createUnderstanding(context.traceId),
                decision = createDecision(context.traceId),
                task = createTask(context.traceId),
            )
        }
    }

    @Test
    fun `createPlan rejects authorization result from a different trace`() {
        val context = createContext("trace-plan-default-004")
        val authority: PlanAuthority = DefaultPlanAuthority()

        assertFailsWith<IllegalArgumentException> {
            authority.createPlan(
                context = context,
                identity = createIdentity(context.traceId),
                trust = createTrust(context.traceId),
                authorization = createAuthorization(
                    TraceId.from("trace-plan-authorization-other"),
                ),
                understanding = createUnderstanding(context.traceId),
                decision = createDecision(context.traceId),
                task = createTask(context.traceId),
            )
        }
    }

    @Test
    fun `createPlan rejects understanding result from a different trace`() {
        val context = createContext("trace-plan-default-005")
        val authority: PlanAuthority = DefaultPlanAuthority()

        assertFailsWith<IllegalArgumentException> {
            authority.createPlan(
                context = context,
                identity = createIdentity(context.traceId),
                trust = createTrust(context.traceId),
                authorization = createAuthorization(context.traceId),
                understanding = createUnderstanding(
                    TraceId.from("trace-plan-understanding-other"),
                ),
                decision = createDecision(context.traceId),
                task = createTask(context.traceId),
            )
        }
    }

    @Test
    fun `createPlan rejects decision result from a different trace`() {
        val context = createContext("trace-plan-default-006")
        val authority: PlanAuthority = DefaultPlanAuthority()

        assertFailsWith<IllegalArgumentException> {
            authority.createPlan(
                context = context,
                identity = createIdentity(context.traceId),
                trust = createTrust(context.traceId),
                authorization = createAuthorization(context.traceId),
                understanding = createUnderstanding(context.traceId),
                decision = createDecision(
                    TraceId.from("trace-plan-decision-other"),
                ),
                task = createTask(context.traceId),
            )
        }
    }

    @Test
    fun `createPlan rejects task result from a different trace`() {
        val context = createContext("trace-plan-default-007")
        val authority: PlanAuthority = DefaultPlanAuthority()

        assertFailsWith<IllegalArgumentException> {
            authority.createPlan(
                context = context,
                identity = createIdentity(context.traceId),
                trust = createTrust(context.traceId),
                authorization = createAuthorization(context.traceId),
                understanding = createUnderstanding(context.traceId),
                decision = createDecision(context.traceId),
                task = createTask(
                    TraceId.from("trace-plan-task-other"),
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

    private fun createContext(traceValue: String): ContextEnvelope {
        return ContextEnvelope.create(
            traceId = TraceId.from(traceValue),
            schemaVersion = SchemaVersion.from(1),
            source = ContextSource.TEST,
            trustLevel = ContextTrustLevel.VERIFIED,
            securityLevel = ContextSecurityLevel.RESTRICTED,
            observedAt = DevilTimestamp.fromEpochMilliseconds(
                1_754_000_021_000L,
            ),
        )
    }
}
