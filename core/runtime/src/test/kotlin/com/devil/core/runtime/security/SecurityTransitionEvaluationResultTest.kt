package com.devil.core.runtime.security

import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.common.SchemaVersion
import com.devil.core.model.common.TraceId
import com.devil.core.model.context.ContextEnvelope
import com.devil.core.model.context.ContextSecurityLevel
import com.devil.core.model.context.ContextSource
import com.devil.core.model.context.ContextTrustLevel
import com.devil.core.model.error.ErrorCode
import com.devil.core.model.error.UniversalErrorRecord
import com.devil.core.model.security.SecurityStage
import com.devil.core.model.security.SecurityStateRecord
import com.devil.core.model.security.SecurityTransitionRequest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class SecurityTransitionEvaluationResultTest {

    @Test
    fun `approved result preserves matching security transition request`() {
        val request = createRequest(
            "trace-security-evaluation-001",
        )

        val result = SecurityTransitionEvaluationResult.create(
            traceId = request.context.traceId,
            status = SecurityTransitionEvaluationStatus.APPROVED,
            request = request,
        )

        assertEquals(
            SecurityTransitionEvaluationStatus.APPROVED,
            result.status,
        )
        assertEquals(request, result.request)
        assertNull(result.error)
    }

    @Test
    fun `unavailable result contains neither request nor error`() {
        val traceId =
            TraceId.from(
                "trace-security-evaluation-002",
            )

        val result = SecurityTransitionEvaluationResult.create(
            traceId = traceId,
            status = SecurityTransitionEvaluationStatus.UNAVAILABLE,
        )

        assertEquals(traceId, result.traceId)
        assertEquals(
            SecurityTransitionEvaluationStatus.UNAVAILABLE,
            result.status,
        )
        assertNull(result.request)
        assertNull(result.error)
    }

    @Test
    fun `failed result requires matching error`() {
        val traceId =
            TraceId.from(
                "trace-security-evaluation-003",
            )

        val error = UniversalErrorRecord.create(
            errorCode =
                ErrorCode.from(
                    "SECURITY_EVALUATION_FAILED",
                ),
            traceId = traceId,
            occurredAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_000_192_000L,
                ),
            summary = "Security transition evaluation failed.",
        )

        val result = SecurityTransitionEvaluationResult.create(
            traceId = traceId,
            status = SecurityTransitionEvaluationStatus.FAILED,
            error = error,
        )

        assertEquals(
            SecurityTransitionEvaluationStatus.FAILED,
            result.status,
        )
        assertNull(result.request)
        assertEquals(error, result.error)
    }

    @Test
    fun `approved result rejects request from another trace`() {
        val request = createRequest(
            "trace-security-evaluation-004",
        )

        assertFailsWith<IllegalArgumentException> {
            SecurityTransitionEvaluationResult.create(
                traceId =
                    TraceId.from(
                        "trace-security-evaluation-other",
                    ),
                status = SecurityTransitionEvaluationStatus.APPROVED,
                request = request,
            )
        }
    }

    @Test
    fun `failed result rejects error from another trace`() {
        val traceId =
            TraceId.from(
                "trace-security-evaluation-005",
            )

        val error = UniversalErrorRecord.create(
            errorCode =
                ErrorCode.from(
                    "SECURITY_EVALUATION_FAILED",
                ),
            traceId =
                TraceId.from(
                    "trace-security-evaluation-error-other",
                ),
            occurredAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_000_192_100L,
                ),
            summary = "Security transition evaluation failed.",
        )

        assertFailsWith<IllegalArgumentException> {
            SecurityTransitionEvaluationResult.create(
                traceId = traceId,
                status = SecurityTransitionEvaluationStatus.FAILED,
                error = error,
            )
        }
    }

    private fun createRequest(
        traceValue: String,
    ): SecurityTransitionRequest {
        val context = ContextEnvelope.create(
            traceId = TraceId.from(traceValue),
            schemaVersion = SchemaVersion.from(1),
            source = ContextSource.SYSTEM,
            trustLevel = ContextTrustLevel.UNVERIFIED,
            securityLevel = ContextSecurityLevel.RESTRICTED,
            observedAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_000_192_000L,
                ),
        )

        return SecurityTransitionRequest.create(
            context = context,
            currentState =
                SecurityStateRecord.create(
                    stage = SecurityStage.LOCKED,
                    rationale = "Application is locked.",
                ),
            requestedStage = SecurityStage.WAKE,
            rationale = "Wake transition evaluation was requested.",
        )
    }
}
