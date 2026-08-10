package com.devil.core.model.reliability

import kotlin.test.Test
import kotlin.test.assertEquals

class RecoveryVerificationPolicyTest {

    private val policy =
        RecoveryVerificationPolicy()

    @Test
    fun `healthy post-attempt evidence establishes bounded recovery`() {
        val result =
            policy.verify(
                postRecoveryEvidence =
                    postEvidence(
                        condition = ReliabilityCondition.HEALTHY,
                    ),
            )

        assertEquals(
            RecoveryVerificationStatus.VERIFIED_RECOVERED,
            result.status,
        )
    }

    @Test
    fun `degraded post-attempt evidence is not recovered`() {
        val result =
            policy.verify(
                postRecoveryEvidence =
                    postEvidence(
                        condition = ReliabilityCondition.DEGRADED,
                    ),
            )

        assertEquals(
            RecoveryVerificationStatus.NOT_RECOVERED,
            result.status,
        )
    }

    @Test
    fun `unavailable post-attempt evidence is not recovered`() {
        val result =
            policy.verify(
                postRecoveryEvidence =
                    postEvidence(
                        condition = ReliabilityCondition.UNAVAILABLE,
                    ),
            )

        assertEquals(
            RecoveryVerificationStatus.NOT_RECOVERED,
            result.status,
        )
    }

    @Test
    fun `failed post-attempt evidence is not recovered`() {
        val result =
            policy.verify(
                postRecoveryEvidence =
                    postEvidence(
                        condition = ReliabilityCondition.FAILED,
                    ),
            )

        assertEquals(
            RecoveryVerificationStatus.NOT_RECOVERED,
            result.status,
        )
    }

    private fun postEvidence(
        condition: ReliabilityCondition,
    ): PostRecoveryEvidence {
        return PostRecoveryEvidence.create(
            attempt =
                ReliabilityTestFixtures.recoveryAttemptRecord(),
            evidence =
                RecoveryEvidence.create(
                    condition = condition,
                ),
        )
    }
}
