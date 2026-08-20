package com.devil.core.model.legal

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertSame

class RightsProcedureGuidanceStage163Test {

    @Test
    fun `record preserves exact Stage 159 foundation and normalized guidance metadata`() {
        val foundation = legalFoundation()

        val record =
            RightsProcedureGuidanceRecord.create(
                legalFoundation = foundation,
                guidanceFocus = "  Supplied procedural guidance context  ",
                suppliedRightsProcedureContextDescription =
                    "  User supplied a description of a rights and procedure question.  ",
                guidanceObjective =
                    "  Preserve bounded guidance context without legal determination.  ",
            )

        assertSame(
            foundation,
            record.legalFoundation,
        )
        assertEquals(
            "Supplied procedural guidance context",
            record.guidanceFocus,
        )
        assertEquals(
            "User supplied a description of a rights and procedure question.",
            record.suppliedRightsProcedureContextDescription,
        )
        assertEquals(
            "Preserve bounded guidance context without legal determination.",
            record.guidanceObjective,
        )
    }

    @Test
    fun `record rejects blank guidance focus`() {
        assertFailsWith<IllegalArgumentException> {
            RightsProcedureGuidanceRecord.create(
                legalFoundation = legalFoundation(),
                guidanceFocus = "   ",
                suppliedRightsProcedureContextDescription =
                    "User supplied rights and procedure context.",
                guidanceObjective =
                    "Preserve bounded guidance context.",
            )
        }
    }

    @Test
    fun `record rejects blank rights procedure context description`() {
        assertFailsWith<IllegalArgumentException> {
            RightsProcedureGuidanceRecord.create(
                legalFoundation = legalFoundation(),
                guidanceFocus = "Procedural guidance context",
                suppliedRightsProcedureContextDescription = "   ",
                guidanceObjective =
                    "Preserve bounded guidance context.",
            )
        }
    }

    @Test
    fun `record rejects blank guidance objective`() {
        assertFailsWith<IllegalArgumentException> {
            RightsProcedureGuidanceRecord.create(
                legalFoundation = legalFoundation(),
                guidanceFocus = "Procedural guidance context",
                suppliedRightsProcedureContextDescription =
                    "User supplied rights and procedure context.",
                guidanceObjective = "   ",
            )
        }
    }

    private fun legalFoundation(): LegalIntelligenceFoundationRecord {
        return LegalIntelligenceFoundationRecord.create(
            legalSubject = "Procedural guidance context",
            legalObjective =
                "Preserve bounded legal-domain context.",
            suppliedLegalContext =
                listOf(
                    "Procedure question was supplied by the user.",
                    "No legal right or filing requirement has been established.",
                ),
        )
    }
}
