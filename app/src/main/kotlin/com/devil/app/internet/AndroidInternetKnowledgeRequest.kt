package com.devil.app.internet

import java.net.URI

/**
 * One explicit Stage 42 request for bounded Internet knowledge retrieval.
 *
 * The request accepts HTTPS destinations only.
 *
 * Creating this request:
 *
 * != proving the source is trustworthy
 * != granting Devil authorization
 * != approving execution
 * != permitting arbitrary browsing
 * != permitting file download
 * != permitting external account access
 * != permitting memory persistence.
 *
 * Stage 42 treats the destination as external and untrusted.
 */
@ConsistentCopyVisibility
data class AndroidInternetKnowledgeRequest private constructor(
    val uri: URI,
) {

    companion object {

        fun create(
            rawUrl: String,
        ): AndroidInternetKnowledgeRequest {
            val normalizedUrl =
                rawUrl.trim()

            require(normalizedUrl.isNotEmpty()) {
                "Internet knowledge URL must not be blank."
            }

            val parsedUri =
                URI(normalizedUrl)

            require(
                parsedUri.scheme.equals(
                    "https",
                    ignoreCase = true,
                ),
            ) {
                "Stage 42 Internet knowledge requests require HTTPS."
            }

            require(!parsedUri.host.isNullOrBlank()) {
                "Stage 42 Internet knowledge requests require an explicit host."
            }

            require(parsedUri.userInfo == null) {
                "Stage 42 Internet knowledge requests must not contain user-info credentials."
            }

            require(parsedUri.fragment == null) {
                "Stage 42 Internet knowledge requests must not contain URL fragments."
            }

            return AndroidInternetKnowledgeRequest(
                uri = parsedUri.normalize(),
            )
        }
    }
}
