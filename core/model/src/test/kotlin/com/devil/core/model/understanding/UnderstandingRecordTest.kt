package com.devil.core.model.understanding

import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.common.SchemaVersion
import com.devil.core.model.common.TraceId
import com.devil.core.model.context.ContextEnvelope
import com.devil.core.model.context.ContextSecurityLevel
import com.devil.core.model.context.ContextSource
import com.devil.core.model.context.ContextTrustLevel
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class UnderstandingRecordTest {

    @Test
    fun `create preserves and normalizes understanding`() {
        val context = createContext("trace-001")

        val record = UnderstandingRecord.create(
            context = context,
            state = UnderstandingState.COMPLETE,
            summary = "  Open the camera application.  ",
        )

        assertEquals(context, record.context)
        assertEquals(UnderstandingState.COMPLETE, record.state)
        assertEquals("Open the camera application.", record.summary)
    }

    @Test
    fun `create rejects blank summary`() {
        assertFailsWith<IllegalArgumentException> {
            UnderstandingRecord.create(
                context = createContext("trace-002"),
                state = UnderstandingState.INCOMPLETE,
                summary = "   ",
            )
        }
    }

    private fun createContext(traceValue: String): ContextEnvelope {
        return ContextEnvelope.create(
            traceId = TraceId.from(traceValue),
            schemaVersion = SchemaVersion.from(1),
            source = ContextSource.TEXT,
            trustLevel = ContextTrustLevel.VERIFIED,
            securityLevel = ContextSecurityLevel.RESTRICTED,
            observedAt = DevilTimestamp.fromEpochMilliseconds(1_754_000_000_000L),
        )
    }
}
