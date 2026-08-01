package com.devil.core.model.context

import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.common.SchemaVersion
import com.devil.core.model.common.TraceId
import kotlin.test.Test
import kotlin.test.assertEquals

class ContextEnvelopeTest {

    @Test
    fun `create preserves the complete constitutional context header`() {
        val traceId = TraceId.from("trace-context-001")
        val schemaVersion = SchemaVersion.from(1)
        val observedAt = DevilTimestamp.fromEpochMilliseconds(1_754_000_000_000L)

        val envelope = ContextEnvelope.create(
            traceId = traceId,
            schemaVersion = schemaVersion,
            source = ContextSource.TEXT,
            trustLevel = ContextTrustLevel.VERIFIED,
            securityLevel = ContextSecurityLevel.RESTRICTED,
            observedAt = observedAt,
        )

        assertEquals(traceId, envelope.traceId)
        assertEquals(schemaVersion, envelope.schemaVersion)
        assertEquals(ContextSource.TEXT, envelope.source)
        assertEquals(ContextTrustLevel.VERIFIED, envelope.trustLevel)
        assertEquals(ContextSecurityLevel.RESTRICTED, envelope.securityLevel)
        assertEquals(observedAt, envelope.observedAt)
    }
}
