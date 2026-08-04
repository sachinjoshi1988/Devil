package com.devil.core.runtime.identity

import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.common.SchemaVersion
import com.devil.core.model.common.TraceId
import com.devil.core.model.context.ContextEnvelope
import com.devil.core.model.context.ContextSecurityLevel
import com.devil.core.model.context.ContextSource
import com.devil.core.model.context.ContextTrustLevel
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class DefaultIdentityResolutionRequestProviderTest {

    @Test
    fun `provide reports unavailable without inventing identity evidence`() {
        val context = createContext()
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

    private fun createContext(): ContextEnvelope {
        return ContextEnvelope.create(
            traceId = TraceId.from(
                "trace-default-identity-request-provider-001",
            ),
            schemaVersion = SchemaVersion.from(1),
            source = ContextSource.TEST,
            trustLevel = ContextTrustLevel.VERIFIED,
            securityLevel = ContextSecurityLevel.RESTRICTED,
            observedAt = DevilTimestamp.fromEpochMilliseconds(
                1_754_000_046_000L,
            ),
        )
    }
}
