package com.devil.core.runtime.identity

import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.common.TraceId
import com.devil.core.model.error.ErrorCode
import com.devil.core.model.error.UniversalErrorRecord
import com.devil.core.model.identity.IdentityId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class IdentityResultTest {

    @Test
    fun `create preserves resolved result with identity`() {
        val traceId = TraceId.from("trace-identity-001")
        val identityId = IdentityId.from("subject-001")

        val result = IdentityResult.create(
            traceId = traceId,
            status = IdentityStatus.RESOLVED,
            identityId = identityId,
        )

        assertEquals(traceId, result.traceId)
        assertEquals(IdentityStatus.RESOLVED, result.status)
        assertEquals(identityId, result.identityId)
        assertNull(result.error)
    }

    @Test
    fun `create preserves unresolved result without identity or error`() {
        val traceId = TraceId.from("trace-identity-002")

        val result = IdentityResult.create(
            traceId = traceId,
            status = IdentityStatus.UNRESOLVED,
        )

        assertEquals(traceId, result.traceId)
        assertEquals(IdentityStatus.UNRESOLVED, result.status)
        assertNull(result.identityId)
        assertNull(result.error)
    }

    @Test
    fun `create preserves failed result with matching error`() {
        val traceId = TraceId.from("trace-identity-003")
        val error = createError(traceId)

        val result = IdentityResult.create(
            traceId = traceId,
            status = IdentityStatus.FAILED,
            error = error,
        )

        assertEquals(traceId, result.traceId)
        assertEquals(IdentityStatus.FAILED, result.status)
        assertNull(result.identityId)
        assertEquals(error, result.error)
    }

    @Test
    fun `create rejects resolved result without identity`() {
        assertFailsWith<IllegalArgumentException> {
            IdentityResult.create(
                traceId = TraceId.from("trace-identity-004"),
                status = IdentityStatus.RESOLVED,
            )
        }
    }

    @Test
    fun `create rejects unresolved result with identity`() {
        assertFailsWith<IllegalArgumentException> {
            IdentityResult.create(
                traceId = TraceId.from("trace-identity-005"),
                status = IdentityStatus.UNRESOLVED,
                identityId = IdentityId.from("subject-005"),
            )
        }
    }

    @Test
    fun `create rejects failed result without error`() {
        assertFailsWith<IllegalArgumentException> {
            IdentityResult.create(
                traceId = TraceId.from("trace-identity-006"),
                status = IdentityStatus.FAILED,
            )
        }
    }

    @Test
    fun `create rejects error from a different trace`() {
        assertFailsWith<IllegalArgumentException> {
            IdentityResult.create(
                traceId = TraceId.from("trace-identity-007"),
                status = IdentityStatus.FAILED,
                error = createError(
                    TraceId.from("trace-identity-other"),
                ),
            )
        }
    }

    private fun createError(traceId: TraceId): UniversalErrorRecord {
        return UniversalErrorRecord.create(
            errorCode = ErrorCode.from("IDENTITY_RESOLUTION_FAILED"),
            traceId = traceId,
            occurredAt = DevilTimestamp.fromEpochMilliseconds(1_754_000_007_000L),
            summary = "Identity resolution failed.",
        )
    }
}
