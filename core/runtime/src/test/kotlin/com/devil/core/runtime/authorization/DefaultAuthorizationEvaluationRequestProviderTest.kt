package com.devil.core.runtime.authorization

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
import com.devil.core.runtime.trust.TrustResult
import com.devil.core.runtime.trust.TrustStatus
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class DefaultAuthorizationEvaluationRequestProviderTest {

    @Test
    fun `provide returns unavailable when identity and trust do not expose a bounded authorization request`() {
        val context = createContext(
            "trace-authorization-provider-001",
        )
        val provider: AuthorizationEvaluationRequestProvider =
            DefaultAuthorizationEvaluationRequestProvider()

        val result = provider.provide(
            context = context,
            identity = IdentityResult.create(
                traceId = context.traceId,
                status = IdentityStatus.RESOLVED,
                identityId = IdentityId.from(
                    "subject-authorization-provider-001",
                ),
            ),
            trust = TrustResult.create(
                traceId = context.traceId,
                status = TrustStatus.DEFERRED,
            ),
        )

        assertEquals(context.traceId, result.traceId)
        assertEquals(
            AuthorizationEvaluationRequestStatus.UNAVAILABLE,
            result.status,
        )
        assertNull(result.request)
        assertNull(result.error)
    }

    @Test
    fun `provide preserves failed identity error`() {
        val context = createContext(
            "trace-authorization-provider-002",
        )
        val error = createError(
            traceId = context.traceId,
            code = "IDENTITY_RESOLUTION_FAILED",
        )
        val provider: AuthorizationEvaluationRequestProvider =
            DefaultAuthorizationEvaluationRequestProvider()

        val result = provider.provide(
            context = context,
            identity = IdentityResult.create(
                traceId = context.traceId,
                status = IdentityStatus.FAILED,
                error = error,
            ),
            trust = TrustResult.create(
                traceId = context.traceId,
                status = TrustStatus.DEFERRED,
            ),
        )

        assertEquals(
            AuthorizationEvaluationRequestStatus.FAILED,
            result.status,
        )
        assertNull(result.request)
        assertEquals(error, result.error)
    }

    @Test
    fun `provide preserves failed trust error`() {
        val context = createContext(
            "trace-authorization-provider-003",
        )
        val error = createError(
            traceId = context.traceId,
            code = "TRUST_EVALUATION_FAILED",
        )
        val provider: AuthorizationEvaluationRequestProvider =
            DefaultAuthorizationEvaluationRequestProvider()

        val result = provider.provide(
            context = context,
            identity = IdentityResult.create(
                traceId = context.traceId,
                status = IdentityStatus.RESOLVED,
                identityId = IdentityId.from(
                    "subject-authorization-provider-003",
                ),
            ),
            trust = TrustResult.create(
                traceId = context.traceId,
                status = TrustStatus.FAILED,
                error = error,
            ),
        )

        assertEquals(
            AuthorizationEvaluationRequestStatus.FAILED,
            result.status,
        )
        assertNull(result.request)
        assertEquals(error, result.error)
    }

    @Test
    fun `provide rejects identity result from a different trace`() {
        val context = createContext(
            "trace-authorization-provider-004",
        )
        val provider: AuthorizationEvaluationRequestProvider =
            DefaultAuthorizationEvaluationRequestProvider()

        assertFailsWith<IllegalArgumentException> {
            provider.provide(
                context = context,
                identity = IdentityResult.create(
                    traceId = TraceId.from(
                        "trace-authorization-provider-identity-other",
                    ),
                    status = IdentityStatus.UNRESOLVED,
                ),
                trust = TrustResult.create(
                    traceId = context.traceId,
                    status = TrustStatus.DEFERRED,
                ),
            )
        }
    }

    @Test
    fun `provide rejects trust result from a different trace`() {
        val context = createContext(
            "trace-authorization-provider-005",
        )
        val provider: AuthorizationEvaluationRequestProvider =
            DefaultAuthorizationEvaluationRequestProvider()

        assertFailsWith<IllegalArgumentException> {
            provider.provide(
                context = context,
                identity = IdentityResult.create(
                    traceId = context.traceId,
                    status = IdentityStatus.UNRESOLVED,
                ),
                trust = TrustResult.create(
                    traceId = TraceId.from(
                        "trace-authorization-provider-trust-other",
                    ),
                    status = TrustStatus.DEFERRED,
                ),
            )
        }
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
            observedAt = DevilTimestamp.fromEpochMilliseconds(
                1_754_000_055_000L,
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
            occurredAt = DevilTimestamp.fromEpochMilliseconds(
                1_754_000_055_500L,
            ),
            summary = "Authorization request dependency failed.",
        )
    }
}
