package com.devil.core.model.reliability

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame

class PostRecoveryEvidenceTest {

    @Test
    fun `post-recovery evidence preserves attempt and explicit later evidence`() {
        val attempt =
            ReliabilityTestFixtures.recoveryAttemptRecord()

        val evidence =
            RecoveryEvidence.create(
                condition = ReliabilityCondition.HEALTHY,
            )

        val postRecoveryEvidence =
            PostRecoveryEvidence.create(
                attempt = attempt,
                evidence = evidence,
            )

        assertSame(
            attempt,
            postRecoveryEvidence.attempt,
        )

        assertSame(
            evidence,
            postRecoveryEvidence.evidence,
        )

        assertEquals(
            ReliabilityCondition.HEALTHY,
            postRecoveryEvidence.evidence.condition,
        )
    }
}
