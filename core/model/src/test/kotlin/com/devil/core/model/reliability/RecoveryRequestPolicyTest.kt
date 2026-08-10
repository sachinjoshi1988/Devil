package com.devil.core.model.reliability

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class RecoveryRequestPolicyTest {

    private val policy =
        RecoveryRequestPolicy()

    @Test
    fun `eligible assessment with remaining budget produces request`() {
        val assessment =
            ReliabilityPolicy().assess(
                RecoveryEvidence.create(
                    condition = ReliabilityCondition.DEGRADED,
                    recoveryPathKnown = true,
                ),
            )

        val result =
            policy.evaluate(
                assessment = assessment,
                strategy =
                    RecoveryStrategy.RETRY_SAME_OPERATION,
                attemptBudget =
                    RecoveryAttemptBudget.create(
                        maximumAttempts = 3,
                    ),
            )

        assertEquals(
            RecoveryRequestStatus.AVAILABLE,
            result.status,
        )
        assertNotNull(
            result.request,
        )
        assertNull(
            result.reason,
        )
    }

    @Test
    fun `non eligible assessment remains unavailable`() {
        val assessment =
            ReliabilityPolicy().assess(
                RecoveryEvidence.create(
                    condition = ReliabilityCondition.HEALTHY,
                ),
            )

        val result =
            policy.evaluate(
                assessment = assessment,
                strategy =
                    RecoveryStrategy.RETRY_SAME_OPERATION,
                attemptBudget =
                    RecoveryAttemptBudget.create(
                        maximumAttempts = 1,
                    ),
            )

        assertEquals(
            RecoveryRequestStatus.UNAVAILABLE,
            result.status,
        )
        assertNull(
            result.request,
        )
    }

    @Test
    fun `exhausted budget blocks recovery request`() {
        val assessment =
            ReliabilityPolicy().assess(
                RecoveryEvidence.create(
                    condition = ReliabilityCondition.UNAVAILABLE,
                    recoveryPathKnown = true,
                ),
            )

        val result =
            policy.evaluate(
                assessment = assessment,
                strategy =
                    RecoveryStrategy.RECONNECT_SOURCE,
                attemptBudget =
                    RecoveryAttemptBudget.create(
                        maximumAttempts = 2,
                        attemptsAlreadyUsed = 2,
                    ),
            )

        assertEquals(
            RecoveryRequestStatus.EXHAUSTED,
            result.status,
        )
        assertNull(
            result.request,
        )
    }
}
