package com.devil.app.capability

import com.devil.core.model.capability.CapabilityCategory
import com.devil.core.model.capability.CapabilityContract
import com.devil.core.model.capability.CapabilityHealthState
import com.devil.core.model.capability.CapabilityId
import kotlin.test.Test
import kotlin.test.assertEquals

class DefaultAndroidCapabilityHealthSourceTest {

    @Test
    fun `default source remains unavailable without genuine health evidence`() {
        val result =
            DefaultAndroidCapabilityHealthSource()
                .health(
                    CapabilityContract.create(
                        capabilityId =
                            CapabilityId.from(
                                "capability-stage-28-health",
                            ),
                        category = CapabilityCategory.ACTION,
                        name = "Health Test",
                        description =
                            "Registered capability without production health evidence.",
                    ),
                )

        assertEquals(
            CapabilityHealthState.UNAVAILABLE,
            result,
        )
    }
}
