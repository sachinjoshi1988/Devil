package com.devil.core.model.reliability

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class RecoveryAttemptPolicyTest {

    private val policy =
        RecoveryAttemptPolicy()

    @Test
    fun `available recovery request consumes exactly one budget unit`() {
        val assessment =
            ReliabilityPolicy().assess(
                RecoveryEvidence.create(
                    condition = ReliabilityCondition.DEGRADED,
                    recoveryPathKnown = true,
                ),
            )

        val requestResult =
            RecoveryRequestPolicy().evaluate(
                assessment = assessment,
                strategy =
                    RecoveryStrategy.REINITIALIZE_COMPONENT,
                attemptBudget =
                    RecoveryAttemptBudget.create(
                        maximumAttempts = 3,
                    ),
            )

        val result =
            policy.record(
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
            record.remainingBudget.attemptsAlreadyUsed,
        )
        assertEquals(
            2,
            record.remainingBudget.remainingAttempts,
        )
        assertNull(
            result.reason,
        )
    }

    @Test
    fun `unavailable recovery request cannot consume attempt`() {
        val requestResult =
            RecoveryRequestResult.unavailable(
                reason =
                    "Recovery assessment is not eligible.",
            )

        val result =
            policy.record(
                requestResult = requestResult,
            )

        assertEquals(
            RecoveryAttemptStatus.UNAVAILABLE,
            result.status,
        )
        assertNull(
            result.record,
        )
    }

    @Test
    fun `exhausted recovery request remains exhausted`() {
        val requestResult =
            RecoveryRequestResult.exhausted(
                reason =
                    "Recovery-attempt budget exhausted.",
            )

        val result =
            policy.record(
                requestResult = requestResult,
            )

        assertEquals(
            RecoveryAttemptStatus.EXHAUSTED,
            result.status,
        )
        assertNull(
            result.record,
        )
    }
}
