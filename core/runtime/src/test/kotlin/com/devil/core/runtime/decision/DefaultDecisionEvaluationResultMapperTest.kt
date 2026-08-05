package com.devil.core.runtime.decision

import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.common.SchemaVersion
import com.devil.core.model.common.TraceId
import com.devil.core.model.context.ContextEnvelope
import com.devil.core.model.context.ContextSecurityLevel
import com.devil.core.model.context.ContextSource
import com.devil.core.model.context.ContextTrustLevel
import com.devil.core.model.decision.DecisionRecord
import com.devil.core.model.decision.DecisionState
import com.devil.core.model.understanding.UnderstandingRecord
import com.devil.core.model.understanding.UnderstandingState
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class DefaultDecisionEvaluationResultMapperTest {

    @Test
    fun `map preserves deferred decision as produced`() {
        val traceId = TraceId.from(
            "trace-decision-result-mapper-001",
        )
        val decision = createDecision(
            traceId = traceId,
            state = DecisionState.DEFERRED,
        )
        val mapper: DecisionEvaluationResultMapper =
            DefaultDecisionEvaluationResultMapper()

        val result = mapper.map(
            traceId = traceId,
            decision = decision,
        )

        assertEquals(traceId, result.traceId)
        assertEquals(
            DecisionAuthorityStatus.PRODUCED,
            result.status,
        )
        assertEquals(decision, result.decision)
        assertEquals(
            DecisionState.DEFERRED,
            result.decision?.state,
        )
        assertNull(result.error)
    }

    @Test
    fun `map preserves selected decision state`() {
        val traceId = TraceId.from(
            "trace-decision-result-mapper-002",
        )

        val result =
            DefaultDecisionEvaluationResultMapper().map(
                traceId = traceId,
                decision = createDecision(
                    traceId = traceId,
                    state = DecisionState.SELECTED,
                ),
            )

        assertEquals(
            DecisionAuthorityStatus.PRODUCED,
            result.status,
        )
        assertEquals(
            DecisionState.SELECTED,
            result.decision?.state,
        )
        assertNull(result.error)
    }

    @Test
    fun `map preserves requires clarification state`() {
        val traceId = TraceId.from(
            "trace-decision-result-mapper-003",
        )

        val result =
            DefaultDecisionEvaluationResultMapper().map(
                traceId = traceId,
                decision = createDecision(
                    traceId = traceId,
                    state =
                        DecisionState.REQUIRES_CLARIFICATION,
                ),
            )

        assertEquals(
            DecisionAuthorityStatus.PRODUCED,
            result.status,
        )
        assertEquals(
            DecisionState.REQUIRES_CLARIFICATION,
            result.decision?.state,
        )
    }

    @Test
    fun `map rejects decision from a different trace`() {
        assertFailsWith<IllegalArgumentException> {
            DefaultDecisionEvaluationResultMapper().map(
                traceId = TraceId.from(
                    "trace-decision-result-mapper-004",
                ),
                decision = createDecision(
                    traceId = TraceId.from(
                        "trace-decision-record-other",
                    ),
                    state = DecisionState.REJECTED,
                ),
            )
        }
    }

    private fun createDecision(
        traceId: TraceId,
        state: DecisionState,
    ): DecisionRecord {
        return DecisionRecord.create(
            understanding = UnderstandingRecord.create(
                context = ContextEnvelope.create(
                    traceId = traceId,
                    schemaVersion = SchemaVersion.from(1),
                    source = ContextSource.TEST,
                    trustLevel =
                        ContextTrustLevel.VERIFIED,
                    securityLevel =
                        ContextSecurityLevel.RESTRICTED,
                    observedAt =
                        DevilTimestamp.fromEpochMilliseconds(
                            1_754_000_075_000L,
                        ),
                ),
                state = UnderstandingState.COMPLETE,
                summary =
                    "Bounded understanding was produced.",
            ),
            state = state,
            summary =
                "Bounded constitutional decision evaluation result.",
        )
    }
}
