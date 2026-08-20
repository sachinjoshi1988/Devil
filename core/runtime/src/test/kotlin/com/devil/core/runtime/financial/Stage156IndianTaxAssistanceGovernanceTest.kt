package com.devil.core.runtime.financial

import com.devil.core.model.common.TraceId
import com.devil.core.model.financial.FinancialAnalysisRecord
import com.devil.core.model.financial.FinancialAnalysisSubject
import com.devil.core.model.financial.FinancialFact
import com.devil.core.model.financial.FinancialIntelligenceIntegrationRecord
import com.devil.core.model.financial.TaxIntelligenceFoundationRecord
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertSame

class Stage156IndianTaxAssistanceGovernanceTest {

    @Test
    fun `coordinator prepares bounded Indian tax assistance`() {
        val foundation = taxFoundation()
        val traceId = TraceId.from("trace:stage156-prepared")

        val result =
            IndianTaxAssistanceCoordinator().prepare(
                traceId = traceId,
                taxIntelligenceFoundation = foundation,
                indianTaxFocus = "Indian income-tax context",
                assistanceObjective =
                    "Support bounded understanding of supplied Indian tax context.",
                indiaTaxContextDescription =
                    "Use explicitly supplied India-tax information only.",
            )

        assertEquals(
            IndianTaxAssistancePreparationStatus.PREPARED,
            result.status,
        )
        assertEquals(traceId, result.traceId)

        val assistance =
            requireNotNull(result.assistance)

        assertSame(
            foundation,
            assistance.taxIntelligenceFoundation,
        )
        assertEquals(
            "Indian income-tax context",
            assistance.indianTaxFocus,
        )
        assertEquals(
            "Support bounded understanding of supplied Indian tax context.",
            assistance.assistanceObjective,
        )
        assertEquals(
            "Use explicitly supplied India-tax information only.",
            assistance.indiaTaxContextDescription,
        )
    }

    @Test
    fun `blank Indian tax focus defers`() {
        val result =
            IndianTaxAssistanceCoordinator().prepare(
                traceId = TraceId.from("trace:stage156-focus"),
                taxIntelligenceFoundation = taxFoundation(),
                indianTaxFocus = "   ",
                assistanceObjective = "Support bounded Indian-tax understanding.",
                indiaTaxContextDescription = "Use supplied India-tax context.",
            )

        assertEquals(
            IndianTaxAssistancePreparationStatus.DEFERRED,
            result.status,
        )
        assertNull(result.assistance)
    }

    @Test
    fun `blank assistance objective defers`() {
        val result =
            IndianTaxAssistanceCoordinator().prepare(
                traceId = TraceId.from("trace:stage156-objective"),
                taxIntelligenceFoundation = taxFoundation(),
                indianTaxFocus = "Indian income-tax context",
                assistanceObjective = "   ",
                indiaTaxContextDescription = "Use supplied India-tax context.",
            )

        assertEquals(
            IndianTaxAssistancePreparationStatus.DEFERRED,
            result.status,
        )
        assertNull(result.assistance)
    }

    @Test
    fun `blank India tax context description defers`() {
        val result =
            IndianTaxAssistanceCoordinator().prepare(
                traceId = TraceId.from("trace:stage156-context"),
                taxIntelligenceFoundation = taxFoundation(),
                indianTaxFocus = "Indian income-tax context",
                assistanceObjective = "Support bounded Indian-tax understanding.",
                indiaTaxContextDescription = "   ",
            )

        assertEquals(
            IndianTaxAssistancePreparationStatus.DEFERRED,
            result.status,
        )
        assertNull(result.assistance)
    }

    @Test
    fun `prepared result requires assistance context`() {
        assertFailsWith<IllegalArgumentException> {
            IndianTaxAssistancePreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace:stage156-invalid-prepared",
                    ),
                status =
                    IndianTaxAssistancePreparationStatus.PREPARED,
            )
        }
    }

    @Test
    fun `deferred result cannot contain assistance context`() {
        val prepared =
            IndianTaxAssistanceCoordinator().prepare(
                traceId = TraceId.from("trace:stage156-source"),
                taxIntelligenceFoundation = taxFoundation(),
                indianTaxFocus = "Indian income-tax context",
                assistanceObjective = "Support bounded Indian-tax understanding.",
                indiaTaxContextDescription = "Use supplied India-tax context.",
            )

        val assistance =
            requireNotNull(prepared.assistance)

        assertFailsWith<IllegalArgumentException> {
            IndianTaxAssistancePreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace:stage156-invalid-deferred",
                    ),
                status =
                    IndianTaxAssistancePreparationStatus.DEFERRED,
                assistance = assistance,
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
