package com.devil.core.runtime.decision

import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.common.SchemaVersion
import com.devil.core.model.common.TraceId
import com.devil.core.model.context.ContextEnvelope
import com.devil.core.model.context.ContextSecurityLevel
import com.devil.core.model.context.ContextSource
import com.devil.core.model.context.ContextTrustLevel
import com.devil.core.model.error.ErrorCode
import com.devil.core.model.error.UniversalErrorRecord
import com.devil.core.model.understanding.UnderstandingRecord
import com.devil.core.model.understanding.UnderstandingState
import com.devil.core.runtime.understanding.UnderstandingAuthorityResult
import com.devil.core.runtime.understanding.UnderstandingAuthorityStatus
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class DefaultDecisionEvaluationRequestProviderTest {

    @Test
    fun `provide returns available request for produced understanding`() {
        val understanding = createProducedUnderstanding(
            traceValue = "trace-decision-provider-001",
            state = UnderstandingState.COMPLETE,
        )
        val provider: DecisionEvaluationRequestProvider =
            DefaultDecisionEvaluationRequestProvider()

        val result = provider.provide(understanding)

        assertEquals(understanding.traceId, result.traceId)
        assertEquals(
            DecisionEvaluationRequestStatus.AVAILABLE,
            result.status,
        )
        assertEquals(
            requireNotNull(understanding.understanding),
            requireNotNull(result.request).understanding,
        )
        assertNull(result.error)
    }

    @Test
    fun `provide preserves unsupported understanding without selecting decision`() {
        val understanding = createProducedUnderstanding(
            traceValue = "trace-decision-provider-002",
            state = UnderstandingState.UNSUPPORTED,
        )

        val result =
            DefaultDecisionEvaluationRequestProvider()
                .provide(understanding)

        assertEquals(
            DecisionEvaluationRequestStatus.AVAILABLE,
            result.status,
        )
        assertEquals(
            UnderstandingState.UNSUPPORTED,
            requireNotNull(result.request)
                .understanding
                .state,
        )
        assertNull(result.error)
    }

    @Test
    fun `provide returns unavailable for deferred understanding`() {
        val traceId = TraceId.from(
            "trace-decision-provider-003",
        )

        val result =
            DefaultDecisionEvaluationRequestProvider()
                .provide(
                    UnderstandingAuthorityResult.create(
                        traceId = traceId,
                        status =
                            UnderstandingAuthorityStatus.DEFERRED,
                    ),
                )

        assertEquals(traceId, result.traceId)
        assertEquals(
            DecisionEvaluationRequestStatus.UNAVAILABLE,
            result.status,
        )
        assertNull(result.request)
        assertNull(result.error)
    }

    @Test
    fun `provide preserves failed understanding error`() {
        val traceId = TraceId.from(
            "trace-decision-provider-004",
        )
        val error = createError(traceId)

        val result =
            DefaultDecisionEvaluationRequestProvider()
                .provide(
                    UnderstandingAuthorityResult.create(
                        traceId = traceId,
                        status =
                            UnderstandingAuthorityStatus.FAILED,
                        error = error,
                    ),
                )

        assertEquals(
            DecisionEvaluationRequestStatus.FAILED,
            result.status,
        )
        assertNull(result.request)
        assertEquals(error, result.error)
    }

    private fun createProducedUnderstanding(
        traceValue: String,
        state: UnderstandingState,
    ): UnderstandingAuthorityResult {
        val traceId = TraceId.from(traceValue)

        return UnderstandingAuthorityResult.create(
            traceId = traceId,
            status = UnderstandingAuthorityStatus.PRODUCED,
            understanding = UnderstandingRecord.create(
                context = ContextEnvelope.create(
                    traceId = traceId,
                    schemaVersion = SchemaVersion.from(1),
                    source = ContextSource.TEXT,
                    trustLevel =
                        ContextTrustLevel.VERIFIED,
                    securityLevel =
                        ContextSecurityLevel.RESTRICTED,
                    observedAt =
                        DevilTimestamp
                            .fromEpochMilliseconds(
                                1_754_000_073_000L,
                            ),
                ),
                state = state,
                summary =
                    "Bounded understanding was produced.",
            ),
        )
    }

    private fun createError(
        traceId: TraceId,
    ): UniversalErrorRecord {
        return UniversalErrorRecord.create(
            errorCode = ErrorCode.from(
                "UNDERSTANDING_FAILED",
            ),
            traceId = traceId,
            occurredAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_000_073_500L,
                ),
            summary = "Understanding failed.",
        )
    }
}
