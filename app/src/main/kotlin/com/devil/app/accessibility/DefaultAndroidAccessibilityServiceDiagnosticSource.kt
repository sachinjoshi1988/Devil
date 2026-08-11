package com.devil.app.accessibility

import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Context
import android.view.accessibility.AccessibilityManager

/**
 * Android platform source for one bounded AccessibilityService diagnosis.
 *
 * The source combines:
 *
 * - the process-local evidence that DevilAccessibilityService is genuinely
 *   connected; and
 * - Android's enabled accessibility-service inventory.
 *
 * This distinction allows Devil to tell the difference between:
 *
 * - service connected;
 * - service enabled in Android but currently disconnected;
 * - service disabled;
 * - and state that cannot safely be established.
 *
 * Reading Android enabled-service state grants no Devil authority.
 *
 * This source does not:
 *
 * - enable or disable accessibility;
 * - modify Android settings;
 * - restart or rebind the service;
 * - perform accessibility actions;
 * - authorize execution;
 * - establish observation;
 * - establish verification;
 * - or establish Outcome.
 */
class DefaultAndroidAccessibilityServiceDiagnosticSource(
    context: Context,
    private val serviceConnectedProvider: () -> Boolean = {
        DevilAccessibilityServiceRegistry.current() != null
    },
) {

    private val applicationContext =
        context.applicationContext

    private val accessibilityManager =
        applicationContext.getSystemService(
            AccessibilityManager::class.java,
        )

    fun diagnose(): AndroidAccessibilityServiceDiagnostic {
        if (serviceConnectedProvider()) {
            return AndroidAccessibilityServiceDiagnosticPolicy.classify(
                serviceConnected = true,
                enabledInAndroid = true,
            )
        }

        val manager =
            accessibilityManager
                ?: return AndroidAccessibilityServiceDiagnosticPolicy
                    .unknown()

        return runCatching {
            val enabledInAndroid =
                manager
                    .getEnabledAccessibilityServiceList(
                        AccessibilityServiceInfo.FEEDBACK_ALL_MASK,
                    )
                    .any { serviceInfo ->
                        val androidServiceInfo =
                            serviceInfo.resolveInfo?.serviceInfo

                        androidServiceInfo != null &&
                            androidServiceInfo.packageName ==
                            applicationContext.packageName &&
                            normalizedServiceClassName(
                                packageName =
                                    androidServiceInfo.packageName,
                                className =
                                    androidServiceInfo.name,
                            ) ==
                            DevilAccessibilityService::class.java.name
                    }

            AndroidAccessibilityServiceDiagnosticPolicy.classify(
                serviceConnected = false,
                enabledInAndroid = enabledInAndroid,
            )
        }.getOrElse {
            AndroidAccessibilityServiceDiagnosticPolicy.unknown()
        }
    }

    private fun normalizedServiceClassName(
        packageName: String,
        className: String,
    ): String {
        return when {
            className.startsWith(".") ->
                packageName + className

            '.' !in className ->
                "$packageName.$className"

            else ->
                className
        }
    }
}
