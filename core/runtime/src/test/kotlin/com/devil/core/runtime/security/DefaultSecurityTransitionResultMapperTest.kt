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

class DefaultSecurityTransitionResultMapperTest {

    @Test
    fun `map converts approved evaluation into approved operational result`() {
        val request = createRequest(
            "trace-security-transition-mapper-001",
        )

        val evaluation = SecurityTransitionEvaluationResult.create(
            traceId = request.context.traceId,
            status = SecurityTransitionEvaluationStatus.APPROVED,
            request = request,
        )

        val result = DefaultSecurityTransitionResultMapper().map(
            traceId = request.context.traceId,
            evaluation = evaluation,
        )

        assertEquals(
            SecurityTransitionStatus.APPROVED,
            result.status,
        )
        assertEquals(request, result.request)
        assertNull(result.error)
    }

    @Test
    fun `map converts unavailable evaluation into deferred operational result`() {
        val traceId = TraceId.from(
            "trace-security-transition-mapper-002",
        )

        val evaluation = SecurityTransitionEvaluationResult.create(
            traceId = traceId,
            status = SecurityTransitionEvaluationStatus.UNAVAILABLE,
        )

        val result = DefaultSecurityTransitionResultMapper().map(
            traceId = traceId,
            evaluation = evaluation,
        )

        assertEquals(
            SecurityTransitionStatus.DEFERRED,
            result.status,
        )
        assertNull(result.request)
        assertNull(result.error)
    }

    @Test
    fun `map preserves failed evaluation error`() {
        val traceId = TraceId.from(
            "trace-security-transition-mapper-003",
        )

        val error = createError(traceId)

        val evaluation = SecurityTransitionEvaluationResult.create(
            traceId = traceId,
            status = SecurityTransitionEvaluationStatus.FAILED,
            error = error,
        )

        val result = DefaultSecurityTransitionResultMapper().map(
            traceId = traceId,
            evaluation = evaluation,
        )

        assertEquals(
            SecurityTransitionStatus.FAILED,
            result.status,
        )
        assertNull(result.request)
        assertEquals(error, result.error)
    }

    @Test
    fun `map rejects evaluation from another trace`() {
        val evaluation = SecurityTransitionEvaluationResult.create(
            traceId =
                TraceId.from(
                    "trace-security-transition-mapper-004",
                ),
            status = SecurityTransitionEvaluationStatus.UNAVAILABLE,
        )

        assertFailsWith<IllegalArgumentException> {
            DefaultSecurityTransitionResultMapper().map(
                traceId =
                    TraceId.from(
                        "trace-security-transition-mapper-other",
                    ),
                evaluation = evaluation,
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
