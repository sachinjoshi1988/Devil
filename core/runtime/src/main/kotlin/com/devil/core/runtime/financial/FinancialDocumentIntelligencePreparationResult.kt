package com.devil.core.runtime.financial

import com.devil.core.model.common.TraceId
import com.devil.core.model.financial.FinancialDocumentIntelligenceRecord

/**
 * Stable Stage 157 result of bounded Financial Document Intelligence
 * preparation.
 *
 * PREPARED requires exactly one FinancialDocumentIntelligenceRecord.
 * DEFERRED must contain none.
 *
 * This result establishes no OCR, extraction, document authenticity, verified
 * financial fact, constitutional Observation or Verification, execution,
 * financial safety, or Memory persistence.
 */
@ConsistentCopyVisibility
data class FinancialDocumentIntelligencePreparationResult private constructor(
    val traceId: TraceId,
    val status: FinancialDocumentIntelligencePreparationStatus,
    val documentIntelligence: FinancialDocumentIntelligenceRecord?,
) {
    companion object {

        fun create(
            traceId: TraceId,
            status: FinancialDocumentIntelligencePreparationStatus,
            documentIntelligence: FinancialDocumentIntelligenceRecord? = null,
        ): FinancialDocumentIntelligencePreparationResult {
            when (status) {
                FinancialDocumentIntelligencePreparationStatus.PREPARED -> {
                    require(documentIntelligence != null) {
                        "Prepared Financial Document Intelligence results require one document context."
                    }
                }

                FinancialDocumentIntelligencePreparationStatus.DEFERRED -> {
                    require(documentIntelligence == null) {
                        "Deferred Financial Document Intelligence results must not contain a document context."
                    }
                }
            }

            return FinancialDocumentIntelligencePreparationResult(
                traceId = traceId,
                status = status,
                documentIntelligence = documentIntelligence,
            )
        }
    }
}
