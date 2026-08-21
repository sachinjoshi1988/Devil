package com.devil.core.model.legal

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertSame

class HighStakesLegalSafetyStage165Test {

    @Test
    fun `record preserves exact Stage 159 foundation and normalized safety metadata`() {
        val foundation = legalFoundation()

        val record =
            HighStakesLegalSafetyRecord.create(
                legalFoundation = foundation,
                highStakesSafetyFocus =
                    "  Supplied high-stakes legal safety context  ",
                suppliedLegalRiskContextDescription =
                    "  User supplied a description of potential legal risk.  ",
                safetyInterpretation =
                    "  Preserve bounded safety interpretation without legal advice.  ",
            )

        assertSame(
            foundation,
            record.legalFoundation,
        )
        assertEquals(
            "Supplied high-stakes legal safety context",
            record.highStakesSafetyFocus,
        )
        assertEquals(
            "User supplied a description of potential legal risk.",
            record.suppliedLegalRiskContextDescription,
        )
        assertEquals(
            "Preserve bounded safety interpretation without legal advice.",
            record.safetyInterpretation,
        )
    }

    @Test
    fun `record rejects blank high stakes safety focus`() {
        assertFailsWith<IllegalArgumentException> {
            HighStakesLegalSafetyRecord.create(
                legalFoundation = legalFoundation(),
                highStakesSafetyFocus = "   ",
                suppliedLegalRiskContextDescription =
                    "User supplied legal-risk context.",
                safetyInterpretation =
                    "Preserve bounded safety interpretation.",
            )
        }
    }

    @Test
    fun `record rejects blank legal risk context description`() {
        assertFailsWith<IllegalArgumentException> {
            HighStakesLegalSafetyRecord.create(
                legalFoundation = legalFoundation(),
                highStakesSafetyFocus =
                    "High-stakes legal safety context",
                suppliedLegalRiskContextDescription = "   ",
                safetyInterpretation =
                    "Preserve bounded safety interpretation.",
            )
        }
    }

    @Test
    fun `record rejects blank safety interpretation`() {
        assertFailsWith<IllegalArgumentException> {
            HighStakesLegalSafetyRecord.create(
                legalFoundation = legalFoundation(),
                highStakesSafetyFocus =
                    "High-stakes legal safety context",
                suppliedLegalRiskContextDescription =
                    "User supplied legal-risk context.",
                safetyInterpretation = "   ",
            )
        }
    }

    private fun legalFoundation(): LegalIntelligenceFoundationRecord {
        return LegalIntelligenceFoundationRecord.create(
            legalSubject =
                "High-stakes legal safety context",
            legalObjective =
                "Preserve bounded legal-domain context.",
            suppliedLegalContext =
                listOf(
                    "Potential legal-risk context was supplied.",
                    "No legal risk, urgency, or emergency has been verified.",
                ),
        )
    }
}
