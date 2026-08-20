package com.devil.core.model.legal

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertSame

class LegalDraftingAssistanceStage162Test {

    @Test
    fun `record preserves exact Stage 159 foundation and normalized drafting metadata`() {
        val foundation = legalFoundation()

        val record =
            LegalDraftingAssistanceRecord.create(
                legalFoundation = foundation,
                draftingFocus = "  Agreement drafting assistance  ",
                requestedDraftPurpose =
                    "  Prepare a bounded non-authoritative agreement draft context.  ",
                draftingObjective =
                    "  Preserve user-supplied drafting intent without legal conclusions.  ",
            )

        assertSame(
            foundation,
            record.legalFoundation,
        )
        assertEquals(
            "Agreement drafting assistance",
            record.draftingFocus,
        )
        assertEquals(
            "Prepare a bounded non-authoritative agreement draft context.",
            record.requestedDraftPurpose,
        )
        assertEquals(
            "Preserve user-supplied drafting intent without legal conclusions.",
            record.draftingObjective,
        )
    }

    @Test
    fun `record rejects blank drafting focus`() {
        assertFailsWith<IllegalArgumentException> {
            LegalDraftingAssistanceRecord.create(
                legalFoundation = legalFoundation(),
                draftingFocus = "   ",
                requestedDraftPurpose =
                    "Prepare a bounded draft context.",
                draftingObjective =
                    "Preserve drafting intent.",
            )
        }
    }

    @Test
    fun `record rejects blank requested draft purpose`() {
        assertFailsWith<IllegalArgumentException> {
            LegalDraftingAssistanceRecord.create(
                legalFoundation = legalFoundation(),
                draftingFocus = "Agreement drafting assistance",
                requestedDraftPurpose = "   ",
                draftingObjective =
                    "Preserve drafting intent.",
            )
        }
    }

    @Test
    fun `record rejects blank drafting objective`() {
        assertFailsWith<IllegalArgumentException> {
            LegalDraftingAssistanceRecord.create(
                legalFoundation = legalFoundation(),
                draftingFocus = "Agreement drafting assistance",
                requestedDraftPurpose =
                    "Prepare a bounded draft context.",
                draftingObjective = "   ",
            )
        }
    }

    private fun legalFoundation(): LegalIntelligenceFoundationRecord {
        return LegalIntelligenceFoundationRecord.create(
            legalSubject = "Agreement drafting context",
            legalObjective =
                "Preserve bounded legal-domain context.",
            suppliedLegalContext =
                listOf(
                    "Drafting intent was supplied by the user.",
                    "No legal effect has been established.",
                ),
        )
    }
}
