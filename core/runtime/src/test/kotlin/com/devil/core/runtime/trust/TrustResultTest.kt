package com.devil.core.runtime.trust

import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.common.TraceId
import com.devil.core.model.context.ContextTrustLevel
import com.devil.core.model.error.ErrorCode
import com.devil.core.model.error.UniversalErrorRecord
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class TrustResultTest {

    @Test
    fun `create preserves evaluated result with trust level`() {
        val traceId = TraceId.from("trace-trust-001")

        val result = TrustResult.create(
            traceId = traceId,
            status = TrustStatus.EVALUATED,
            trustLevel = ContextTrustLevel.VERIFIED,
        )

        assertEquals(traceId, result.traceId)
        assertEquals(TrustStatus.EVALUATED, result.status)
        assertEquals(ContextTrustLevel.VERIFIED, result.trustLevel)
        assertNull(result.error)
    }

    @Test
    fun `create preserves deferred result without trust level or error`() {
        val traceId = TraceId.from("trace-trust-002")

        val result = TrustResult.create(
            traceId = traceId,
            status = TrustStatus.DEFERRED,
        )

        assertEquals(traceId, result.traceId)
        assertEquals(TrustStatus.DEFERRED, result.status)
        assertNull(result.trustLevel)
        assertNull(result.error)
    }

    @Test
    fun `create preserves failed result with matching error`() {
        val traceId = TraceId.from("trace-trust-003")
        val error = createError(traceId)

        val result = TrustResult.create(
            traceId = traceId,
            status = TrustStatus.FAILED,
            error = error,
        )

        assertEquals(traceId, result.traceId)
        assertEquals(TrustStatus.FAILED, result.status)
        assertNull(result.trustLevel)
        assertEquals(error, result.error)
    }

    @Test
    fun `create rejects evaluated result without trust level`() {
        assertFailsWith<IllegalArgumentException> {
            TrustResult.create(
                traceId = TraceId.from("trace-trust-004"),
                status = TrustStatus.EVALUATED,
            )
        }
    }

    @Test
    fun `create rejects deferred result with trust level`() {
        assertFailsWith<IllegalArgumentException> {
            TrustResult.create(
                traceId = TraceId.from("trace-trust-005"),
                status = TrustStatus.DEFERRED,
                trustLevel = ContextTrustLevel.TRUSTED,
            )
        }
    }

    @Test
    fun `create rejects failed result without error`() {
        assertFailsWith<IllegalArgumentException> {
            TrustResult.create(
                traceId = TraceId.from("trace-trust-006"),
                status = TrustStatus.FAILED,
            )
        }
    }

    @Test
    fun `create rejects error from a different trace`() {
        assertFailsWith<IllegalArgumentException> {
            TrustResult.create(
                traceId = TraceId.from("trace-trust-007"),
                status = TrustStatus.FAILED,
                error = createError(
                    TraceId.from("trace-trust-other"),
                ),
            )
        }
    }

    private fun createError(traceId: TraceId): UniversalErrorRecord {
        return UniversalErrorRecord.create(
            errorCode = ErrorCode.from("TRUST_EVALUATION_FAILED"),
            traceId = traceId,
            occurredAt = DevilTimestamp.fromEpochMilliseconds(1_754_000_009_000L),
            summary = "Trust evaluation failed.",
        )
    }
}
