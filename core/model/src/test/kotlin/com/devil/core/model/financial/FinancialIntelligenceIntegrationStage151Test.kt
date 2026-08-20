package com.devil.core.model.financial

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertSame

class FinancialIntelligenceIntegrationStage151Test {

    @Test
    fun `record preserves exact Stage 89 analysis and normalized integration metadata`() {
        val financialAnalysis = financialAnalysis()

        val record =
            FinancialIntelligenceIntegrationRecord.create(
                financialAnalysis = financialAnalysis,
                integrationFocus =
                    "  Bounded financial intelligence integration  ",
                integrationObjective =
                    "  Preserve Stage 89 financial-domain provenance.  ",
            )

        assertSame(financialAnalysis, record.financialAnalysis)
        assertEquals(
            "Bounded financial intelligence integration",
            record.integrationFocus,
        )
        assertEquals(
            "Preserve Stage 89 financial-domain provenance.",
            record.integrationObjective,
        )
    }

    @Test
    fun `record rejects blank integration focus`() {
        assertFailsWith<IllegalArgumentException> {
            FinancialIntelligenceIntegrationRecord.create(
                financialAnalysis = financialAnalysis(),
                integrationFocus = "   ",
                integrationObjective =
                    "Preserve bounded financial-domain provenance.",
            )
        }
    }

    @Test
    fun `record rejects blank integration objective`() {
        assertFailsWith<IllegalArgumentException> {
            FinancialIntelligenceIntegrationRecord.create(
                financialAnalysis = financialAnalysis(),
                integrationFocus =
                    "Bounded financial intelligence integration",
                integrationObjective = "   ",
            )
        }
    }

    private fun financialAnalysis(): FinancialAnalysisRecord {
        return FinancialAnalysisRecord.create(
            subject =
                FinancialAnalysisSubject.from(
                    "Household budget",
                ),
            facts =
                listOf(
                    FinancialFact.create(
                        label = "Monthly income",
                        value = "50000 INR",
                    ),
                    FinancialFact.create(
                        label = "Monthly expenses",
                        value = "32000 INR",
                    ),
                ),
        )
    }
}
