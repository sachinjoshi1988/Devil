package com.devil.core.runtime.legal

import com.devil.core.model.common.TraceId
import com.devil.core.model.legal.LegalDraftingAssistanceRecord

/**
 * Stable Stage 162 result of bounded Legal Drafting Assistance preparation.
 *
 * PREPARED requires exactly one LegalDraftingAssistanceRecord.
 * DEFERRED must contain none.
 *
 * This result establishes no verified current law, legal advice, legal
 * authority, legal-effect determination, enforceability determination, rights
 * determination, liability determination, authoritative legal document,
 * filing, signature, execution, constitutional Verification, Stage 163 rights
 * or procedure guidance, or Memory persistence.
 */
@ConsistentCopyVisibility
data class LegalDraftingAssistancePreparationResult private constructor(
    val traceId: TraceId,
    val status: LegalDraftingAssistancePreparationStatus,
    val draftingAssistance: LegalDraftingAssistanceRecord?,
) {
    companion object {

        fun create(
            traceId: TraceId,
            status: LegalDraftingAssistancePreparationStatus,
            draftingAssistance: LegalDraftingAssistanceRecord? = null,
        ): LegalDraftingAssistancePreparationResult {
            when (status) {
                LegalDraftingAssistancePreparationStatus.PREPARED -> {
                    require(draftingAssistance != null) {
                        "Prepared Legal Drafting Assistance results require one drafting context."
                    }
                }

                LegalDraftingAssistancePreparationStatus.DEFERRED -> {
                    require(draftingAssistance == null) {
                        "Deferred Legal Drafting Assistance results must not contain a drafting context."
                    }
                }
            }

            return LegalDraftingAssistancePreparationResult(
                traceId = traceId,
                status = status,
                draftingAssistance = draftingAssistance,
            )
        }
    }
}
