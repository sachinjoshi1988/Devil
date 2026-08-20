package com.devil.core.runtime.financial

import com.devil.core.model.common.TraceId
import com.devil.core.model.financial.FinancialAnalysisRecord
import com.devil.core.model.financial.FinancialAnalysisSubject
import com.devil.core.model.financial.FinancialFact
import com.devil.core.model.financial.FinancialIntelligenceIntegrationRecord
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertSame

class Stage158FinancialSafetyVerificationGovernanceTest {

    @Test
    fun `coordinator prepares bounded financial safety verification context`() {
        val integration = financialIntegration()
        val traceId =
            TraceId.from("trace:stage158-prepared")

        val result =
            FinancialSafetyVerificationCoordinator().prepare(
                traceId = traceId,
                financialIntelligenceIntegration = integration,
                safetyFocus = "Supplied transaction-safety context",
                verificationBasisDescription =
                    "User supplied the relevant financial context.",
                safetyInterpretation =
                    "Treat the supplied context as unverified and require caution.",
            )

        assertEquals(
            FinancialSafetyVerificationPreparationStatus.PREPARED,
            result.status,
        )
        assertEquals(traceId, result.traceId)

        val safetyVerification =
            requireNotNull(result.safetyVerification)

        assertSame(
            integration,
            safetyVerification.financialIntelligenceIntegration,
        )
        assertEquals(
            "Supplied transaction-safety context",
            safetyVerification.safetyFocus,
        )
        assertEquals(
            "User supplied the relevant financial context.",
            safetyVerification.verificationBasisDescription,
        )
        assertEquals(
            "Treat the supplied context as unverified and require caution.",
            safetyVerification.safetyInterpretation,
        )
    }

    @Test
    fun `blank safety focus defers`() {
        val result =
            FinancialSafetyVerificationCoordinator().prepare(
                traceId = TraceId.from("trace:stage158-focus"),
                financialIntelligenceIntegration = financialIntegration(),
                safetyFocus = "   ",
                verificationBasisDescription =
                    "Supplied financial context.",
                safetyInterpretation =
                    "Use bounded safety interpretation.",
            )

        assertEquals(
            FinancialSafetyVerificationPreparationStatus.DEFERRED,
            result.status,
        )
        assertNull(result.safetyVerification)
    }

    @Test
    fun `blank verification basis description defers`() {
        val result =
            FinancialSafetyVerificationCoordinator().prepare(
                traceId = TraceId.from("trace:stage158-basis"),
                financialIntelligenceIntegration = financialIntegration(),
                safetyFocus = "Financial safety context",
                verificationBasisDescription = "   ",
                safetyInterpretation =
                    "Use bounded safety interpretation.",
            )

        assertEquals(
            FinancialSafetyVerificationPreparationStatus.DEFERRED,
            result.status,
        )
        assertNull(result.safetyVerification)
    }

    @Test
    fun `blank safety interpretation defers`() {
        val result =
            FinancialSafetyVerificationCoordinator().prepare(
                traceId = TraceId.from("trace:stage158-interpretation"),
                financialIntelligenceIntegration = financialIntegration(),
                safetyFocus = "Financial safety context",
                verificationBasisDescription =
                    "Supplied financial context.",
                safetyInterpretation = "   ",
            )

        assertEquals(
            FinancialSafetyVerificationPreparationStatus.DEFERRED,
            result.status,
        )
        assertNull(result.safetyVerification)
    }

    @Test
    fun `prepared result requires safety verification context`() {
        assertFailsWith<IllegalArgumentException> {
            FinancialSafetyVerificationPreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace:stage158-invalid-prepared",
                    ),
                status =
                    FinancialSafetyVerificationPreparationStatus.PREPARED,
            )
        }
    }

    @Test
    fun `deferred result cannot contain safety verification context`() {
        val prepared =
            FinancialSafetyVerificationCoordinator().prepare(
                traceId = TraceId.from("trace:stage158-source"),
                financialIntelligenceIntegration = financialIntegration(),
                safetyFocus = "Financial safety context",
                verificationBasisDescription =
                    "Supplied financial context.",
                safetyInterpretation =
                    "Use bounded safety interpretation.",
            )

        val safetyVerification =
            requireNotNull(prepared.safetyVerification)

        assertFailsWith<IllegalArgumentException> {
            FinancialSafetyVerificationPreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace:stage158-invalid-deferred",
                    ),
                status =
                    FinancialSafetyVerificationPreparationStatus.DEFERRED,
                safetyVerification = safetyVerification,
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
