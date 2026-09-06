package com.devil.app.internet

import com.devil.app.capability.AndroidCapabilityAvailabilitySource
import com.devil.app.capability.AndroidCapabilityHealthSource
import com.devil.app.capability.DefaultAndroidCapabilityAvailabilitySource
import com.devil.app.capability.DefaultAndroidCapabilityHealthSource
import com.devil.core.model.capability.CapabilityAvailabilityState
import com.devil.core.model.capability.CapabilityHealthState
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Preserves Stage 42 Internet production-source coverage while applying the
 * tighter Stage 337K capability-state evidence boundary.
 *
 * A genuine retrieval implementation proves that bounded retrieval code exists.
 * Its presence alone does not prove current network availability or operational
 * health.
 */
class Stage42InternetKnowledgeProductionStateTest {

    private val realSource =
        DefaultAndroidInternetKnowledgeSource()

    @Test
    fun `Internet retrieval source presence does not establish capability availability`() {
        val source: AndroidCapabilityAvailabilitySource =
            DefaultAndroidCapabilityAvailabilitySource(
                internetKnowledgeSource =
                    realSource,
            )

        assertEquals(
            CapabilityAvailabilityState.UNAVAILABLE,
            source.availability(
                AndroidInternetKnowledgeCapability.contract,
            ),
        )
    }

    @Test
    fun `Internet retrieval source presence does not establish capability health`() {
        val source: AndroidCapabilityHealthSource =
            DefaultAndroidCapabilityHealthSource(
                internetKnowledgeSource =
                    realSource,
            )

        assertEquals(
            CapabilityHealthState.UNAVAILABLE,
            source.health(
                AndroidInternetKnowledgeCapability.contract,
            ),
        )
    }

    @Test
    fun `Internet Knowledge remains unavailable without retrieval source`() {
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
