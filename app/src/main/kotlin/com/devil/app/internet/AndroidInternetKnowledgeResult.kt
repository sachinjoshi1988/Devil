package com.devil.app.internet

/**
 * Stage 42 result from the bounded Internet knowledge source.
 *
 * AVAILABLE requires one genuine external document.
 *
 * UNAVAILABLE contains neither a document nor an error.
 *
 * FAILED contains one bounded error description and no document.
 *
 * This result describes retrieval only.
 *
 * Retrieval result
 * != understanding
 * != truth
 * != trust
 * != authorization
 * != memory
 * != execution
 * != verified Outcome.
 */
@ConsistentCopyVisibility
data class AndroidInternetKnowledgeResult private constructor(
    val status: AndroidInternetKnowledgeStatus,
    val document: AndroidInternetKnowledgeDocument?,
    val error: String?,
) {

    companion object {

        fun available(
            document: AndroidInternetKnowledgeDocument,
        ): AndroidInternetKnowledgeResult {
            return AndroidInternetKnowledgeResult(
                status = AndroidInternetKnowledgeStatus.AVAILABLE,
                document = document,
                error = null,
            )
        }

        fun unavailable(): AndroidInternetKnowledgeResult {
            return AndroidInternetKnowledgeResult(
                status = AndroidInternetKnowledgeStatus.UNAVAILABLE,
                document = null,
                error = null,
            )
        }

        fun failed(
            error: String,
        ): AndroidInternetKnowledgeResult {
            val normalizedError =
                error.trim()

            require(normalizedError.isNotEmpty()) {
                "Failed Internet knowledge result requires an error."
            }

            return AndroidInternetKnowledgeResult(
                status = AndroidInternetKnowledgeStatus.FAILED,
                document = null,
                error = normalizedError,
            )
        }
    }
}
