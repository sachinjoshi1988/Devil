package com.devil.app.communication

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class Stage184ContactsCommunicationTest {

    @Test
    fun `recipient normalizes explicitly supplied metadata`() {
        val recipient =
            AndroidCommunicationRecipient.create(
                displayName = "  Alice Example  ",
                address = "  +91 98765 43210  ",
            )

        assertEquals("Alice Example", recipient.displayName)
        assertEquals("+91 98765 43210", recipient.address)
    }

    @Test
    fun `blank recipient metadata is rejected`() {
        assertFailsWith<IllegalArgumentException> {
            AndroidCommunicationRecipient.create(
                displayName = "   ",
                address = "+91 98765 43210",
            )
        }

        assertFailsWith<IllegalArgumentException> {
            AndroidCommunicationRecipient.create(
                displayName = "Alice",
                address = "   ",
            )
        }
    }

    @Test
    fun `explicit recipient becomes available unchanged`() {
        val recipient =
            AndroidCommunicationRecipient.create(
                displayName = "Alice",
                address = "+91 98765 43210",
            )

        val result =
            AndroidCommunicationIntelligenceCoordinator()
                .integrate(recipient)

        assertEquals(
            AndroidCommunicationIntelligenceStatus.AVAILABLE,
            result.status,
        )
        assertEquals(recipient, result.recipient)
    }

    @Test
    fun `absent recipient remains deferred`() {
        val result =
            AndroidCommunicationIntelligenceCoordinator()
                .integrate(null)

        assertEquals(
            AndroidCommunicationIntelligenceStatus.DEFERRED,
            result.status,
        )
        assertNull(result.recipient)
    }

    @Test
    fun `available result requires recipient`() {
        assertFailsWith<IllegalArgumentException> {
            AndroidCommunicationIntelligenceResult.create(
                status =
                    AndroidCommunicationIntelligenceStatus.AVAILABLE,
            )
        }
    }

    @Test
    fun `deferred result rejects recipient`() {
        val recipient =
            AndroidCommunicationRecipient.create(
                displayName = "Alice",
                address = "+91 98765 43210",
            )

        assertFailsWith<IllegalArgumentException> {
            AndroidCommunicationIntelligenceResult.create(
                status =
                    AndroidCommunicationIntelligenceStatus.DEFERRED,
                recipient = recipient,
            )
        }
    }
}
