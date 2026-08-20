package com.devil.core.runtime.legal

import com.devil.core.model.common.TraceId
import com.devil.core.model.legal.LegalIntelligenceFoundationRecord
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertSame

class Stage162LegalDraftingAssistanceGovernanceTest {

    @Test
    fun `coordinator prepares bounded legal drafting assistance context`() {
        val foundation = legalFoundation()
        val traceId =
            TraceId.from("trace:stage162-prepared")

        val result =
            LegalDraftingAssistanceCoordinator().prepare(
                traceId = traceId,
                legalFoundation = foundation,
                draftingFocus = "Agreement drafting assistance",
                requestedDraftPurpose =
                    "Prepare a bounded non-authoritative agreement draft context.",
                draftingObjective =
                    "Preserve user-supplied drafting intent without legal conclusions.",
            )

        assertEquals(
            LegalDraftingAssistancePreparationStatus.PREPARED,
            result.status,
        )
        assertEquals(
            traceId,
            result.traceId,
        )

        val draftingAssistance =
            requireNotNull(result.draftingAssistance)

        assertSame(
            foundation,
            draftingAssistance.legalFoundation,
        )
        assertEquals(
            "Agreement drafting assistance",
            draftingAssistance.draftingFocus,
        )
        assertEquals(
            "Prepare a bounded non-authoritative agreement draft context.",
            draftingAssistance.requestedDraftPurpose,
        )
        assertEquals(
            "Preserve user-supplied drafting intent without legal conclusions.",
            draftingAssistance.draftingObjective,
        )
    }

    @Test
    fun `blank drafting focus defers`() {
        val result =
            LegalDraftingAssistanceCoordinator().prepare(
                traceId = TraceId.from("trace:stage162-focus"),
                legalFoundation = legalFoundation(),
                draftingFocus = "   ",
                requestedDraftPurpose =
                    "Prepare a bounded draft context.",
                draftingObjective =
                    "Preserve drafting intent.",
            )

        assertEquals(
            LegalDraftingAssistancePreparationStatus.DEFERRED,
            result.status,
        )
        assertNull(result.draftingAssistance)
    }

    @Test
    fun `blank requested draft purpose defers`() {
        val result =
            LegalDraftingAssistanceCoordinator().prepare(
                traceId = TraceId.from("trace:stage162-purpose"),
                legalFoundation = legalFoundation(),
                draftingFocus = "Agreement drafting assistance",
                requestedDraftPurpose = "   ",
                draftingObjective =
                    "Preserve drafting intent.",
            )

        assertEquals(
            LegalDraftingAssistancePreparationStatus.DEFERRED,
            result.status,
        )
        assertNull(result.draftingAssistance)
    }

    @Test
    fun `blank drafting objective defers`() {
        val result =
            LegalDraftingAssistanceCoordinator().prepare(
                traceId = TraceId.from("trace:stage162-objective"),
                legalFoundation = legalFoundation(),
                draftingFocus = "Agreement drafting assistance",
                requestedDraftPurpose =
                    "Prepare a bounded draft context.",
                draftingObjective = "   ",
            )

        assertEquals(
            LegalDraftingAssistancePreparationStatus.DEFERRED,
            result.status,
        )
        assertNull(result.draftingAssistance)
    }

    @Test
    fun `prepared result requires drafting assistance context`() {
        assertFailsWith<IllegalArgumentException> {
            LegalDraftingAssistancePreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace:stage162-invalid-prepared",
                    ),
                status =
                    LegalDraftingAssistancePreparationStatus.PREPARED,
            )
        }
    }

    @Test
    fun `deferred result cannot contain drafting assistance context`() {
        val prepared =
            LegalDraftingAssistanceCoordinator().prepare(
                traceId = TraceId.from("trace:stage162-source"),
                legalFoundation = legalFoundation(),
                draftingFocus = "Agreement drafting assistance",
                requestedDraftPurpose =
                    "Prepare a bounded draft context.",
                draftingObjective =
                    "Preserve drafting intent.",
            )

        val draftingAssistance =
            requireNotNull(prepared.draftingAssistance)

        assertFailsWith<IllegalArgumentException> {
            LegalDraftingAssistancePreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace:stage162-invalid-deferred",
                    ),
                status =
                    LegalDraftingAssistancePreparationStatus.DEFERRED,
                draftingAssistance = draftingAssistance,
            )
        }
    }

    private fun legalFoundation(): LegalIntelligenceFoundationRecord {
        return LegalIntelligenceFoundationRecord.create(
            legalSubject = "Agreement drafting context",
            legalObjective =
                "Preserve bounded legal-domain context.",
            suppliedLegalContext =
                listOf(
                    "Drafting intent was supplied by the user.",
                    "No legal effect has been established.",
                ),
        )
    }
}
