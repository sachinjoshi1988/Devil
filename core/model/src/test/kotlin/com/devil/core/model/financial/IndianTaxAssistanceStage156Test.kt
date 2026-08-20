package com.devil.core.model.financial

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertSame

class IndianTaxAssistanceStage156Test {

    @Test
    fun `record preserves exact Stage 155 foundation and normalized India tax metadata`() {
        val foundation = taxFoundation()

        val record =
            IndianTaxAssistanceRecord.create(
                taxIntelligenceFoundation = foundation,
                indianTaxFocus = "  Indian income-tax context  ",
                assistanceObjective =
                    "  Support bounded understanding of supplied Indian tax context.  ",
                indiaTaxContextDescription =
                    "  Use explicitly supplied India-tax information only.  ",
            )

        assertSame(
            foundation,
            record.taxIntelligenceFoundation,
        )
        assertEquals(
            "Indian income-tax context",
            record.indianTaxFocus,
        )
        assertEquals(
            "Support bounded understanding of supplied Indian tax context.",
            record.assistanceObjective,
        )
        assertEquals(
            "Use explicitly supplied India-tax information only.",
            record.indiaTaxContextDescription,
        )
    }

    @Test
    fun `record rejects blank Indian tax focus`() {
        assertFailsWith<IllegalArgumentException> {
            IndianTaxAssistanceRecord.create(
                taxIntelligenceFoundation = taxFoundation(),
                indianTaxFocus = "   ",
                assistanceObjective = "Support bounded Indian-tax understanding.",
                indiaTaxContextDescription = "Use supplied India-tax context.",
            )
        }
    }

    @Test
    fun `record rejects blank assistance objective`() {
        assertFailsWith<IllegalArgumentException> {
            IndianTaxAssistanceRecord.create(
                taxIntelligenceFoundation = taxFoundation(),
                indianTaxFocus = "Indian income-tax context",
                assistanceObjective = "   ",
                indiaTaxContextDescription = "Use supplied India-tax context.",
            )
        }
    }

    @Test
    fun `record rejects blank India tax context description`() {
        assertFailsWith<IllegalArgumentException> {
            IndianTaxAssistanceRecord.create(
                taxIntelligenceFoundation = taxFoundation(),
                indianTaxFocus = "Indian income-tax context",
                assistanceObjective = "Support bounded Indian-tax understanding.",
                indiaTaxContextDescription = "   ",
            )
        }
    }

    private fun taxFoundation(): TaxIntelligenceFoundationRecord {
        return TaxIntelligenceFoundationRecord.create(
            financialIntelligenceIntegration =
                FinancialIntelligenceIntegrationRecord.create(
                    financialAnalysis =
                        FinancialAnalysisRecord.create(
                            subject =
                                FinancialAnalysisSubject.from(
                                    "India tax-related financial context",
                                ),
                            facts =
                                listOf(
                                    FinancialFact.create(
                                        label = "Income context",
                                        value = "Supplied by user",
                                    ),
                                    FinancialFact.create(
                                        label = "Tax context",
                                        value = "India",
                                    ),
                                ),
                        ),
                    integrationFocus =
                        "Bounded financial intelligence integration",
                    integrationObjective =
                        "Preserve Stage 89 financial-domain provenance.",
                ),
            taxFocus = "General tax context",
            taxObjective = "Establish bounded tax-intelligence context.",
            taxContextDescription =
                "Use explicitly supplied financial information only.",
        )
    }
}
