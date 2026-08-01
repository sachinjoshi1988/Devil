package com.devil.core.runtime.constitution

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

class DefaultConstitutionValidationAuthorityTest {

    @Test
    fun `validate returns valid result with the same trace`() {
        val context = createContext()
        val authority: ConstitutionValidationAuthority =
            DefaultConstitutionValidationAuthority()

        val result = authority.validate(context)

        assertEquals(context.traceId, result.traceId)
        assertEquals(ConstitutionValidationStatus.VALID, result.status)
        assertNull(result.error)
    }

    private fun createContext(): ContextEnvelope {
        return ContextEnvelope.create(
            traceId = TraceId.from("trace-constitution-default-001"),
            schemaVersion = SchemaVersion.from(1),
            source = ContextSource.TEST,
            trustLevel = ContextTrustLevel.VERIFIED,
            securityLevel = ContextSecurityLevel.RESTRICTED,
            observedAt = DevilTimestamp.fromEpochMilliseconds(1_754_000_006_000L),
        )
    }
}
