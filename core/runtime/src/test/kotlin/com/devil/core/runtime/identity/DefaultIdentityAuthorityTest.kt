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
import com.devil.core.model.identity.IdentityConfidence
import com.devil.core.model.identity.IdentityEvidence
import com.devil.core.model.identity.IdentityEvidenceSet
import com.devil.core.model.identity.IdentityEvidenceSource
import com.devil.core.model.identity.IdentityId
import com.devil.core.model.identity.IdentityResolutionCandidate
import com.devil.core.model.identity.IdentityResolutionCandidateSet
import com.devil.core.model.identity.IdentityResolutionRecord
import com.devil.core.model.identity.IdentityResolutionRequest
import com.devil.core.model.identity.IdentityResolutionSelection
import com.devil.core.model.identity.IdentityResolutionState
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class DefaultIdentityAuthorityTest {

    @Test
    fun `resolve returns unresolved result when no request is available`() {
        val context = createContext(
            "trace-identity-default-001",
        )
        val authority: IdentityAuthority =
            DefaultIdentityAuthority()

        val result = authority.resolve(context)

        assertEquals(context.traceId, result.traceId)
        assertEquals(IdentityStatus.UNRESOLVED, result.status)
        assertNull(result.identityId)
        assertNull(result.error)
    }

    @Test
    fun `resolve coordinates available request through resolver and mapper`() {
        val context = createContext(
            "trace-identity-default-002",
        )
        val request = createRequest(context)
        val selectedIdentityId = request.evidenceSet.claimedIdentityId

        val authority: IdentityAuthority =
            DefaultIdentityAuthority(
                requestProvider = availableProvider(request),
                resolver = resolvedResolver(request),
                resultMapper = DefaultIdentityResolutionResultMapper(),
            )

        val result = authority.resolve(context)

        assertEquals(context.traceId, result.traceId)
        assertEquals(IdentityStatus.RESOLVED, result.status)
        assertEquals(selectedIdentityId, result.identityId)
        assertNull(result.error)
    }

    @Test
    fun `resolve preserves failed request provider result`() {
        val context = createContext(
            "trace-identity-default-003",
        )
        val error = createError(context.traceId)

        val authority: IdentityAuthority =
            DefaultIdentityAuthority(
                requestProvider = object :
                    IdentityResolutionRequestProvider {
                    override fun provide(
                        context: ContextEnvelope,
                    ): IdentityResolutionRequestResult {
                        return IdentityResolutionRequestResult.create(
                            traceId = context.traceId,
                            status = IdentityResolutionRequestStatus.FAILED,
                            error = error,
                        )
                    }
                },
            )

        val result = authority.resolve(context)

        assertEquals(context.traceId, result.traceId)
        assertEquals(IdentityStatus.FAILED, result.status)
        assertNull(result.identityId)
        assertEquals(error, result.error)
    }

    @Test
    fun `resolve rejects request provider result from a different trace`() {
        val context = createContext(
            "trace-identity-default-004",
        )

        val authority: IdentityAuthority =
            DefaultIdentityAuthority(
                requestProvider = object :
                    IdentityResolutionRequestProvider {
                    override fun provide(
                        context: ContextEnvelope,
                    ): IdentityResolutionRequestResult {
                        return IdentityResolutionRequestResult.create(
                            traceId = TraceId.from(
                                "trace-identity-default-provider-other",
                            ),
                            status = IdentityResolutionRequestStatus.UNAVAILABLE,
                        )
                    }
                },
            )

        assertFailsWith<IllegalArgumentException> {
            authority.resolve(context)
        }
    }

    @Test
    fun `resolve rejects mapped result from a different trace`() {
        val context = createContext(
            "trace-identity-default-005",
        )
        val request = createRequest(context)

        val authority: IdentityAuthority =
            DefaultIdentityAuthority(
                requestProvider = availableProvider(request),
                resolver = resolvedResolver(request),
                resultMapper = object :
                    IdentityResolutionResultMapper {
                    override fun map(
                        traceId: TraceId,
                        record: IdentityResolutionRecord,
                    ): IdentityResult {
                        return IdentityResult.create(
                            traceId = TraceId.from(
                                "trace-identity-default-mapper-other",
                            ),
                            status = IdentityStatus.RESOLVED,
                            identityId = requireNotNull(
                                record.selection,
                            ).candidate.identityId,
                        )
                    }
                },
            )

        assertFailsWith<IllegalArgumentException> {
            authority.resolve(context)
        }
    }

    private fun availableProvider(
        request: IdentityResolutionRequest,
    ): IdentityResolutionRequestProvider {
        return object : IdentityResolutionRequestProvider {
            override fun provide(
                context: ContextEnvelope,
            ): IdentityResolutionRequestResult {
                return IdentityResolutionRequestResult.create(
                    traceId = context.traceId,
                    status = IdentityResolutionRequestStatus.AVAILABLE,
                    request = request,
                )
            }
        }
    }

    private fun resolvedResolver(
        request: IdentityResolutionRequest,
    ): IdentityResolutionResolver {
        return object : IdentityResolutionResolver {
            override fun resolve(
                request: IdentityResolutionRequest,
            ): IdentityResolutionRecord {
                val candidate = IdentityResolutionCandidate.create(
                    identityId = request.evidenceSet.claimedIdentityId,
                    evidenceSet = request.evidenceSet,
                )

                return IdentityResolutionRecord.create(
                    candidateSet =
                        IdentityResolutionCandidateSet.create(
                            candidates = listOf(candidate),
                        ),
                    state = IdentityResolutionState.RESOLVED,
                    selection = IdentityResolutionSelection.create(
                        candidate = candidate,
                        confidence = IdentityConfidence.from(90),
                        rationale = "Test resolver selected one candidate.",
                    ),
                    rationale = "Test identity resolution completed.",
                )
            }
        }
    }

    private fun createRequest(
        context: ContextEnvelope,
    ): IdentityResolutionRequest {
        val identityId = IdentityId.from(
            "subject-default-identity-authority",
        )

        return IdentityResolutionRequest.create(
            context = context,
            evidenceSet = IdentityEvidenceSet.create(
                claimedIdentityId = identityId,
                evidence = listOf(
                    IdentityEvidence.create(
                        claimedIdentityId = identityId,
                        source = IdentityEvidenceSource.TEST,
                        observedAt =
                            DevilTimestamp.fromEpochMilliseconds(
                                1_754_000_047_000L,
                            ),
                        reference = "default-authority-test-evidence",
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
                1_754_000_046_500L,
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
                1_754_000_047_500L,
            ),
            summary = "Identity resolution request supply failed.",
        )
    }
}
