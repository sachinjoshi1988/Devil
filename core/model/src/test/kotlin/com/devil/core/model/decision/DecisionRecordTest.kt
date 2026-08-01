package com.devil.core.model.decision

import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.common.SchemaVersion
import com.devil.core.model.common.TraceId
import com.devil.core.model.context.ContextEnvelope
import com.devil.core.model.context.ContextSecurityLevel
import com.devil.core.model.context.ContextSource
import com.devil.core.model.context.ContextTrustLevel
import com.devil.core.model.understanding.UnderstandingRecord
import com.devil.core.model.understanding.UnderstandingState
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class DecisionRecordTest {

    @Test
    fun `create preserves and normalizes decision`() {
        val understanding = createUnderstanding()

        val decision = DecisionRecord.create(
            understanding = understanding,
            state = DecisionState.SELECTED,
            summary = "  Open the camera application.  ",
        )

        assertEquals(understanding, decision.understanding)
        assertEquals(DecisionState.SELECTED, decision.state)
        assertEquals("Open the camera application.", decision.summary)
    }

    @Test
    fun `create rejects blank summary`() {
        assertFailsWith<IllegalArgumentException> {
            DecisionRecord.create(
                understanding = createUnderstanding(),
                state = DecisionState.DEFERRED,
                summary = "   ",
            )
        }
    }

    private fun createUnderstanding(): UnderstandingRecord {
        return UnderstandingRecord.create(
            context = ContextEnvelope.create(
                traceId = TraceId.from("trace-001"),
                schemaVersion = SchemaVersion.from(1),
                source = ContextSource.TEXT,
                trustLevel = ContextTrustLevel.VERIFIED,
                securityLevel = ContextSecurityLevel.RESTRICTED,
                observedAt = DevilTimestamp.fromEpochMilliseconds(1_754_000_000_000L),
            ),
            state = UnderstandingState.COMPLETE,
            summary = "Open the camera application.",
        )
    }
}
