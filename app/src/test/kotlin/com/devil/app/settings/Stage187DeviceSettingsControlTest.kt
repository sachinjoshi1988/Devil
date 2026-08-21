package com.devil.app.settings

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class Stage187DeviceSettingsControlTest {

    @Test
    fun `explicit settings command becomes ready unchanged`() {
        val result =
            AndroidDeviceSettingsControlCoordinator()
                .prepare(AndroidDeviceSettingsCommand.OPEN_WIFI_SETTINGS)

        assertEquals(AndroidDeviceSettingsControlStatus.READY, result.status)
        assertEquals(AndroidDeviceSettingsCommand.OPEN_WIFI_SETTINGS, result.command)
    }

    @Test
    fun `absent settings command remains deferred`() {
        val result =
            AndroidDeviceSettingsControlCoordinator()
                .prepare(null)

        assertEquals(AndroidDeviceSettingsControlStatus.DEFERRED, result.status)
        assertNull(result.command)
    }

    @Test
    fun `all bounded settings commands can be prepared`() {
        AndroidDeviceSettingsCommand.entries.forEach { command ->
            val result =
                AndroidDeviceSettingsControlCoordinator()
                    .prepare(command)

            assertEquals(AndroidDeviceSettingsControlStatus.READY, result.status)
            assertEquals(command, result.command)
        }
    }

    @Test
    fun `ready result requires command`() {
        assertFailsWith<IllegalArgumentException> {
            AndroidDeviceSettingsControlResult.create(
                status = AndroidDeviceSettingsControlStatus.READY,
            )
        }
    }

    @Test
    fun `deferred result rejects command`() {
        assertFailsWith<IllegalArgumentException> {
            AndroidDeviceSettingsControlResult.create(
                status = AndroidDeviceSettingsControlStatus.DEFERRED,
                command = AndroidDeviceSettingsCommand.OPEN_SETTINGS,
            )
        }
    }
}
