package com.devil.app.internet

import java.net.URI

/**
 * Immutable Stage 42 safety assessment for one Internet knowledge retrieval.
 *
 * requestedUri preserves the destination explicitly supplied by the bounded
 * Internet request.
 *
 * retrievedDocument preserves the external document only when retrieval
 * genuinely produced one.
 *
 * The disposition describes whether that document may approach later bounded
 * analysis.
 *
 * This object does not authenticate, trust, interpret, authorize, persist,
 * execute, or verify external content.
 *
 * ELIGIBLE_FOR_LATER_ANALYSIS
 * != trusted
 * != true
 * != Devil instruction
 * != owner instruction
 * != authorization
 * != memory
 * != execution.
 */
@ConsistentCopyVisibility
data class AndroidInternetKnowledgeSafetyResult private constructor(
    val requestedUri: URI,
    val retrievalStatus: AndroidInternetKnowledgeStatus,
    val disposition: AndroidInternetKnowledgeContentDisposition,
    val retrievedDocument: AndroidInternetKnowledgeDocument?,
) {
    companion object {

        fun create(
            requestedUri: URI,
            retrievalStatus: AndroidInternetKnowledgeStatus,
            disposition: AndroidInternetKnowledgeContentDisposition,
            retrievedDocument: AndroidInternetKnowledgeDocument? = null,
        ): AndroidInternetKnowledgeSafetyResult {
            require(
                requestedUri.scheme.equals(
                    "https",
                    ignoreCase = true,
                ),
            ) {
                "Internet knowledge safety result requires HTTPS request provenance."
            }

            require(!requestedUri.host.isNullOrBlank()) {
                "Internet knowledge safety result requires an explicit request host."
            }

            when (retrievalStatus) {
                AndroidInternetKnowledgeStatus.AVAILABLE -> {
                    require(retrievedDocument != null) {
                        "AVAILABLE Internet knowledge safety result requires a retrieved document."
                    }
                }

                AndroidInternetKnowledgeStatus.UNAVAILABLE,
                AndroidInternetKnowledgeStatus.FAILED,
                -> {
                    require(retrievedDocument == null) {
                        "Non-available Internet knowledge safety result must not preserve a document."
                    }

                    require(
                        disposition ==
                            AndroidInternetKnowledgeContentDisposition.RETRIEVAL_ONLY,
                    ) {
                        "Non-available Internet knowledge must remain retrieval-only."
                    }
                }
            }

            if (
                disposition ==
                AndroidInternetKnowledgeContentDisposition.ELIGIBLE_FOR_LATER_ANALYSIS
            ) {
                require(
                    retrievalStatus ==
                        AndroidInternetKnowledgeStatus.AVAILABLE,
                ) {
                    "Only AVAILABLE Internet knowledge may become eligible for later analysis."
                }

                require(retrievedDocument != null) {
                    "Eligible Internet knowledge requires a retrieved document."
                }
            }

            return AndroidInternetKnowledgeSafetyResult(
                requestedUri = requestedUri.normalize(),
                retrievalStatus = retrievalStatus,
                disposition = disposition,
                retrievedDocument = retrievedDocument,
            )
        }
    }
}
