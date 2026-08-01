package com.devil.core.runtime.trust

import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.common.SchemaVersion
import com.devil.core.model.common.TraceId
import com.devil.core.model.context.ContextEnvelope
import com.devil.core.model.context.ContextSecurityLevel
import com.devil.core.model.context.ContextSource
import com.devil.core.model.context.ContextTrustLevel
import com.devil.core.model.error.ErrorCode
import com.devil.core.model.error.UniversalErrorRecord
import com.devil.core.model.identity.IdentityId
import com.devil.core.runtime.identity.IdentityResult
import com.devil.core.runtime.identity.IdentityStatus
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class DefaultTrustAuthorityTest {

    @Test
    fun `evaluate preserves context trust when identity is resolved`() {
        val context = createContext("trace-trust-default-001")
        val identity = IdentityResult.create(
            traceId = context.traceId,
            status = IdentityStatus.RESOLVED,
            identityId = IdentityId.from("subject-001"),
        )
        val authority: TrustAuthority = DefaultTrustAuthority()

        val result = authority.evaluate(
            context = context,
            identity = identity,
        )

        assertEquals(context.traceId, result.traceId)
        assertEquals(TrustStatus.EVALUATED, result.status)
        assertEquals(context.trustLevel, result.trustLevel)
        assertNull(result.error)
    }

    @Test
    fun `evaluate defers when identity is unresolved`() {
        val context = createContext("trace-trust-default-002")
        val identity = IdentityResult.create(
            traceId = context.traceId,
            status = IdentityStatus.UNRESOLVED,
        )
        val authority: TrustAuthority = DefaultTrustAuthority()

        val result = authority.evaluate(
            context = context,
            identity = identity,
        )

        assertEquals(context.traceId, result.traceId)
        assertEquals(TrustStatus.DEFERRED, result.status)
        assertNull(result.trustLevel)
        assertNull(result.error)
    }

    @Test
    fun `evaluate defers when identity resolution failed`() {
        val context = createContext("trace-trust-default-003")
        val identity = IdentityResult.create(
            traceId = context.traceId,
            status = IdentityStatus.FAILED,
            error = createIdentityError(context.traceId),
        )
        val authority: TrustAuthority = DefaultTrustAuthority()

        val result = authority.evaluate(
            context = context,
            identity = identity,
        )

        assertEquals(context.traceId, result.traceId)
        assertEquals(TrustStatus.DEFERRED, result.status)
        assertNull(result.trustLevel)
        assertNull(result.error)
    }

    @Test
    fun `evaluate rejects identity result from a different trace`() {
        val context = createContext("trace-trust-default-004")
        val identity = IdentityResult.create(
            traceId = TraceId.from("trace-trust-other"),
            status = IdentityStatus.UNRESOLVED,
        )
        val authority: TrustAuthority = DefaultTrustAuthority()

        assertFailsWith<IllegalArgumentException> {
            authority.evaluate(
                context = context,
                identity = identity,
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
            observedAt = DevilTimestamp.fromEpochMilliseconds(1_754_000_010_000L),
        )
    }

    private fun createIdentityError(
        traceId: TraceId,
    ): UniversalErrorRecord {
        return UniversalErrorRecord.create(
            errorCode = ErrorCode.from("IDENTITY_RESOLUTION_FAILED"),
            traceId = traceId,
            occurredAt = DevilTimestamp.fromEpochMilliseconds(1_754_000_010_500L),
            summary = "Identity resolution failed.",
        )
    }
}
