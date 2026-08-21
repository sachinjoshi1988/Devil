package com.devil.core.model.legal

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertSame

class LegalEvidenceCitationStage164Test {

    @Test
    fun `record preserves exact Stage 159 foundation and normalized evidence citation metadata`() {
        val foundation = legalFoundation()

        val record =
            LegalEvidenceCitationRecord.create(
                legalFoundation = foundation,
                evidenceCitationFocus = "  Supplied legal evidence and citation context  ",
                suppliedLegalSourceEvidenceDescription =
                    "  User supplied a legal source and evidence description.  ",
                citationObjective =
                    "  Preserve bounded citation context without legal verification.  ",
            )

        assertSame(
            foundation,
            record.legalFoundation,
        )
        assertEquals(
            "Supplied legal evidence and citation context",
            record.evidenceCitationFocus,
        )
        assertEquals(
            "User supplied a legal source and evidence description.",
            record.suppliedLegalSourceEvidenceDescription,
        )
        assertEquals(
            "Preserve bounded citation context without legal verification.",
            record.citationObjective,
        )
    }

    @Test
    fun `record rejects blank evidence citation focus`() {
        assertFailsWith<IllegalArgumentException> {
            LegalEvidenceCitationRecord.create(
                legalFoundation = legalFoundation(),
                evidenceCitationFocus = "   ",
                suppliedLegalSourceEvidenceDescription =
                    "User supplied legal source and evidence context.",
                citationObjective =
                    "Preserve bounded citation context.",
            )
        }
    }

    @Test
    fun `record rejects blank legal source evidence description`() {
        assertFailsWith<IllegalArgumentException> {
            LegalEvidenceCitationRecord.create(
                legalFoundation = legalFoundation(),
                evidenceCitationFocus =
                    "Supplied legal evidence and citation context",
                suppliedLegalSourceEvidenceDescription = "   ",
                citationObjective =
                    "Preserve bounded citation context.",
            )
        }
    }

    @Test
    fun `record rejects blank citation objective`() {
        assertFailsWith<IllegalArgumentException> {
            LegalEvidenceCitationRecord.create(
                legalFoundation = legalFoundation(),
                evidenceCitationFocus =
                    "Supplied legal evidence and citation context",
                suppliedLegalSourceEvidenceDescription =
                    "User supplied legal source and evidence context.",
                citationObjective = "   ",
            )
        }
    }

    private fun legalFoundation(): LegalIntelligenceFoundationRecord {
        return LegalIntelligenceFoundationRecord.create(
            legalSubject =
                "Legal evidence and citation context",
            legalObjective =
                "Preserve bounded legal-domain context.",
            suppliedLegalContext =
                listOf(
                    "Legal source and evidence material was supplied.",
                    "No evidence authenticity or authoritative citation has been established.",
                ),
        )
    }
}
