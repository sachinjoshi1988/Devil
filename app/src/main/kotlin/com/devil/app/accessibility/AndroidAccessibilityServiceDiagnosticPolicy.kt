package com.devil.app.accessibility

/**
 * Pure classification policy for established accessibility-service evidence.
 *
 * The policy performs no Android inspection itself.
 */
internal object AndroidAccessibilityServiceDiagnosticPolicy {

    fun classify(
        serviceConnected: Boolean,
        enabledInAndroid: Boolean,
    ): AndroidAccessibilityServiceDiagnostic {
        if (serviceConnected) {
            return AndroidAccessibilityServiceDiagnostic(
                status =
                    AndroidAccessibilityServiceDiagnosticStatus.CONNECTED,
                message =
                    "Devil accessibility service is connected.",
            )
        }

        if (enabledInAndroid) {
            return AndroidAccessibilityServiceDiagnostic(
                status =
                    AndroidAccessibilityServiceDiagnosticStatus.ENABLED_BUT_DISCONNECTED,
                message =
                    "Accessibility is enabled for Devil in Android, but Devil's accessibility service is not currently connected. Open Android Accessibility settings and reconnect Devil's accessibility service.",
            )
        }

        return AndroidAccessibilityServiceDiagnostic(
            status =
                AndroidAccessibilityServiceDiagnosticStatus.DISABLED,
            message =
                "Devil's accessibility service is not enabled in Android Accessibility settings. Open Accessibility settings and enable Devil.",
        )
    }

    fun unknown(): AndroidAccessibilityServiceDiagnostic {
        return AndroidAccessibilityServiceDiagnostic(
            status =
                AndroidAccessibilityServiceDiagnosticStatus.UNKNOWN,
            message =
                "Devil could not determine the Android accessibility service state. Open Accessibility settings and verify that Devil is enabled.",
        )
    }
}
