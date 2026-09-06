package com.devil.app.capability

import com.devil.app.device.AndroidDeviceKnowledgeCapability
import com.devil.app.internet.AndroidInternetKnowledgeCapability
import com.devil.app.internet.DefaultAndroidInternetKnowledgeSource
import com.devil.core.model.capability.CapabilityAvailabilityState
import com.devil.core.model.capability.CapabilityHealthState
import kotlin.test.Test
import kotlin.test.assertEquals

class Stage337KCapabilityAvailabilityAndHealthTest {

    @Test
    fun `real Internet retrieval source cannot become availability or health evidence`() {
        val realRetrievalSource =
            DefaultAndroidInternetKnowledgeSource()

        val stateProvider =
            DefaultAndroidCapabilityStateProvider(
                availabilitySource =
                    DefaultAndroidCapabilityAvailabilitySource(
                        internetKnowledgeSource =
                            realRetrievalSource,
                    ),
                healthSource =
                    DefaultAndroidCapabilityHealthSource(
                        internetKnowledgeSource =
                            realRetrievalSource,
                    ),
            )

        val state =
            stateProvider.stateOf(
                capability =
                    AndroidInternetKnowledgeCapability.contract,
            )

        assertEquals(
            AndroidInternetKnowledgeCapability.contract,
            state.capability,
        )
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
    fun `Device Knowledge retains genuine available and ready embodiment evidence`() {
        val state =
            DefaultAndroidCapabilityStateProvider()
                .stateOf(
                    capability =
                        AndroidDeviceKnowledgeCapability.contract,
                )

        assertEquals(
            AndroidDeviceKnowledgeCapability.contract,
            state.capability,
        )
        assertEquals(
            CapabilityAvailabilityState.AVAILABLE,
            state.availability,
        )
        assertEquals(
            CapabilityHealthState.READY,
            state.health,
        )
    }
}
