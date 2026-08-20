package com.devil.core.model.legal

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class LegalIntelligenceFoundationStage159Test {

    @Test
    fun `record normalizes supplied legal metadata`() {
        val record =
            LegalIntelligenceFoundationRecord.create(
                legalSubject = "  Contract dispute context  ",
                legalObjective =
                    "  Preserve bounded legal-domain context.  ",
                suppliedLegalContext =
                    listOf(
                        "  Agreement was supplied by the user.  ",
                        "  No jurisdiction has been established.  ",
                    ),
            )

        assertEquals(
            "Contract dispute context",
            record.legalSubject,
        )
        assertEquals(
            "Preserve bounded legal-domain context.",
            record.legalObjective,
        )
        assertEquals(
            listOf(
                "Agreement was supplied by the user.",
                "No jurisdiction has been established.",
            ),
            record.suppliedLegalContext,
        )
    }

    @Test
    fun `record rejects blank legal subject`() {
        assertFailsWith<IllegalArgumentException> {
            LegalIntelligenceFoundationRecord.create(
                legalSubject = "   ",
                legalObjective = "Preserve bounded legal context.",
                suppliedLegalContext =
                    listOf("User supplied legal context."),
            )
        }
    }

    @Test
    fun `record rejects blank legal objective`() {
        assertFailsWith<IllegalArgumentException> {
            LegalIntelligenceFoundationRecord.create(
                legalSubject = "Contract dispute context",
                legalObjective = "   ",
                suppliedLegalContext =
                    listOf("User supplied legal context."),
            )
        }
    }

    @Test
    fun `record rejects empty supplied legal context`() {
        assertFailsWith<IllegalArgumentException> {
            LegalIntelligenceFoundationRecord.create(
                legalSubject = "Contract dispute context",
                legalObjective = "Preserve bounded legal context.",
                suppliedLegalContext = emptyList(),
            )
        }
    }

    @Test
    fun `record rejects blank supplied legal context item`() {
        assertFailsWith<IllegalArgumentException> {
            LegalIntelligenceFoundationRecord.create(
                legalSubject = "Contract dispute context",
                legalObjective = "Preserve bounded legal context.",
                suppliedLegalContext =
                    listOf(
                        "User supplied legal context.",
                        "   ",
                    ),
            )
        }
    }
}
