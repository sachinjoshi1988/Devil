package com.devil.app.browser

/**
 * Stage 192 bounded Browser & Web Interaction coordinator.
 *
 * It accepts only an explicitly supplied browser interaction request.
 *
 * It does not:
 *
 * - create or use WebView;
 * - use Custom Tabs;
 * - create or launch ACTION_VIEW;
 * - launch an Activity;
 * - load or retrieve a web page;
 * - establish URI trust, reachability, or safety;
 * - automate browser interaction;
 * - grant Devil authorization;
 * - establish execution, Observation, Verification, or Outcome;
 * - implement Stage 193 Android Background Operation.
 *
 * BROWSER_REQUEST_READY != PAGE_OPENED.
 * URI_SUPPLIED != URI_TRUSTED.
 * PAGE_OPENED != VERIFIED_OUTCOME.
 */
class AndroidBrowserInteractionCoordinator {
    fun prepare(
        request: AndroidBrowserInteractionRequest?,
    ): AndroidBrowserInteractionResult {
        if (request == null) {
            return AndroidBrowserInteractionResult.create(
                status = AndroidBrowserInteractionStatus.DEFERRED,
            )
        }

        return AndroidBrowserInteractionResult.create(
            status = AndroidBrowserInteractionStatus.READY,
            request = request,
        )
    }
}
