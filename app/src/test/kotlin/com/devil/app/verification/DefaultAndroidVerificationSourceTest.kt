package com.devil.app.verification

import com.devil.app.observation.AndroidObservationEvidence
import com.devil.core.model.capability.CapabilityId
import com.devil.core.model.common.TraceId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class DefaultAndroidVerificationSourceTest {

    @Test
    fun `default source defers without fabricating verification evidence`() {
        val result =
            DefaultAndroidVerificationSource().verify(
                traceId = TraceId.from("trace-stage-32-source"),
                observationEvidence =
                    AndroidObservationEvidence.create(
                        capabilityId =
                            CapabilityId.from("capability-stage-32"),
                        description = "Observed bounded Android effect",
                    ),
            )

        assertEquals(AndroidVerificationStatus.DEFERRED, result.status)
        assertNull(result.evidence)
        assertNull(result.error)
    }
}
