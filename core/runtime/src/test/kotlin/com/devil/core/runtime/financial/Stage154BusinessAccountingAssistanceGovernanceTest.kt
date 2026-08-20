package com.devil.core.runtime.financial

import com.devil.core.model.common.TraceId
import com.devil.core.model.financial.AccountingFoundationRecord
import com.devil.core.model.financial.FinancialAnalysisRecord
import com.devil.core.model.financial.FinancialAnalysisSubject
import com.devil.core.model.financial.FinancialFact
import com.devil.core.model.financial.FinancialIntelligenceIntegrationRecord
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertSame

class Stage154BusinessAccountingAssistanceGovernanceTest {

    @Test
    fun `coordinator prepares bounded business accounting assistance`() {
        val foundation = accountingFoundation()
        val traceId = TraceId.from("trace:stage154-prepared")

        val result =
            BusinessAccountingAssistanceCoordinator().prepare(
                traceId = traceId,
                accountingFoundation = foundation,
                businessAccountingFocus = "Revenue and expense review",
                assistanceObjective =
                    "Support bounded business accounting understanding.",
                assistanceApproach =
                    "Use supplied accounting context without posting entries.",
            )

        assertEquals(
            BusinessAccountingAssistancePreparationStatus.PREPARED,
            result.status,
        )
        assertEquals(traceId, result.traceId)

        val assistance = requireNotNull(result.assistance)

        assertSame(foundation, assistance.accountingFoundation)
        assertEquals(
            "Revenue and expense review",
            assistance.businessAccountingFocus,
        )
        assertEquals(
            "Support bounded business accounting understanding.",
            assistance.assistanceObjective,
        )
        assertEquals(
            "Use supplied accounting context without posting entries.",
            assistance.assistanceApproach,
        )
    }

    @Test
    fun `blank business accounting focus defers`() {
        val result =
            BusinessAccountingAssistanceCoordinator().prepare(
                traceId = TraceId.from("trace:stage154-focus"),
                accountingFoundation = accountingFoundation(),
                businessAccountingFocus = "   ",
                assistanceObjective = "Support bounded accounting understanding.",
                assistanceApproach = "Use supplied accounting context.",
            )

        assertEquals(
            BusinessAccountingAssistancePreparationStatus.DEFERRED,
            result.status,
        )
        assertNull(result.assistance)
    }

    @Test
    fun `blank assistance objective defers`() {
        val result =
            BusinessAccountingAssistanceCoordinator().prepare(
                traceId = TraceId.from("trace:stage154-objective"),
                accountingFoundation = accountingFoundation(),
                businessAccountingFocus = "Revenue and expense review",
                assistanceObjective = "   ",
                assistanceApproach = "Use supplied accounting context.",
            )

        assertEquals(
            BusinessAccountingAssistancePreparationStatus.DEFERRED,
            result.status,
        )
        assertNull(result.assistance)
    }

    @Test
    fun `blank assistance approach defers`() {
        val result =
            BusinessAccountingAssistanceCoordinator().prepare(
                traceId = TraceId.from("trace:stage154-approach"),
                accountingFoundation = accountingFoundation(),
                businessAccountingFocus = "Revenue and expense review",
                assistanceObjective = "Support bounded accounting understanding.",
                assistanceApproach = "   ",
            )

        assertEquals(
            BusinessAccountingAssistancePreparationStatus.DEFERRED,
            result.status,
        )
        assertNull(result.assistance)
    }

    @Test
    fun `prepared result requires assistance context`() {
        assertFailsWith<IllegalArgumentException> {
            BusinessAccountingAssistancePreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace:stage154-invalid-prepared",
                    ),
                status =
                    BusinessAccountingAssistancePreparationStatus.PREPARED,
            )
        }
    }

    @Test
    fun `deferred result cannot contain assistance context`() {
        val prepared =
            BusinessAccountingAssistanceCoordinator().prepare(
                traceId = TraceId.from("trace:stage154-source"),
                accountingFoundation = accountingFoundation(),
                businessAccountingFocus = "Revenue and expense review",
                assistanceObjective = "Support bounded accounting understanding.",
                assistanceApproach = "Use supplied accounting context.",
            )

        val assistance = requireNotNull(prepared.assistance)

        assertFailsWith<IllegalArgumentException> {
            BusinessAccountingAssistancePreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace:stage154-invalid-deferred",
                    ),
                status =
                    BusinessAccountingAssistancePreparationStatus.DEFERRED,
                assistance = assistance,
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
