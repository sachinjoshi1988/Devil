package com.devil.app.voice

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class HandsFreeProductionCoordinatorTest {

    @Test
    fun `wake phrase establishes attention and requests spoken authentication phrase`() {
        val coordinator =
            HandsFreeProductionCoordinator()

        val result =
            coordinator.handleRecognizedTranscript(
                state = HandsFreeConversationState.IDLE,
                transcript = "Hey Devil",
            )

        assertEquals(
            HandsFreeConversationState.AWAITING_AUTHENTICATION_PHRASE,
            result.state,
        )
        assertEquals(
            HandsFreeProductionAction.SPEAK_AND_LISTEN,
            result.action,
        )
        assertEquals(
            "I'm here. Say Code Red to request authentication.",
            result.message,
        )
        assertNull(result.runtimeTranscript)
        assertNull(result.authenticationResult)
    }

    @Test
    fun `code red reaches authentication boundary but does not authenticate`() {
        val coordinator =
            HandsFreeProductionCoordinator()

        val result =
            coordinator.handleRecognizedTranscript(
                state =
                    HandsFreeConversationState
                        .AWAITING_AUTHENTICATION_PHRASE,
                transcript = "Code Red",
            )

        assertEquals(
            HandsFreeConversationState.AUTHENTICATION_REQUESTED,
            result.state,
        )
        assertEquals(
            HandsFreeProductionAction.AUTHENTICATION_HANDOFF,
            result.action,
        )

        val authenticationResult =
            requireNotNull(
                result.authenticationResult,
            )

        assertEquals(
            HandsFreeAuthenticationHandoffStatus.UNAVAILABLE,
            authenticationResult.status,
        )
        assertNull(result.runtimeTranscript)
    }

    @Test
    fun `ordinary speech while idle never enters conversation runtime`() {
        val coordinator =
            HandsFreeProductionCoordinator()

        val result =
            coordinator.handleRecognizedTranscript(
                state = HandsFreeConversationState.IDLE,
                transcript = "Open my messages",
            )

        assertEquals(
            HandsFreeConversationState.IDLE,
            result.state,
        )
        assertEquals(
            HandsFreeProductionAction.LISTEN,
            result.action,
        )
        assertNull(result.runtimeTranscript)
        assertNull(result.authenticationResult)
    }

    @Test
    fun `authentication requested state remains blocked`() {
        val coordinator =
            HandsFreeProductionCoordinator()

        val result =
            coordinator.handleRecognizedTranscript(
                state =
                    HandsFreeConversationState
                        .AUTHENTICATION_REQUESTED,
                transcript = "Tell me the weather",
            )

        assertEquals(
            HandsFreeConversationState.AUTHENTICATION_REQUESTED,
            result.state,
        )
        assertNull(result.runtimeTranscript)
    }

    @Test
    fun `active session is not created by production coordinator`() {
        val coordinator =
            HandsFreeProductionCoordinator()

        val wake =
            coordinator.handleRecognizedTranscript(
                state = HandsFreeConversationState.IDLE,
                transcript = "Devil",
            )

        val codeRed =
            coordinator.handleRecognizedTranscript(
                state = wake.state,
                transcript = "Code Red",
            )

        assertEquals(
            HandsFreeConversationState.AUTHENTICATION_REQUESTED,
            codeRed.state,
        )
        assertEquals(
            false,
            codeRed.state ==
                HandsFreeConversationState.ACTIVE_SESSION,
        )
    }
}
