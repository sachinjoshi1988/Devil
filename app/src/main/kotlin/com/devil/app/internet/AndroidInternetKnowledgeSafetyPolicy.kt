package com.devil.app.internet

import java.net.URI

/**
 * Stage 42 safety policy between Internet retrieval and any future descriptive
 * analysis.
 *
 * This policy performs only bounded structural provenance evaluation.
 *
 * It never parses external prose to determine whether statements are truthful,
 * whether an apparent instruction should be obeyed, whether a sender is who they
 * claim to be, or whether content deserves trust.
 *
 * A document may become ELIGIBLE_FOR_LATER_ANALYSIS only when:
 *
 * - retrieval genuinely returned AVAILABLE;
 * - one document exists;
 * - the document provenance remains HTTPS;
 * - and the document preserves the explicitly requested Internet origin.
 *
 * Same-origin here is intentionally conservative. Stage 42 does not silently
 * bless redirected or substituted origins.
 *
 * Even eligible content remains external untrusted data.
 *
 * Internet access
 * != source authenticity
 * != factual truth
 * != trusted instruction
 * != user intent
 * != authentication
 * != authorization
 * != memory
 * != execution.
 */
class AndroidInternetKnowledgeSafetyPolicy {

    fun evaluate(
        request: AndroidInternetKnowledgeRequest,
        retrievalResult: AndroidInternetKnowledgeResult,
    ): AndroidInternetKnowledgeSafetyResult {
        if (
            retrievalResult.status !=
            AndroidInternetKnowledgeStatus.AVAILABLE
        ) {
            return AndroidInternetKnowledgeSafetyResult.create(
                requestedUri = request.uri,
                retrievalStatus = retrievalResult.status,
                disposition =
                    AndroidInternetKnowledgeContentDisposition.RETRIEVAL_ONLY,
            )
        }

        val document =
            retrievalResult.document
                ?: return AndroidInternetKnowledgeSafetyResult.create(
                    requestedUri = request.uri,
                    retrievalStatus =
                        AndroidInternetKnowledgeStatus.UNAVAILABLE,
                    disposition =
                        AndroidInternetKnowledgeContentDisposition.RETRIEVAL_ONLY,
                )

        val disposition =
            if (
                document.sourceUri.scheme.equals(
                    "https",
                    ignoreCase = true,
                ) &&
                sameOrigin(
                    requestedUri = request.uri,
                    documentUri = document.sourceUri,
                )
            ) {
                AndroidInternetKnowledgeContentDisposition
                    .ELIGIBLE_FOR_LATER_ANALYSIS
            } else {
                AndroidInternetKnowledgeContentDisposition.RETRIEVAL_ONLY
            }

        return AndroidInternetKnowledgeSafetyResult.create(
            requestedUri = request.uri,
            retrievalStatus = retrievalResult.status,
            disposition = disposition,
            retrievedDocument = document,
        )
    }

    private fun sameOrigin(
        requestedUri: URI,
        documentUri: URI,
    ): Boolean {
        return requestedUri.scheme.equals(
            documentUri.scheme,
            ignoreCase = true,
        ) &&
            requestedUri.host.equals(
                documentUri.host,
                ignoreCase = true,
            ) &&
            effectivePort(requestedUri) ==
            effectivePort(documentUri)
    }

    private fun effectivePort(
        uri: URI,
    ): Int {
        if (uri.port >= 0) {
            return uri.port
        }

        return if (
            uri.scheme.equals(
                "https",
                ignoreCase = true,
            )
        ) {
            HTTPS_DEFAULT_PORT
        } else {
            UNKNOWN_PORT
        }
    }

    private companion object {
        const val HTTPS_DEFAULT_PORT: Int = 443
        const val UNKNOWN_PORT: Int = -1
    }
}
