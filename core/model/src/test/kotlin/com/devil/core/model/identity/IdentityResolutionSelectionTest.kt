package com.devil.core.model.identity

import com.devil.core.model.common.DevilTimestamp
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class IdentityResolutionSelectionTest {

    @Test
    fun `create preserves and normalizes identity selection`() {
        val candidate = createCandidate()
        val confidence = IdentityConfidence.from(84)

        val selection = IdentityResolutionSelection.create(
            candidate = candidate,
            confidence = confidence,
            rationale = "  Device and session evidence identify the same subject.  ",
        )

        assertEquals(candidate, selection.candidate)
        assertEquals(confidence, selection.confidence)
        assertEquals(
            "Device and session evidence identify the same subject.",
            selection.rationale,
        )
    }

    @Test
    fun `create preserves zero confidence without implying resolution`() {
        val selection = IdentityResolutionSelection.create(
            candidate = createCandidate(),
            confidence = IdentityConfidence.from(0),
            rationale = "Candidate retained for unresolved comparison.",
        )

        assertEquals(0, selection.confidence.value)
    }

    @Test
    fun `create rejects blank selection rationale`() {
        assertFailsWith<IllegalArgumentException> {
            IdentityResolutionSelection.create(
                candidate = createCandidate(),
                confidence = IdentityConfidence.from(50),
                rationale = "   ",
            )
        }
    }

    private fun createCandidate(): IdentityResolutionCandidate {
        val identityId = IdentityId.from(
            "subject-resolution-selection-001",
        )

        val evidenceSet = IdentityEvidenceSet.create(
            claimedIdentityId = identityId,
            evidence = listOf(
                IdentityEvidence.create(
                    claimedIdentityId = identityId,
                    source = IdentityEvidenceSource.DEVICE,
                    observedAt = DevilTimestamp.fromEpochMilliseconds(
                        1_754_000_035_000L,
                    ),
                    reference = "selection-device-evidence",
                ),
                IdentityEvidence.create(
                    claimedIdentityId = identityId,
                    source = IdentityEvidenceSource.SESSION,
                    observedAt = DevilTimestamp.fromEpochMilliseconds(
                        1_754_000_035_500L,
                    ),
                    reference = "selection-session-evidence",
                ),
            ),
        )

        return IdentityResolutionCandidate.create(
            identityId = identityId,
            evidenceSet = evidenceSet,
        )
    }
}
