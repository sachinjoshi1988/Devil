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

class SecurityTransitionResultTest {

    @Test
    fun `approved result preserves matching security transition request`() {
        val request = createRequest(
            "trace-security-transition-result-001",
        )

        val result = SecurityTransitionResult.create(
            traceId = request.context.traceId,
            status = SecurityTransitionStatus.APPROVED,
            request = request,
        )

        assertEquals(
            SecurityTransitionStatus.APPROVED,
            result.status,
        )
        assertEquals(request, result.request)
        assertNull(result.error)
    }

    @Test
    fun `deferred result contains neither request nor error`() {
        val traceId = TraceId.from(
            "trace-security-transition-result-002",
        )

        val result = SecurityTransitionResult.create(
            traceId = traceId,
            status = SecurityTransitionStatus.DEFERRED,
        )

        assertEquals(traceId, result.traceId)
        assertEquals(
            SecurityTransitionStatus.DEFERRED,
            result.status,
        )
        assertNull(result.request)
        assertNull(result.error)
    }

    @Test
    fun `failed result preserves matching error`() {
        val traceId = TraceId.from(
            "trace-security-transition-result-003",
        )

        val error = createError(traceId)

        val result = SecurityTransitionResult.create(
            traceId = traceId,
            status = SecurityTransitionStatus.FAILED,
            error = error,
        )

        assertEquals(
            SecurityTransitionStatus.FAILED,
            result.status,
        )
        assertNull(result.request)
        assertEquals(error, result.error)
    }

    @Test
    fun `approved result rejects request from another trace`() {
        val request = createRequest(
            "trace-security-transition-result-004",
        )

        assertFailsWith<IllegalArgumentException> {
            SecurityTransitionResult.create(
                traceId = TraceId.from(
                    "trace-security-transition-result-other",
                ),
                status = SecurityTransitionStatus.APPROVED,
                request = request,
            )
        }
    }

    @Test
    fun `failed result rejects error from another trace`() {
        assertFailsWith<IllegalArgumentException> {
            SecurityTransitionResult.create(
                traceId = TraceId.from(
                    "trace-security-transition-result-005",
                ),
                status = SecurityTransitionStatus.FAILED,
                error = createError(
                    TraceId.from(
                        "trace-security-transition-result-error-other",
                    ),
                ),
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
                    1_754_000_194_000L,
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

    private fun createError(
        traceId: TraceId,
    ): UniversalErrorRecord {
        return UniversalErrorRecord.create(
            errorCode =
                ErrorCode.from(
                    "SECURITY_TRANSITION_FAILED",
                ),
            traceId = traceId,
            occurredAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_000_194_100L,
                ),
            summary = "Security transition failed.",
        )
    }
}
