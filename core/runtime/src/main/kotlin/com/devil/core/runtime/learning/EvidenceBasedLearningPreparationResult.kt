package com.devil.core.runtime.learning

import com.devil.core.model.common.TraceId
import com.devil.core.model.learning.EvidenceBasedLearningRecord

/**
 * Stable Stage 92 result of bounded Evidence-Based Learning V2 preparation.
 *
 * PREPARED requires exactly one EvidenceBasedLearningRecord.
 *
 * DEFERRED must contain no record.
 *
 * This result creates no evidence, Learning Authority result, Memory Proposal,
 * Memory Authority approval, Memory commitment, strategy adaptation,
 * constitutional Decision, authorization, execution, or Controlled Autonomy.
 */
@ConsistentCopyVisibility
data class EvidenceBasedLearningPreparationResult private constructor(
    val traceId: TraceId,
    val status: EvidenceBasedLearningPreparationStatus,
    val record: EvidenceBasedLearningRecord?,
) {
    companion object {

        fun create(
            traceId: TraceId,
            status: EvidenceBasedLearningPreparationStatus,
            record: EvidenceBasedLearningRecord? = null,
        ): EvidenceBasedLearningPreparationResult {
            when (status) {
                EvidenceBasedLearningPreparationStatus.PREPARED -> {
                    require(record != null) {
                        "Prepared evidence-based learning results require one record."
                    }

                    require(
                        record.worldModelRepresentation.traceId ==
                            traceId,
                    ) {
                        "Evidence-based learning result and World Model representation must use the same trace identity."
                    }
                }

                EvidenceBasedLearningPreparationStatus.DEFERRED -> {
                    require(record == null) {
                        "Deferred evidence-based learning results must not contain a record."
                    }
                }
            }

            return EvidenceBasedLearningPreparationResult(
                traceId = traceId,
                status = status,
                record = record,
            )
        }
    }
}
