package com.devil.core.model.reliability

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class RecoveryAttemptCoordinatorTest {

    @Test
    fun `coordinator records one bounded attempt without executing recovery`() {
        val assessment =
            ReliabilityPolicy().assess(
                RecoveryEvidence.create(
                    condition = ReliabilityCondition.UNAVAILABLE,
                    recoveryPathKnown = true,
                ),
            )

        val requestResult =
            RecoveryRequestCoordinator().request(
                assessment = assessment,
                strategy =
                    RecoveryStrategy.RECONNECT_SOURCE,
                attemptBudget =
                    RecoveryAttemptBudget.create(
                        maximumAttempts = 2,
                    ),
            )

        val result =
            RecoveryAttemptCoordinator().record(
                requestResult = requestResult,
            )

        assertEquals(
            RecoveryAttemptStatus.RECORDED,
            result.status,
        )

        val record =
            assertNotNull(
                result.record,
            )

        assertEquals(
            1,
            record.attemptNumber,
        )
        assertEquals(
            1,
            record.remainingBudget.remainingAttempts,
        )
    }
}
