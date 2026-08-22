package com.devil.app.voice

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class Stage200NaturalTurnTakingTest {

    private val coordinator =
        AndroidNaturalTurnTakingCoordinator()

    @Test
    fun `completed output may request next listening turn`() {
        val result =
            coordinator.evaluate(
                isListening = false,
                isSpeaking = false,
                resumeListeningAfterOutput = true,
            )

        assertEquals(
            AndroidNaturalTurnTakingStatus.LISTEN,
            result.status,
        )
        assertTrue(result.shouldListen)
    }

    @Test
    fun `speaking with continuation waits rather than listening simultaneously`() {
        val result =
            coordinator.evaluate(
                isListening = false,
                isSpeaking = true,
                resumeListeningAfterOutput = true,
            )

        assertEquals(
            AndroidNaturalTurnTakingStatus.WAITING_TO_LISTEN,
            result.status,
        )
        assertFalse(result.shouldListen)
    }

    @Test
    fun `speaking without continuation remains speaking`() {
        val result =
            coordinator.evaluate(
                isListening = false,
                isSpeaking = true,
                resumeListeningAfterOutput = false,
            )

        assertEquals(
            AndroidNaturalTurnTakingStatus.SPEAKING,
            result.status,
        )
        assertFalse(result.shouldListen)
    }

    @Test
    fun `already listening does not request duplicate listening`() {
        val result =
            coordinator.evaluate(
                isListening = true,
                isSpeaking = false,
                resumeListeningAfterOutput = true,
            )

        assertEquals(
            AndroidNaturalTurnTakingStatus.DEFERRED,
            result.status,
        )
        assertFalse(result.shouldListen)
    }

    @Test
    fun `idle turn without continuation remains deferred`() {
        val result =
            coordinator.evaluate(
                isListening = false,
                isSpeaking = false,
                resumeListeningAfterOutput = false,
            )

        assertEquals(
            AndroidNaturalTurnTakingStatus.DEFERRED,
            result.status,
        )
        assertFalse(result.shouldListen)
    }

    @Test
    fun `simultaneous listening and speaking is rejected`() {
        assertFailsWith<IllegalArgumentException> {
            coordinator.evaluate(
                isListening = true,
                isSpeaking = true,
                resumeListeningAfterOutput = true,
            )
        }
    }

    @Test
    fun `listen result requires listening request`() {
        assertFailsWith<IllegalArgumentException> {
            AndroidNaturalTurnTakingResult.create(
                status = AndroidNaturalTurnTakingStatus.LISTEN,
                shouldListen = false,
            )
        }
    }

    @Test
    fun `non listening result rejects listening request`() {
        assertFailsWith<IllegalArgumentException> {
            AndroidNaturalTurnTakingResult.create(
                status = AndroidNaturalTurnTakingStatus.DEFERRED,
                shouldListen = true,
            )
        }
    }
}
