package com.devil.app.media

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class Stage186MediaControlTest {

    @Test
    fun `explicit media command becomes ready unchanged`() {
        val result =
            AndroidMediaControlCoordinator()
                .prepare(AndroidMediaControlCommand.PLAY)

        assertEquals(
            AndroidMediaControlStatus.READY,
            result.status,
        )
        assertEquals(
            AndroidMediaControlCommand.PLAY,
            result.command,
        )
    }

    @Test
    fun `absent media command remains deferred`() {
        val result =
            AndroidMediaControlCoordinator()
                .prepare(null)

        assertEquals(
            AndroidMediaControlStatus.DEFERRED,
            result.status,
        )
        assertNull(result.command)
    }

    @Test
    fun `all bounded media commands can be prepared`() {
        AndroidMediaControlCommand.entries.forEach { command ->
            val result =
                AndroidMediaControlCoordinator()
                    .prepare(command)

            assertEquals(
                AndroidMediaControlStatus.READY,
                result.status,
            )
            assertEquals(command, result.command)
        }
    }

    @Test
    fun `ready result requires command`() {
        assertFailsWith<IllegalArgumentException> {
            AndroidMediaControlResult.create(
                status = AndroidMediaControlStatus.READY,
            )
        }
    }

    @Test
    fun `deferred result rejects command`() {
        assertFailsWith<IllegalArgumentException> {
            AndroidMediaControlResult.create(
                status = AndroidMediaControlStatus.DEFERRED,
                command = AndroidMediaControlCommand.PAUSE,
            )
        }
    }
}
