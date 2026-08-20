package com.devil.core.model.legal

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertSame

class LegalDocumentUnderstandingStage161Test {

    @Test
    fun `record preserves exact Stage 159 foundation and normalized document metadata`() {
        val foundation = legalFoundation()

        val record =
            LegalDocumentUnderstandingRecord.create(
                legalFoundation = foundation,
                documentFocus = "  Supplied agreement context  ",
                suppliedLegalDocumentDescription =
                    "  User supplied a description of a legal agreement.  ",
                interpretationObjective =
                    "  Preserve bounded legal-document context.  ",
            )

        assertSame(
            foundation,
            record.legalFoundation,
        )
        assertEquals(
            "Supplied agreement context",
            record.documentFocus,
        )
        assertEquals(
            "User supplied a description of a legal agreement.",
            record.suppliedLegalDocumentDescription,
        )
        assertEquals(
            "Preserve bounded legal-document context.",
            record.interpretationObjective,
        )
    }

    @Test
    fun `record rejects blank document focus`() {
        assertFailsWith<IllegalArgumentException> {
            LegalDocumentUnderstandingRecord.create(
                legalFoundation = legalFoundation(),
                documentFocus = "   ",
                suppliedLegalDocumentDescription =
                    "User supplied a legal-document description.",
                interpretationObjective =
                    "Preserve bounded legal-document context.",
            )
        }
    }

    @Test
    fun `record rejects blank supplied legal document description`() {
        assertFailsWith<IllegalArgumentException> {
            LegalDocumentUnderstandingRecord.create(
                legalFoundation = legalFoundation(),
                documentFocus = "Supplied legal document",
                suppliedLegalDocumentDescription = "   ",
                interpretationObjective =
                    "Preserve bounded legal-document context.",
            )
        }
    }

    @Test
    fun `record rejects blank interpretation objective`() {
        assertFailsWith<IllegalArgumentException> {
            LegalDocumentUnderstandingRecord.create(
                legalFoundation = legalFoundation(),
                documentFocus = "Supplied legal document",
                suppliedLegalDocumentDescription =
                    "User supplied a legal-document description.",
                interpretationObjective = "   ",
            )
        }
    }

    private fun legalFoundation(): LegalIntelligenceFoundationRecord {
        return LegalIntelligenceFoundationRecord.create(
            legalSubject = "Contract interpretation context",
            legalObjective =
                "Preserve bounded legal-domain context.",
            suppliedLegalContext =
                listOf(
                    "Agreement context was supplied by the user.",
                    "No legal effect has been established.",
                ),
        )
    }
}
