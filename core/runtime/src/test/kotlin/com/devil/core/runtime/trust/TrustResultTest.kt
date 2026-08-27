package com.devil.core.runtime.trust

import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.common.TraceId
import com.devil.core.model.context.ContextTrustLevel
import com.devil.core.model.error.ErrorCode
import com.devil.core.model.error.UniversalErrorRecord
import com.devil.core.model.identity.IdentityId
import com.devil.core.model.trust.SubjectTrustLevel
import com.devil.core.model.trust.TrustAssessment
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertSame

class TrustResultTest {

    @Test
    fun `create preserves evaluated result with independently justified context trust`() {
        val traceId = TraceId.from("trace-trust-001")

        val result =
            TrustResult.create(
                traceId = traceId,
                status = TrustStatus.EVALUATED,
                trustLevel = ContextTrustLevel.VERIFIED,
            )

        assertEquals(traceId, result.traceId)
        assertEquals(TrustStatus.EVALUATED, result.status)
        assertEquals(
            ContextTrustLevel.VERIFIED,
            result.trustLevel,
        )
        assertNull(result.assessment)
        assertNull(result.error)
    }

    @Test
    fun `create preserves exact evaluated subject trust assessment`() {
        val traceId = TraceId.from("trace-trust-002")
        val assessment =
            TrustAssessment.create(
                subjectIdentityId =
                    IdentityId.from(
                        "subject-trust-002",
                    ),
                level = SubjectTrustLevel.UNESTABLISHED,
                rationale =
                    "Bounded subject trust assessment.",
            )

        val result =
            TrustResult.create(
                traceId = traceId,
                status = TrustStatus.EVALUATED,
                assessment = assessment,
            )

        assertEquals(traceId, result.traceId)
        assertEquals(TrustStatus.EVALUATED, result.status)
        assertNull(result.trustLevel)
        assertSame(assessment, result.assessment)
        assertNull(result.error)
    }

    @Test
    fun `create may preserve distinct subject and context trust representations together`() {
        val traceId = TraceId.from("trace-trust-003")
        val assessment =
            TrustAssessment.create(
                subjectIdentityId =
                    IdentityId.from(
                        "subject-trust-003",
                    ),
                level = SubjectTrustLevel.RESTRICTED,
                rationale =
                    "Bounded subject trust assessment.",
            )

        val result =
            TrustResult.create(
                traceId = traceId,
                status = TrustStatus.EVALUATED,
                trustLevel = ContextTrustLevel.TRUSTED,
                assessment = assessment,
            )

        assertEquals(TrustStatus.EVALUATED, result.status)
        assertEquals(
            ContextTrustLevel.TRUSTED,
            result.trustLevel,
        )
        assertSame(assessment, result.assessment)
        assertNull(result.error)
    }

    @Test
    fun `create preserves deferred result without trust representation or error`() {
        val traceId = TraceId.from("trace-trust-004")

        val result =
            TrustResult.create(
                traceId = traceId,
                status = TrustStatus.DEFERRED,
            )

        assertEquals(traceId, result.traceId)
        assertEquals(TrustStatus.DEFERRED, result.status)
        assertNull(result.trustLevel)
        assertNull(result.assessment)
        assertNull(result.error)
    }

    @Test
    fun `create preserves failed result with matching error`() {
        val traceId = TraceId.from("trace-trust-005")
        val error = createError(traceId)

        val result =
            TrustResult.create(
                traceId = traceId,
                status = TrustStatus.FAILED,
                error = error,
            )

        assertEquals(traceId, result.traceId)
        assertEquals(TrustStatus.FAILED, result.status)
        assertNull(result.trustLevel)
        assertNull(result.assessment)
        assertEquals(error, result.error)
    }

    @Test
    fun `create rejects evaluated result without any trust representation`() {
        assertFailsWith<IllegalArgumentException> {
            TrustResult.create(
                traceId = TraceId.from("trace-trust-006"),
                status = TrustStatus.EVALUATED,
            )
        }
    }

    @Test
    fun `create rejects deferred result with context trust`() {
        assertFailsWith<IllegalArgumentException> {
            TrustResult.create(
                traceId = TraceId.from("trace-trust-007"),
                status = TrustStatus.DEFERRED,
                trustLevel = ContextTrustLevel.TRUSTED,
            )
        }
    }

    @Test
    fun `create rejects deferred result with subject trust assessment`() {
        assertFailsWith<IllegalArgumentException> {
            TrustResult.create(
                traceId = TraceId.from("trace-trust-008"),
                status = TrustStatus.DEFERRED,
                assessment =
                    TrustAssessment.create(
                        subjectIdentityId =
                            IdentityId.from(
                                "subject-trust-008",
                            ),
                        level =
                            SubjectTrustLevel.UNESTABLISHED,
                        rationale =
                            "Bounded subject trust assessment.",
                    ),
            )
        }
    }

    @Test
    fun `create rejects failed result without error`() {
        assertFailsWith<IllegalArgumentException> {
            TrustResult.create(
                traceId = TraceId.from("trace-trust-009"),
                status = TrustStatus.FAILED,
            )
        }
    }

    @Test
    fun `create rejects failed result carrying trust representation`() {
        val traceId = TraceId.from("trace-trust-010")

        assertFailsWith<IllegalArgumentException> {
            TrustResult.create(
                traceId = traceId,
                status = TrustStatus.FAILED,
                assessment =
                    TrustAssessment.create(
                        subjectIdentityId =
                            IdentityId.from(
                                "subject-trust-010",
                            ),
                        level =
                            SubjectTrustLevel.RESTRICTED,
                        rationale =
                            "Bounded subject trust assessment.",
                    ),
                error = createError(traceId),
            )
        }
    }

    @Test
    fun `create rejects error from a different trace`() {
        assertFailsWith<IllegalArgumentException> {
            TrustResult.create(
                traceId = TraceId.from("trace-trust-011"),
                status = TrustStatus.FAILED,
                error =
                    createError(
                        TraceId.from(
                            "trace-trust-other",
                        ),
                    ),
            )
        }
    }

    private fun createError(
        traceId: TraceId,
    ): UniversalErrorRecord {
        return UniversalErrorRecord.create(
            errorCode =
                ErrorCode.from(
                    "TRUST_EVALUATION_FAILED",
                ),
            traceId = traceId,
            occurredAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_000_009_000L,
                ),
            summary = "Trust evaluation failed.",
        )
    }
}
