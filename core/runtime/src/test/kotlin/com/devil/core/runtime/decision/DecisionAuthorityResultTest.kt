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
import com.devil.core.model.error.ErrorCode
import com.devil.core.model.error.UniversalErrorRecord
import com.devil.core.model.understanding.UnderstandingRecord
import com.devil.core.model.understanding.UnderstandingState
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class DecisionAuthorityResultTest {

    @Test
    fun `create preserves produced result with matching decision`() {
        val understanding = createUnderstanding("trace-decision-001")
        val decision = createDecision(understanding)

        val result = DecisionAuthorityResult.create(
            traceId = understanding.context.traceId,
            status = DecisionAuthorityStatus.PRODUCED,
            decision = decision,
        )

        assertEquals(understanding.context.traceId, result.traceId)
        assertEquals(DecisionAuthorityStatus.PRODUCED, result.status)
        assertEquals(decision, result.decision)
        assertNull(result.error)
    }

    @Test
    fun `create preserves deferred result without decision or error`() {
        val traceId = TraceId.from("trace-decision-002")

        val result = DecisionAuthorityResult.create(
            traceId = traceId,
            status = DecisionAuthorityStatus.DEFERRED,
        )

        assertEquals(traceId, result.traceId)
        assertEquals(DecisionAuthorityStatus.DEFERRED, result.status)
        assertNull(result.decision)
        assertNull(result.error)
    }

    @Test
    fun `create preserves failed result with matching error`() {
        val traceId = TraceId.from("trace-decision-003")
        val error = createError(traceId)

        val result = DecisionAuthorityResult.create(
            traceId = traceId,
            status = DecisionAuthorityStatus.FAILED,
            error = error,
        )

        assertEquals(traceId, result.traceId)
        assertEquals(DecisionAuthorityStatus.FAILED, result.status)
        assertNull(result.decision)
        assertEquals(error, result.error)
    }

    @Test
    fun `create rejects produced result without decision`() {
        assertFailsWith<IllegalArgumentException> {
            DecisionAuthorityResult.create(
                traceId = TraceId.from("trace-decision-004"),
                status = DecisionAuthorityStatus.PRODUCED,
            )
        }
    }

    @Test
    fun `create rejects deferred result with decision`() {
        val understanding = createUnderstanding("trace-decision-005")

        assertFailsWith<IllegalArgumentException> {
            DecisionAuthorityResult.create(
                traceId = understanding.context.traceId,
                status = DecisionAuthorityStatus.DEFERRED,
                decision = createDecision(understanding),
            )
        }
    }

    @Test
    fun `create rejects failed result without error`() {
        assertFailsWith<IllegalArgumentException> {
            DecisionAuthorityResult.create(
                traceId = TraceId.from("trace-decision-006"),
                status = DecisionAuthorityStatus.FAILED,
            )
        }
    }

    @Test
    fun `create rejects decision from a different trace`() {
        val understanding = createUnderstanding("trace-decision-record-other")

        assertFailsWith<IllegalArgumentException> {
            DecisionAuthorityResult.create(
                traceId = TraceId.from("trace-decision-007"),
                status = DecisionAuthorityStatus.PRODUCED,
                decision = createDecision(understanding),
            )
        }
    }

    @Test
    fun `create rejects error from a different trace`() {
        assertFailsWith<IllegalArgumentException> {
            DecisionAuthorityResult.create(
                traceId = TraceId.from("trace-decision-008"),
                status = DecisionAuthorityStatus.FAILED,
                error = createError(
                    TraceId.from("trace-decision-error-other"),
                ),
            )
        }
    }

    private fun createDecision(
        understanding: UnderstandingRecord,
    ): DecisionRecord {
        return DecisionRecord.create(
            understanding = understanding,
            state = DecisionState.SELECTED,
            summary = "Open the camera application.",
        )
    }

    private fun createUnderstanding(
        traceValue: String,
    ): UnderstandingRecord {
        return UnderstandingRecord.create(
            context = createContext(traceValue),
            state = UnderstandingState.COMPLETE,
            summary = "Open the camera application.",
        )
    }

    private fun createContext(traceValue: String): ContextEnvelope {
        return ContextEnvelope.create(
            traceId = TraceId.from(traceValue),
            schemaVersion = SchemaVersion.from(1),
            source = ContextSource.TEST,
            trustLevel = ContextTrustLevel.VERIFIED,
            securityLevel = ContextSecurityLevel.RESTRICTED,
            observedAt = DevilTimestamp.fromEpochMilliseconds(1_754_000_015_000L),
        )
    }

    private fun createError(traceId: TraceId): UniversalErrorRecord {
        return UniversalErrorRecord.create(
            errorCode = ErrorCode.from("DECISION_FAILED"),
            traceId = traceId,
            occurredAt = DevilTimestamp.fromEpochMilliseconds(1_754_000_015_500L),
            summary = "Decision selection failed.",
        )
    }
}
