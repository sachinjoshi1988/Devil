package com.devil.app.application

/**
 * Stage 177 bounded Application Intelligence coordinator.
 *
 * The coordinator delegates one explicitly supplied package-name inspection to
 * the Android platform source and preserves that result unchanged.
 *
 * It does not:
 *
 * - enumerate applications;
 * - inspect application-private data;
 * - infer user behavior;
 * - grant Android permission;
 * - grant Devil authorization;
 * - create an ExecutionRequest;
 * - launch or interact with applications;
 * - observe or verify effects;
 * - establish Outcome;
 * - or implement Stage 178 Android Accessibility Foundation V2.
 *
 * APPLICATION_INTELLIGENCE != APPLICATION_EXECUTION.
 */
class AndroidApplicationIntelligenceCoordinator(
    private val source: AndroidApplicationIntelligenceSource,
) {

    fun inspect(
        packageName: String,
    ): AndroidApplicationInspectionResult {
        val normalizedPackageName = packageName.trim()

        require(normalizedPackageName.isNotEmpty()) {
            "Android application package name must not be blank."
        }

        return source.inspect(
            packageName = normalizedPackageName,
        )
    }
}
