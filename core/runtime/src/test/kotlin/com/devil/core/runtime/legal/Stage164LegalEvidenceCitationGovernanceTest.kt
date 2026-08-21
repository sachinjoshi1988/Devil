package com.devil.core.runtime.legal

import com.devil.core.model.common.TraceId
import com.devil.core.model.legal.LegalIntelligenceFoundationRecord
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertSame

class Stage164LegalEvidenceCitationGovernanceTest {

    @Test
    fun `coordinator prepares bounded legal evidence citation context`() {
        val foundation = legalFoundation()
        val traceId =
            TraceId.from("trace:stage164-prepared")

        val result =
            LegalEvidenceCitationCoordinator().prepare(
                traceId = traceId,
                legalFoundation = foundation,
                evidenceCitationFocus =
                    "Supplied legal evidence and citation context",
                suppliedLegalSourceEvidenceDescription =
                    "User supplied a legal source and evidence description.",
                citationObjective =
                    "Preserve bounded citation context without legal verification.",
            )

        assertEquals(
            LegalEvidenceCitationPreparationStatus.PREPARED,
            result.status,
        )
        assertEquals(
            traceId,
            result.traceId,
        )

        val evidenceCitation =
            requireNotNull(result.evidenceCitation)

        assertSame(
            foundation,
            evidenceCitation.legalFoundation,
        )
        assertEquals(
            "Supplied legal evidence and citation context",
            evidenceCitation.evidenceCitationFocus,
        )
        assertEquals(
            "User supplied a legal source and evidence description.",
            evidenceCitation.suppliedLegalSourceEvidenceDescription,
        )
        assertEquals(
            "Preserve bounded citation context without legal verification.",
            evidenceCitation.citationObjective,
        )
    }

    @Test
    fun `blank evidence citation focus defers`() {
        val result =
            LegalEvidenceCitationCoordinator().prepare(
                traceId = TraceId.from("trace:stage164-focus"),
                legalFoundation = legalFoundation(),
                evidenceCitationFocus = "   ",
                suppliedLegalSourceEvidenceDescription =
                    "User supplied legal source and evidence context.",
                citationObjective =
                    "Preserve bounded citation context.",
            )

        assertEquals(
            LegalEvidenceCitationPreparationStatus.DEFERRED,
            result.status,
        )
        assertNull(result.evidenceCitation)
    }

    @Test
    fun `blank legal source evidence description defers`() {
        val result =
            LegalEvidenceCitationCoordinator().prepare(
                traceId = TraceId.from("trace:stage164-description"),
                legalFoundation = legalFoundation(),
                evidenceCitationFocus =
                    "Supplied legal evidence and citation context",
                suppliedLegalSourceEvidenceDescription = "   ",
                citationObjective =
                    "Preserve bounded citation context.",
            )

        assertEquals(
            LegalEvidenceCitationPreparationStatus.DEFERRED,
            result.status,
        )
        assertNull(result.evidenceCitation)
    }

    @Test
    fun `blank citation objective defers`() {
        val result =
            LegalEvidenceCitationCoordinator().prepare(
                traceId = TraceId.from("trace:stage164-objective"),
                legalFoundation = legalFoundation(),
                evidenceCitationFocus =
                    "Supplied legal evidence and citation context",
                suppliedLegalSourceEvidenceDescription =
                    "User supplied legal source and evidence context.",
                citationObjective = "   ",
            )

        assertEquals(
            LegalEvidenceCitationPreparationStatus.DEFERRED,
            result.status,
        )
        assertNull(result.evidenceCitation)
    }

    @Test
    fun `prepared result requires evidence citation context`() {
        assertFailsWith<IllegalArgumentException> {
            LegalEvidenceCitationPreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace:stage164-invalid-prepared",
                    ),
                status =
                    LegalEvidenceCitationPreparationStatus.PREPARED,
            )
        }
    }

    @Test
    fun `deferred result cannot contain evidence citation context`() {
        val prepared =
            LegalEvidenceCitationCoordinator().prepare(
                traceId = TraceId.from("trace:stage164-source"),
                legalFoundation = legalFoundation(),
                evidenceCitationFocus =
                    "Supplied legal evidence and citation context",
                suppliedLegalSourceEvidenceDescription =
                    "User supplied legal source and evidence context.",
                citationObjective =
                    "Preserve bounded citation context.",
            )

        val evidenceCitation =
            requireNotNull(prepared.evidenceCitation)

        assertFailsWith<IllegalArgumentException> {
            LegalEvidenceCitationPreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace:stage164-invalid-deferred",
                    ),
                status =
                    LegalEvidenceCitationPreparationStatus.DEFERRED,
                evidenceCitation = evidenceCitation,
            )
        }
    }

    private fun legalFoundation(): LegalIntelligenceFoundationRecord {
        return LegalIntelligenceFoundationRecord.create(
            legalSubject =
                "Legal evidence and citation context",
            legalObjective =
                "Preserve bounded legal-domain context.",
            suppliedLegalContext =
                listOf(
                    "Legal source and evidence material was supplied.",
                    "No evidence authenticity or authoritative citation has been established.",
                ),
        )
    }
}
