package com.devil.app.vision

/**
 * Stage 208 bounded Document Vision coordinator.
 *
 * It associates one explicitly supplied bounded document description with one
 * already-understood Stage 206 image.
 *
 * It does not:
 *
 * - perform OCR;
 * - extract text, clauses, fields, signatures, dates, totals, or tables;
 * - parse document structure;
 * - establish document authenticity;
 * - verify signatures;
 * - establish that supplied document content matches actual pixels;
 * - open arbitrary files or PDFs;
 * - invoke external document or vision providers;
 * - create Memory;
 * - establish constitutional Observation, Verification, or Outcome;
 * - implement Stage 209 Screen Vision.
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
class AndroidDocumentVisionCoordinator {

    fun understand(
        imageUnderstanding: AndroidImageUnderstandingResult,
        documentDescription: String?,
    ): AndroidDocumentVisionResult {
        if (
            imageUnderstanding.status !=
                AndroidImageUnderstandingStatus.UNDERSTOOD ||
            documentDescription.isNullOrBlank()
        ) {
            return AndroidDocumentVisionResult.create(
                status = AndroidDocumentVisionStatus.DEFERRED,
                imageUnderstanding = imageUnderstanding,
            )
        }

        return AndroidDocumentVisionResult.create(
            status = AndroidDocumentVisionStatus.UNDERSTOOD,
            imageUnderstanding = imageUnderstanding,
            documentDescription = documentDescription,
        )
    }
}
