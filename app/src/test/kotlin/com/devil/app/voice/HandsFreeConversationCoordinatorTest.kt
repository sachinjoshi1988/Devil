package com.devil.app.voice

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class HandsFreeConversationCoordinatorTest {

    private val coordinator =
        HandsFreeConversationCoordinator()

    @Test
    fun `ordinary text while idle does not enter hands free conversation`() {
        val result =
            coordinator.handleRecognizedTranscript(
                state =
                    HandsFreeConversationState.IDLE,
                transcript =
                    "Open the settings",
            )

        assertEquals(
            HandsFreeConversationState.IDLE,
            result.state,
        )
        assertEquals(
            HandsFreeConversationResultStatus.IGNORED,
            result.status,
        )
        assertNull(result.runtimeTranscript)
        assertNull(result.message)
    }

    @Test
    fun `approved wake phrase establishes attention only`() {
        val result =
            coordinator.handleRecognizedTranscript(
                state =
                    HandsFreeConversationState.IDLE,
                transcript =
                    "Hey Devil",
            )

        assertEquals(
            HandsFreeConversationState
                .AWAITING_AUTHENTICATION_PHRASE,
            result.state,
        )
        assertEquals(
            HandsFreeConversationResultStatus
                .WAKE_ESTABLISHED,
            result.status,
        )
        assertEquals(
            "I'm here. Say Code Red to request authentication.",
            result.message,
        )
        assertNull(result.runtimeTranscript)
    }

    @Test
    fun `wake does not establish authenticated session`() {
        val result =
            coordinator.handleRecognizedTranscript(
                state =
                    HandsFreeConversationState.IDLE,
                transcript =
                    "Devil",
            )

        assertEquals(
            false,
            result.state ==
                HandsFreeConversationState.ACTIVE_SESSION,
        )
        assertNull(result.runtimeTranscript)
    }

    @Test
    fun `wrong phrase after wake keeps authentication phrase required`() {
        val result =
            coordinator.handleRecognizedTranscript(
                state =
                    HandsFreeConversationState
                        .AWAITING_AUTHENTICATION_PHRASE,
                transcript =
                    "Hello",
            )

        assertEquals(
            HandsFreeConversationState
                .AWAITING_AUTHENTICATION_PHRASE,
            result.state,
        )
        assertEquals(
            HandsFreeConversationResultStatus
                .AUTHENTICATION_PHRASE_REQUIRED,
            result.status,
        )
        assertEquals(
            "Say Code Red to request authentication.",
            result.message,
        )
        assertNull(result.runtimeTranscript)
    }

    @Test
    fun `code red requests authentication without authenticating speaker`() {
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
            HandsFreeConversationResultStatus
                .AUTHENTICATION_REQUESTED,
            result.status,
        )
        assertEquals(
            "Code Red recognized. Authentication is required.",
            result.message,
        )
        assertNull(result.runtimeTranscript)

        assertEquals(
            false,
            result.state ==
                HandsFreeConversationState.ACTIVE_SESSION,
        )
    }

    @Test
    fun `ordinary conversation remains blocked while authentication is pending`() {
        val result =
            coordinator.handleRecognizedTranscript(
                state =
                    HandsFreeConversationState
                        .AUTHENTICATION_REQUESTED,
                transcript =
                    "Open YouTube",
            )

        assertEquals(
            HandsFreeConversationState
                .AUTHENTICATION_REQUESTED,
            result.state,
        )
        assertEquals(
            HandsFreeConversationResultStatus
                .AUTHENTICATION_REQUESTED,
            result.status,
        )
        assertNull(result.runtimeTranscript)
    }

    @Test
    fun `only already active authenticated session may expose runtime transcript`() {
        val result =
            coordinator.handleRecognizedTranscript(
                state =
                    HandsFreeConversationState.ACTIVE_SESSION,
                transcript =
                    "  Hello Devil  ",
            )

        assertEquals(
            HandsFreeConversationState.ACTIVE_SESSION,
            result.state,
        )
        assertEquals(
            HandsFreeConversationResultStatus
                .CONVERSATION_INPUT_ALLOWED,
            result.status,
        )
        assertEquals(
            "Hello Devil",
            result.runtimeTranscript,
        )
        assertNull(result.message)
    }

    @Test
    fun `reset returns hands free state to idle`() {
        val result =
            coordinator.reset()

        assertEquals(
            HandsFreeConversationState.IDLE,
            result.state,
        )
        assertEquals(
            HandsFreeConversationResultStatus.IGNORED,
            result.status,
        )
        assertNull(result.runtimeTranscript)
        assertNull(result.message)
    }
}
