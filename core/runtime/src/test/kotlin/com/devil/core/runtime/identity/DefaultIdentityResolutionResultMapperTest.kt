package com.devil.core.runtime.identity

import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.common.TraceId
import com.devil.core.model.identity.IdentityConfidence
import com.devil.core.model.identity.IdentityEvidence
import com.devil.core.model.identity.IdentityEvidenceSet
import com.devil.core.model.identity.IdentityEvidenceSource
import com.devil.core.model.identity.IdentityId
import com.devil.core.model.identity.IdentityResolutionCandidate
import com.devil.core.model.identity.IdentityResolutionCandidateSet
import com.devil.core.model.identity.IdentityResolutionRecord
import com.devil.core.model.identity.IdentityResolutionSelection
import com.devil.core.model.identity.IdentityResolutionState
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class DefaultIdentityResolutionResultMapperTest {

    @Test
    fun `map preserves resolved identity and supplied trace`() {
        val traceId = TraceId.from(
            "trace-identity-result-mapper-001",
        )
        val candidate = createCandidate(
            identityValue = "subject-result-mapper-001",
            timestamp = 1_754_000_041_000L,
        )
        val record = IdentityResolutionRecord.create(
            candidateSet = IdentityResolutionCandidateSet.create(
                candidates = listOf(candidate),
            ),
            state = IdentityResolutionState.RESOLVED,
            selection = IdentityResolutionSelection.create(
                candidate = candidate,
                confidence = IdentityConfidence.from(92),
                rationale = "One candidate was selected.",
            ),
            rationale = "Identity resolution completed.",
        )
        val mapper: IdentityResolutionResultMapper =
            DefaultIdentityResolutionResultMapper()

        val result = mapper.map(
            traceId = traceId,
            record = record,
        )

        assertEquals(traceId, result.traceId)
        assertEquals(IdentityStatus.RESOLVED, result.status)
        assertEquals(candidate.identityId, result.identityId)
        assertNull(result.error)
    }

    @Test
    fun `map converts unresolved record to unresolved runtime result`() {
        val traceId = TraceId.from(
            "trace-identity-result-mapper-002",
        )
        val record = createNonResolvedRecord(
            state = IdentityResolutionState.UNRESOLVED,
            identityValue = "subject-result-mapper-002",
            timestamp = 1_754_000_041_500L,
        )
        val mapper: IdentityResolutionResultMapper =
            DefaultIdentityResolutionResultMapper()

        val result = mapper.map(
            traceId = traceId,
            record = record,
        )

        assertEquals(traceId, result.traceId)
        assertEquals(IdentityStatus.UNRESOLVED, result.status)
        assertNull(result.identityId)
        assertNull(result.error)
    }

    @Test
    fun `map converts ambiguous record to unresolved runtime result`() {
        val traceId = TraceId.from(
            "trace-identity-result-mapper-003",
        )
        val record = createNonResolvedRecord(
            state = IdentityResolutionState.AMBIGUOUS,
            identityValue = "subject-result-mapper-003",
            timestamp = 1_754_000_042_000L,
        )
        val mapper: IdentityResolutionResultMapper =
            DefaultIdentityResolutionResultMapper()

        val result = mapper.map(
            traceId = traceId,
            record = record,
        )

        assertEquals(traceId, result.traceId)
        assertEquals(IdentityStatus.UNRESOLVED, result.status)
        assertNull(result.identityId)
        assertNull(result.error)
    }

    private fun createNonResolvedRecord(
        state: IdentityResolutionState,
        identityValue: String,
        timestamp: Long,
    ): IdentityResolutionRecord {
        val candidate = createCandidate(
            identityValue = identityValue,
            timestamp = timestamp,
        )

        return IdentityResolutionRecord.create(
            candidateSet = IdentityResolutionCandidateSet.create(
                candidates = listOf(candidate),
            ),
            state = state,
            rationale = "No identity was selected.",
        )
    }

    private fun createCandidate(
        identityValue: String,
        timestamp: Long,
    ): IdentityResolutionCandidate {
        val identityId = IdentityId.from(identityValue)

        return IdentityResolutionCandidate.create(
            identityId = identityId,
            evidenceSet = IdentityEvidenceSet.create(
                claimedIdentityId = identityId,
                evidence = listOf(
                    IdentityEvidence.create(
                        claimedIdentityId = identityId,
                        source = IdentityEvidenceSource.TEST,
                        observedAt = DevilTimestamp.fromEpochMilliseconds(
                            timestamp,
                        ),
                        reference = "identity-result-mapper-evidence",
                    ),
                ),
            ),
        )
    }
}
