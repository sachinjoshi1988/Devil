package com.devil.core.runtime.legal

import com.devil.core.model.common.TraceId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class Stage159LegalIntelligenceFoundationGovernanceTest {

    @Test
    fun `coordinator prepares bounded legal intelligence foundation`() {
        val traceId =
            TraceId.from("trace:stage159-prepared")

        val result =
            LegalIntelligenceFoundationCoordinator().prepare(
                traceId = traceId,
                legalSubject = "Contract dispute context",
                legalObjective =
                    "Preserve bounded legal-domain context.",
                suppliedLegalContext =
                    listOf(
                        "Agreement was supplied by the user.",
                        "No jurisdiction has been established.",
                    ),
            )

        assertEquals(
            LegalIntelligenceFoundationPreparationStatus.PREPARED,
            result.status,
        )
        assertEquals(
            traceId,
            result.traceId,
        )

        val legalFoundation =
            requireNotNull(result.legalFoundation)

        assertEquals(
            "Contract dispute context",
            legalFoundation.legalSubject,
        )
        assertEquals(
            "Preserve bounded legal-domain context.",
            legalFoundation.legalObjective,
        )
        assertEquals(
            listOf(
                "Agreement was supplied by the user.",
                "No jurisdiction has been established.",
            ),
            legalFoundation.suppliedLegalContext,
        )
    }

    @Test
    fun `blank legal subject defers`() {
        val result =
            LegalIntelligenceFoundationCoordinator().prepare(
                traceId = TraceId.from("trace:stage159-subject"),
                legalSubject = "   ",
                legalObjective = "Preserve bounded legal context.",
                suppliedLegalContext =
                    listOf("User supplied legal context."),
            )

        assertEquals(
            LegalIntelligenceFoundationPreparationStatus.DEFERRED,
            result.status,
        )
        assertNull(result.legalFoundation)
    }

    @Test
    fun `blank legal objective defers`() {
        val result =
            LegalIntelligenceFoundationCoordinator().prepare(
                traceId = TraceId.from("trace:stage159-objective"),
                legalSubject = "Contract dispute context",
                legalObjective = "   ",
                suppliedLegalContext =
                    listOf("User supplied legal context."),
            )

        assertEquals(
            LegalIntelligenceFoundationPreparationStatus.DEFERRED,
            result.status,
        )
        assertNull(result.legalFoundation)
    }

    @Test
    fun `empty supplied legal context defers`() {
        val result =
            LegalIntelligenceFoundationCoordinator().prepare(
                traceId = TraceId.from("trace:stage159-empty-context"),
                legalSubject = "Contract dispute context",
                legalObjective = "Preserve bounded legal context.",
                suppliedLegalContext = emptyList(),
            )

        assertEquals(
            LegalIntelligenceFoundationPreparationStatus.DEFERRED,
            result.status,
        )
        assertNull(result.legalFoundation)
    }

    @Test
    fun `blank supplied legal context item defers`() {
        val result =
            LegalIntelligenceFoundationCoordinator().prepare(
                traceId = TraceId.from("trace:stage159-blank-context"),
                legalSubject = "Contract dispute context",
                legalObjective = "Preserve bounded legal context.",
                suppliedLegalContext =
                    listOf(
                        "User supplied legal context.",
                        "   ",
                    ),
            )

        assertEquals(
            LegalIntelligenceFoundationPreparationStatus.DEFERRED,
            result.status,
        )
        assertNull(result.legalFoundation)
    }

    @Test
    fun `prepared result requires legal foundation context`() {
        assertFailsWith<IllegalArgumentException> {
            LegalIntelligenceFoundationPreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace:stage159-invalid-prepared",
                    ),
                status =
                    LegalIntelligenceFoundationPreparationStatus.PREPARED,
            )
        }
    }

    @Test
    fun `deferred result cannot contain legal foundation context`() {
        val prepared =
            LegalIntelligenceFoundationCoordinator().prepare(
                traceId = TraceId.from("trace:stage159-source"),
                legalSubject = "Contract dispute context",
                legalObjective = "Preserve bounded legal context.",
                suppliedLegalContext =
                    listOf("User supplied legal context."),
            )

        val legalFoundation =
            requireNotNull(prepared.legalFoundation)

        assertFailsWith<IllegalArgumentException> {
            LegalIntelligenceFoundationPreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace:stage159-invalid-deferred",
                    ),
                status =
                    LegalIntelligenceFoundationPreparationStatus.DEFERRED,
                legalFoundation = legalFoundation,
            )
        }
    }
}
