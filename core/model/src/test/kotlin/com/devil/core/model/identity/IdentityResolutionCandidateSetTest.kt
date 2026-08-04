package com.devil.core.model.identity

import com.devil.core.model.common.DevilTimestamp
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class IdentityResolutionCandidateSetTest {

    @Test
    fun `create preserves unique identity candidates`() {
        val firstCandidate = createCandidate(
            identityValue = "subject-candidate-set-001",
            reference = "candidate-set-evidence-001",
            timestamp = 1_754_000_033_000L,
        )
        val secondCandidate = createCandidate(
            identityValue = "subject-candidate-set-002",
            reference = "candidate-set-evidence-002",
            timestamp = 1_754_000_033_500L,
        )

        val candidateSet = IdentityResolutionCandidateSet.create(
            candidates = listOf(firstCandidate, secondCandidate),
        )

        assertEquals(
            listOf(firstCandidate, secondCandidate),
            candidateSet.candidates,
        )
    }

    @Test
    fun `create rejects an empty candidate collection`() {
        assertFailsWith<IllegalArgumentException> {
            IdentityResolutionCandidateSet.create(
                candidates = emptyList(),
            )
        }
    }

    @Test
    fun `create rejects duplicate candidate identities`() {
        val firstCandidate = createCandidate(
            identityValue = "subject-candidate-set-duplicate",
            reference = "candidate-set-evidence-first",
            timestamp = 1_754_000_034_000L,
        )
        val duplicateCandidate = createCandidate(
            identityValue = "subject-candidate-set-duplicate",
            reference = "candidate-set-evidence-second",
            timestamp = 1_754_000_034_500L,
        )

        assertFailsWith<IllegalArgumentException> {
            IdentityResolutionCandidateSet.create(
                candidates = listOf(
                    firstCandidate,
                    duplicateCandidate,
                ),
            )
        }
    }

    private fun createCandidate(
        identityValue: String,
        reference: String,
        timestamp: Long,
    ): IdentityResolutionCandidate {
        val identityId = IdentityId.from(identityValue)

        val evidenceSet = IdentityEvidenceSet.create(
            claimedIdentityId = identityId,
            evidence = listOf(
                IdentityEvidence.create(
                    claimedIdentityId = identityId,
                    source = IdentityEvidenceSource.TEST,
                    observedAt = DevilTimestamp.fromEpochMilliseconds(
                        timestamp,
                    ),
                    reference = reference,
                ),
            ),
        )

        return IdentityResolutionCandidate.create(
            identityId = identityId,
            evidenceSet = evidenceSet,
        )
    }
}
