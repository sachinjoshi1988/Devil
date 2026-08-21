package com.devil.core.runtime.legal

import com.devil.core.model.common.TraceId
import com.devil.core.model.legal.LegalEvidenceCitationRecord

/**
 * Stable Stage 164 result of bounded Legal Evidence & Citation preparation.
 *
 * PREPARED requires exactly one LegalEvidenceCitationRecord.
 * DEFERRED must contain none.
 *
 * This result establishes no verified current law, jurisdiction, legal advice,
 * rights determination, obligation determination, liability determination,
 * authoritative legal procedure, evidence authenticity, admissibility, weight,
 * authoritative source status, authoritative citation, precedent, execution,
 * constitutional Verification, Stage 165 behavior, or Memory persistence.
 */
@ConsistentCopyVisibility
data class LegalEvidenceCitationPreparationResult private constructor(
    val traceId: TraceId,
    val status: LegalEvidenceCitationPreparationStatus,
    val evidenceCitation: LegalEvidenceCitationRecord?,
) {
    companion object {

        fun create(
            traceId: TraceId,
            status: LegalEvidenceCitationPreparationStatus,
            evidenceCitation: LegalEvidenceCitationRecord? = null,
        ): LegalEvidenceCitationPreparationResult {
            when (status) {
                LegalEvidenceCitationPreparationStatus.PREPARED -> {
                    require(evidenceCitation != null) {
                        "Prepared Legal Evidence & Citation results require one evidence/citation context."
                    }
                }

                LegalEvidenceCitationPreparationStatus.DEFERRED -> {
                    require(evidenceCitation == null) {
                        "Deferred Legal Evidence & Citation results must not contain an evidence/citation context."
                    }
                }
            }

            return LegalEvidenceCitationPreparationResult(
                traceId = traceId,
                status = status,
                evidenceCitation = evidenceCitation,
            )
        }
    }
}
