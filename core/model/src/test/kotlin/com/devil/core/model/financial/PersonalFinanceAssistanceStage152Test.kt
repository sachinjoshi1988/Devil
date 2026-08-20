package com.devil.core.model.financial

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertSame

class PersonalFinanceAssistanceStage152Test {

    @Test
    fun `record preserves exact Stage 151 integration and normalized assistance metadata`() {
        val integration = financialIntegration()

        val record =
            PersonalFinanceAssistanceRecord.create(
                financialIntelligenceIntegration = integration,
                assistanceFocus = "  Household budgeting  ",
                assistanceObjective =
                    "  Help understand income and expenses.  ",
                assistanceApproach =
                    "  Use supplied facts for bounded educational guidance.  ",
            )

        assertSame(
            integration,
            record.financialIntelligenceIntegration,
        )
        assertEquals(
            "Household budgeting",
            record.assistanceFocus,
        )
        assertEquals(
            "Help understand income and expenses.",
            record.assistanceObjective,
        )
        assertEquals(
            "Use supplied facts for bounded educational guidance.",
            record.assistanceApproach,
        )
    }

    @Test
    fun `record rejects blank assistance focus`() {
        assertFailsWith<IllegalArgumentException> {
            PersonalFinanceAssistanceRecord.create(
                financialIntelligenceIntegration = financialIntegration(),
                assistanceFocus = "   ",
                assistanceObjective = "Understand household finances.",
                assistanceApproach = "Use supplied facts.",
            )
        }
    }

    @Test
    fun `record rejects blank assistance objective`() {
        assertFailsWith<IllegalArgumentException> {
            PersonalFinanceAssistanceRecord.create(
                financialIntelligenceIntegration = financialIntegration(),
                assistanceFocus = "Household budgeting",
                assistanceObjective = "   ",
                assistanceApproach = "Use supplied facts.",
            )
        }
    }

    @Test
    fun `record rejects blank assistance approach`() {
        assertFailsWith<IllegalArgumentException> {
            PersonalFinanceAssistanceRecord.create(
                financialIntelligenceIntegration = financialIntegration(),
                assistanceFocus = "Household budgeting",
                assistanceObjective = "Understand household finances.",
                assistanceApproach = "   ",
            )
        }
    }

    private fun financialIntegration(): FinancialIntelligenceIntegrationRecord {
        return FinancialIntelligenceIntegrationRecord.create(
            financialAnalysis =
                FinancialAnalysisRecord.create(
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
                ),
            integrationFocus =
                "Bounded financial intelligence integration",
            integrationObjective =
                "Preserve Stage 89 financial-domain provenance.",
        )
    }
}
