package com.devil.app.permission

import android.content.Context
import android.content.pm.PackageManager

/**
 * Default Stage 29 Android runtime-permission grant checker.
 *
 * This implementation uses Context.checkSelfPermission only to inspect current
 * Android operating-system permission state.
 *
 * It never requests or grants Android permission and never converts Android
 * permission state into Devil constitutional authorization.
 */
class DefaultAndroidPermissionGrantChecker(
    private val context: Context,
) : AndroidPermissionGrantChecker {

    override fun isGranted(
        permission: String,
    ): Boolean {
        val normalizedPermission = permission.trim()

        require(normalizedPermission.isNotEmpty()) {
            "Android permission name must not be blank."
        }

        return context.checkSelfPermission(normalizedPermission) ==
            PackageManager.PERMISSION_GRANTED
    }
}
