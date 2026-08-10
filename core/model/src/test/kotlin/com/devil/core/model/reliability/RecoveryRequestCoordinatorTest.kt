package com.devil.core.model.reliability

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class RecoveryRequestCoordinatorTest {

    @Test
    fun `coordinator produces bounded request without executing recovery`() {
        val assessment =
            ReliabilityPolicy().assess(
                RecoveryEvidence.create(
                    condition = ReliabilityCondition.DEGRADED,
                    recoveryPathKnown = true,
                ),
            )

        val result =
            RecoveryRequestCoordinator().request(
                assessment = assessment,
                strategy =
                    RecoveryStrategy.REINITIALIZE_COMPONENT,
                attemptBudget =
                    RecoveryAttemptBudget.create(
                        maximumAttempts = 2,
                    ),
            )

        assertEquals(
            RecoveryRequestStatus.AVAILABLE,
            result.status,
        )
        assertNotNull(
            result.request,
        )
    }
}
