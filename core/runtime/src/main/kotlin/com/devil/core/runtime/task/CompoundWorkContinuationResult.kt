package com.devil.core.runtime.task

import com.devil.core.model.common.TraceId
import com.devil.core.model.task.CompoundWorkContinuationRecord

/**
 * Stable Stage 114 result of bounded compound-work continuation eligibility.
 *
 * ELIGIBLE_FOR_RECONSIDERATION contains exactly one
 * CompoundWorkContinuationRecord.
 *
 * DEFERRED contains no continuation record.
 *
 * The result preserves only eligibility for fresh constitutional
 * reconsideration.
 *
 * It grants no authorization and performs no Decision, Task, Plan, capability,
 * Executive, execution, Observation, Verification, Outcome, World Model,
 * Learning, Memory, platform, network, or Controlled Autonomy operation.
 *
 * CONTINUATION_ELIGIBILITY != CONTINUATION_EXECUTION.
 * ELIGIBLE != AUTHORIZED.
 * STEP_COMPLETED != NEXT_STEP_AUTHORIZATION.
 * ELIGIBILITY != AUTOMATIC_CONTINUATION.
 */
@ConsistentCopyVisibility
data class CompoundWorkContinuationResult private constructor(
    val traceId: TraceId,
    val status: CompoundWorkContinuationStatus,
    val record: CompoundWorkContinuationRecord?,
) {
    companion object {

        fun create(
            traceId: TraceId,
            status: CompoundWorkContinuationStatus,
            record: CompoundWorkContinuationRecord? = null,
        ): CompoundWorkContinuationResult {
            when (status) {
                CompoundWorkContinuationStatus.ELIGIBLE_FOR_RECONSIDERATION -> {
                    require(record != null) {
                        "Eligible compound-work continuation results require one continuation record."
                    }
                }

                CompoundWorkContinuationStatus.DEFERRED -> {
                    require(record == null) {
                        "Deferred compound-work continuation results must not contain a continuation record."
                    }
                }
            }

            require(
                record == null ||
                    record.request
                        .decision
                        .understanding
                        .context
                        .traceId == traceId,
            ) {
                "Compound-work continuation result and preserved request must use the same trace identity."
            }

            return CompoundWorkContinuationResult(
                traceId = traceId,
                status = status,
                record = record,
            )
        }
    }
}
