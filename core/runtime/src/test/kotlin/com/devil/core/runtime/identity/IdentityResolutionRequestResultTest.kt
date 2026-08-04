package com.devil.core.runtime.identity

import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.common.SchemaVersion
import com.devil.core.model.common.TraceId
import com.devil.core.model.context.ContextEnvelope
import com.devil.core.model.context.ContextSecurityLevel
import com.devil.core.model.context.ContextSource
import com.devil.core.model.context.ContextTrustLevel
import com.devil.core.model.error.ErrorCode
import com.devil.core.model.error.UniversalErrorRecord
import com.devil.core.model.identity.IdentityEvidence
import com.devil.core.model.identity.IdentityEvidenceSet
import com.devil.core.model.identity.IdentityEvidenceSource
import com.devil.core.model.identity.IdentityId
import com.devil.core.model.identity.IdentityResolutionRequest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class IdentityResolutionRequestResultTest {

    @Test
    fun `create preserves available result with matching request`() {
        val context = createContext(
            "trace-identity-request-result-001",
        )
        val request = createRequest(context)

        val result = IdentityResolutionRequestResult.create(
            traceId = context.traceId,
            status = IdentityResolutionRequestStatus.AVAILABLE,
            request = request,
        )

        assertEquals(context.traceId, result.traceId)
        assertEquals(
            IdentityResolutionRequestStatus.AVAILABLE,
            result.status,
        )
        assertEquals(request, result.request)
        assertNull(result.error)
    }

    @Test
    fun `create preserves unavailable result without request or error`() {
        val traceId = TraceId.from(
            "trace-identity-request-result-002",
        )

        val result = IdentityResolutionRequestResult.create(
            traceId = traceId,
            status = IdentityResolutionRequestStatus.UNAVAILABLE,
        )

        assertEquals(traceId, result.traceId)
        assertEquals(
            IdentityResolutionRequestStatus.UNAVAILABLE,
            result.status,
        )
        assertNull(result.request)
        assertNull(result.error)
    }

    @Test
    fun `create preserves failed result with matching error`() {
        val traceId = TraceId.from(
            "trace-identity-request-result-003",
        )
        val error = createError(traceId)

        val result = IdentityResolutionRequestResult.create(
            traceId = traceId,
            status = IdentityResolutionRequestStatus.FAILED,
            error = error,
        )

        assertEquals(
            IdentityResolutionRequestStatus.FAILED,
            result.status,
        )
        assertNull(result.request)
        assertEquals(error, result.error)
    }

    @Test
    fun `create rejects available result without request`() {
        assertFailsWith<IllegalArgumentException> {
            IdentityResolutionRequestResult.create(
                traceId = TraceId.from(
                    "trace-identity-request-result-004",
                ),
                status = IdentityResolutionRequestStatus.AVAILABLE,
            )
        }
    }

    @Test
    fun `create rejects request from a different trace`() {
        assertFailsWith<IllegalArgumentException> {
            IdentityResolutionRequestResult.create(
                traceId = TraceId.from(
                    "trace-identity-request-result-005",
                ),
                status = IdentityResolutionRequestStatus.AVAILABLE,
                request = createRequest(
                    createContext(
                        "trace-identity-request-result-other",
                    ),
                ),
            )
        }
    }

    @Test
    fun `create rejects unavailable result with request`() {
        val context = createContext(
            "trace-identity-request-result-006",
        )

        assertFailsWith<IllegalArgumentException> {
            IdentityResolutionRequestResult.create(
                traceId = context.traceId,
                status = IdentityResolutionRequestStatus.UNAVAILABLE,
                request = createRequest(context),
            )
        }
    }

    @Test
    fun `create rejects failed result without error`() {
        assertFailsWith<IllegalArgumentException> {
            IdentityResolutionRequestResult.create(
                traceId = TraceId.from(
                    "trace-identity-request-result-007",
                ),
                status = IdentityResolutionRequestStatus.FAILED,
            )
        }
    }

    @Test
    fun `create rejects error from a different trace`() {
        assertFailsWith<IllegalArgumentException> {
            IdentityResolutionRequestResult.create(
                traceId = TraceId.from(
                    "trace-identity-request-result-008",
                ),
                status = IdentityResolutionRequestStatus.FAILED,
                error = createError(
                    TraceId.from(
                        "trace-identity-request-error-other",
                    ),
                ),
            )
        }
    }

    private fun createRequest(
        context: ContextEnvelope,
    ): IdentityResolutionRequest {
        val identityId = IdentityId.from(
            "subject-identity-request-result",
        )

        return IdentityResolutionRequest.create(
            context = context,
            evidenceSet = IdentityEvidenceSet.create(
                claimedIdentityId = identityId,
                evidence = listOf(
                    IdentityEvidence.create(
                        claimedIdentityId = identityId,
                        source = IdentityEvidenceSource.TEST,
                        observedAt = DevilTimestamp.fromEpochMilliseconds(
                            1_754_000_045_000L,
                        ),
                        reference = "identity-request-result-evidence",
                    ),
                ),
            ),
        )
    }

    private fun createContext(
        traceValue: String,
    ): ContextEnvelope {
        return ContextEnvelope.create(
            traceId = TraceId.from(traceValue),
            schemaVersion = SchemaVersion.from(1),
            source = ContextSource.TEST,
            trustLevel = ContextTrustLevel.VERIFIED,
            securityLevel = ContextSecurityLevel.RESTRICTED,
            observedAt = DevilTimestamp.fromEpochMilliseconds(
                1_754_000_044_500L,
            ),
        )
    }

    private fun createError(
        traceId: TraceId,
    ): UniversalErrorRecord {
        return UniversalErrorRecord.create(
            errorCode = ErrorCode.from(
                "IDENTITY_RESOLUTION_REQUEST_FAILED",
            ),
            traceId = traceId,
            occurredAt = DevilTimestamp.fromEpochMilliseconds(
                1_754_000_045_500L,
            ),
            summary = "Identity resolution request supply failed.",
        )
    }
}
