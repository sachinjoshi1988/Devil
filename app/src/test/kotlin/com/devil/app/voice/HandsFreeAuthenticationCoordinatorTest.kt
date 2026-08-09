package com.devil.app.voice

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class HandsFreeAuthenticationCoordinatorTest {

    @Test
    fun `default authentication handoff fails closed`() {
        val coordinator =
            HandsFreeAuthenticationCoordinator()

        val result =
            coordinator.requestAuthentication(
                state =
                    HandsFreeConversationState
                        .AUTHENTICATION_REQUESTED,
            )

        assertEquals(
            HandsFreeAuthenticationHandoffStatus.UNAVAILABLE,
            result.status,
        )

        assertEquals(
            "Authentication is not yet available for hands-free continuation.",
            result.message,
        )
    }

    @Test
    fun `authentication handoff is not allowed directly from wake state`() {
        val coordinator =
            HandsFreeAuthenticationCoordinator()

        assertFailsWith<IllegalArgumentException> {
            coordinator.requestAuthentication(
                state =
                    HandsFreeConversationState
                        .AWAITING_AUTHENTICATION_PHRASE,
            )
        }
    }

    @Test
    fun `authentication handoff is not allowed directly from idle`() {
        val coordinator =
            HandsFreeAuthenticationCoordinator()

        assertFailsWith<IllegalArgumentException> {
            coordinator.requestAuthentication(
                state =
                    HandsFreeConversationState.IDLE,
            )
        }
    }

    @Test
    fun `custom authentication boundary result is preserved without reinterpretation`() {
        var calls = 0

        val coordinator =
            HandsFreeAuthenticationCoordinator(
                authenticationHandoff =
                    HandsFreeAuthenticationHandoff {
                        calls += 1

                        HandsFreeAuthenticationHandoffResult(
                            status =
                                HandsFreeAuthenticationHandoffStatus.REQUIRED,
                            message =
                                "External authentication required.",
                        )
                    },
            )

        val result =
            coordinator.requestAuthentication(
                state =
                    HandsFreeConversationState
                        .AUTHENTICATION_REQUESTED,
            )

        assertEquals(1, calls)

        assertEquals(
            HandsFreeAuthenticationHandoffStatus.REQUIRED,
            result.status,
        )

        assertEquals(
            "External authentication required.",
            result.message,
        )
    }
}
