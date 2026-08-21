package com.devil.app.communication

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class Stage185MessagingAssistanceTest {

    @Test
    fun `available Stage 184 recipient prepares normalized message`() {
        val recipient =
            AndroidCommunicationRecipient.create(
                displayName = "Alice",
                address = "+91 98765 43210",
            )

        val communication =
            AndroidCommunicationIntelligenceResult.create(
                status = AndroidCommunicationIntelligenceStatus.AVAILABLE,
                recipient = recipient,
            )

        val result =
            AndroidMessagingAssistanceCoordinator()
                .prepare(
                    communicationIntelligence = communication,
                    messageText = "  Hello Alice  ",
                )

        assertEquals(
            AndroidMessagingAssistanceStatus.READY,
            result.status,
        )
        assertEquals(communication, result.communicationIntelligence)
        assertEquals(recipient, result.preparedMessage?.recipient)
        assertEquals("Hello Alice", result.preparedMessage?.messageText)
    }

    @Test
    fun `deferred Stage 184 communication remains deferred`() {
        val communication =
            AndroidCommunicationIntelligenceResult.create(
                status = AndroidCommunicationIntelligenceStatus.DEFERRED,
            )

        val result =
            AndroidMessagingAssistanceCoordinator()
                .prepare(
                    communicationIntelligence = communication,
                    messageText = "Hello",
                )

        assertEquals(
            AndroidMessagingAssistanceStatus.DEFERRED,
            result.status,
        )
        assertEquals(communication, result.communicationIntelligence)
        assertNull(result.preparedMessage)
    }

    @Test
    fun `blank message text is rejected when recipient is available`() {
        val recipient =
            AndroidCommunicationRecipient.create(
                displayName = "Alice",
                address = "+91 98765 43210",
            )

        val communication =
            AndroidCommunicationIntelligenceResult.create(
                status = AndroidCommunicationIntelligenceStatus.AVAILABLE,
                recipient = recipient,
            )

        assertFailsWith<IllegalArgumentException> {
            AndroidMessagingAssistanceCoordinator()
                .prepare(
                    communicationIntelligence = communication,
                    messageText = "   ",
                )
        }
    }

    @Test
    fun `ready result rejects prepared message for foreign recipient`() {
        val recipient =
            AndroidCommunicationRecipient.create(
                displayName = "Alice",
                address = "+91 98765 43210",
            )

        val foreignRecipient =
            AndroidCommunicationRecipient.create(
                displayName = "Bob",
                address = "+91 90000 00000",
            )

        val communication =
            AndroidCommunicationIntelligenceResult.create(
                status = AndroidCommunicationIntelligenceStatus.AVAILABLE,
                recipient = recipient,
            )

        val preparedMessage =
            AndroidPreparedMessage.create(
                recipient = foreignRecipient,
                messageText = "Hello",
            )

        assertFailsWith<IllegalArgumentException> {
            AndroidMessagingAssistanceResult.create(
                status = AndroidMessagingAssistanceStatus.READY,
                communicationIntelligence = communication,
                preparedMessage = preparedMessage,
            )
        }
    }

    @Test
    fun `deferred result rejects prepared message`() {
        val communication =
            AndroidCommunicationIntelligenceResult.create(
                status = AndroidCommunicationIntelligenceStatus.DEFERRED,
            )

        val recipient =
            AndroidCommunicationRecipient.create(
                displayName = "Alice",
                address = "+91 98765 43210",
            )

        val preparedMessage =
            AndroidPreparedMessage.create(
                recipient = recipient,
                messageText = "Hello",
            )

        assertFailsWith<IllegalArgumentException> {
            AndroidMessagingAssistanceResult.create(
                status = AndroidMessagingAssistanceStatus.DEFERRED,
                communicationIntelligence = communication,
                preparedMessage = preparedMessage,
            )
        }
    }
}
