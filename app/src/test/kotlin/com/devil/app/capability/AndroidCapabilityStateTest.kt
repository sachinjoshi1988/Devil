package com.devil.app.capability

import com.devil.core.model.capability.CapabilityAvailabilityState
import com.devil.core.model.capability.CapabilityCategory
import com.devil.core.model.capability.CapabilityContract
import com.devil.core.model.capability.CapabilityHealthState
import com.devil.core.model.capability.CapabilityId
import kotlin.test.Test
import kotlin.test.assertEquals

class AndroidCapabilityStateTest {

    @Test
    fun `create preserves registered capability availability and health`() {
        val capability = createCapability()

        val state =
            AndroidCapabilityState.create(
                capability = capability,
                availability =
                    CapabilityAvailabilityState.AVAILABLE,
                health = CapabilityHealthState.DEGRADED,
            )

        assertEquals(capability, state.capability)
        assertEquals(
            CapabilityAvailabilityState.AVAILABLE,
            state.availability,
        )
        assertEquals(
            CapabilityHealthState.DEGRADED,
            state.health,
        )
    }

    private fun createCapability(): CapabilityContract {
        return CapabilityContract.create(
            capabilityId =
                CapabilityId.from(
                    "capability-stage-28-state",
                ),
            category = CapabilityCategory.ACTION,
            name = "Stage 28 Test Capability",
            description =
                "Represents one registered capability without granting execution authority.",
        )
    }
}
