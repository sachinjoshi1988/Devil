package com.devil.core.runtime.authorization

import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.common.SchemaVersion
import com.devil.core.model.common.TraceId
import com.devil.core.model.context.ContextEnvelope
import com.devil.core.model.context.ContextSecurityLevel
import com.devil.core.model.context.ContextSource
import com.devil.core.model.context.ContextTrustLevel
import com.devil.core.runtime.identity.IdentityResult
import com.devil.core.runtime.identity.IdentityStatus
import com.devil.core.runtime.trust.TrustResult
import com.devil.core.runtime.trust.TrustStatus
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class DefaultAuthorizationAuthorityTest {

    @Test
    fun `authorize defers without inventing authority`() {
        val context = createContext("trace-authorization-default-001")
        val identity = IdentityResult.create(
            traceId = context.traceId,
            status = IdentityStatus.UNRESOLVED,
        )
        val trust = TrustResult.create(
            traceId = context.traceId,
            status = TrustStatus.DEFERRED,
        )
        val authority: AuthorizationAuthority =
            DefaultAuthorizationAuthority()

        val result = authority.authorize(
            context = context,
            identity = identity,
            trust = trust,
        )

        assertEquals(context.traceId, result.traceId)
        assertEquals(AuthorizationStatus.DEFERRED, result.status)
        assertNull(result.error)
    }

    @Test
    fun `authorize rejects identity result from a different trace`() {
        val context = createContext("trace-authorization-default-002")
        val identity = IdentityResult.create(
            traceId = TraceId.from("trace-authorization-identity-other"),
            status = IdentityStatus.UNRESOLVED,
        )
        val trust = TrustResult.create(
            traceId = context.traceId,
            status = TrustStatus.DEFERRED,
        )
        val authority: AuthorizationAuthority =
            DefaultAuthorizationAuthority()

        assertFailsWith<IllegalArgumentException> {
            authority.authorize(
                context = context,
                identity = identity,
                trust = trust,
            )
        }
    }

    @Test
    fun `authorize rejects trust result from a different trace`() {
        val context = createContext("trace-authorization-default-003")
        val identity = IdentityResult.create(
            traceId = context.traceId,
            status = IdentityStatus.UNRESOLVED,
        )
        val trust = TrustResult.create(
            traceId = TraceId.from("trace-authorization-trust-other"),
            status = TrustStatus.DEFERRED,
        )
        val authority: AuthorizationAuthority =
            DefaultAuthorizationAuthority()

        assertFailsWith<IllegalArgumentException> {
            authority.authorize(
                context = context,
                identity = identity,
                trust = trust,
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
            observedAt = DevilTimestamp.fromEpochMilliseconds(1_754_000_012_000L),
        )
    }
}
