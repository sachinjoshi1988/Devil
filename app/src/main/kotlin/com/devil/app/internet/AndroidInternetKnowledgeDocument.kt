package com.devil.app.internet

import java.net.URI

/**
 * Immutable Stage 42 representation of bounded external Internet content.
 *
 * sourceUri preserves retrieval provenance.
 *
 * mediaType preserves optional externally supplied representation metadata.
 *
 * content contains bounded external text only.
 *
 * None of these fields establish:
 *
 * - source authenticity beyond transport destination;
 * - factual truth;
 * - sender identity;
 * - subject trust;
 * - constitutional authority;
 * - command semantics;
 * - authorization;
 * - memory eligibility;
 * - execution approval;
 * - or verified Outcome.
 *
 * External content must remain data.
 */
@ConsistentCopyVisibility
data class AndroidInternetKnowledgeDocument private constructor(
    val sourceUri: URI,
    val mediaType: String?,
    val content: String,
) {

    companion object {

        fun create(
            sourceUri: URI,
            mediaType: String? = null,
            content: String,
        ): AndroidInternetKnowledgeDocument {
            require(
                sourceUri.scheme.equals(
                    "https",
                    ignoreCase = true,
                ),
            ) {
                "Internet knowledge document provenance must use HTTPS."
            }

            require(!sourceUri.host.isNullOrBlank()) {
                "Internet knowledge document provenance requires a host."
            }

            val normalizedContent =
                content.trim()

            require(normalizedContent.isNotEmpty()) {
                "Internet knowledge document content must not be blank."
            }

            val normalizedMediaType =
                mediaType
                    ?.trim()
                    ?.takeIf {
                        it.isNotEmpty()
                    }

            return AndroidInternetKnowledgeDocument(
                sourceUri = sourceUri.normalize(),
                mediaType = normalizedMediaType,
                content = normalizedContent,
            )
        }
    }
}
