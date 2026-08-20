package com.devil.core.model.financial

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertSame

class BusinessAccountingAssistanceStage154Test {

    @Test
    fun `record preserves exact Stage 153 accounting foundation and normalized assistance metadata`() {
        val foundation = accountingFoundation()

        val record =
            BusinessAccountingAssistanceRecord.create(
                accountingFoundation = foundation,
                businessAccountingFocus = "  Revenue and expense review  ",
                assistanceObjective =
                    "  Support bounded business accounting understanding.  ",
                assistanceApproach =
                    "  Use supplied accounting context without posting entries.  ",
            )

        assertSame(foundation, record.accountingFoundation)
        assertEquals(
            "Revenue and expense review",
            record.businessAccountingFocus,
        )
        assertEquals(
            "Support bounded business accounting understanding.",
            record.assistanceObjective,
        )
        assertEquals(
            "Use supplied accounting context without posting entries.",
            record.assistanceApproach,
        )
    }

    @Test
    fun `record rejects blank business accounting focus`() {
        assertFailsWith<IllegalArgumentException> {
            BusinessAccountingAssistanceRecord.create(
                accountingFoundation = accountingFoundation(),
                businessAccountingFocus = "   ",
                assistanceObjective = "Support bounded accounting understanding.",
                assistanceApproach = "Use supplied accounting context.",
            )
        }
    }

    @Test
    fun `record rejects blank assistance objective`() {
        assertFailsWith<IllegalArgumentException> {
            BusinessAccountingAssistanceRecord.create(
                accountingFoundation = accountingFoundation(),
                businessAccountingFocus = "Revenue and expense review",
                assistanceObjective = "   ",
                assistanceApproach = "Use supplied accounting context.",
            )
        }
    }

    @Test
    fun `record rejects blank assistance approach`() {
        assertFailsWith<IllegalArgumentException> {
            BusinessAccountingAssistanceRecord.create(
                accountingFoundation = accountingFoundation(),
                businessAccountingFocus = "Revenue and expense review",
                assistanceObjective = "Support bounded accounting understanding.",
                assistanceApproach = "   ",
            )
        }
    }

    private fun accountingFoundation(): AccountingFoundationRecord {
        return AccountingFoundationRecord.create(
            financialIntelligenceIntegration =
                FinancialIntelligenceIntegrationRecord.create(
                    financialAnalysis =
                        FinancialAnalysisRecord.create(
                            subject =
                                FinancialAnalysisSubject.from(
                                    "Business revenue and expenses",
                                ),
                            facts =
                                listOf(
                                    FinancialFact.create(
                                        label = "Revenue",
                                        value = "120000 INR",
                                    ),
                                    FinancialFact.create(
                                        label = "Expenses",
                                        value = "70000 INR",
                                    ),
                                ),
                        ),
                    integrationFocus =
                        "Bounded financial intelligence integration",
                    integrationObjective =
                        "Preserve Stage 89 financial-domain provenance.",
                ),
            accountingFocus = "Basic bookkeeping structure",
            accountingObjective = "Establish bounded accounting context.",
            accountingBasisDescription =
                "Use explicitly supplied financial facts only.",
        )
    }
}
