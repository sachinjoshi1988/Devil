package com.devil.core.model.identity

import com.devil.core.model.common.DevilTimestamp
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class IdentityEvidenceSetTest {

    @Test
    fun `create preserves evidence for one claimed identity`() {
        val identityId = IdentityId.from("subject-evidence-set-001")
        val firstEvidence = createEvidence(
            identityId = identityId,
            source = IdentityEvidenceSource.DECLARED,
            reference = "declaration-001",
            timestamp = 1_754_000_028_000L,
        )
        val secondEvidence = createEvidence(
            identityId = identityId,
            source = IdentityEvidenceSource.DEVICE,
            reference = "device-profile-001",
            timestamp = 1_754_000_028_500L,
        )

        val evidenceSet = IdentityEvidenceSet.create(
            claimedIdentityId = identityId,
            evidence = listOf(firstEvidence, secondEvidence),
        )

        assertEquals(identityId, evidenceSet.claimedIdentityId)
        assertEquals(
            listOf(firstEvidence, secondEvidence),
            evidenceSet.evidence,
        )
    }

    @Test
    fun `create rejects an empty evidence collection`() {
        assertFailsWith<IllegalArgumentException> {
            IdentityEvidenceSet.create(
                claimedIdentityId = IdentityId.from(
                    "subject-evidence-set-002",
                ),
                evidence = emptyList(),
            )
        }
    }

    @Test
    fun `create rejects evidence for a different claimed identity`() {
        val expectedIdentityId = IdentityId.from(
            "subject-evidence-set-003",
        )
        val otherIdentityId = IdentityId.from(
            "subject-evidence-set-other",
        )

        assertFailsWith<IllegalArgumentException> {
            IdentityEvidenceSet.create(
                claimedIdentityId = expectedIdentityId,
                evidence = listOf(
                    createEvidence(
                        identityId = expectedIdentityId,
                        source = IdentityEvidenceSource.DECLARED,
                        reference = "declaration-003",
                        timestamp = 1_754_000_029_000L,
                    ),
                    createEvidence(
                        identityId = otherIdentityId,
                        source = IdentityEvidenceSource.DEVICE,
                        reference = "device-profile-other",
                        timestamp = 1_754_000_029_500L,
                    ),
                ),
            )
        }
    }

    private fun createEvidence(
        identityId: IdentityId,
        source: IdentityEvidenceSource,
        reference: String,
        timestamp: Long,
    ): IdentityEvidence {
        return IdentityEvidence.create(
            claimedIdentityId = identityId,
            source = source,
            observedAt = DevilTimestamp.fromEpochMilliseconds(timestamp),
            reference = reference,
        )
    }
}
