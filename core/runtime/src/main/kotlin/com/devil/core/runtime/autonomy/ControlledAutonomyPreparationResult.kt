package com.devil.core.runtime.autonomy

import com.devil.core.model.autonomy.ControlledAutonomyRecord
import com.devil.core.model.common.TraceId

/**
 * Stable Stage 95 result of bounded Controlled Autonomy preparation.
 *
 * PREPARED requires exactly one ControlledAutonomyRecord whose preserved
 * evidence-backed World Model provenance uses the same constitutional trace.
 *
 * DEFERRED must contain no Controlled Autonomy record.
 *
 * This result creates no authorization, Brain Decision, Task, Plan, Planner
 * mutation, Executive readiness, capability activation, ExecutionRequest,
 * execution, trigger, Proactive Assistance, Memory commitment, or autonomous
 * continuation.
 */
@ConsistentCopyVisibility
data class ControlledAutonomyPreparationResult private constructor(
    val traceId: TraceId,
    val status: ControlledAutonomyPreparationStatus,
    val record: ControlledAutonomyRecord?,
) {
    companion object {

        fun create(
            traceId: TraceId,
            status: ControlledAutonomyPreparationStatus,
            record: ControlledAutonomyRecord? = null,
        ): ControlledAutonomyPreparationResult {
            when (status) {
                ControlledAutonomyPreparationStatus.PREPARED -> {
                    require(record != null) {
                        "Prepared Controlled Autonomy results require one record."
                    }

                    require(
                        record.strategyAdaptation
                            .failureLearning
                            .evidenceBasedLearning
                            .worldModelRepresentation
                            .traceId == traceId,
                    ) {
                        "Controlled Autonomy result and preserved World Model representation must use the same trace identity."
                    }
                }

                ControlledAutonomyPreparationStatus.DEFERRED -> {
                    require(record == null) {
                        "Deferred Controlled Autonomy results must not contain a record."
                    }
                }
            }

            return ControlledAutonomyPreparationResult(
                traceId = traceId,
                status = status,
                record = record,
            )
        }
    }
}
