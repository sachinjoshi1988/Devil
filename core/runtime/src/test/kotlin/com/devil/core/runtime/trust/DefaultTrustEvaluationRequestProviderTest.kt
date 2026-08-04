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

class DefaultTrustEvaluationRequestProviderTest {

    @Test
    fun `provide returns available request for resolved identity`() {
        val context = createContext(
            "trace-trust-request-provider-001",
        )
        val identityId = IdentityId.from(
            "subject-trust-request-provider-001",
        )
        val provider: TrustEvaluationRequestProvider =
            DefaultTrustEvaluationRequestProvider()

        val result = provider.provide(
            context = context,
            identity = IdentityResult.create(
                traceId = context.traceId,
                status = IdentityStatus.RESOLVED,
                identityId = identityId,
            ),
        )

        assertEquals(context.traceId, result.traceId)
        assertEquals(
            TrustEvaluationRequestStatus.AVAILABLE,
            result.status,
        )
        assertEquals(context, requireNotNull(result.request).context)
        assertEquals(
            identityId,
            requireNotNull(result.request).subjectIdentityId,
        )
        assertNull(result.error)
    }

    @Test
    fun `provide returns unavailable for unresolved identity`() {
        val context = createContext(
            "trace-trust-request-provider-002",
        )
        val provider: TrustEvaluationRequestProvider =
            DefaultTrustEvaluationRequestProvider()

        val result = provider.provide(
            context = context,
            identity = IdentityResult.create(
                traceId = context.traceId,
                status = IdentityStatus.UNRESOLVED,
            ),
        )

        assertEquals(
            TrustEvaluationRequestStatus.UNAVAILABLE,
            result.status,
        )
        assertNull(result.request)
        assertNull(result.error)
    }

    @Test
    fun `provide preserves failed identity error`() {
        val context = createContext(
            "trace-trust-request-provider-003",
        )
        val error = createIdentityError(context.traceId)
        val provider: TrustEvaluationRequestProvider =
            DefaultTrustEvaluationRequestProvider()

        val result = provider.provide(
            context = context,
            identity = IdentityResult.create(
                traceId = context.traceId,
                status = IdentityStatus.FAILED,
                error = error,
            ),
        )

        assertEquals(
            TrustEvaluationRequestStatus.FAILED,
            result.status,
        )
        assertNull(result.request)
        assertEquals(error, result.error)
    }

    @Test
    fun `provide rejects identity result from a different trace`() {
        val context = createContext(
            "trace-trust-request-provider-004",
        )
        val provider: TrustEvaluationRequestProvider =
            DefaultTrustEvaluationRequestProvider()

        assertFailsWith<IllegalArgumentException> {
            provider.provide(
                context = context,
                identity = IdentityResult.create(
                    traceId = TraceId.from(
                        "trace-trust-request-provider-other",
                    ),
                    status = IdentityStatus.UNRESOLVED,
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
                1_754_000_050_000L,
            ),
        )
    }

    private fun createIdentityError(
        traceId: TraceId,
    ): UniversalErrorRecord {
        return UniversalErrorRecord.create(
            errorCode = ErrorCode.from(
                "IDENTITY_RESOLUTION_FAILED",
            ),
            traceId = traceId,
            occurredAt = DevilTimestamp.fromEpochMilliseconds(
                1_754_000_050_500L,
            ),
            summary = "Identity resolution failed.",
        )
    }
}
