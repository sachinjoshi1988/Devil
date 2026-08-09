package com.devil.app.observation

import com.devil.core.model.capability.CapabilityId
import com.devil.core.model.common.TraceId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class DefaultAndroidObservationSourceTest {

    @Test
    fun `default source defers without fabricating observation evidence`() {
        val traceId =
            TraceId.from(
                "trace-default-android-observation-source-001",
            )

        val result =
            DefaultAndroidObservationSource().observe(
                traceId = traceId,
                capabilityId =
                    CapabilityId.from(
                        "capability-default-observation",
                    ),
            )

        assertEquals(traceId, result.traceId)
        assertEquals(
            AndroidObservationStatus.DEFERRED,
            result.status,
        )
        assertNull(result.evidence)
        assertNull(result.error)
    }
}
