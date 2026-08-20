package com.devil.core.model.financial

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertSame

class AccountingFoundationStage153Test {

    @Test
    fun `record preserves exact Stage 151 integration and normalized accounting metadata`() {
        val integration = financialIntegration()

        val record =
            AccountingFoundationRecord.create(
                financialIntelligenceIntegration = integration,
                accountingFocus = "  Basic bookkeeping structure  ",
                accountingObjective =
                    "  Establish bounded accounting context.  ",
                accountingBasisDescription =
                    "  Use explicitly supplied financial facts only.  ",
            )

        assertSame(
            integration,
            record.financialIntelligenceIntegration,
        )
        assertEquals(
            "Basic bookkeeping structure",
            record.accountingFocus,
        )
        assertEquals(
            "Establish bounded accounting context.",
            record.accountingObjective,
        )
        assertEquals(
            "Use explicitly supplied financial facts only.",
            record.accountingBasisDescription,
        )
    }

    @Test
    fun `record rejects blank accounting focus`() {
        assertFailsWith<IllegalArgumentException> {
            AccountingFoundationRecord.create(
                financialIntelligenceIntegration = financialIntegration(),
                accountingFocus = "   ",
                accountingObjective = "Establish bounded accounting context.",
                accountingBasisDescription = "Use supplied financial facts.",
            )
        }
    }

    @Test
    fun `record rejects blank accounting objective`() {
        assertFailsWith<IllegalArgumentException> {
            AccountingFoundationRecord.create(
                financialIntelligenceIntegration = financialIntegration(),
                accountingFocus = "Basic bookkeeping structure",
                accountingObjective = "   ",
                accountingBasisDescription = "Use supplied financial facts.",
            )
        }
    }

    @Test
    fun `record rejects blank accounting basis description`() {
        assertFailsWith<IllegalArgumentException> {
            AccountingFoundationRecord.create(
                financialIntelligenceIntegration = financialIntegration(),
                accountingFocus = "Basic bookkeeping structure",
                accountingObjective = "Establish bounded accounting context.",
                accountingBasisDescription = "   ",
            )
        }
    }

    private fun financialIntegration(): FinancialIntelligenceIntegrationRecord {
        return FinancialIntelligenceIntegrationRecord.create(
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
        )
    }
}
