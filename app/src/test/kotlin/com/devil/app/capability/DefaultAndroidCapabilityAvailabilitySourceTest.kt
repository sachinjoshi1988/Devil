package com.devil.app.capability

import com.devil.core.model.capability.CapabilityAvailabilityState
import com.devil.core.model.capability.CapabilityCategory
import com.devil.core.model.capability.CapabilityContract
import com.devil.core.model.capability.CapabilityId
import kotlin.test.Test
import kotlin.test.assertEquals

class DefaultAndroidCapabilityAvailabilitySourceTest {

    @Test
    fun `default source remains unavailable without genuine evidence`() {
        val result =
            DefaultAndroidCapabilityAvailabilitySource()
                .availability(
                    CapabilityContract.create(
                        capabilityId =
                            CapabilityId.from(
                                "capability-stage-28-availability",
                            ),
                        category = CapabilityCategory.ACTION,
                        name = "Availability Test",
                        description =
                            "Registered capability without production availability evidence.",
                    ),
                )

        assertEquals(
            CapabilityAvailabilityState.UNAVAILABLE,
            result,
        )
    }
}
