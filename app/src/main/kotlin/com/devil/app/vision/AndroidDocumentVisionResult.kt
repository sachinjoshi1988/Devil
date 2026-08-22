package com.devil.app.vision

/**
 * Stage 208 bounded Document Vision result.
 *
 * UNDERSTOOD preserves the exact Stage 206 image-understanding result together
 * with one normalized explicitly supplied bounded document description.
 *
 * DEFERRED preserves the exact Stage 206 result and no document description.
 *
 * DOCUMENT_VISION != OCR.
 * DOCUMENT_VISION != DOCUMENT_EXTRACTION.
 * DOCUMENT_DESCRIPTION != OBSERVED_TEXT.
 * DOCUMENT_VISION != DOCUMENT_AUTHENTICITY.
 * DOCUMENT_VISION != SIGNATURE_VERIFICATION.
 * DOCUMENT_VISION != VERIFIED_DOCUMENT_CONTENT.
 * DOCUMENT_VISION != MEMORY.
 * DOCUMENT_VISION != CONSTITUTIONAL_VERIFICATION.
 */
@ConsistentCopyVisibility
data class AndroidDocumentVisionResult private constructor(
    val status: AndroidDocumentVisionStatus,
    val imageUnderstanding: AndroidImageUnderstandingResult,
    val documentDescription: String?,
) {
    companion object {
        fun create(
            status: AndroidDocumentVisionStatus,
            imageUnderstanding: AndroidImageUnderstandingResult,
            documentDescription: String? = null,
        ): AndroidDocumentVisionResult {
            return when (status) {
                AndroidDocumentVisionStatus.UNDERSTOOD -> {
                    require(
                        imageUnderstanding.status ==
                            AndroidImageUnderstandingStatus.UNDERSTOOD,
                    ) {
                        "Understood Stage 208 document vision requires understood Stage 206 image understanding."
                    }

                    val normalizedDescription =
                        requireNotNull(documentDescription)
                            .trim()

                    require(normalizedDescription.isNotEmpty()) {
                        "Stage 208 document description must not be blank."
                    }

                    AndroidDocumentVisionResult(
                        status = status,
                        imageUnderstanding = imageUnderstanding,
                        documentDescription = normalizedDescription,
                    )
                }

                AndroidDocumentVisionStatus.DEFERRED -> {
                    require(documentDescription == null) {
                        "Deferred Stage 208 document vision must not contain a document description."
                    }

                    AndroidDocumentVisionResult(
                        status = status,
                        imageUnderstanding = imageUnderstanding,
                        documentDescription = null,
                    )
                }
            }
        }
    }
}
