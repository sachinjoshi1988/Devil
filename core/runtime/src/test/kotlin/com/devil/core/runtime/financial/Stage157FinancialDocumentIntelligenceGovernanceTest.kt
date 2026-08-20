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

class Stage157FinancialDocumentIntelligenceGovernanceTest {

    @Test
    fun `coordinator prepares bounded financial document intelligence`() {
        val integration = financialIntegration()
        val traceId =
            TraceId.from("trace:stage157-prepared")

        val result =
            FinancialDocumentIntelligenceCoordinator().prepare(
                traceId = traceId,
                financialIntelligenceIntegration = integration,
                documentFocus = "Supplied bank statement context",
                suppliedDocumentDescription =
                    "User describes a monthly financial statement.",
                interpretationObjective =
                    "Preserve bounded supplied document context.",
            )

        assertEquals(
            FinancialDocumentIntelligencePreparationStatus.PREPARED,
            result.status,
        )
        assertEquals(traceId, result.traceId)

        val documentIntelligence =
            requireNotNull(result.documentIntelligence)

        assertSame(
            integration,
            documentIntelligence.financialIntelligenceIntegration,
        )
        assertEquals(
            "Supplied bank statement context",
            documentIntelligence.documentFocus,
        )
        assertEquals(
            "User describes a monthly financial statement.",
            documentIntelligence.suppliedDocumentDescription,
        )
        assertEquals(
            "Preserve bounded supplied document context.",
            documentIntelligence.interpretationObjective,
        )
    }

    @Test
    fun `blank document focus defers`() {
        val result =
            FinancialDocumentIntelligenceCoordinator().prepare(
                traceId = TraceId.from("trace:stage157-focus"),
                financialIntelligenceIntegration = financialIntegration(),
                documentFocus = "   ",
                suppliedDocumentDescription =
                    "User-supplied financial document description.",
                interpretationObjective =
                    "Preserve bounded document context.",
            )

        assertEquals(
            FinancialDocumentIntelligencePreparationStatus.DEFERRED,
            result.status,
        )
        assertNull(result.documentIntelligence)
    }

    @Test
    fun `blank supplied document description defers`() {
        val result =
            FinancialDocumentIntelligenceCoordinator().prepare(
                traceId = TraceId.from("trace:stage157-description"),
                financialIntelligenceIntegration = financialIntegration(),
                documentFocus = "Supplied financial document",
                suppliedDocumentDescription = "   ",
                interpretationObjective =
                    "Preserve bounded document context.",
            )

        assertEquals(
            FinancialDocumentIntelligencePreparationStatus.DEFERRED,
            result.status,
        )
        assertNull(result.documentIntelligence)
    }

    @Test
    fun `blank interpretation objective defers`() {
        val result =
            FinancialDocumentIntelligenceCoordinator().prepare(
                traceId = TraceId.from("trace:stage157-objective"),
                financialIntelligenceIntegration = financialIntegration(),
                documentFocus = "Supplied financial document",
                suppliedDocumentDescription =
                    "User-supplied financial document description.",
                interpretationObjective = "   ",
            )

        assertEquals(
            FinancialDocumentIntelligencePreparationStatus.DEFERRED,
            result.status,
        )
        assertNull(result.documentIntelligence)
    }

    @Test
    fun `prepared result requires document intelligence context`() {
        assertFailsWith<IllegalArgumentException> {
            FinancialDocumentIntelligencePreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace:stage157-invalid-prepared",
                    ),
                status =
                    FinancialDocumentIntelligencePreparationStatus.PREPARED,
            )
        }
    }

    @Test
    fun `deferred result cannot contain document intelligence context`() {
        val prepared =
            FinancialDocumentIntelligenceCoordinator().prepare(
                traceId = TraceId.from("trace:stage157-source"),
                financialIntelligenceIntegration = financialIntegration(),
                documentFocus = "Supplied financial document",
                suppliedDocumentDescription =
                    "User-supplied financial document description.",
                interpretationObjective =
                    "Preserve bounded document context.",
            )

        val documentIntelligence =
            requireNotNull(prepared.documentIntelligence)

        assertFailsWith<IllegalArgumentException> {
            FinancialDocumentIntelligencePreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace:stage157-invalid-deferred",
                    ),
                status =
                    FinancialDocumentIntelligencePreparationStatus.DEFERRED,
                documentIntelligence = documentIntelligence,
            )
        }
    }

    private fun financialIntegration(): FinancialIntelligenceIntegrationRecord {
        return FinancialIntelligenceIntegrationRecord.create(
            financialAnalysis =
                FinancialAnalysisRecord.create(
                    subject =
                        FinancialAnalysisSubject.from(
                            "Financial document context",
                        ),
                    facts =
                        listOf(
                            FinancialFact.create(
                                label = "Document type",
                                value = "Bank statement",
                            ),
                            FinancialFact.create(
                                label = "Document provenance",
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
