package com.devil.core.model.identity

import com.devil.core.model.common.DevilTimestamp
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class IdentityResolutionCandidateTest {

    @Test
    fun `create preserves candidate with matching evidence identity`() {
        val identityId = IdentityId.from(
            "subject-resolution-candidate-001",
        )
        val evidenceSet = createEvidenceSet(identityId)

        val candidate = IdentityResolutionCandidate.create(
            identityId = identityId,
            evidenceSet = evidenceSet,
        )

        assertEquals(identityId, candidate.identityId)
        assertEquals(evidenceSet, candidate.evidenceSet)
    }

    @Test
    fun `create rejects evidence set for a different identity`() {
        val candidateIdentityId = IdentityId.from(
            "subject-resolution-candidate-002",
        )
        val evidenceIdentityId = IdentityId.from(
            "subject-resolution-candidate-other",
        )

        assertFailsWith<IllegalArgumentException> {
            IdentityResolutionCandidate.create(
                identityId = candidateIdentityId,
                evidenceSet = createEvidenceSet(evidenceIdentityId),
            )
        }
    }

    private fun createEvidenceSet(
        identityId: IdentityId,
    ): IdentityEvidenceSet {
        return IdentityEvidenceSet.create(
            claimedIdentityId = identityId,
            evidence = listOf(
                IdentityEvidence.create(
                    claimedIdentityId = identityId,
                    source = IdentityEvidenceSource.TEST,
                    observedAt = DevilTimestamp.fromEpochMilliseconds(
                        1_754_000_032_000L,
                    ),
                    reference = "resolution-candidate-evidence",
                ),
            ),
        )
    }
}
