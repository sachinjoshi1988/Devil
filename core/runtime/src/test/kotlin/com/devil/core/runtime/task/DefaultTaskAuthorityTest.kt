package com.devil.core.runtime.task

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
import com.devil.core.runtime.trust.TrustResult
import com.devil.core.runtime.trust.TrustStatus
import com.devil.core.runtime.understanding.UnderstandingAuthorityResult
import com.devil.core.runtime.understanding.UnderstandingAuthorityStatus
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class DefaultTaskAuthorityTest {

    @Test
    fun `createTask defers without inventing a task`() {
        val context = createContext("trace-task-default-001")
        val authority: TaskAuthority = DefaultTaskAuthority()

        val result = authority.createTask(
            context = context,
            identity = createIdentity(context.traceId),
            trust = createTrust(context.traceId),
            authorization = createAuthorization(context.traceId),
            understanding = createUnderstanding(context.traceId),
            decision = createDecision(context.traceId),
        )

        assertEquals(context.traceId, result.traceId)
        assertEquals(TaskAuthorityStatus.DEFERRED, result.status)
        assertNull(result.task)
        assertNull(result.error)
    }

    @Test
    fun `createTask rejects identity result from a different trace`() {
        val context = createContext("trace-task-default-002")
        val authority: TaskAuthority = DefaultTaskAuthority()

        assertFailsWith<IllegalArgumentException> {
            authority.createTask(
                context = context,
                identity = createIdentity(
                    TraceId.from("trace-task-identity-other"),
                ),
                trust = createTrust(context.traceId),
                authorization = createAuthorization(context.traceId),
                understanding = createUnderstanding(context.traceId),
                decision = createDecision(context.traceId),
            )
        }
    }

    @Test
    fun `createTask rejects trust result from a different trace`() {
        val context = createContext("trace-task-default-003")
        val authority: TaskAuthority = DefaultTaskAuthority()

        assertFailsWith<IllegalArgumentException> {
            authority.createTask(
                context = context,
                identity = createIdentity(context.traceId),
                trust = createTrust(
                    TraceId.from("trace-task-trust-other"),
                ),
                authorization = createAuthorization(context.traceId),
                understanding = createUnderstanding(context.traceId),
                decision = createDecision(context.traceId),
            )
        }
    }

    @Test
    fun `createTask rejects authorization result from a different trace`() {
        val context = createContext("trace-task-default-004")
        val authority: TaskAuthority = DefaultTaskAuthority()

        assertFailsWith<IllegalArgumentException> {
            authority.createTask(
                context = context,
                identity = createIdentity(context.traceId),
                trust = createTrust(context.traceId),
                authorization = createAuthorization(
                    TraceId.from("trace-task-authorization-other"),
                ),
                understanding = createUnderstanding(context.traceId),
                decision = createDecision(context.traceId),
            )
        }
    }

    @Test
    fun `createTask rejects understanding result from a different trace`() {
        val context = createContext("trace-task-default-005")
        val authority: TaskAuthority = DefaultTaskAuthority()

        assertFailsWith<IllegalArgumentException> {
            authority.createTask(
                context = context,
                identity = createIdentity(context.traceId),
                trust = createTrust(context.traceId),
                authorization = createAuthorization(context.traceId),
                understanding = createUnderstanding(
                    TraceId.from("trace-task-understanding-other"),
                ),
                decision = createDecision(context.traceId),
            )
        }
    }

    @Test
    fun `createTask rejects decision result from a different trace`() {
        val context = createContext("trace-task-default-006")
        val authority: TaskAuthority = DefaultTaskAuthority()

        assertFailsWith<IllegalArgumentException> {
            authority.createTask(
                context = context,
                identity = createIdentity(context.traceId),
                trust = createTrust(context.traceId),
                authorization = createAuthorization(context.traceId),
                understanding = createUnderstanding(context.traceId),
                decision = createDecision(
                    TraceId.from("trace-task-decision-other"),
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

    private fun createContext(traceValue: String): ContextEnvelope {
        return ContextEnvelope.create(
            traceId = TraceId.from(traceValue),
            schemaVersion = SchemaVersion.from(1),
            source = ContextSource.TEST,
            trustLevel = ContextTrustLevel.VERIFIED,
            securityLevel = ContextSecurityLevel.RESTRICTED,
            observedAt = DevilTimestamp.fromEpochMilliseconds(1_754_000_018_000L),
        )
    }
}
