package com.devil.core.model.identity

import com.devil.core.model.common.DevilTimestamp
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class IdentityResolutionRecordTest {

    @Test
    fun `create preserves resolved record with selection from candidate set`() {
        val candidate = createCandidate(
            identityValue = "subject-resolution-record-001",
            reference = "record-evidence-001",
            timestamp = 1_754_000_036_000L,
        )
        val candidateSet = IdentityResolutionCandidateSet.create(
            candidates = listOf(candidate),
        )
        val selection = IdentityResolutionSelection.create(
            candidate = candidate,
            confidence = IdentityConfidence.from(91),
            rationale = "Evidence consistently identifies one subject.",
        )

        val record = IdentityResolutionRecord.create(
            candidateSet = candidateSet,
            state = IdentityResolutionState.RESOLVED,
            selection = selection,
            rationale = "  One candidate was selected from consistent evidence.  ",
        )

        assertEquals(candidateSet, record.candidateSet)
        assertEquals(IdentityResolutionState.RESOLVED, record.state)
        assertEquals(selection, record.selection)
        assertEquals(
            "One candidate was selected from consistent evidence.",
            record.rationale,
        )
    }

    @Test
    fun `create preserves unresolved record without selection`() {
        val candidateSet = IdentityResolutionCandidateSet.create(
            candidates = listOf(
                createCandidate(
                    identityValue = "subject-resolution-record-002",
                    reference = "record-evidence-002",
                    timestamp = 1_754_000_036_500L,
                ),
            ),
        )

        val record = IdentityResolutionRecord.create(
            candidateSet = candidateSet,
            state = IdentityResolutionState.UNRESOLVED,
            rationale = "Evidence is insufficient for resolution.",
        )

        assertEquals(IdentityResolutionState.UNRESOLVED, record.state)
        assertNull(record.selection)
    }

    @Test
    fun `create preserves ambiguous record without selection`() {
        val candidateSet = IdentityResolutionCandidateSet.create(
            candidates = listOf(
                createCandidate(
                    identityValue = "subject-resolution-record-003",
                    reference = "record-evidence-003",
                    timestamp = 1_754_000_037_000L,
                ),
                createCandidate(
                    identityValue = "subject-resolution-record-004",
                    reference = "record-evidence-004",
                    timestamp = 1_754_000_037_500L,
                ),
            ),
        )

        val record = IdentityResolutionRecord.create(
            candidateSet = candidateSet,
            state = IdentityResolutionState.AMBIGUOUS,
            rationale = "Multiple candidates remain plausible.",
        )

        assertEquals(IdentityResolutionState.AMBIGUOUS, record.state)
        assertNull(record.selection)
    }

    @Test
    fun `create rejects resolved record without selection`() {
        assertFailsWith<IllegalArgumentException> {
            IdentityResolutionRecord.create(
                candidateSet = IdentityResolutionCandidateSet.create(
                    candidates = listOf(
                        createCandidate(
                            identityValue = "subject-resolution-record-005",
                            reference = "record-evidence-005",
                            timestamp = 1_754_000_038_000L,
                        ),
                    ),
                ),
                state = IdentityResolutionState.RESOLVED,
                rationale = "Invalid resolved record.",
            )
        }
    }

    @Test
    fun `create rejects resolved selection outside candidate set`() {
        val includedCandidate = createCandidate(
            identityValue = "subject-resolution-record-006",
            reference = "record-evidence-006",
            timestamp = 1_754_000_038_500L,
        )
        val outsideCandidate = createCandidate(
            identityValue = "subject-resolution-record-outside",
            reference = "record-evidence-outside",
            timestamp = 1_754_000_039_000L,
        )

        assertFailsWith<IllegalArgumentException> {
            IdentityResolutionRecord.create(
                candidateSet = IdentityResolutionCandidateSet.create(
                    candidates = listOf(includedCandidate),
                ),
                state = IdentityResolutionState.RESOLVED,
                selection = IdentityResolutionSelection.create(
                    candidate = outsideCandidate,
                    confidence = IdentityConfidence.from(80),
                    rationale = "Outside candidate selection.",
                ),
                rationale = "Invalid candidate membership.",
            )
        }
    }

    @Test
    fun `create rejects unresolved record with selection`() {
        val candidate = createCandidate(
            identityValue = "subject-resolution-record-007",
            reference = "record-evidence-007",
            timestamp = 1_754_000_039_500L,
        )

        assertFailsWith<IllegalArgumentException> {
            IdentityResolutionRecord.create(
                candidateSet = IdentityResolutionCandidateSet.create(
                    candidates = listOf(candidate),
                ),
                state = IdentityResolutionState.UNRESOLVED,
                selection = IdentityResolutionSelection.create(
                    candidate = candidate,
                    confidence = IdentityConfidence.from(40),
                    rationale = "Selection must not be retained.",
                ),
                rationale = "Invalid unresolved record.",
            )
        }
    }

    @Test
    fun `create rejects blank rationale`() {
        assertFailsWith<IllegalArgumentException> {
            IdentityResolutionRecord.create(
                candidateSet = IdentityResolutionCandidateSet.create(
                    candidates = listOf(
                        createCandidate(
                            identityValue = "subject-resolution-record-008",
                            reference = "record-evidence-008",
                            timestamp = 1_754_000_040_000L,
                        ),
                    ),
                ),
                state = IdentityResolutionState.UNRESOLVED,
                rationale = "   ",
            )
        }
    }

    private fun createCandidate(
        identityValue: String,
        reference: String,
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
                        reference = reference,
                    ),
                ),
            ),
        )
    }
}
