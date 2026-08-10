package com.devil.app.internet

import com.devil.app.capability.DefaultAndroidCapabilityRegistrationSource
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class Stage42InternetKnowledgeCapabilityRegistrationTest {

    @Test
    fun `default Android registration source exposes Internet Knowledge exactly once`() {
        val registrations =
            DefaultAndroidCapabilityRegistrationSource()
                .registrations()

        assertTrue(
            registrations.contains(
                AndroidInternetKnowledgeCapability.contract,
            ),
        )

        assertEquals(
            1,
            registrations.count {
                it.capabilityId ==
                    AndroidInternetKnowledgeCapability.capabilityId
            },
        )
    }
}
