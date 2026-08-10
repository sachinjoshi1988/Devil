package com.devil.core.model.reliability

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ReliabilityAssessmentTest {

    @Test
    fun `assessment preserves evidence disposition and normalized rationale`() {
        val evidence =
            RecoveryEvidence.create(
                condition = ReliabilityCondition.DEGRADED,
            )

        val assessment =
            ReliabilityAssessment.create(
                evidence = evidence,
                disposition =
                    RecoveryDisposition.UNAVAILABLE,
                rationale = "  Insufficient evidence.  ",
            )

        assertEquals(
            evidence,
            assessment.evidence,
        )
        assertEquals(
            RecoveryDisposition.UNAVAILABLE,
            assessment.disposition,
        )
        assertEquals(
            "Insufficient evidence.",
            assessment.rationale,
        )
    }

    @Test
    fun `blank rationale is rejected`() {
        assertFailsWith<IllegalArgumentException> {
            ReliabilityAssessment.create(
                evidence =
                    RecoveryEvidence.create(
                        condition = ReliabilityCondition.HEALTHY,
                    ),
                disposition =
                    RecoveryDisposition.RECOVERY_NOT_REQUIRED,
                rationale = "   ",
            )
        }
    }
}
