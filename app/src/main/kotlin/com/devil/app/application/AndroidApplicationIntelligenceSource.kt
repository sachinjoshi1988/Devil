package com.devil.app.application

/**
 * Android platform source for bounded Stage 177 application intelligence.
 *
 * The caller supplies exactly one package name to inspect.
 *
 * Implementations must not enumerate applications, launch applications, grant
 * permission or authorization, or execute Android behavior.
 */
fun interface AndroidApplicationIntelligenceSource {

    fun inspect(
        packageName: String,
    ): AndroidApplicationInspectionResult
}
