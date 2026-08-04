package com.devil.core.model.identity

import com.devil.core.model.common.DevilTimestamp
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class IdentityEvidenceTest {

    @Test
    fun `create preserves and normalizes identity evidence`() {
        val identityId = IdentityId.from("subject-001")
        val observedAt = DevilTimestamp.fromEpochMilliseconds(
            1_754_000_026_000L,
        )

        val evidence = IdentityEvidence.create(
            claimedIdentityId = identityId,
            source = IdentityEvidenceSource.DEVICE,
            observedAt = observedAt,
            reference = "  local-device-owner-profile  ",
        )

        assertEquals(identityId, evidence.claimedIdentityId)
        assertEquals(IdentityEvidenceSource.DEVICE, evidence.source)
        assertEquals(observedAt, evidence.observedAt)
        assertEquals("local-device-owner-profile", evidence.reference)
    }

    @Test
    fun `create preserves declared identity evidence without proving identity`() {
        val evidence = IdentityEvidence.create(
            claimedIdentityId = IdentityId.from("subject-declared-001"),
            source = IdentityEvidenceSource.DECLARED,
            observedAt = DevilTimestamp.fromEpochMilliseconds(
                1_754_000_026_500L,
            ),
            reference = "conversation-declaration-001",
        )

        assertEquals(
            IdentityEvidenceSource.DECLARED,
            evidence.source,
        )
        assertEquals(
            "subject-declared-001",
            evidence.claimedIdentityId.value,
        )
    }

    @Test
    fun `create rejects blank evidence reference`() {
        assertFailsWith<IllegalArgumentException> {
            IdentityEvidence.create(
                claimedIdentityId = IdentityId.from("subject-002"),
                source = IdentityEvidenceSource.TEST,
                observedAt = DevilTimestamp.fromEpochMilliseconds(
                    1_754_000_027_000L,
                ),
                reference = "   ",
            )
        }
    }
}
