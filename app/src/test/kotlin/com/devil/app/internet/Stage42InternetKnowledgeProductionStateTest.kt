package com.devil.app.internet

import com.devil.app.capability.AndroidCapabilityAvailabilitySource
import com.devil.app.capability.AndroidCapabilityHealthSource
import com.devil.app.capability.DefaultAndroidCapabilityAvailabilitySource
import com.devil.app.capability.DefaultAndroidCapabilityHealthSource
import com.devil.core.model.capability.CapabilityAvailabilityState
import com.devil.core.model.capability.CapabilityHealthState
import kotlin.test.Test
import kotlin.test.assertEquals

class Stage42InternetKnowledgeProductionStateTest {

    private val realSource =
        DefaultAndroidInternetKnowledgeSource()

    @Test
    fun `Internet Knowledge becomes available only when real source is supplied`() {
        val source: AndroidCapabilityAvailabilitySource =
            DefaultAndroidCapabilityAvailabilitySource(
                internetKnowledgeSource =
                    realSource,
            )

        assertEquals(
            CapabilityAvailabilityState.AVAILABLE,
            source.availability(
                AndroidInternetKnowledgeCapability.contract,
            ),
        )
    }

    @Test
    fun `Internet Knowledge becomes ready only when real source is supplied`() {
        val source: AndroidCapabilityHealthSource =
            DefaultAndroidCapabilityHealthSource(
                internetKnowledgeSource =
                    realSource,
            )

        assertEquals(
            CapabilityHealthState.READY,
            source.health(
                AndroidInternetKnowledgeCapability.contract,
            ),
        )
    }

    @Test
    fun `Internet Knowledge remains unavailable without real source evidence`() {
        assertEquals(
            CapabilityAvailabilityState.UNAVAILABLE,
            DefaultAndroidCapabilityAvailabilitySource()
                .availability(
                    AndroidInternetKnowledgeCapability.contract,
                ),
        )

        assertEquals(
            CapabilityHealthState.UNAVAILABLE,
            DefaultAndroidCapabilityHealthSource()
                .health(
                    AndroidInternetKnowledgeCapability.contract,
                ),
        )
    }
}
