package com.devil.core.model.financial

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertSame

class FinancialSafetyVerificationStage158Test {

    @Test
    fun `record preserves exact Stage 151 integration and normalized safety metadata`() {
        val integration = financialIntegration()

        val record =
            FinancialSafetyVerificationRecord.create(
                financialIntelligenceIntegration = integration,
                safetyFocus = "  Supplied transaction-safety context  ",
                verificationBasisDescription =
                    "  User supplied the relevant financial context.  ",
                safetyInterpretation =
                    "  Treat the supplied context as unverified and require caution.  ",
            )

        assertSame(
            integration,
            record.financialIntelligenceIntegration,
        )
        assertEquals(
            "Supplied transaction-safety context",
            record.safetyFocus,
        )
        assertEquals(
            "User supplied the relevant financial context.",
            record.verificationBasisDescription,
        )
        assertEquals(
            "Treat the supplied context as unverified and require caution.",
            record.safetyInterpretation,
        )
    }

    @Test
    fun `record rejects blank safety focus`() {
        assertFailsWith<IllegalArgumentException> {
            FinancialSafetyVerificationRecord.create(
                financialIntelligenceIntegration = financialIntegration(),
                safetyFocus = "   ",
                verificationBasisDescription =
                    "Supplied financial context.",
                safetyInterpretation =
                    "Use bounded safety interpretation.",
            )
        }
    }

    @Test
    fun `record rejects blank verification basis description`() {
        assertFailsWith<IllegalArgumentException> {
            FinancialSafetyVerificationRecord.create(
                financialIntelligenceIntegration = financialIntegration(),
                safetyFocus = "Financial safety context",
                verificationBasisDescription = "   ",
                safetyInterpretation =
                    "Use bounded safety interpretation.",
            )
        }
    }

    @Test
    fun `record rejects blank safety interpretation`() {
        assertFailsWith<IllegalArgumentException> {
            FinancialSafetyVerificationRecord.create(
                financialIntelligenceIntegration = financialIntegration(),
                safetyFocus = "Financial safety context",
                verificationBasisDescription =
                    "Supplied financial context.",
                safetyInterpretation = "   ",
            )
        }
    }

    private fun financialIntegration(): FinancialIntelligenceIntegrationRecord {
        return FinancialIntelligenceIntegrationRecord.create(
            financialAnalysis =
                FinancialAnalysisRecord.create(
                    subject =
                        FinancialAnalysisSubject.from(
                            "Financial safety context",
                        ),
                    facts =
                        listOf(
                            FinancialFact.create(
                                label = "Transaction description",
                                value = "Supplied by user",
                            ),
                            FinancialFact.create(
                                label = "Verification state",
                                value = "Not independently verified",
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
