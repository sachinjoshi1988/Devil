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

class DefaultIdentityAuthorityTest {

    @Test
    fun `resolve returns unresolved result with the same trace`() {
        val context = createContext()
        val authority: IdentityAuthority = DefaultIdentityAuthority()

        val result = authority.resolve(context)

        assertEquals(context.traceId, result.traceId)
        assertEquals(IdentityStatus.UNRESOLVED, result.status)
        assertNull(result.identityId)
        assertNull(result.error)
    }

    private fun createContext(): ContextEnvelope {
        return ContextEnvelope.create(
            traceId = TraceId.from("trace-identity-default-001"),
            schemaVersion = SchemaVersion.from(1),
            source = ContextSource.TEST,
            trustLevel = ContextTrustLevel.VERIFIED,
            securityLevel = ContextSecurityLevel.RESTRICTED,
            observedAt = DevilTimestamp.fromEpochMilliseconds(1_754_000_008_000L),
        )
    }
}
