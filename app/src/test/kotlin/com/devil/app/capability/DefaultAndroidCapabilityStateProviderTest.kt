package com.devil.app.capability

import com.devil.core.model.capability.CapabilityAvailabilityState
import com.devil.core.model.capability.CapabilityCategory
import com.devil.core.model.capability.CapabilityContract
import com.devil.core.model.capability.CapabilityHealthState
import com.devil.core.model.capability.CapabilityId
import kotlin.test.Test
import kotlin.test.assertEquals

class DefaultAndroidCapabilityStateProviderTest {

    @Test
    fun `default provider does not fabricate availability or healthy readiness`() {
        val capability =
            createCapability(
                id = "capability-stage-28-default",
            )

        val state =
            DefaultAndroidCapabilityStateProvider()
                .stateOf(capability)

        assertEquals(capability, state.capability)
        assertEquals(
            CapabilityAvailabilityState.UNAVAILABLE,
            state.availability,
        )
        assertEquals(
            CapabilityHealthState.UNAVAILABLE,
            state.health,
        )
    }

    @Test
    fun `provider preserves genuine available and ready health evidence`() {
        val capability =
            createCapability(
                id = "capability-stage-28-ready",
            )

        val provider =
            DefaultAndroidCapabilityStateProvider(
                availabilitySource =
                    AndroidCapabilityAvailabilitySource {
                        CapabilityAvailabilityState.AVAILABLE
                    },
                healthSource =
                    AndroidCapabilityHealthSource {
                        CapabilityHealthState.READY
                    },
            )

        val state = provider.stateOf(capability)

        assertEquals(capability, state.capability)
        assertEquals(
            CapabilityAvailabilityState.AVAILABLE,
            state.availability,
        )
        assertEquals(
            CapabilityHealthState.READY,
            state.health,
        )
    }

    @Test
    fun `provider preserves degraded health independently from availability`() {
        val capability =
            createCapability(
                id = "capability-stage-28-degraded",
            )

        val provider =
            DefaultAndroidCapabilityStateProvider(
                availabilitySource =
                    AndroidCapabilityAvailabilitySource {
                        CapabilityAvailabilityState.AVAILABLE
                    },
                healthSource =
                    AndroidCapabilityHealthSource {
                        CapabilityHealthState.DEGRADED
                    },
            )

        val state = provider.stateOf(capability)

        assertEquals(
            CapabilityAvailabilityState.AVAILABLE,
            state.availability,
        )
        assertEquals(
            CapabilityHealthState.DEGRADED,
            state.health,
        )
    }

    @Test
    fun `provider does not change registered capability identity`() {
        val capability =
            createCapability(
                id = "capability-stage-28-identity",
            )

        val provider =
            DefaultAndroidCapabilityStateProvider(
                availabilitySource =
                    AndroidCapabilityAvailabilitySource {
                        CapabilityAvailabilityState.UNAVAILABLE
                    },
                healthSource =
                    AndroidCapabilityHealthSource {
                        CapabilityHealthState.RECOVERING
                    },
            )

        val state = provider.stateOf(capability)

        assertEquals(
            capability.capabilityId,
            state.capability.capabilityId,
        )
        assertEquals(
            CapabilityHealthState.RECOVERING,
            state.health,
        )
    }

    private fun createCapability(
        id: String,
    ): CapabilityContract {
        return CapabilityContract.create(
            capabilityId = CapabilityId.from(id),
            category = CapabilityCategory.ACTION,
            name = "Stage 28 Test Capability",
            description =
                "Represents one bounded registered capability for availability and health tests.",
        )
    }
}
