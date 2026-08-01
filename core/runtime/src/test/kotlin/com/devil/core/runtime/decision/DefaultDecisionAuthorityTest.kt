package com.devil.core.runtime.decision

import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.common.SchemaVersion
import com.devil.core.model.common.TraceId
import com.devil.core.model.context.ContextEnvelope
import com.devil.core.model.context.ContextSecurityLevel
import com.devil.core.model.context.ContextSource
import com.devil.core.model.context.ContextTrustLevel
import com.devil.core.runtime.authorization.AuthorizationResult
import com.devil.core.runtime.authorization.AuthorizationStatus
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

class DefaultDecisionAuthorityTest {

    @Test
    fun `decide defers without inventing a decision`() {
        val context = createContext("trace-decision-default-001")
        val identity = IdentityResult.create(
            traceId = context.traceId,
            status = IdentityStatus.UNRESOLVED,
        )
        val trust = TrustResult.create(
            traceId = context.traceId,
            status = TrustStatus.DEFERRED,
        )
        val authorization = AuthorizationResult.create(
            traceId = context.traceId,
            status = AuthorizationStatus.DEFERRED,
        )
        val understanding = UnderstandingAuthorityResult.create(
            traceId = context.traceId,
            status = UnderstandingAuthorityStatus.DEFERRED,
        )
        val authority: DecisionAuthority = DefaultDecisionAuthority()

        val result = authority.decide(
            context = context,
            identity = identity,
            trust = trust,
            authorization = authorization,
            understanding = understanding,
        )

        assertEquals(context.traceId, result.traceId)
        assertEquals(DecisionAuthorityStatus.DEFERRED, result.status)
        assertNull(result.decision)
        assertNull(result.error)
    }

    @Test
    fun `decide rejects identity result from a different trace`() {
        val context = createContext("trace-decision-default-002")
        val authority: DecisionAuthority = DefaultDecisionAuthority()

        assertFailsWith<IllegalArgumentException> {
            authority.decide(
                context = context,
                identity = IdentityResult.create(
                    traceId = TraceId.from("trace-decision-identity-other"),
                    status = IdentityStatus.UNRESOLVED,
                ),
                trust = createTrust(context.traceId),
                authorization = createAuthorization(context.traceId),
                understanding = createUnderstanding(context.traceId),
            )
        }
    }

    @Test
    fun `decide rejects trust result from a different trace`() {
        val context = createContext("trace-decision-default-003")
        val authority: DecisionAuthority = DefaultDecisionAuthority()

        assertFailsWith<IllegalArgumentException> {
            authority.decide(
                context = context,
                identity = createIdentity(context.traceId),
                trust = createTrust(
                    TraceId.from("trace-decision-trust-other"),
                ),
                authorization = createAuthorization(context.traceId),
                understanding = createUnderstanding(context.traceId),
            )
        }
    }

    @Test
    fun `decide rejects authorization result from a different trace`() {
        val context = createContext("trace-decision-default-004")
        val authority: DecisionAuthority = DefaultDecisionAuthority()

        assertFailsWith<IllegalArgumentException> {
            authority.decide(
                context = context,
                identity = createIdentity(context.traceId),
                trust = createTrust(context.traceId),
                authorization = createAuthorization(
                    TraceId.from("trace-decision-authorization-other"),
                ),
                understanding = createUnderstanding(context.traceId),
            )
        }
    }

    @Test
    fun `decide rejects understanding result from a different trace`() {
        val context = createContext("trace-decision-default-005")
        val authority: DecisionAuthority = DefaultDecisionAuthority()

        assertFailsWith<IllegalArgumentException> {
            authority.decide(
                context = context,
                identity = createIdentity(context.traceId),
                trust = createTrust(context.traceId),
                authorization = createAuthorization(context.traceId),
                understanding = createUnderstanding(
                    TraceId.from("trace-decision-understanding-other"),
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

    private fun createContext(traceValue: String): ContextEnvelope {
        return ContextEnvelope.create(
            traceId = TraceId.from(traceValue),
            schemaVersion = SchemaVersion.from(1),
            source = ContextSource.TEST,
            trustLevel = ContextTrustLevel.VERIFIED,
            securityLevel = ContextSecurityLevel.RESTRICTED,
            observedAt = DevilTimestamp.fromEpochMilliseconds(1_754_000_016_000L),
        )
    }
}
