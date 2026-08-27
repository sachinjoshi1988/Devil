package com.devil.core.runtime.identity

import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.common.SchemaVersion
import com.devil.core.model.common.TraceId
import com.devil.core.model.context.ContextEnvelope
import com.devil.core.model.context.ContextSecurityLevel
import com.devil.core.model.context.ContextSource
import com.devil.core.model.context.ContextTrustLevel
import com.devil.core.model.identity.IdentityEvidence
import com.devil.core.model.identity.IdentityEvidenceSet
import com.devil.core.model.identity.IdentityEvidenceSource
import com.devil.core.model.identity.IdentityId
import com.devil.core.model.identity.IdentityResolutionRequest
import com.devil.core.model.identity.IdentityResolutionState
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertSame

class DefaultIdentityResolutionResolverTest {

    @Test
    fun `resolve selects sole coherent logical subject identity represented by request`() {
        val request = createRequest()
        val resolver: IdentityResolutionResolver =
            DefaultIdentityResolutionResolver()

        val record = resolver.resolve(request)

        assertEquals(
            IdentityResolutionState.RESOLVED,
            record.state,
        )

        val selection = assertNotNull(record.selection)

        assertEquals(
            request.evidenceSet.claimedIdentityId,
            selection.candidate.identityId,
        )
        assertEquals(
            100,
            selection.confidence.value,
        )
        assertEquals(
            "The request contains one coherent claimed subject identity and therefore one exact logical identity candidate.",
            selection.rationale,
        )
        assertEquals(
            "The sole coherent logical subject identity represented by the request was selected.",
            record.rationale,
        )
    }

    @Test
    fun `resolve preserves claimed identity and exact evidence set in one candidate`() {
        val request = createRequest()
        val resolver: IdentityResolutionResolver =
            DefaultIdentityResolutionResolver()

        val record = resolver.resolve(request)

        assertEquals(
            1,
            record.candidateSet.candidates.size,
        )

        val candidate =
            record.candidateSet.candidates.single()

        assertEquals(
            request.evidenceSet.claimedIdentityId,
            candidate.identityId,
        )
        assertSame(
            request.evidenceSet,
            candidate.evidenceSet,
        )

        assertSame(
            candidate,
            requireNotNull(record.selection).candidate,
        )
    }

    @Test
    fun `resolved logical identity does not alter context trust classification`() {
        val request = createRequest()
        val record =
            DefaultIdentityResolutionResolver()
                .resolve(request)

        assertEquals(
            IdentityResolutionState.RESOLVED,
            record.state,
        )
        assertEquals(
            ContextTrustLevel.VERIFIED,
            request.context.trustLevel,
        )
        assertEquals(
            request.evidenceSet.claimedIdentityId,
            requireNotNull(record.selection)
                .candidate
                .identityId,
        )
    }

    private fun createRequest(): IdentityResolutionRequest {
        val identityId =
            IdentityId.from(
                "subject-default-resolution-resolver-001",
            )

        return IdentityResolutionRequest.create(
            context =
                ContextEnvelope.create(
                    traceId =
                        TraceId.from(
                            "trace-default-resolution-resolver-001",
                        ),
                    schemaVersion = SchemaVersion.from(1),
                    source = ContextSource.TEST,
                    trustLevel = ContextTrustLevel.VERIFIED,
                    securityLevel =
                        ContextSecurityLevel.RESTRICTED,
                    observedAt =
                        DevilTimestamp.fromEpochMilliseconds(
                            1_754_000_043_000L,
                        ),
                ),
            evidenceSet =
                IdentityEvidenceSet.create(
                    claimedIdentityId = identityId,
                    evidence =
                        listOf(
                            IdentityEvidence.create(
                                claimedIdentityId = identityId,
                                source =
                                    IdentityEvidenceSource.DECLARED,
                                observedAt =
                                    DevilTimestamp.fromEpochMilliseconds(
                                        1_754_000_043_500L,
                                    ),
                                reference =
                                    "declared-resolution-evidence",
                            ),
                            IdentityEvidence.create(
                                claimedIdentityId = identityId,
                                source =
                                    IdentityEvidenceSource.DEVICE,
                                observedAt =
                                    DevilTimestamp.fromEpochMilliseconds(
                                        1_754_000_044_000L,
                                    ),
                                reference =
                                    "device-resolution-evidence",
                            ),
                        ),
                ),
        )
    }
}
