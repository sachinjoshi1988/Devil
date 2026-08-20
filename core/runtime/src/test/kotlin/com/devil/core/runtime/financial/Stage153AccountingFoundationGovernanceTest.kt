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

class Stage153AccountingFoundationGovernanceTest {

    @Test
    fun `coordinator prepares bounded accounting foundation`() {
        val integration = financialIntegration()
        val traceId = TraceId.from("trace:stage153-prepared")

        val result =
            AccountingFoundationCoordinator().prepare(
                traceId = traceId,
                financialIntelligenceIntegration = integration,
                accountingFocus = "Basic bookkeeping structure",
                accountingObjective =
                    "Establish bounded accounting context.",
                accountingBasisDescription =
                    "Use explicitly supplied financial facts only.",
            )

        assertEquals(
            AccountingFoundationPreparationStatus.PREPARED,
            result.status,
        )
        assertEquals(traceId, result.traceId)

        val accountingFoundation =
            requireNotNull(result.accountingFoundation)

        assertSame(
            integration,
            accountingFoundation.financialIntelligenceIntegration,
        )
        assertEquals(
            "Basic bookkeeping structure",
            accountingFoundation.accountingFocus,
        )
        assertEquals(
            "Establish bounded accounting context.",
            accountingFoundation.accountingObjective,
        )
        assertEquals(
            "Use explicitly supplied financial facts only.",
            accountingFoundation.accountingBasisDescription,
        )
    }

    @Test
    fun `blank accounting focus defers`() {
        val result =
            AccountingFoundationCoordinator().prepare(
                traceId = TraceId.from("trace:stage153-focus"),
                financialIntelligenceIntegration = financialIntegration(),
                accountingFocus = "   ",
                accountingObjective = "Establish bounded accounting context.",
                accountingBasisDescription = "Use supplied financial facts.",
            )

        assertEquals(
            AccountingFoundationPreparationStatus.DEFERRED,
            result.status,
        )
        assertNull(result.accountingFoundation)
    }

    @Test
    fun `blank accounting objective defers`() {
        val result =
            AccountingFoundationCoordinator().prepare(
                traceId = TraceId.from("trace:stage153-objective"),
                financialIntelligenceIntegration = financialIntegration(),
                accountingFocus = "Basic bookkeeping structure",
                accountingObjective = "   ",
                accountingBasisDescription = "Use supplied financial facts.",
            )

        assertEquals(
            AccountingFoundationPreparationStatus.DEFERRED,
            result.status,
        )
        assertNull(result.accountingFoundation)
    }

    @Test
    fun `blank accounting basis description defers`() {
        val result =
            AccountingFoundationCoordinator().prepare(
                traceId = TraceId.from("trace:stage153-basis"),
                financialIntelligenceIntegration = financialIntegration(),
                accountingFocus = "Basic bookkeeping structure",
                accountingObjective = "Establish bounded accounting context.",
                accountingBasisDescription = "   ",
            )

        assertEquals(
            AccountingFoundationPreparationStatus.DEFERRED,
            result.status,
        )
        assertNull(result.accountingFoundation)
    }

    @Test
    fun `prepared result requires accounting foundation context`() {
        assertFailsWith<IllegalArgumentException> {
            AccountingFoundationPreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace:stage153-invalid-prepared",
                    ),
                status =
                    AccountingFoundationPreparationStatus.PREPARED,
            )
        }
    }

    @Test
    fun `deferred result cannot contain accounting foundation context`() {
        val prepared =
            AccountingFoundationCoordinator().prepare(
                traceId = TraceId.from("trace:stage153-source"),
                financialIntelligenceIntegration = financialIntegration(),
                accountingFocus = "Basic bookkeeping structure",
                accountingObjective = "Establish bounded accounting context.",
                accountingBasisDescription = "Use supplied financial facts.",
            )

        val accountingFoundation =
            requireNotNull(prepared.accountingFoundation)

        assertFailsWith<IllegalArgumentException> {
            AccountingFoundationPreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace:stage153-invalid-deferred",
                    ),
                status =
                    AccountingFoundationPreparationStatus.DEFERRED,
                accountingFoundation = accountingFoundation,
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
