package com.devil.core.model.financial

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertSame

class FinancialDocumentIntelligenceStage157Test {

    @Test
    fun `record preserves exact Stage 151 integration and normalized document metadata`() {
        val integration = financialIntegration()

        val record =
            FinancialDocumentIntelligenceRecord.create(
                financialIntelligenceIntegration = integration,
                documentFocus = "  Supplied bank statement context  ",
                suppliedDocumentDescription =
                    "  User describes a monthly financial statement.  ",
                interpretationObjective =
                    "  Preserve bounded supplied document context.  ",
            )

        assertSame(
            integration,
            record.financialIntelligenceIntegration,
        )
        assertEquals(
            "Supplied bank statement context",
            record.documentFocus,
        )
        assertEquals(
            "User describes a monthly financial statement.",
            record.suppliedDocumentDescription,
        )
        assertEquals(
            "Preserve bounded supplied document context.",
            record.interpretationObjective,
        )
    }

    @Test
    fun `record rejects blank document focus`() {
        assertFailsWith<IllegalArgumentException> {
            FinancialDocumentIntelligenceRecord.create(
                financialIntelligenceIntegration = financialIntegration(),
                documentFocus = "   ",
                suppliedDocumentDescription =
                    "User-supplied financial document description.",
                interpretationObjective =
                    "Preserve bounded document context.",
            )
        }
    }

    @Test
    fun `record rejects blank supplied document description`() {
        assertFailsWith<IllegalArgumentException> {
            FinancialDocumentIntelligenceRecord.create(
                financialIntelligenceIntegration = financialIntegration(),
                documentFocus = "Supplied financial document",
                suppliedDocumentDescription = "   ",
                interpretationObjective =
                    "Preserve bounded document context.",
            )
        }
    }

    @Test
    fun `record rejects blank interpretation objective`() {
        assertFailsWith<IllegalArgumentException> {
            FinancialDocumentIntelligenceRecord.create(
                financialIntelligenceIntegration = financialIntegration(),
                documentFocus = "Supplied financial document",
                suppliedDocumentDescription =
                    "User-supplied financial document description.",
                interpretationObjective = "   ",
            )
        }
    }

    private fun financialIntegration(): FinancialIntelligenceIntegrationRecord {
        return FinancialIntelligenceIntegrationRecord.create(
            financialAnalysis =
                FinancialAnalysisRecord.create(
                    subject =
                        FinancialAnalysisSubject.from(
                            "Financial document context",
                        ),
                    facts =
                        listOf(
                            FinancialFact.create(
                                label = "Document type",
                                value = "Bank statement",
                            ),
                            FinancialFact.create(
                                label = "Document provenance",
                                value = "Supplied by user",
                            ),
                        ),
                ),
            integrationFocus =
                "Bounded financial intelligence integration",
            integrationObjective =
                "Preserve Stage 89 financial-domain provenance.",
        )
    }
}
