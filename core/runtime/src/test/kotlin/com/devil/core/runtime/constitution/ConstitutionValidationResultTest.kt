package com.devil.core.runtime.constitution

import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.common.TraceId
import com.devil.core.model.error.ErrorCode
import com.devil.core.model.error.UniversalErrorRecord
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class ConstitutionValidationResultTest {

    @Test
    fun `create preserves valid result without error`() {
        val traceId = TraceId.from("trace-constitution-001")

        val result = ConstitutionValidationResult.create(
            traceId = traceId,
            status = ConstitutionValidationStatus.VALID,
        )

        assertEquals(traceId, result.traceId)
        assertEquals(ConstitutionValidationStatus.VALID, result.status)
        assertNull(result.error)
    }

    @Test
    fun `create preserves invalid result with matching error`() {
        val traceId = TraceId.from("trace-constitution-002")
        val error = createError(traceId)

        val result = ConstitutionValidationResult.create(
            traceId = traceId,
            status = ConstitutionValidationStatus.INVALID,
            error = error,
        )

        assertEquals(traceId, result.traceId)
        assertEquals(ConstitutionValidationStatus.INVALID, result.status)
        assertEquals(error, result.error)
    }

    @Test
    fun `create rejects invalid result without error`() {
        assertFailsWith<IllegalArgumentException> {
            ConstitutionValidationResult.create(
                traceId = TraceId.from("trace-constitution-003"),
                status = ConstitutionValidationStatus.INVALID,
            )
        }
    }

    @Test
    fun `create rejects valid result with error`() {
        val traceId = TraceId.from("trace-constitution-004")

        assertFailsWith<IllegalArgumentException> {
            ConstitutionValidationResult.create(
                traceId = traceId,
                status = ConstitutionValidationStatus.VALID,
                error = createError(traceId),
            )
        }
    }

    @Test
    fun `create rejects error from a different trace`() {
        assertFailsWith<IllegalArgumentException> {
            ConstitutionValidationResult.create(
                traceId = TraceId.from("trace-constitution-005"),
                status = ConstitutionValidationStatus.INVALID,
                error = createError(
                    TraceId.from("trace-constitution-other"),
                ),
            )
        }
    }

    private fun createError(traceId: TraceId): UniversalErrorRecord {
        return UniversalErrorRecord.create(
            errorCode = ErrorCode.from("CONSTITUTION_INVALID"),
            traceId = traceId,
            occurredAt = DevilTimestamp.fromEpochMilliseconds(1_754_000_005_000L),
            summary = "The context violated a constitutional invariant.",
        )
    }
}
