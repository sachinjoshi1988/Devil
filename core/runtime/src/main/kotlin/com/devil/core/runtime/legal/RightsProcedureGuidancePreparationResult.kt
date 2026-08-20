package com.devil.core.runtime.legal

import com.devil.core.model.common.TraceId
import com.devil.core.model.legal.RightsProcedureGuidanceRecord

/**
 * Stable Stage 163 result of bounded Rights & Procedure Guidance preparation.
 *
 * PREPARED requires exactly one RightsProcedureGuidanceRecord.
 * DEFERRED must contain none.
 *
 * This result establishes no verified current law, jurisdiction, legal advice,
 * rights determination, obligation determination, liability determination,
 * authoritative procedure, verified deadline, filing requirement, court
 * access, filing, execution, constitutional Verification, evidence or citation
 * verification, Stage 164 behavior, or Memory persistence.
 */
@ConsistentCopyVisibility
data class RightsProcedureGuidancePreparationResult private constructor(
    val traceId: TraceId,
    val status: RightsProcedureGuidancePreparationStatus,
    val guidance: RightsProcedureGuidanceRecord?,
) {
    companion object {

        fun create(
            traceId: TraceId,
            status: RightsProcedureGuidancePreparationStatus,
            guidance: RightsProcedureGuidanceRecord? = null,
        ): RightsProcedureGuidancePreparationResult {
            when (status) {
                RightsProcedureGuidancePreparationStatus.PREPARED -> {
                    require(guidance != null) {
                        "Prepared Rights & Procedure Guidance results require one guidance context."
                    }
                }

                RightsProcedureGuidancePreparationStatus.DEFERRED -> {
                    require(guidance == null) {
                        "Deferred Rights & Procedure Guidance results must not contain a guidance context."
                    }
                }
            }

            return RightsProcedureGuidancePreparationResult(
                traceId = traceId,
                status = status,
                guidance = guidance,
            )
        }
    }
}
