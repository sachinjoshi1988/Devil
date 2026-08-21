package com.devil.core.runtime.legal

import com.devil.core.model.common.TraceId
import com.devil.core.model.legal.LegalIntelligenceFoundationRecord
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertSame

class Stage165HighStakesLegalSafetyGovernanceTest {

    @Test
    fun `coordinator prepares bounded high stakes legal safety context`() {
        val foundation = legalFoundation()
        val traceId =
            TraceId.from("trace:stage165-prepared")

        val result =
            HighStakesLegalSafetyCoordinator().prepare(
                traceId = traceId,
                legalFoundation = foundation,
                highStakesSafetyFocus =
                    "Supplied high-stakes legal safety context",
                suppliedLegalRiskContextDescription =
                    "User supplied a description of potential legal risk.",
                safetyInterpretation =
                    "Preserve bounded safety interpretation without legal advice.",
            )

        assertEquals(
            HighStakesLegalSafetyPreparationStatus.PREPARED,
            result.status,
        )
        assertEquals(
            traceId,
            result.traceId,
        )

        val safety =
            requireNotNull(result.safety)

        assertSame(
            foundation,
            safety.legalFoundation,
        )
        assertEquals(
            "Supplied high-stakes legal safety context",
            safety.highStakesSafetyFocus,
        )
        assertEquals(
            "User supplied a description of potential legal risk.",
            safety.suppliedLegalRiskContextDescription,
        )
        assertEquals(
            "Preserve bounded safety interpretation without legal advice.",
            safety.safetyInterpretation,
        )
    }

    @Test
    fun `blank high stakes safety focus defers`() {
        val result =
            HighStakesLegalSafetyCoordinator().prepare(
                traceId = TraceId.from("trace:stage165-focus"),
                legalFoundation = legalFoundation(),
                highStakesSafetyFocus = "   ",
                suppliedLegalRiskContextDescription =
                    "User supplied legal-risk context.",
                safetyInterpretation =
                    "Preserve bounded safety interpretation.",
            )

        assertEquals(
            HighStakesLegalSafetyPreparationStatus.DEFERRED,
            result.status,
        )
        assertNull(result.safety)
    }

    @Test
    fun `blank legal risk context description defers`() {
        val result =
            HighStakesLegalSafetyCoordinator().prepare(
                traceId = TraceId.from("trace:stage165-description"),
                legalFoundation = legalFoundation(),
                highStakesSafetyFocus =
                    "High-stakes legal safety context",
                suppliedLegalRiskContextDescription = "   ",
                safetyInterpretation =
                    "Preserve bounded safety interpretation.",
            )

        assertEquals(
            HighStakesLegalSafetyPreparationStatus.DEFERRED,
            result.status,
        )
        assertNull(result.safety)
    }

    @Test
    fun `blank safety interpretation defers`() {
        val result =
            HighStakesLegalSafetyCoordinator().prepare(
                traceId = TraceId.from("trace:stage165-interpretation"),
                legalFoundation = legalFoundation(),
                highStakesSafetyFocus =
                    "High-stakes legal safety context",
                suppliedLegalRiskContextDescription =
                    "User supplied legal-risk context.",
                safetyInterpretation = "   ",
            )

        assertEquals(
            HighStakesLegalSafetyPreparationStatus.DEFERRED,
            result.status,
        )
        assertNull(result.safety)
    }

    @Test
    fun `prepared result requires safety context`() {
        assertFailsWith<IllegalArgumentException> {
            HighStakesLegalSafetyPreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace:stage165-invalid-prepared",
                    ),
                status =
                    HighStakesLegalSafetyPreparationStatus.PREPARED,
            )
        }
    }

    @Test
    fun `deferred result cannot contain safety context`() {
        val prepared =
            HighStakesLegalSafetyCoordinator().prepare(
                traceId = TraceId.from("trace:stage165-source"),
                legalFoundation = legalFoundation(),
                highStakesSafetyFocus =
                    "High-stakes legal safety context",
                suppliedLegalRiskContextDescription =
                    "User supplied legal-risk context.",
                safetyInterpretation =
                    "Preserve bounded safety interpretation.",
            )

        val safety =
            requireNotNull(prepared.safety)

        assertFailsWith<IllegalArgumentException> {
            HighStakesLegalSafetyPreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace:stage165-invalid-deferred",
                    ),
                status =
                    HighStakesLegalSafetyPreparationStatus.DEFERRED,
                safety = safety,
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
