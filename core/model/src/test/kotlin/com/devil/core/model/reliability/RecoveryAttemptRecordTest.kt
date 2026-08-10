package com.devil.core.model.reliability

import kotlin.test.Test
import kotlin.test.assertEquals

class RecoveryAttemptRecordTest {

    @Test
    fun `record consumes exactly one recovery attempt`() {
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
                        maximumAttempts = 3,
                        attemptsAlreadyUsed = 1,
                    ),
            )

        val record =
            RecoveryAttemptRecord.create(
                request = request,
            )

        assertEquals(
            2,
            record.attemptNumber,
        )
        assertEquals(
            2,
            record.remainingBudget.attemptsAlreadyUsed,
        )
        assertEquals(
            1,
            record.remainingBudget.remainingAttempts,
        )

        assertEquals(
            1,
            request.attemptBudget.attemptsAlreadyUsed,
        )
    }
}
