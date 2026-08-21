package com.devil.app.application

import android.content.Context
import android.content.pm.PackageManager

/**
 * Default Stage 177 Android application-intelligence platform source.
 *
 * This source performs bounded read-only inspection of one explicitly supplied
 * Android package name.
 *
 * It does not enumerate installed applications or launch an application.
 */
class DefaultAndroidApplicationIntelligenceSource(
    context: Context,
) : AndroidApplicationIntelligenceSource {

    private val applicationContext =
        context.applicationContext

    override fun inspect(
        packageName: String,
    ): AndroidApplicationInspectionResult {
        val normalizedPackageName = packageName.trim()

        require(normalizedPackageName.isNotEmpty()) {
            "Android application package name must not be blank."
        }

        val packageManager =
            applicationContext.packageManager

        val applicationInfo =
            try {
                packageManager.getApplicationInfo(
                    normalizedPackageName,
                    0,
                )
            } catch (_: PackageManager.NameNotFoundException) {
                return AndroidApplicationInspectionResult.create(
                    status = AndroidApplicationInspectionStatus.NOT_FOUND,
                )
            }

        val label =
            packageManager
                .getApplicationLabel(applicationInfo)
                .toString()
                .trim()

        require(label.isNotEmpty()) {
            "Android application label must not be blank."
        }

        val launchable =
            packageManager.getLaunchIntentForPackage(
                normalizedPackageName,
            ) != null

        return AndroidApplicationInspectionResult.create(
            status = AndroidApplicationInspectionStatus.FOUND,
            application =
                AndroidApplicationDescriptor.create(
                    packageName = normalizedPackageName,
                    applicationLabel = label,
                    launchable = launchable,
                ),
        )
    }
}
