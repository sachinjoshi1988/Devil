package com.devil.core.model.legal

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertSame

class LegalResearchStage160Test {

    @Test
    fun `record preserves exact Stage 159 foundation and normalized research metadata`() {
        val foundation = legalFoundation()

        val record =
            LegalResearchRecord.create(
                legalFoundation = foundation,
                researchFocus = "  Supplied case-law research context  ",
                suppliedLegalSourceDescription =
                    "  User supplied a description of a legal source.  ",
                researchObjective =
                    "  Preserve bounded legal-research context.  ",
            )

        assertSame(
            foundation,
            record.legalFoundation,
        )
        assertEquals(
            "Supplied case-law research context",
            record.researchFocus,
        )
        assertEquals(
            "User supplied a description of a legal source.",
            record.suppliedLegalSourceDescription,
        )
        assertEquals(
            "Preserve bounded legal-research context.",
            record.researchObjective,
        )
    }

    @Test
    fun `record rejects blank research focus`() {
        assertFailsWith<IllegalArgumentException> {
            LegalResearchRecord.create(
                legalFoundation = legalFoundation(),
                researchFocus = "   ",
                suppliedLegalSourceDescription =
                    "User supplied a legal-source description.",
                researchObjective =
                    "Preserve bounded legal research context.",
            )
        }
    }

    @Test
    fun `record rejects blank supplied legal source description`() {
        assertFailsWith<IllegalArgumentException> {
            LegalResearchRecord.create(
                legalFoundation = legalFoundation(),
                researchFocus = "Legal research context",
                suppliedLegalSourceDescription = "   ",
                researchObjective =
                    "Preserve bounded legal research context.",
            )
        }
    }

    @Test
    fun `record rejects blank research objective`() {
        assertFailsWith<IllegalArgumentException> {
            LegalResearchRecord.create(
                legalFoundation = legalFoundation(),
                researchFocus = "Legal research context",
                suppliedLegalSourceDescription =
                    "User supplied a legal-source description.",
                researchObjective = "   ",
            )
        }
    }

    private fun legalFoundation(): LegalIntelligenceFoundationRecord {
        return LegalIntelligenceFoundationRecord.create(
            legalSubject = "Contract dispute context",
            legalObjective = "Preserve bounded legal-domain context.",
            suppliedLegalContext =
                listOf(
                    "Agreement was supplied by the user.",
                    "No jurisdiction has been established.",
                ),
        )
    }
}
