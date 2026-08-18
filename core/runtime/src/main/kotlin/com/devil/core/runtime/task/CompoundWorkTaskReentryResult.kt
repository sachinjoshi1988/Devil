package com.devil.core.runtime.task

import com.devil.core.model.common.TraceId
import com.devil.core.model.task.CompoundWorkTaskReentryRecord

/**
 * Stable Stage 116 result of bounded compound-work Task re-entry preparation.
 *
 * traceId belongs to the current fresh constitutional reasoning cycle.
 *
 * PREPARED contains exactly one CompoundWorkTaskReentryRecord.
 *
 * DEFERRED contains no Task re-entry record.
 *
 * This result preserves Stage 115 reconsideration provenance transitively but
 * does not create a TaskRecord or invoke the existing Task Authority.
 *
 * It performs no Plan, capability, Executive, ExecutionRequest, execution,
 * Observation, Verification, Outcome, World Model, Learning, Memory, platform,
 * network, automatic-continuation, or Controlled-Autonomy operation.
 *
 * TASK_REENTRY_PREPARED != TASK_CREATED.
 * TASK_REENTRY_PREPARED != TASK_AUTHORITY_RESULT.
 * AUTHORIZATION != TASK_CREATED.
 * TASK_REENTRY != AUTOMATIC_CONTINUATION.
 * TASK_REENTRY != CONTROLLED_AUTONOMY.
 */
@ConsistentCopyVisibility
data class CompoundWorkTaskReentryResult private constructor(
    val traceId: TraceId,
    val status: CompoundWorkTaskReentryStatus,
    val record: CompoundWorkTaskReentryRecord?,
) {
    companion object {

        fun create(
            traceId: TraceId,
            status: CompoundWorkTaskReentryStatus,
            record: CompoundWorkTaskReentryRecord? = null,
        ): CompoundWorkTaskReentryResult {
            when (status) {
                CompoundWorkTaskReentryStatus.PREPARED -> {
                    require(record != null) {
                        "Prepared compound-work Task re-entry results require one Task re-entry record."
                    }
                }

                CompoundWorkTaskReentryStatus.DEFERRED -> {
                    require(record == null) {
                        "Deferred compound-work Task re-entry results must not contain a Task re-entry record."
                    }
                }
            }

            require(
                record == null ||
                    record.request
                        .reconsideration
                        .request
                        .freshDecision
                        .understanding
                        .context
                        .traceId == traceId,
            ) {
                "Compound-work Task re-entry result and fresh Decision must use the same current trace identity."
            }

            require(
                record == null ||
                    record.request
                        .reconsideration
                        .request
                        .continuation
                        .request
                        .decision
                        .understanding
                        .context
                        .traceId != traceId,
            ) {
                "Compound-work Task re-entry current trace must remain distinct from the originating compound-work trace."
            }

            return CompoundWorkTaskReentryResult(
                traceId = traceId,
                status = status,
                record = record,
            )
        }
    }
}
