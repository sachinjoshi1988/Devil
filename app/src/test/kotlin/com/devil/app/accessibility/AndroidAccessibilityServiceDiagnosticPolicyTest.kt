package com.devil.app.accessibility

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AndroidAccessibilityServiceDiagnosticPolicyTest {

    @Test
    fun `connected service is diagnosed as connected`() {
        val result =
            AndroidAccessibilityServiceDiagnosticPolicy.classify(
                serviceConnected = true,
                enabledInAndroid = true,
            )

        assertEquals(
            AndroidAccessibilityServiceDiagnosticStatus.CONNECTED,
            result.status,
        )

        assertTrue(
            result.message.contains(
                "connected",
                ignoreCase = true,
            ),
        )
    }

    @Test
    fun `enabled Android service without live connection remains distinct`() {
        val result =
            AndroidAccessibilityServiceDiagnosticPolicy.classify(
                serviceConnected = false,
                enabledInAndroid = true,
            )

        assertEquals(
            AndroidAccessibilityServiceDiagnosticStatus.ENABLED_BUT_DISCONNECTED,
            result.status,
        )

        assertTrue(
            result.message.contains(
                "not currently connected",
                ignoreCase = true,
            ),
        )
    }

    @Test
    fun `disabled service is diagnosed separately`() {
        val result =
            AndroidAccessibilityServiceDiagnosticPolicy.classify(
                serviceConnected = false,
                enabledInAndroid = false,
            )

        assertEquals(
            AndroidAccessibilityServiceDiagnosticStatus.DISABLED,
            result.status,
        )

        assertTrue(
            result.message.contains(
                "enable Devil",
                ignoreCase = true,
            ),
        )
    }

    @Test
    fun `unknown diagnosis does not fabricate Android state`() {
        val result =
            AndroidAccessibilityServiceDiagnosticPolicy.unknown()

        assertEquals(
            AndroidAccessibilityServiceDiagnosticStatus.UNKNOWN,
            result.status,
        )

        assertTrue(
            result.message.contains(
                "could not determine",
                ignoreCase = true,
            ),
        )
    }

    @Test
    fun `live connection takes precedence over enabled inventory evidence`() {
        val result =
            AndroidAccessibilityServiceDiagnosticPolicy.classify(
                serviceConnected = true,
                enabledInAndroid = false,
            )

        assertEquals(
            AndroidAccessibilityServiceDiagnosticStatus.CONNECTED,
            result.status,
        )
    }
}
