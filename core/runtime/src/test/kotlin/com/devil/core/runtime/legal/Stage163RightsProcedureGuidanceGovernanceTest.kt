package com.devil.core.runtime.legal

import com.devil.core.model.common.TraceId
import com.devil.core.model.legal.LegalIntelligenceFoundationRecord
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertSame

class Stage163RightsProcedureGuidanceGovernanceTest {

    @Test
    fun `coordinator prepares bounded rights procedure guidance context`() {
        val foundation = legalFoundation()
        val traceId =
            TraceId.from("trace:stage163-prepared")

        val result =
            RightsProcedureGuidanceCoordinator().prepare(
                traceId = traceId,
                legalFoundation = foundation,
                guidanceFocus =
                    "Supplied procedural guidance context",
                suppliedRightsProcedureContextDescription =
                    "User supplied a description of a rights and procedure question.",
                guidanceObjective =
                    "Preserve bounded guidance context without legal determination.",
            )

        assertEquals(
            RightsProcedureGuidancePreparationStatus.PREPARED,
            result.status,
        )
        assertEquals(
            traceId,
            result.traceId,
        )

        val guidance =
            requireNotNull(result.guidance)

        assertSame(
            foundation,
            guidance.legalFoundation,
        )
        assertEquals(
            "Supplied procedural guidance context",
            guidance.guidanceFocus,
        )
        assertEquals(
            "User supplied a description of a rights and procedure question.",
            guidance.suppliedRightsProcedureContextDescription,
        )
        assertEquals(
            "Preserve bounded guidance context without legal determination.",
            guidance.guidanceObjective,
        )
    }

    @Test
    fun `blank guidance focus defers`() {
        val result =
            RightsProcedureGuidanceCoordinator().prepare(
                traceId = TraceId.from("trace:stage163-focus"),
                legalFoundation = legalFoundation(),
                guidanceFocus = "   ",
                suppliedRightsProcedureContextDescription =
                    "User supplied rights and procedure context.",
                guidanceObjective =
                    "Preserve bounded guidance context.",
            )

        assertEquals(
            RightsProcedureGuidancePreparationStatus.DEFERRED,
            result.status,
        )
        assertNull(result.guidance)
    }

    @Test
    fun `blank rights procedure context description defers`() {
        val result =
            RightsProcedureGuidanceCoordinator().prepare(
                traceId = TraceId.from("trace:stage163-description"),
                legalFoundation = legalFoundation(),
                guidanceFocus = "Procedural guidance context",
                suppliedRightsProcedureContextDescription = "   ",
                guidanceObjective =
                    "Preserve bounded guidance context.",
            )

        assertEquals(
            RightsProcedureGuidancePreparationStatus.DEFERRED,
            result.status,
        )
        assertNull(result.guidance)
    }

    @Test
    fun `blank guidance objective defers`() {
        val result =
            RightsProcedureGuidanceCoordinator().prepare(
                traceId = TraceId.from("trace:stage163-objective"),
                legalFoundation = legalFoundation(),
                guidanceFocus = "Procedural guidance context",
                suppliedRightsProcedureContextDescription =
                    "User supplied rights and procedure context.",
                guidanceObjective = "   ",
            )

        assertEquals(
            RightsProcedureGuidancePreparationStatus.DEFERRED,
            result.status,
        )
        assertNull(result.guidance)
    }

    @Test
    fun `prepared result requires guidance context`() {
        assertFailsWith<IllegalArgumentException> {
            RightsProcedureGuidancePreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace:stage163-invalid-prepared",
                    ),
                status =
                    RightsProcedureGuidancePreparationStatus.PREPARED,
            )
        }
    }

    @Test
    fun `deferred result cannot contain guidance context`() {
        val prepared =
            RightsProcedureGuidanceCoordinator().prepare(
                traceId = TraceId.from("trace:stage163-source"),
                legalFoundation = legalFoundation(),
                guidanceFocus = "Procedural guidance context",
                suppliedRightsProcedureContextDescription =
                    "User supplied rights and procedure context.",
                guidanceObjective =
                    "Preserve bounded guidance context.",
            )

        val guidance =
            requireNotNull(prepared.guidance)

        assertFailsWith<IllegalArgumentException> {
            RightsProcedureGuidancePreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace:stage163-invalid-deferred",
                    ),
                status =
                    RightsProcedureGuidancePreparationStatus.DEFERRED,
                guidance = guidance,
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
