package com.devil.core.runtime.legal

import com.devil.core.model.common.TraceId
import com.devil.core.model.legal.HighStakesLegalSafetyRecord

/**
 * Stable Stage 165 result of bounded High-Stakes Legal Safety preparation.
 *
 * PREPARED requires exactly one HighStakesLegalSafetyRecord.
 * DEFERRED must contain none.
 *
 * This result establishes no verified legal risk, emergency determination,
 * legal advice, jurisdiction, authoritative procedure, evidence or citation
 * verification, escalation, execution authorization, constitutional
 * Verification, Stage 166 behavior, or Memory persistence.
 */
@ConsistentCopyVisibility
data class HighStakesLegalSafetyPreparationResult private constructor(
    val traceId: TraceId,
    val status: HighStakesLegalSafetyPreparationStatus,
    val safety: HighStakesLegalSafetyRecord?,
) {
    companion object {

        fun create(
            traceId: TraceId,
            status: HighStakesLegalSafetyPreparationStatus,
            safety: HighStakesLegalSafetyRecord? = null,
        ): HighStakesLegalSafetyPreparationResult {
            when (status) {
                HighStakesLegalSafetyPreparationStatus.PREPARED -> {
                    require(safety != null) {
                        "Prepared High-Stakes Legal Safety results require one safety context."
                    }
                }

                HighStakesLegalSafetyPreparationStatus.DEFERRED -> {
                    require(safety == null) {
                        "Deferred High-Stakes Legal Safety results must not contain a safety context."
                    }
                }
            }

            return HighStakesLegalSafetyPreparationResult(
                traceId = traceId,
                status = status,
                safety = safety,
            )
        }
    }
}
