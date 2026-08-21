package com.devil.app.application

/**
 * Stage 177 bounded Android application descriptor.
 *
 * This record preserves directly observed Android package/application metadata
 * for one explicitly supplied package name.
 *
 * It does not:
 *
 * - enumerate installed applications;
 * - inspect application data or private storage;
 * - infer application usage or user behavior;
 * - establish Android permission;
 * - grant Devil authorization;
 * - create an ExecutionRequest;
 * - launch or interact with an application;
 * - observe or verify an execution effect;
 * - establish Outcome;
 * - or implement Stage 178 Android Accessibility Foundation V2.
 *
 * APPLICATION_DESCRIPTOR != EXECUTION_TARGET_AUTHORIZATION.
 * INSTALLED_APPLICATION != AUTHORIZED_APPLICATION.
 * LAUNCHABLE_APPLICATION != EXECUTION_APPROVAL.
 */
@ConsistentCopyVisibility
data class AndroidApplicationDescriptor private constructor(
    val packageName: String,
    val applicationLabel: String,
    val launchable: Boolean,
) {
    companion object {
        fun create(
            packageName: String,
            applicationLabel: String,
            launchable: Boolean,
        ): AndroidApplicationDescriptor {
            val normalizedPackageName = packageName.trim()
            val normalizedApplicationLabel = applicationLabel.trim()

            require(normalizedPackageName.isNotEmpty()) {
                "Android application package name must not be blank."
            }

            require(normalizedApplicationLabel.isNotEmpty()) {
                "Android application label must not be blank."
            }

            return AndroidApplicationDescriptor(
                packageName = normalizedPackageName,
                applicationLabel = normalizedApplicationLabel,
                launchable = launchable,
            )
        }
    }
}
