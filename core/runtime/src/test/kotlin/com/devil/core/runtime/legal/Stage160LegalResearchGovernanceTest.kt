package com.devil.core.runtime.legal

import com.devil.core.model.common.TraceId
import com.devil.core.model.legal.LegalIntelligenceFoundationRecord
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertSame

class Stage160LegalResearchGovernanceTest {

    @Test
    fun `coordinator prepares bounded legal research context`() {
        val foundation = legalFoundation()
        val traceId =
            TraceId.from("trace:stage160-prepared")

        val result =
            LegalResearchCoordinator().prepare(
                traceId = traceId,
                legalFoundation = foundation,
                researchFocus = "Supplied case-law research context",
                suppliedLegalSourceDescription =
                    "User supplied a description of a legal source.",
                researchObjective =
                    "Preserve bounded legal-research context.",
            )

        assertEquals(
            LegalResearchPreparationStatus.PREPARED,
            result.status,
        )
        assertEquals(
            traceId,
            result.traceId,
        )

        val legalResearch =
            requireNotNull(result.legalResearch)

        assertSame(
            foundation,
            legalResearch.legalFoundation,
        )
        assertEquals(
            "Supplied case-law research context",
            legalResearch.researchFocus,
        )
        assertEquals(
            "User supplied a description of a legal source.",
            legalResearch.suppliedLegalSourceDescription,
        )
        assertEquals(
            "Preserve bounded legal-research context.",
            legalResearch.researchObjective,
        )
    }

    @Test
    fun `blank research focus defers`() {
        val result =
            LegalResearchCoordinator().prepare(
                traceId = TraceId.from("trace:stage160-focus"),
                legalFoundation = legalFoundation(),
                researchFocus = "   ",
                suppliedLegalSourceDescription =
                    "User supplied a legal-source description.",
                researchObjective =
                    "Preserve bounded legal research context.",
            )

        assertEquals(
            LegalResearchPreparationStatus.DEFERRED,
            result.status,
        )
        assertNull(result.legalResearch)
    }

    @Test
    fun `blank supplied legal source description defers`() {
        val result =
            LegalResearchCoordinator().prepare(
                traceId = TraceId.from("trace:stage160-source"),
                legalFoundation = legalFoundation(),
                researchFocus = "Legal research context",
                suppliedLegalSourceDescription = "   ",
                researchObjective =
                    "Preserve bounded legal research context.",
            )

        assertEquals(
            LegalResearchPreparationStatus.DEFERRED,
            result.status,
        )
        assertNull(result.legalResearch)
    }

    @Test
    fun `blank research objective defers`() {
        val result =
            LegalResearchCoordinator().prepare(
                traceId = TraceId.from("trace:stage160-objective"),
                legalFoundation = legalFoundation(),
                researchFocus = "Legal research context",
                suppliedLegalSourceDescription =
                    "User supplied a legal-source description.",
                researchObjective = "   ",
            )

        assertEquals(
            LegalResearchPreparationStatus.DEFERRED,
            result.status,
        )
        assertNull(result.legalResearch)
    }

    @Test
    fun `prepared result requires legal research context`() {
        assertFailsWith<IllegalArgumentException> {
            LegalResearchPreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace:stage160-invalid-prepared",
                    ),
                status =
                    LegalResearchPreparationStatus.PREPARED,
            )
        }
    }

    @Test
    fun `deferred result cannot contain legal research context`() {
        val prepared =
            LegalResearchCoordinator().prepare(
                traceId = TraceId.from("trace:stage160-source-context"),
                legalFoundation = legalFoundation(),
                researchFocus = "Legal research context",
                suppliedLegalSourceDescription =
                    "User supplied a legal-source description.",
                researchObjective =
                    "Preserve bounded legal research context.",
            )

        val legalResearch =
            requireNotNull(prepared.legalResearch)

        assertFailsWith<IllegalArgumentException> {
            LegalResearchPreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace:stage160-invalid-deferred",
                    ),
                status =
                    LegalResearchPreparationStatus.DEFERRED,
                legalResearch = legalResearch,
            )
        }
    }

    private fun legalFoundation(): LegalIntelligenceFoundationRecord {
        return LegalIntelligenceFoundationRecord.create(
            legalSubject = "Contract dispute context",
            legalObjective = "Preserve bounded legal-domain context.",
            suppliedLegalContext =
                listOf(
                    "Agreement was supplied by the user.",
                    "No jurisdiction has been established.",
                ),
        )
    }
}
