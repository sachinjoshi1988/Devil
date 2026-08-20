package com.devil.core.runtime.legal

/**
 * Stage 161 bounded Legal Document Understanding preparation status.
 *
 * PREPARED means one structurally valid supplied legal-document context was
 * prepared from one existing Stage 159 Legal Intelligence Foundation context
 * and explicitly supplied document metadata.
 *
 * PREPARED does not mean:
 *
 * - a file was opened or inspected;
 * - OCR occurred;
 * - clauses or fields were extracted;
 * - document authenticity was established;
 * - a signature was verified;
 * - document content was verified;
 * - legal effect was determined;
 * - legal advice was produced;
 * - constitutional Verification occurred;
 * - or Stage 162 was implemented.
 *
 * DEFERRED means no truthful Legal Document Understanding context was produced.
 */
enum class LegalDocumentUnderstandingPreparationStatus {
    PREPARED,
    DEFERRED,
}
