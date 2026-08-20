package com.devil.core.runtime.legal

import com.devil.core.model.common.TraceId
import com.devil.core.model.legal.LegalIntelligenceFoundationRecord
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertSame

class Stage161LegalDocumentUnderstandingGovernanceTest {

    @Test
    fun `coordinator prepares bounded legal document understanding context`() {
        val foundation = legalFoundation()
        val traceId =
            TraceId.from("trace:stage161-prepared")

        val result =
            LegalDocumentUnderstandingCoordinator().prepare(
                traceId = traceId,
                legalFoundation = foundation,
                documentFocus = "Supplied agreement context",
                suppliedLegalDocumentDescription =
                    "User supplied a description of a legal agreement.",
                interpretationObjective =
                    "Preserve bounded legal-document context.",
            )

        assertEquals(
            LegalDocumentUnderstandingPreparationStatus.PREPARED,
            result.status,
        )
        assertEquals(
            traceId,
            result.traceId,
        )

        val documentUnderstanding =
            requireNotNull(result.documentUnderstanding)

        assertSame(
            foundation,
            documentUnderstanding.legalFoundation,
        )
        assertEquals(
            "Supplied agreement context",
            documentUnderstanding.documentFocus,
        )
        assertEquals(
            "User supplied a description of a legal agreement.",
            documentUnderstanding.suppliedLegalDocumentDescription,
        )
        assertEquals(
            "Preserve bounded legal-document context.",
            documentUnderstanding.interpretationObjective,
        )
    }

    @Test
    fun `blank document focus defers`() {
        val result =
            LegalDocumentUnderstandingCoordinator().prepare(
                traceId = TraceId.from("trace:stage161-focus"),
                legalFoundation = legalFoundation(),
                documentFocus = "   ",
                suppliedLegalDocumentDescription =
                    "User supplied a legal-document description.",
                interpretationObjective =
                    "Preserve bounded legal-document context.",
            )

        assertEquals(
            LegalDocumentUnderstandingPreparationStatus.DEFERRED,
            result.status,
        )
        assertNull(result.documentUnderstanding)
    }

    @Test
    fun `blank supplied legal document description defers`() {
        val result =
            LegalDocumentUnderstandingCoordinator().prepare(
                traceId = TraceId.from("trace:stage161-description"),
                legalFoundation = legalFoundation(),
                documentFocus = "Supplied legal document",
                suppliedLegalDocumentDescription = "   ",
                interpretationObjective =
                    "Preserve bounded legal-document context.",
            )

        assertEquals(
            LegalDocumentUnderstandingPreparationStatus.DEFERRED,
            result.status,
        )
        assertNull(result.documentUnderstanding)
    }

    @Test
    fun `blank interpretation objective defers`() {
        val result =
            LegalDocumentUnderstandingCoordinator().prepare(
                traceId = TraceId.from("trace:stage161-objective"),
                legalFoundation = legalFoundation(),
                documentFocus = "Supplied legal document",
                suppliedLegalDocumentDescription =
                    "User supplied a legal-document description.",
                interpretationObjective = "   ",
            )

        assertEquals(
            LegalDocumentUnderstandingPreparationStatus.DEFERRED,
            result.status,
        )
        assertNull(result.documentUnderstanding)
    }

    @Test
    fun `prepared result requires document understanding context`() {
        assertFailsWith<IllegalArgumentException> {
            LegalDocumentUnderstandingPreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace:stage161-invalid-prepared",
                    ),
                status =
                    LegalDocumentUnderstandingPreparationStatus.PREPARED,
            )
        }
    }

    @Test
    fun `deferred result cannot contain document understanding context`() {
        val prepared =
            LegalDocumentUnderstandingCoordinator().prepare(
                traceId = TraceId.from("trace:stage161-source"),
                legalFoundation = legalFoundation(),
                documentFocus = "Supplied legal document",
                suppliedLegalDocumentDescription =
                    "User supplied a legal-document description.",
                interpretationObjective =
                    "Preserve bounded legal-document context.",
            )

        val documentUnderstanding =
            requireNotNull(prepared.documentUnderstanding)

        assertFailsWith<IllegalArgumentException> {
            LegalDocumentUnderstandingPreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace:stage161-invalid-deferred",
                    ),
                status =
                    LegalDocumentUnderstandingPreparationStatus.DEFERRED,
                documentUnderstanding = documentUnderstanding,
            )
        }
    }

    private fun legalFoundation(): LegalIntelligenceFoundationRecord {
        return LegalIntelligenceFoundationRecord.create(
            legalSubject = "Contract interpretation context",
            legalObjective =
                "Preserve bounded legal-domain context.",
            suppliedLegalContext =
                listOf(
                    "Agreement context was supplied by the user.",
                    "No legal effect has been established.",
                ),
        )
    }
}
