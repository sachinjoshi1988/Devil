package com.devil.core.model.reliability

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame

class ReliabilityPolicyTest {

    private val policy =
        ReliabilityPolicy()

    @Test
    fun `healthy evidence requires no recovery`() {
        val evidence =
            RecoveryEvidence.create(
                condition = ReliabilityCondition.HEALTHY,
            )

        val result =
            policy.assess(
                evidence = evidence,
            )

        assertSame(
            evidence,
            result.evidence,
        )
        assertEquals(
            RecoveryDisposition.RECOVERY_NOT_REQUIRED,
            result.disposition,
        )
    }

    @Test
    fun `degraded evidence with known recovery path becomes recovery eligible`() {
        val result =
            policy.assess(
                RecoveryEvidence.create(
                    condition = ReliabilityCondition.DEGRADED,
                    recoveryPathKnown = true,
                ),
            )

        assertEquals(
            RecoveryDisposition.RECOVERY_ELIGIBLE,
            result.disposition,
        )
    }

    @Test
    fun `manual intervention overrides automatic recovery eligibility`() {
        val result =
            policy.assess(
                RecoveryEvidence.create(
                    condition = ReliabilityCondition.FAILED,
                    error = ReliabilityTestFixtures.error(),
                    recoveryPathKnown = true,
                    manualInterventionRequired = true,
                ),
            )

        assertEquals(
            RecoveryDisposition.MANUAL_INTERVENTION_REQUIRED,
            result.disposition,
        )
    }

    @Test
    fun `failed evidence with error and no recovery path is not recoverable`() {
        val result =
            policy.assess(
                RecoveryEvidence.create(
                    condition = ReliabilityCondition.FAILED,
                    error = ReliabilityTestFixtures.error(),
                ),
            )

        assertEquals(
            RecoveryDisposition.NOT_RECOVERABLE,
            result.disposition,
        )
    }

    @Test
    fun `failed evidence without constitutional error remains unavailable`() {
        val result =
            policy.assess(
                RecoveryEvidence.create(
                    condition = ReliabilityCondition.FAILED,
                    recoveryPathKnown = true,
                ),
            )

        assertEquals(
            RecoveryDisposition.UNAVAILABLE,
            result.disposition,
        )
    }
}
