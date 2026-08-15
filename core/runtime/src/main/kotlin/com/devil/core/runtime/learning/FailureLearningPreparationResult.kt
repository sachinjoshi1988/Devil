package com.devil.core.runtime.learning

import com.devil.core.model.common.TraceId
import com.devil.core.model.learning.FailureLearningRecord

/**
 * Stable Stage 93 result of bounded Failure Learning preparation.
 *
 * PREPARED requires exactly one FailureLearningRecord whose preserved Stage 92
 * World Model representation uses the same constitutional trace identity.
 *
 * DEFERRED must contain no Failure Learning record.
 *
 * This result creates no new evidence, constitutional Learning result, Memory
 * Proposal, Memory Authority approval, Memory commitment, strategy adaptation,
 * Decision, authorization, execution, or Controlled Autonomy.
 */
@ConsistentCopyVisibility
data class FailureLearningPreparationResult private constructor(
    val traceId: TraceId,
    val status: FailureLearningPreparationStatus,
    val record: FailureLearningRecord?,
) {
    companion object {

        fun create(
            traceId: TraceId,
            status: FailureLearningPreparationStatus,
            record: FailureLearningRecord? = null,
        ): FailureLearningPreparationResult {
            when (status) {
                FailureLearningPreparationStatus.PREPARED -> {
                    require(record != null) {
                        "Prepared Failure Learning results require one record."
                    }

                    require(
                        record.evidenceBasedLearning
                            .worldModelRepresentation
                            .traceId == traceId,
                    ) {
                        "Failure Learning result and preserved World Model representation must use the same trace identity."
                    }
                }

                FailureLearningPreparationStatus.DEFERRED -> {
                    require(record == null) {
                        "Deferred Failure Learning results must not contain a record."
                    }
                }
            }

            return FailureLearningPreparationResult(
                traceId = traceId,
                status = status,
                record = record,
            )
        }
    }
}
