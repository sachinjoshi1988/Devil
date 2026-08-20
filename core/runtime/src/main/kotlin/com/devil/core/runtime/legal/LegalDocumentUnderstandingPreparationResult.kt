package com.devil.core.runtime.legal

import com.devil.core.model.common.TraceId
import com.devil.core.model.legal.LegalDocumentUnderstandingRecord

/**
 * Stable Stage 161 result of bounded Legal Document Understanding preparation.
 *
 * PREPARED requires exactly one LegalDocumentUnderstandingRecord.
 * DEFERRED must contain none.
 *
 * This result establishes no OCR, document extraction, document authenticity,
 * signature verification, verified document content, legal-effect
 * determination, legal advice, constitutional Verification, execution, or
 * Memory persistence.
 */
@ConsistentCopyVisibility
data class LegalDocumentUnderstandingPreparationResult private constructor(
    val traceId: TraceId,
    val status: LegalDocumentUnderstandingPreparationStatus,
    val documentUnderstanding: LegalDocumentUnderstandingRecord?,
) {
    companion object {

        fun create(
            traceId: TraceId,
            status: LegalDocumentUnderstandingPreparationStatus,
            documentUnderstanding: LegalDocumentUnderstandingRecord? = null,
        ): LegalDocumentUnderstandingPreparationResult {
            when (status) {
                LegalDocumentUnderstandingPreparationStatus.PREPARED -> {
                    require(documentUnderstanding != null) {
                        "Prepared Legal Document Understanding results require one document context."
                    }
                }

                LegalDocumentUnderstandingPreparationStatus.DEFERRED -> {
                    require(documentUnderstanding == null) {
                        "Deferred Legal Document Understanding results must not contain a document context."
                    }
                }
            }

            return LegalDocumentUnderstandingPreparationResult(
                traceId = traceId,
                status = status,
                documentUnderstanding = documentUnderstanding,
            )
        }
    }
}
