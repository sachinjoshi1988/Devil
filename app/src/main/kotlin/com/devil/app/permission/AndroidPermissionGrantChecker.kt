package com.devil.app.permission

/**
 * Reads whether Android currently grants one explicit runtime permission.
 *
 * This boundary performs platform-state inspection only.
 *
 * It must not request permission, mutate permission state, grant Devil
 * authorization, establish capability availability or health, establish
 * Executive readiness, or execute capabilities.
 */
fun interface AndroidPermissionGrantChecker {

    fun isGranted(
        permission: String,
    ): Boolean
}
