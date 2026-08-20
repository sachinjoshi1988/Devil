package com.devil.core.model.financial

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertSame

class TaxIntelligenceFoundationStage155Test {

    @Test
    fun `record preserves exact Stage 151 integration and normalized tax metadata`() {
        val integration = financialIntegration()

        val record =
            TaxIntelligenceFoundationRecord.create(
                financialIntelligenceIntegration = integration,
                taxFocus = "  General tax context  ",
                taxObjective =
                    "  Establish bounded tax-intelligence context.  ",
                taxContextDescription =
                    "  Use explicitly supplied financial information only.  ",
            )

        assertSame(
            integration,
            record.financialIntelligenceIntegration,
        )
        assertEquals(
            "General tax context",
            record.taxFocus,
        )
        assertEquals(
            "Establish bounded tax-intelligence context.",
            record.taxObjective,
        )
        assertEquals(
            "Use explicitly supplied financial information only.",
            record.taxContextDescription,
        )
    }

    @Test
    fun `record rejects blank tax focus`() {
        assertFailsWith<IllegalArgumentException> {
            TaxIntelligenceFoundationRecord.create(
                financialIntelligenceIntegration = financialIntegration(),
                taxFocus = "   ",
                taxObjective = "Establish bounded tax context.",
                taxContextDescription = "Use supplied financial information.",
            )
        }
    }

    @Test
    fun `record rejects blank tax objective`() {
        assertFailsWith<IllegalArgumentException> {
            TaxIntelligenceFoundationRecord.create(
                financialIntelligenceIntegration = financialIntegration(),
                taxFocus = "General tax context",
                taxObjective = "   ",
                taxContextDescription = "Use supplied financial information.",
            )
        }
    }

    @Test
    fun `record rejects blank tax context description`() {
        assertFailsWith<IllegalArgumentException> {
            TaxIntelligenceFoundationRecord.create(
                financialIntelligenceIntegration = financialIntegration(),
                taxFocus = "General tax context",
                taxObjective = "Establish bounded tax context.",
                taxContextDescription = "   ",
            )
        }
    }

    private fun financialIntegration(): FinancialIntelligenceIntegrationRecord {
        return FinancialIntelligenceIntegrationRecord.create(
            financialAnalysis =
                FinancialAnalysisRecord.create(
                    subject =
                        FinancialAnalysisSubject.from(
                            "Tax-relevant financial context",
                        ),
                    facts =
                        listOf(
                            FinancialFact.create(
                                label = "Income",
                                value = "800000 INR",
                            ),
                            FinancialFact.create(
                                label = "Eligible expense context",
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
