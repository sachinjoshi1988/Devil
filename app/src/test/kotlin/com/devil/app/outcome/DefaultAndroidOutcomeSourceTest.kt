package com.devil.app.outcome

import com.devil.app.verification.AndroidVerificationEvidence
import com.devil.core.model.capability.CapabilityId
import com.devil.core.model.common.TraceId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class DefaultAndroidOutcomeSourceTest {

    @Test
    fun `default source defers without fabricating outcome evidence`() {
        val result =
            DefaultAndroidOutcomeSource().establish(
                traceId = TraceId.from("trace-stage-33-source"),
                verificationEvidence =
                    AndroidVerificationEvidence.create(
                        capabilityId =
                            CapabilityId.from("capability-stage-33"),
                        description =
                            "Observed effect independently verified",
                    ),
            )

        assertEquals(AndroidOutcomeStatus.DEFERRED, result.status)
        assertNull(result.evidence)
        assertNull(result.error)
    }
}
