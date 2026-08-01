package com.devil.core.runtime.understanding

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
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class DefaultUnderstandingAuthorityTest {

    @Test
    fun `understand defers without inventing meaning`() {
        val context = createContext("trace-understanding-default-001")
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
        val authority: UnderstandingAuthority =
            DefaultUnderstandingAuthority()

        val result = authority.understand(
            context = context,
            identity = identity,
            trust = trust,
            authorization = authorization,
        )

        assertEquals(context.traceId, result.traceId)
        assertEquals(UnderstandingAuthorityStatus.DEFERRED, result.status)
        assertNull(result.understanding)
        assertNull(result.error)
    }

    @Test
    fun `understand rejects identity result from a different trace`() {
        val context = createContext("trace-understanding-default-002")
        val identity = IdentityResult.create(
            traceId = TraceId.from("trace-understanding-identity-other"),
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
        val authority: UnderstandingAuthority =
            DefaultUnderstandingAuthority()

        assertFailsWith<IllegalArgumentException> {
            authority.understand(
                context = context,
                identity = identity,
                trust = trust,
                authorization = authorization,
            )
        }
    }

    @Test
    fun `understand rejects trust result from a different trace`() {
        val context = createContext("trace-understanding-default-003")
        val identity = IdentityResult.create(
            traceId = context.traceId,
            status = IdentityStatus.UNRESOLVED,
        )
        val trust = TrustResult.create(
            traceId = TraceId.from("trace-understanding-trust-other"),
            status = TrustStatus.DEFERRED,
        )
        val authorization = AuthorizationResult.create(
            traceId = context.traceId,
            status = AuthorizationStatus.DEFERRED,
        )
        val authority: UnderstandingAuthority =
            DefaultUnderstandingAuthority()

        assertFailsWith<IllegalArgumentException> {
            authority.understand(
                context = context,
                identity = identity,
                trust = trust,
                authorization = authorization,
            )
        }
    }

    @Test
    fun `understand rejects authorization result from a different trace`() {
        val context = createContext("trace-understanding-default-004")
        val identity = IdentityResult.create(
            traceId = context.traceId,
            status = IdentityStatus.UNRESOLVED,
        )
        val trust = TrustResult.create(
            traceId = context.traceId,
            status = TrustStatus.DEFERRED,
        )
        val authorization = AuthorizationResult.create(
            traceId = TraceId.from("trace-understanding-authorization-other"),
            status = AuthorizationStatus.DEFERRED,
        )
        val authority: UnderstandingAuthority =
            DefaultUnderstandingAuthority()

        assertFailsWith<IllegalArgumentException> {
            authority.understand(
                context = context,
                identity = identity,
                trust = trust,
                authorization = authorization,
            )
        }
    }

    private fun createContext(traceValue: String): ContextEnvelope {
        return ContextEnvelope.create(
            traceId = TraceId.from(traceValue),
            schemaVersion = SchemaVersion.from(1),
            source = ContextSource.TEST,
            trustLevel = ContextTrustLevel.VERIFIED,
            securityLevel = ContextSecurityLevel.RESTRICTED,
            observedAt = DevilTimestamp.fromEpochMilliseconds(1_754_000_014_000L),
        )
    }
}
