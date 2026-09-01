package com.devil.app.voice

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class HandsFreeInteractionCoordinatorTest {

    private val coordinator =
        HandsFreeInteractionCoordinator()

    @Test
    fun `ordinary speech while idle produces no action`() {
        val result =
            coordinator.handleRecognizedTranscript(
                state =
                    HandsFreeConversationState.IDLE,
                transcript =
                    "Open YouTube",
            )

        assertEquals(
            HandsFreeConversationState.IDLE,
            result.state,
        )
        assertEquals(
            HandsFreeInteractionAction.NONE,
            result.action,
        )
        assertNull(result.spokenMessage)
        assertNull(result.conversationTranscript)
    }

    @Test
    fun `wake phrase requests acknowledgement then another recognition attempt`() {
        val result =
            coordinator.handleRecognizedTranscript(
                state =
                    HandsFreeConversationState.IDLE,
                transcript =
                    "Devil are you there?",
            )

        assertEquals(
            HandsFreeConversationState
                .AWAITING_AUTHENTICATION_PHRASE,
            result.state,
        )

        assertEquals(
            HandsFreeInteractionAction.SPEAK_AND_LISTEN,
            result.action,
        )

        assertEquals(
            "I'm here, sir. Authentication please.",
            result.spokenMessage,
        )

        assertNull(result.conversationTranscript)
    }

    @Test
    fun `incorrect authentication phrase requests code red again`() {
        val result =
            coordinator.handleRecognizedTranscript(
                state =
                    HandsFreeConversationState
                        .AWAITING_AUTHENTICATION_PHRASE,
                transcript =
                    "Hello there",
            )

        assertEquals(
            HandsFreeConversationState
                .AWAITING_AUTHENTICATION_PHRASE,
            result.state,
        )

        assertEquals(
            HandsFreeInteractionAction.SPEAK_AND_LISTEN,
            result.action,
        )

        assertEquals(
            "Authentication please.",
            result.spokenMessage,
        )

        assertNull(result.conversationTranscript)
    }

    @Test
    fun `code red produces authentication handoff action only`() {
        val result =
            coordinator.handleRecognizedTranscript(
                state =
                    HandsFreeConversationState
                        .AWAITING_AUTHENTICATION_PHRASE,
                transcript =
                    "Code Red",
            )

        assertEquals(
            HandsFreeConversationState
                .AUTHENTICATION_REQUESTED,
            result.state,
        )

        assertEquals(
            HandsFreeInteractionAction.REQUEST_AUTHENTICATION,
            result.action,
        )

        assertEquals(
            "Authentication requested.",
            result.spokenMessage,
        )

        assertNull(result.conversationTranscript)

        assertEquals(
            false,
            result.state ==
                HandsFreeConversationState.ACTIVE_SESSION,
        )
    }

    @Test
    fun `pending authentication cannot submit ordinary conversation`() {
        val result =
            coordinator.handleRecognizedTranscript(
                state =
                    HandsFreeConversationState
                        .AUTHENTICATION_REQUESTED,
                transcript =
                    "Open WhatsApp",
            )

        assertEquals(
            HandsFreeInteractionAction.REQUEST_AUTHENTICATION,
            result.action,
        )

        assertNull(result.conversationTranscript)

        assertEquals(
            HandsFreeConversationState
                .AUTHENTICATION_REQUESTED,
            result.state,
        )
    }

    @Test
    fun `already active future session may submit recognized text`() {
        val result =
            coordinator.handleRecognizedTranscript(
                state =
                    HandsFreeConversationState.ACTIVE_SESSION,
                transcript =
                    "  Open WhatsApp  ",
            )

        assertEquals(
            HandsFreeConversationState.ACTIVE_SESSION,
            result.state,
        )

        assertEquals(
            HandsFreeInteractionAction.SUBMIT_CONVERSATION,
            result.action,
        )

        assertEquals(
            "Open WhatsApp",
            result.conversationTranscript,
        )

        assertNull(result.spokenMessage)
    }

    @Test
    fun `reset returns orchestration to idle with no action`() {
        val result =
            coordinator.reset()

        assertEquals(
            HandsFreeConversationState.IDLE,
            result.state,
        )

        assertEquals(
            HandsFreeInteractionAction.NONE,
            result.action,
        )

        assertNull(result.spokenMessage)
        assertNull(result.conversationTranscript)
    }
}
