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

class Stage152PersonalFinanceAssistanceGovernanceTest {

    @Test
    fun `coordinator prepares bounded personal finance assistance`() {
        val integration = financialIntegration()
        val traceId =
            TraceId.from("trace:stage152-prepared")

        val result =
            PersonalFinanceAssistanceCoordinator().prepare(
                traceId = traceId,
                financialIntelligenceIntegration = integration,
                assistanceFocus = "Household budgeting",
                assistanceObjective =
                    "Help understand income and expenses.",
                assistanceApproach =
                    "Use supplied facts for bounded educational guidance.",
            )

        assertEquals(
            PersonalFinanceAssistancePreparationStatus.PREPARED,
            result.status,
        )
        assertEquals(traceId, result.traceId)

        val assistance =
            requireNotNull(result.assistance)

        assertSame(
            integration,
            assistance.financialIntelligenceIntegration,
        )
        assertEquals(
            "Household budgeting",
            assistance.assistanceFocus,
        )
        assertEquals(
            "Help understand income and expenses.",
            assistance.assistanceObjective,
        )
        assertEquals(
            "Use supplied facts for bounded educational guidance.",
            assistance.assistanceApproach,
        )
    }

    @Test
    fun `blank assistance focus defers`() {
        val result =
            PersonalFinanceAssistanceCoordinator().prepare(
                traceId = TraceId.from("trace:stage152-focus"),
                financialIntelligenceIntegration = financialIntegration(),
                assistanceFocus = "   ",
                assistanceObjective = "Understand household finances.",
                assistanceApproach = "Use supplied facts.",
            )

        assertEquals(
            PersonalFinanceAssistancePreparationStatus.DEFERRED,
            result.status,
        )
        assertNull(result.assistance)
    }

    @Test
    fun `blank assistance objective defers`() {
        val result =
            PersonalFinanceAssistanceCoordinator().prepare(
                traceId = TraceId.from("trace:stage152-objective"),
                financialIntelligenceIntegration = financialIntegration(),
                assistanceFocus = "Household budgeting",
                assistanceObjective = "   ",
                assistanceApproach = "Use supplied facts.",
            )

        assertEquals(
            PersonalFinanceAssistancePreparationStatus.DEFERRED,
            result.status,
        )
        assertNull(result.assistance)
    }

    @Test
    fun `blank assistance approach defers`() {
        val result =
            PersonalFinanceAssistanceCoordinator().prepare(
                traceId = TraceId.from("trace:stage152-approach"),
                financialIntelligenceIntegration = financialIntegration(),
                assistanceFocus = "Household budgeting",
                assistanceObjective = "Understand household finances.",
                assistanceApproach = "   ",
            )

        assertEquals(
            PersonalFinanceAssistancePreparationStatus.DEFERRED,
            result.status,
        )
        assertNull(result.assistance)
    }

    @Test
    fun `prepared result requires assistance context`() {
        assertFailsWith<IllegalArgumentException> {
            PersonalFinanceAssistancePreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace:stage152-invalid-prepared",
                    ),
                status =
                    PersonalFinanceAssistancePreparationStatus.PREPARED,
            )
        }
    }

    @Test
    fun `deferred result cannot contain assistance context`() {
        val prepared =
            PersonalFinanceAssistanceCoordinator().prepare(
                traceId = TraceId.from("trace:stage152-source"),
                financialIntelligenceIntegration = financialIntegration(),
                assistanceFocus = "Household budgeting",
                assistanceObjective = "Understand household finances.",
                assistanceApproach = "Use supplied facts.",
            )

        val assistance =
            requireNotNull(prepared.assistance)

        assertFailsWith<IllegalArgumentException> {
            PersonalFinanceAssistancePreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace:stage152-invalid-deferred",
                    ),
                status =
                    PersonalFinanceAssistancePreparationStatus.DEFERRED,
                assistance = assistance,
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
