package com.devil.core.runtime.legal

import com.devil.core.model.common.TraceId
import com.devil.core.model.legal.LegalResearchRecord

/**
 * Stable Stage 160 result of bounded Legal Research preparation.
 *
 * PREPARED requires exactly one LegalResearchRecord.
 * DEFERRED must contain none.
 *
 * This result establishes no verified current law, jurisdiction, legal advice,
 * legal authority, authoritative citation, precedent, rights determination,
 * liability determination, court access, constitutional Verification,
 * execution, legal-document understanding, or Memory persistence.
 */
@ConsistentCopyVisibility
data class LegalResearchPreparationResult private constructor(
    val traceId: TraceId,
    val status: LegalResearchPreparationStatus,
    val legalResearch: LegalResearchRecord?,
) {
    companion object {

        fun create(
            traceId: TraceId,
            status: LegalResearchPreparationStatus,
            legalResearch: LegalResearchRecord? = null,
        ): LegalResearchPreparationResult {
            when (status) {
                LegalResearchPreparationStatus.PREPARED -> {
                    require(legalResearch != null) {
                        "Prepared Legal Research results require one research context."
                    }
                }

                LegalResearchPreparationStatus.DEFERRED -> {
                    require(legalResearch == null) {
                        "Deferred Legal Research results must not contain a research context."
                    }
                }
            }

            return LegalResearchPreparationResult(
                traceId = traceId,
                status = status,
                legalResearch = legalResearch,
            )
        }
    }
}
