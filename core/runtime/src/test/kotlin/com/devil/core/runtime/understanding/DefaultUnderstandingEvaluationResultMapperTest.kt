package com.devil.core.runtime.understanding

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
import kotlin.test.assertNull

class DefaultUnderstandingEvaluationResultMapperTest {

    @Test
    fun `map preserves unsupported understanding as produced`() {
        val traceId = TraceId.from(
            "trace-understanding-result-mapper-001",
        )
        val understanding = createUnderstanding(
            traceId = traceId,
            state = UnderstandingState.UNSUPPORTED,
        )
        val mapper: UnderstandingEvaluationResultMapper =
            DefaultUnderstandingEvaluationResultMapper()

        val result = mapper.map(
            traceId = traceId,
            understanding = understanding,
        )

        assertEquals(traceId, result.traceId)
        assertEquals(
            UnderstandingAuthorityStatus.PRODUCED,
            result.status,
        )
        assertEquals(understanding, result.understanding)
        assertEquals(
            UnderstandingState.UNSUPPORTED,
            result.understanding?.state,
        )
        assertNull(result.error)
    }

    @Test
    fun `map preserves complete understanding quality`() {
        val traceId = TraceId.from(
            "trace-understanding-result-mapper-002",
        )

        val result =
            DefaultUnderstandingEvaluationResultMapper().map(
                traceId = traceId,
                understanding = createUnderstanding(
                    traceId = traceId,
                    state = UnderstandingState.COMPLETE,
                ),
            )

        assertEquals(
            UnderstandingAuthorityStatus.PRODUCED,
            result.status,
        )
        assertEquals(
            UnderstandingState.COMPLETE,
            result.understanding?.state,
        )
        assertNull(result.error)
    }

    @Test
    fun `map preserves ambiguous understanding quality`() {
        val traceId = TraceId.from(
            "trace-understanding-result-mapper-003",
        )

        val result =
            DefaultUnderstandingEvaluationResultMapper().map(
                traceId = traceId,
                understanding = createUnderstanding(
                    traceId = traceId,
                    state = UnderstandingState.AMBIGUOUS,
                ),
            )

        assertEquals(
            UnderstandingAuthorityStatus.PRODUCED,
            result.status,
        )
        assertEquals(
            UnderstandingState.AMBIGUOUS,
            result.understanding?.state,
        )
    }

    @Test
    fun `map rejects understanding from a different trace`() {
        assertFailsWith<IllegalArgumentException> {
            DefaultUnderstandingEvaluationResultMapper().map(
                traceId = TraceId.from(
                    "trace-understanding-result-mapper-004",
                ),
                understanding = createUnderstanding(
                    traceId = TraceId.from(
                        "trace-understanding-record-other",
                    ),
                    state = UnderstandingState.INCOMPLETE,
                ),
            )
        }
    }

    private fun createUnderstanding(
        traceId: TraceId,
        state: UnderstandingState,
    ): UnderstandingRecord {
        return UnderstandingRecord.create(
            context = ContextEnvelope.create(
                traceId = traceId,
                schemaVersion = SchemaVersion.from(1),
                source = ContextSource.TEST,
                trustLevel = ContextTrustLevel.VERIFIED,
                securityLevel =
                    ContextSecurityLevel.RESTRICTED,
                observedAt =
                    DevilTimestamp.fromEpochMilliseconds(
                        1_754_000_069_000L,
                    ),
            ),
            state = state,
            summary =
                "Bounded structured-understanding evaluation result.",
        )
    }
}
