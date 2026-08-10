package com.devil.core.model.reliability

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame

class RecoveryVerificationCoordinatorTest {

    @Test
    fun `coordinator verifies only explicit post-attempt evidence`() {
        val attempt =
            ReliabilityTestFixtures.recoveryAttemptRecord()

        val evidence =
            RecoveryEvidence.create(
                condition = ReliabilityCondition.HEALTHY,
            )

        val result =
            RecoveryVerificationCoordinator()
                .verify(
                    attempt = attempt,
                    evidence = evidence,
                )

        assertEquals(
            RecoveryVerificationStatus.VERIFIED_RECOVERED,
            result.status,
        )

        assertSame(
            attempt,
            result.postRecoveryEvidence.attempt,
        )

        assertSame(
            evidence,
            result.postRecoveryEvidence.evidence,
        )
    }

    @Test
    fun `recorded attempt alone does not establish recovery`() {
        val result =
            RecoveryVerificationCoordinator()
                .verify(
                    attempt =
                        ReliabilityTestFixtures.recoveryAttemptRecord(),
                    evidence =
                        RecoveryEvidence.create(
                            condition =
                                ReliabilityCondition.FAILED,
                        ),
                )

        assertEquals(
            RecoveryVerificationStatus.NOT_RECOVERED,
            result.status,
        )
    }
}
