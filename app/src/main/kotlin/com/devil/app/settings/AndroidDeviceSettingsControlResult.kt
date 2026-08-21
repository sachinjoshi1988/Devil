package com.devil.app.settings

@ConsistentCopyVisibility
data class AndroidDeviceSettingsControlResult private constructor(
    val status: AndroidDeviceSettingsControlStatus,
    val command: AndroidDeviceSettingsCommand?,
) {
    companion object {
        fun create(
            status: AndroidDeviceSettingsControlStatus,
            command: AndroidDeviceSettingsCommand? = null,
        ): AndroidDeviceSettingsControlResult {
            when (status) {
                AndroidDeviceSettingsControlStatus.READY ->
                    require(command != null) {
                        "Ready Android device-settings control requires one command."
                    }

                AndroidDeviceSettingsControlStatus.DEFERRED ->
                    require(command == null) {
                        "Deferred Android device-settings control must not contain a command."
                    }
            }

            return AndroidDeviceSettingsControlResult(
                status = status,
                command = command,
            )
        }
    }
}
