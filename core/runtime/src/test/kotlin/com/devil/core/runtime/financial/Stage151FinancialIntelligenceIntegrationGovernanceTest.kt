package com.devil.core.runtime.financial

import com.devil.core.model.common.TraceId
import com.devil.core.model.financial.FinancialAnalysisRecord
import com.devil.core.model.financial.FinancialAnalysisSubject
import com.devil.core.model.financial.FinancialFact
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertSame

class Stage151FinancialIntelligenceIntegrationGovernanceTest {

    @Test
    fun `coordinator prepares bounded financial intelligence integration`() {
        val financialAnalysis = financialAnalysis()
        val traceId =
            TraceId.from("trace:stage151-prepared")

        val result =
            FinancialIntelligenceIntegrationCoordinator().prepare(
                traceId = traceId,
                financialAnalysis = financialAnalysis,
                integrationFocus =
                    "Bounded financial intelligence integration",
                integrationObjective =
                    "Preserve Stage 89 financial-domain provenance.",
            )

        assertEquals(
            FinancialIntelligenceIntegrationPreparationStatus.PREPARED,
            result.status,
        )
        assertEquals(traceId, result.traceId)

        val integration =
            requireNotNull(result.integration)

        assertSame(
            financialAnalysis,
            integration.financialAnalysis,
        )
        assertEquals(
            "Bounded financial intelligence integration",
            integration.integrationFocus,
        )
        assertEquals(
            "Preserve Stage 89 financial-domain provenance.",
            integration.integrationObjective,
        )
    }

    @Test
    fun `blank integration focus defers`() {
        val result =
            FinancialIntelligenceIntegrationCoordinator().prepare(
                traceId =
                    TraceId.from("trace:stage151-focus"),
                financialAnalysis = financialAnalysis(),
                integrationFocus = "   ",
                integrationObjective =
                    "Preserve bounded financial-domain provenance.",
            )

        assertEquals(
            FinancialIntelligenceIntegrationPreparationStatus.DEFERRED,
            result.status,
        )
        assertNull(result.integration)
    }

    @Test
    fun `blank integration objective defers`() {
        val result =
            FinancialIntelligenceIntegrationCoordinator().prepare(
                traceId =
                    TraceId.from("trace:stage151-objective"),
                financialAnalysis = financialAnalysis(),
                integrationFocus =
                    "Bounded financial intelligence integration",
                integrationObjective = "   ",
            )

        assertEquals(
            FinancialIntelligenceIntegrationPreparationStatus.DEFERRED,
            result.status,
        )
        assertNull(result.integration)
    }

    @Test
    fun `prepared result requires integration context`() {
        assertFailsWith<IllegalArgumentException> {
            FinancialIntelligenceIntegrationPreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace:stage151-invalid-prepared",
                    ),
                status =
                    FinancialIntelligenceIntegrationPreparationStatus.PREPARED,
            )
        }
    }

    @Test
    fun `deferred result cannot contain integration context`() {
        val prepared =
            FinancialIntelligenceIntegrationCoordinator().prepare(
                traceId =
                    TraceId.from("trace:stage151-source"),
                financialAnalysis = financialAnalysis(),
                integrationFocus =
                    "Bounded financial intelligence integration",
                integrationObjective =
                    "Preserve bounded financial-domain provenance.",
            )

        val integration =
            requireNotNull(prepared.integration)

        assertFailsWith<IllegalArgumentException> {
            FinancialIntelligenceIntegrationPreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace:stage151-invalid-deferred",
                    ),
                status =
                    FinancialIntelligenceIntegrationPreparationStatus.DEFERRED,
                integration = integration,
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
