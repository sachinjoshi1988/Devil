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

class Stage155TaxIntelligenceFoundationGovernanceTest {

    @Test
    fun `coordinator prepares bounded tax intelligence foundation`() {
        val integration = financialIntegration()
        val traceId = TraceId.from("trace:stage155-prepared")

        val result =
            TaxIntelligenceFoundationCoordinator().prepare(
                traceId = traceId,
                financialIntelligenceIntegration = integration,
                taxFocus = "General tax context",
                taxObjective =
                    "Establish bounded tax-intelligence context.",
                taxContextDescription =
                    "Use explicitly supplied financial information only.",
            )

        assertEquals(
            TaxIntelligenceFoundationPreparationStatus.PREPARED,
            result.status,
        )
        assertEquals(traceId, result.traceId)

        val taxFoundation =
            requireNotNull(result.taxFoundation)

        assertSame(
            integration,
            taxFoundation.financialIntelligenceIntegration,
        )
        assertEquals(
            "General tax context",
            taxFoundation.taxFocus,
        )
        assertEquals(
            "Establish bounded tax-intelligence context.",
            taxFoundation.taxObjective,
        )
        assertEquals(
            "Use explicitly supplied financial information only.",
            taxFoundation.taxContextDescription,
        )
    }

    @Test
    fun `blank tax focus defers`() {
        val result =
            TaxIntelligenceFoundationCoordinator().prepare(
                traceId = TraceId.from("trace:stage155-focus"),
                financialIntelligenceIntegration = financialIntegration(),
                taxFocus = "   ",
                taxObjective = "Establish bounded tax context.",
                taxContextDescription = "Use supplied financial information.",
            )

        assertEquals(
            TaxIntelligenceFoundationPreparationStatus.DEFERRED,
            result.status,
        )
        assertNull(result.taxFoundation)
    }

    @Test
    fun `blank tax objective defers`() {
        val result =
            TaxIntelligenceFoundationCoordinator().prepare(
                traceId = TraceId.from("trace:stage155-objective"),
                financialIntelligenceIntegration = financialIntegration(),
                taxFocus = "General tax context",
                taxObjective = "   ",
                taxContextDescription = "Use supplied financial information.",
            )

        assertEquals(
            TaxIntelligenceFoundationPreparationStatus.DEFERRED,
            result.status,
        )
        assertNull(result.taxFoundation)
    }

    @Test
    fun `blank tax context description defers`() {
        val result =
            TaxIntelligenceFoundationCoordinator().prepare(
                traceId = TraceId.from("trace:stage155-context"),
                financialIntelligenceIntegration = financialIntegration(),
                taxFocus = "General tax context",
                taxObjective = "Establish bounded tax context.",
                taxContextDescription = "   ",
            )

        assertEquals(
            TaxIntelligenceFoundationPreparationStatus.DEFERRED,
            result.status,
        )
        assertNull(result.taxFoundation)
    }

    @Test
    fun `prepared result requires tax foundation context`() {
        assertFailsWith<IllegalArgumentException> {
            TaxIntelligenceFoundationPreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace:stage155-invalid-prepared",
                    ),
                status =
                    TaxIntelligenceFoundationPreparationStatus.PREPARED,
            )
        }
    }

    @Test
    fun `deferred result cannot contain tax foundation context`() {
        val prepared =
            TaxIntelligenceFoundationCoordinator().prepare(
                traceId = TraceId.from("trace:stage155-source"),
                financialIntelligenceIntegration = financialIntegration(),
                taxFocus = "General tax context",
                taxObjective = "Establish bounded tax context.",
                taxContextDescription = "Use supplied financial information.",
            )

        val taxFoundation =
            requireNotNull(prepared.taxFoundation)

        assertFailsWith<IllegalArgumentException> {
            TaxIntelligenceFoundationPreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace:stage155-invalid-deferred",
                    ),
                status =
                    TaxIntelligenceFoundationPreparationStatus.DEFERRED,
                taxFoundation = taxFoundation,
            )
        }
    }

    private fun financialIntegration(): FinancialIntelligenceIntegrationRecord {
        return FinancialIntelligenceIntegrationRecord.create(
            financialAnalysis =
                FinancialAnalysisRecord.create(
                    subject =
                        FinancialAnalysisSubject.from(
                            "Tax-relevant financial context",
                        ),
                    facts =
                        listOf(
                            FinancialFact.create(
                                label = "Income",
                                value = "800000 INR",
                            ),
                            FinancialFact.create(
                                label = "Eligible expense context",
                                value = "Supplied by user",
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
