package com.devil.app.browser

/**
 * Stage 192 bounded Browser & Web Interaction result.
 *
 * READY contains exactly one explicitly supplied browser request.
 * DEFERRED contains no request.
 *
 * BROWSER_REQUEST_READY != PAGE_OPENED.
 * PAGE_OPENED != VERIFIED_OUTCOME.
 */
@ConsistentCopyVisibility
data class AndroidBrowserInteractionResult private constructor(
    val status: AndroidBrowserInteractionStatus,
    val request: AndroidBrowserInteractionRequest?,
) {
    companion object {
        fun create(
            status: AndroidBrowserInteractionStatus,
            request: AndroidBrowserInteractionRequest? = null,
        ): AndroidBrowserInteractionResult {
            when (status) {
                AndroidBrowserInteractionStatus.READY ->
                    require(request != null) {
                        "Ready Android browser interaction requires one request."
                    }

                AndroidBrowserInteractionStatus.DEFERRED ->
                    require(request == null) {
                        "Deferred Android browser interaction must not contain a request."
                    }
            }

            return AndroidBrowserInteractionResult(
                status = status,
                request = request,
            )
        }
    }
}
