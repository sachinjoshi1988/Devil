package com.devil.app.voice

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class Stage37HandsFreeControlSequenceTest {

    @Test
    fun `wake then code red stops at genuine authentication boundary`() {
        val interactionCoordinator =
            HandsFreeInteractionCoordinator()

        val authenticationCoordinator =
            HandsFreeAuthenticationCoordinator()

        val wakeResult =
            interactionCoordinator
                .handleRecognizedTranscript(
                    state =
                        HandsFreeConversationState.IDLE,
                    transcript =
                        "Hey Devil",
                )

        assertEquals(
            HandsFreeConversationState
                .AWAITING_AUTHENTICATION_PHRASE,
            wakeResult.state,
        )

        assertEquals(
            HandsFreeInteractionAction.SPEAK_AND_LISTEN,
            wakeResult.action,
        )

        assertNull(wakeResult.conversationTranscript)

        val codeRedResult =
            interactionCoordinator
                .handleRecognizedTranscript(
                    state = wakeResult.state,
                    transcript = "Code Red",
                )

        assertEquals(
            HandsFreeConversationState
                .AUTHENTICATION_REQUESTED,
            codeRedResult.state,
        )

        assertEquals(
            HandsFreeInteractionAction.REQUEST_AUTHENTICATION,
            codeRedResult.action,
        )

        assertNull(codeRedResult.conversationTranscript)

        val authenticationResult =
            authenticationCoordinator
                .requestAuthentication(
                    state = codeRedResult.state,
                )

        assertEquals(
            HandsFreeAuthenticationHandoffStatus.UNAVAILABLE,
            authenticationResult.status,
        )

        assertEquals(
            false,
            codeRedResult.state ==
                HandsFreeConversationState.ACTIVE_SESSION,
        )
    }

    @Test
    fun `wake phrase is never submitted as ordinary conversation`() {
        val coordinator =
            HandsFreeInteractionCoordinator()

        val result =
            coordinator.handleRecognizedTranscript(
                state =
                    HandsFreeConversationState.IDLE,
                transcript =
                    "Devil",
            )

        assertEquals(
            HandsFreeInteractionAction.SPEAK_AND_LISTEN,
            result.action,
        )

        assertNull(result.conversationTranscript)
    }

    @Test
    fun `code red is never submitted as ordinary conversation`() {
        val coordinator =
            HandsFreeInteractionCoordinator()

        val result =
            coordinator.handleRecognizedTranscript(
                state =
                    HandsFreeConversationState
                        .AWAITING_AUTHENTICATION_PHRASE,
                transcript =
                    "Code Red",
            )

        assertEquals(
            HandsFreeInteractionAction.REQUEST_AUTHENTICATION,
            result.action,
        )

        assertNull(result.conversationTranscript)
    }

    @Test
    fun `unauthenticated command cannot enter ordinary conversation`() {
        val coordinator =
            HandsFreeInteractionCoordinator()

        val result =
            coordinator.handleRecognizedTranscript(
                state =
                    HandsFreeConversationState
                        .AUTHENTICATION_REQUESTED,
                transcript =
                    "Open YouTube",
            )

        assertEquals(
            HandsFreeInteractionAction.REQUEST_AUTHENTICATION,
            result.action,
        )

        assertNull(result.conversationTranscript)
    }
}
