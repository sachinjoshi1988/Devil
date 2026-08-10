package com.devil.core.model.reliability

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertSame

class RecoveryVerificationResultTest {

    @Test
    fun `verification result preserves explicit post-attempt evidence`() {
        val postRecoveryEvidence =
            PostRecoveryEvidence.create(
                attempt =
                    ReliabilityTestFixtures.recoveryAttemptRecord(),
                evidence =
                    RecoveryEvidence.create(
                        condition = ReliabilityCondition.HEALTHY,
                    ),
            )

        val result =
            RecoveryVerificationResult.create(
                status =
                    RecoveryVerificationStatus.VERIFIED_RECOVERED,
                postRecoveryEvidence = postRecoveryEvidence,
                rationale = " Explicit recovery evidence is healthy. ",
            )

        assertEquals(
            RecoveryVerificationStatus.VERIFIED_RECOVERED,
            result.status,
        )

        assertSame(
            postRecoveryEvidence,
            result.postRecoveryEvidence,
        )

        assertEquals(
            "Explicit recovery evidence is healthy.",
            result.rationale,
        )
    }

    @Test
    fun `verification result rejects blank rationale`() {
        val postRecoveryEvidence =
            PostRecoveryEvidence.create(
                attempt =
                    ReliabilityTestFixtures.recoveryAttemptRecord(),
                evidence =
                    RecoveryEvidence.create(
                        condition = ReliabilityCondition.HEALTHY,
                    ),
            )

        assertFailsWith<IllegalArgumentException> {
            RecoveryVerificationResult.create(
                status =
                    RecoveryVerificationStatus.VERIFIED_RECOVERED,
                postRecoveryEvidence = postRecoveryEvidence,
                rationale = "   ",
            )
        }
    }
}
