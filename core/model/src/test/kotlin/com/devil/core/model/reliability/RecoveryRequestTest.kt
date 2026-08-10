package com.devil.core.model.reliability

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class RecoveryRequestTest {

    @Test
    fun `recovery eligible assessment creates bounded request`() {
        val assessment =
            ReliabilityPolicy().assess(
                RecoveryEvidence.create(
                    condition = ReliabilityCondition.DEGRADED,
                    recoveryPathKnown = true,
                ),
            )

        val request =
            RecoveryRequest.create(
                assessment = assessment,
                strategy =
                    RecoveryStrategy.RETRY_SAME_OPERATION,
                attemptBudget =
                    RecoveryAttemptBudget.create(
                        maximumAttempts = 2,
                    ),
            )

        assertEquals(
            RecoveryDisposition.RECOVERY_ELIGIBLE,
            request.assessment.disposition,
        )
        assertEquals(
            RecoveryStrategy.RETRY_SAME_OPERATION,
            request.strategy,
        )
        assertEquals(
            2,
            request.attemptBudget.remainingAttempts,
        )
    }

    @Test
    fun `non eligible assessment cannot create recovery request`() {
        val assessment =
            ReliabilityPolicy().assess(
                RecoveryEvidence.create(
                    condition = ReliabilityCondition.HEALTHY,
                ),
            )

        assertFailsWith<IllegalArgumentException> {
            RecoveryRequest.create(
                assessment = assessment,
                strategy =
                    RecoveryStrategy.RETRY_SAME_OPERATION,
                attemptBudget =
                    RecoveryAttemptBudget.create(
                        maximumAttempts = 1,
                    ),
            )
        }
    }

    @Test
    fun `exhausted budget cannot create recovery request`() {
        val assessment =
            ReliabilityPolicy().assess(
                RecoveryEvidence.create(
                    condition = ReliabilityCondition.UNAVAILABLE,
                    recoveryPathKnown = true,
                ),
            )

        assertFailsWith<IllegalArgumentException> {
            RecoveryRequest.create(
                assessment = assessment,
                strategy =
                    RecoveryStrategy.REINITIALIZE_COMPONENT,
                attemptBudget =
                    RecoveryAttemptBudget.create(
                        maximumAttempts = 1,
                        attemptsAlreadyUsed = 1,
                    ),
            )
        }
    }
}
