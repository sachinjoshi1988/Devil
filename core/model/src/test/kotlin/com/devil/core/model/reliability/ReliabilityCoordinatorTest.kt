package com.devil.core.model.reliability

import kotlin.test.Test
import kotlin.test.assertEquals

class ReliabilityCoordinatorTest {

    @Test
    fun `coordinator delegates bounded reliability assessment`() {
        val coordinator =
            ReliabilityCoordinator()

        val result =
            coordinator.assess(
                RecoveryEvidence.create(
                    condition =
                        ReliabilityCondition.UNAVAILABLE,
                    recoveryPathKnown = true,
                ),
            )

        assertEquals(
            RecoveryDisposition.RECOVERY_ELIGIBLE,
            result.disposition,
        )
    }
}
