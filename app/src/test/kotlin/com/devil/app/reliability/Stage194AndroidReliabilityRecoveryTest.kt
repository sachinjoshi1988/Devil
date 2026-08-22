package com.devil.app.reliability

import com.devil.core.model.reliability.RecoveryAttemptBudget
import com.devil.core.model.reliability.RecoveryDisposition
import com.devil.core.model.reliability.RecoveryEvidence
import com.devil.core.model.reliability.RecoveryRequestResult
import com.devil.core.model.reliability.RecoveryStrategy
import com.devil.core.model.reliability.ReliabilityAssessment
import com.devil.core.model.reliability.ReliabilityCondition
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertSame

class Stage194AndroidReliabilityRecoveryTest {

    @Test
    fun `eligible Stage 45 assessment produces available recovery request with exact provenance`() {
        val assessment = eligibleAssessment()

        val result =
            AndroidReliabilityRecoveryCoordinator()
                .prepare(
                    assessment = assessment,
                    strategy = RecoveryStrategy.RETRY_SAME_OPERATION,
                    attemptBudget =
                        RecoveryAttemptBudget.create(
                            maximumAttempts = 2,
                        ),
                )

        assertEquals(
            AndroidReliabilityRecoveryStatus.AVAILABLE,
            result.status,
        )
        assertSame(assessment, result.assessment)
        assertSame(
            assessment,
            result.recoveryRequestResult.request?.assessment,
        )
    }

    @Test
    fun `non eligible Stage 45 assessment remains deferred`() {
        val assessment =
            ReliabilityAssessment.create(
                evidence =
                    RecoveryEvidence.create(
                        condition = ReliabilityCondition.HEALTHY,
                    ),
                disposition = RecoveryDisposition.RECOVERY_NOT_REQUIRED,
                rationale = "Component is healthy.",
            )

        val result =
            AndroidReliabilityRecoveryCoordinator()
                .prepare(
                    assessment = assessment,
                    strategy = RecoveryStrategy.RETRY_SAME_OPERATION,
                    attemptBudget =
                        RecoveryAttemptBudget.create(
                            maximumAttempts = 1,
                        ),
                )

        assertEquals(
            AndroidReliabilityRecoveryStatus.DEFERRED,
            result.status,
        )
        assertSame(assessment, result.assessment)
    }

    @Test
    fun `exhausted budget remains deferred`() {
        val assessment = eligibleAssessment()

        val result =
            AndroidReliabilityRecoveryCoordinator()
                .prepare(
                    assessment = assessment,
                    strategy = RecoveryStrategy.RETRY_SAME_OPERATION,
                    attemptBudget =
                        RecoveryAttemptBudget.create(
                            maximumAttempts = 1,
                            attemptsAlreadyUsed = 1,
                        ),
                )

        assertEquals(
            AndroidReliabilityRecoveryStatus.DEFERRED,
            result.status,
        )
    }

    @Test
    fun `available Android result rejects non available Stage 45 result`() {
        val assessment =
            ReliabilityAssessment.create(
                evidence =
                    RecoveryEvidence.create(
                        condition = ReliabilityCondition.HEALTHY,
                    ),
                disposition = RecoveryDisposition.RECOVERY_NOT_REQUIRED,
                rationale = "No recovery required.",
            )

        assertFailsWith<IllegalArgumentException> {
            AndroidReliabilityRecoveryResult.create(
                status = AndroidReliabilityRecoveryStatus.AVAILABLE,
                assessment = assessment,
                recoveryRequestResult =
                    RecoveryRequestResult.unavailable(
                        reason = "No recovery required.",
                    ),
            )
        }
    }

    private fun eligibleAssessment(): ReliabilityAssessment {
        return ReliabilityAssessment.create(
            evidence =
                RecoveryEvidence.create(
                    condition = ReliabilityCondition.FAILED,
                    recoveryPathKnown = true,
                ),
            disposition = RecoveryDisposition.RECOVERY_ELIGIBLE,
            rationale = "A bounded recovery path is known.",
        )
    }
}
