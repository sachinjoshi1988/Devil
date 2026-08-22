package com.devil.app.browser

/**
 * Stage 192 bounded explicitly supplied browser interaction request.
 *
 * The URI is supplied metadata only. It is not established as trusted,
 * reachable, safe, loaded, or verified.
 *
 * URI_SUPPLIED != URI_TRUSTED.
 */
@ConsistentCopyVisibility
data class AndroidBrowserInteractionRequest private constructor(
    val uri: String,
) {
    companion object {
        fun create(
            uri: String,
        ): AndroidBrowserInteractionRequest {
            val normalizedUri = uri.trim()

            require(normalizedUri.isNotEmpty()) {
                "Android browser interaction URI must not be blank."
            }

            return AndroidBrowserInteractionRequest(
                uri = normalizedUri,
            )
        }
    }
}
