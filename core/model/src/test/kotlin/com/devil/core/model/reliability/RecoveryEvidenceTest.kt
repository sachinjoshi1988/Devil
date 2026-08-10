package com.devil.core.model.reliability

import com.devil.core.model.capability.CapabilityHealthState
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class RecoveryEvidenceTest {

    @Test
    fun `healthy evidence preserves bounded state`() {
        val evidence =
            RecoveryEvidence.create(
                condition = ReliabilityCondition.HEALTHY,
                capabilityHealth = CapabilityHealthState.READY,
            )

        assertEquals(
            ReliabilityCondition.HEALTHY,
            evidence.condition,
        )
        assertEquals(
            CapabilityHealthState.READY,
            evidence.capabilityHealth,
        )
        assertNull(evidence.error)
    }

    @Test
    fun `non-failed condition rejects constitutional error attachment`() {
        assertFailsWith<IllegalArgumentException> {
            RecoveryEvidence.create(
                condition = ReliabilityCondition.DEGRADED,
                error =
                    ReliabilityTestFixtures.error(),
            )
        }
    }

    @Test
    fun `healthy evidence rejects manual intervention requirement`() {
        assertFailsWith<IllegalArgumentException> {
            RecoveryEvidence.create(
                condition = ReliabilityCondition.HEALTHY,
                manualInterventionRequired = true,
            )
        }
    }
}
