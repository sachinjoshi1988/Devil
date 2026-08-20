package com.devil.core.runtime.legal

import com.devil.core.model.common.TraceId
import com.devil.core.model.legal.LegalIntelligenceFoundationRecord

/**
 * Stable Stage 159 result of bounded Legal Intelligence Foundation preparation.
 *
 * PREPARED requires exactly one LegalIntelligenceFoundationRecord.
 * DEFERRED must contain none.
 *
 * This result establishes no jurisdiction, verified current law, legal advice,
 * legal authority, rights determination, liability determination, legal
 * conclusion, constitutional Verification, execution, legal research, or
 * Memory persistence.
 */
@ConsistentCopyVisibility
data class LegalIntelligenceFoundationPreparationResult private constructor(
    val traceId: TraceId,
    val status: LegalIntelligenceFoundationPreparationStatus,
    val legalFoundation: LegalIntelligenceFoundationRecord?,
) {
    companion object {

        fun create(
            traceId: TraceId,
            status: LegalIntelligenceFoundationPreparationStatus,
            legalFoundation: LegalIntelligenceFoundationRecord? = null,
        ): LegalIntelligenceFoundationPreparationResult {
            when (status) {
                LegalIntelligenceFoundationPreparationStatus.PREPARED -> {
                    require(legalFoundation != null) {
                        "Prepared Legal Intelligence Foundation results require one legal context."
                    }
                }

                LegalIntelligenceFoundationPreparationStatus.DEFERRED -> {
                    require(legalFoundation == null) {
                        "Deferred Legal Intelligence Foundation results must not contain a legal context."
                    }
                }
            }

            return LegalIntelligenceFoundationPreparationResult(
                traceId = traceId,
                status = status,
                legalFoundation = legalFoundation,
            )
        }
    }
}
