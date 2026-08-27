package com.devil.core.runtime.identity

import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.common.SchemaVersion
import com.devil.core.model.common.TraceId
import com.devil.core.model.context.ContextEnvelope
import com.devil.core.model.context.ContextSecurityLevel
import com.devil.core.model.context.ContextSource
import com.devil.core.model.context.ContextTrustLevel
import com.devil.core.model.identity.IdentityEvidenceSource
import com.devil.core.model.identity.IdentityId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertSame

class DefaultIdentityResolutionRequestProviderTest {

    @Test
    fun `provide reports unavailable without inventing identity evidence`() {
        val context = createContext(
            "trace-default-identity-request-provider-001",
        )

        val provider: IdentityResolutionRequestProvider =
            DefaultIdentityResolutionRequestProvider()

        val result = provider.provide(context)

        assertEquals(context.traceId, result.traceId)
        assertEquals(
            IdentityResolutionRequestStatus.UNAVAILABLE,
            result.status,
        )
        assertNull(result.request)
        assertNull(result.error)
    }

    @Test
    fun `provide preserves explicitly configured subject as declared identity evidence`() {
        val context = createContext(
            "trace-default-identity-request-provider-002",
        )
        val identityId =
            IdentityId.from(
                "android-primary-local-subject",
            )

        val provider: IdentityResolutionRequestProvider =
            DefaultIdentityResolutionRequestProvider(
                configuredSubjectIdentityId = identityId,
            )

        val result = provider.provide(context)

        assertEquals(context.traceId, result.traceId)
        assertEquals(
            IdentityResolutionRequestStatus.AVAILABLE,
            result.status,
        )

        val request = requireNotNull(result.request)

        assertSame(context, request.context)
        assertEquals(
            identityId,
            request.evidenceSet.claimedIdentityId,
        )
        assertEquals(
            1,
            request.evidenceSet.evidence.size,
        )

        val evidence =
            request.evidenceSet.evidence.single()

        assertEquals(
            identityId,
            evidence.claimedIdentityId,
        )
        assertEquals(
            IdentityEvidenceSource.DECLARED,
            evidence.source,
        )
        assertEquals(
            context.observedAt,
            evidence.observedAt,
        )
        assertEquals(
            "application-configured-subject-identity",
            evidence.reference,
        )
        assertNull(result.error)
    }

    @Test
    fun `configured subject does not convert context trust into identity evidence`() {
        val context = createContext(
            "trace-default-identity-request-provider-003",
        )
        val identityId =
            IdentityId.from(
                "android-primary-local-subject",
            )

        val result =
            DefaultIdentityResolutionRequestProvider(
                configuredSubjectIdentityId = identityId,
            ).provide(context)

        val evidence =
            requireNotNull(result.request)
                .evidenceSet
                .evidence
                .single()

        assertEquals(
            IdentityEvidenceSource.DECLARED,
            evidence.source,
        )
        assertEquals(
            ContextTrustLevel.VERIFIED,
            context.trustLevel,
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
                    1_754_000_046_000L,
                ),
        )
    }
}
