package com.devil.core.runtime.understanding

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
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class UnderstandingAuthorityResultTest {

    @Test
    fun `create preserves produced result with matching record`() {
        val context = createContext("trace-understanding-001")
        val understanding = createUnderstanding(context)

        val result = UnderstandingAuthorityResult.create(
            traceId = context.traceId,
            status = UnderstandingAuthorityStatus.PRODUCED,
            understanding = understanding,
        )

        assertEquals(context.traceId, result.traceId)
        assertEquals(UnderstandingAuthorityStatus.PRODUCED, result.status)
        assertEquals(understanding, result.understanding)
        assertNull(result.error)
    }

    @Test
    fun `create preserves deferred result without record or error`() {
        val traceId = TraceId.from("trace-understanding-002")

        val result = UnderstandingAuthorityResult.create(
            traceId = traceId,
            status = UnderstandingAuthorityStatus.DEFERRED,
        )

        assertEquals(traceId, result.traceId)
        assertEquals(UnderstandingAuthorityStatus.DEFERRED, result.status)
        assertNull(result.understanding)
        assertNull(result.error)
    }

    @Test
    fun `create preserves failed result with matching error`() {
        val traceId = TraceId.from("trace-understanding-003")
        val error = createError(traceId)

        val result = UnderstandingAuthorityResult.create(
            traceId = traceId,
            status = UnderstandingAuthorityStatus.FAILED,
            error = error,
        )

        assertEquals(traceId, result.traceId)
        assertEquals(UnderstandingAuthorityStatus.FAILED, result.status)
        assertNull(result.understanding)
        assertEquals(error, result.error)
    }

    @Test
    fun `create rejects produced result without record`() {
        assertFailsWith<IllegalArgumentException> {
            UnderstandingAuthorityResult.create(
                traceId = TraceId.from("trace-understanding-004"),
                status = UnderstandingAuthorityStatus.PRODUCED,
            )
        }
    }

    @Test
    fun `create rejects deferred result with record`() {
        val context = createContext("trace-understanding-005")

        assertFailsWith<IllegalArgumentException> {
            UnderstandingAuthorityResult.create(
                traceId = context.traceId,
                status = UnderstandingAuthorityStatus.DEFERRED,
                understanding = createUnderstanding(context),
            )
        }
    }

    @Test
    fun `create rejects failed result without error`() {
        assertFailsWith<IllegalArgumentException> {
            UnderstandingAuthorityResult.create(
                traceId = TraceId.from("trace-understanding-006"),
                status = UnderstandingAuthorityStatus.FAILED,
            )
        }
    }

    @Test
    fun `create rejects record from a different trace`() {
        val context = createContext("trace-understanding-record-other")

        assertFailsWith<IllegalArgumentException> {
            UnderstandingAuthorityResult.create(
                traceId = TraceId.from("trace-understanding-007"),
                status = UnderstandingAuthorityStatus.PRODUCED,
                understanding = createUnderstanding(context),
            )
        }
    }

    @Test
    fun `create rejects error from a different trace`() {
        assertFailsWith<IllegalArgumentException> {
            UnderstandingAuthorityResult.create(
                traceId = TraceId.from("trace-understanding-008"),
                status = UnderstandingAuthorityStatus.FAILED,
                error = createError(
                    TraceId.from("trace-understanding-error-other"),
                ),
            )
        }
    }

    private fun createUnderstanding(
        context: ContextEnvelope,
    ): UnderstandingRecord {
        return UnderstandingRecord.create(
            context = context,
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
            observedAt = DevilTimestamp.fromEpochMilliseconds(1_754_000_013_000L),
        )
    }

    private fun createError(traceId: TraceId): UniversalErrorRecord {
        return UniversalErrorRecord.create(
            errorCode = ErrorCode.from("UNDERSTANDING_FAILED"),
            traceId = traceId,
            occurredAt = DevilTimestamp.fromEpochMilliseconds(1_754_000_013_500L),
            summary = "Understanding failed.",
        )
    }
}
