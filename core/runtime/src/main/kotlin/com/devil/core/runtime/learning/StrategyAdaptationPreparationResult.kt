package com.devil.core.runtime.learning

import com.devil.core.model.common.TraceId
import com.devil.core.model.learning.StrategyAdaptationRecord

/**
 * Stable Stage 94 result of bounded Strategy Adaptation preparation.
 *
 * PREPARED requires exactly one StrategyAdaptationRecord whose preserved
 * Stage 92 World Model representation uses the same constitutional trace.
 *
 * DEFERRED must contain no Strategy Adaptation record.
 *
 * This result creates no Planner decision, Plan mutation, RecoveryRequest,
 * authorization, execution, Memory commitment, or Controlled Autonomy.
 */
@ConsistentCopyVisibility
data class StrategyAdaptationPreparationResult private constructor(
    val traceId: TraceId,
    val status: StrategyAdaptationPreparationStatus,
    val record: StrategyAdaptationRecord?,
) {
    companion object {

        fun create(
            traceId: TraceId,
            status: StrategyAdaptationPreparationStatus,
            record: StrategyAdaptationRecord? = null,
        ): StrategyAdaptationPreparationResult {
            when (status) {
                StrategyAdaptationPreparationStatus.PREPARED -> {
                    require(record != null) {
                        "Prepared Strategy Adaptation results require one record."
                    }

                    require(
                        record.failureLearning
                            .evidenceBasedLearning
                            .worldModelRepresentation
                            .traceId == traceId,
                    ) {
                        "Strategy Adaptation result and preserved World Model representation must use the same trace identity."
                    }
                }

                StrategyAdaptationPreparationStatus.DEFERRED -> {
                    require(record == null) {
                        "Deferred Strategy Adaptation results must not contain a record."
                    }
                }
            }

            return StrategyAdaptationPreparationResult(
                traceId = traceId,
                status = status,
                record = record,
            )
        }
    }
}
