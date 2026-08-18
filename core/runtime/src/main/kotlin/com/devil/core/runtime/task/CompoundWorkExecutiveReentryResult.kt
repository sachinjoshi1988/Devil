package com.devil.core.runtime.task

import com.devil.core.model.common.TraceId
import com.devil.core.model.task.CompoundWorkExecutiveReentryRecord

/**
 * Stable Stage 119 result of bounded compound-work Executive re-entry
 * preparation.
 *
 * PREPARED contains exactly one CompoundWorkExecutiveReentryRecord.
 * DEFERRED contains no Executive re-entry record.
 *
 * This result preserves Stage 118 Capability re-entry provenance transitively
 * but does not establish Executive readiness or invoke Executive Readiness
 * Authority.
 *
 * It performs no ExecutionRequest, execution, Observation, Verification,
 * Outcome, World Model, Learning, Memory, platform, network,
 * automatic-continuation, or Controlled-Autonomy operation.
 *
 * CAPABILITY_SELECTED != EXECUTIVE_READY.
 * EXECUTIVE_REENTRY_PREPARED != EXECUTIVE_READY.
 * EXECUTIVE_REENTRY_PREPARED != EXECUTIVE_READINESS_RESULT.
 * EXECUTIVE_REENTRY != EXECUTION_REQUEST.
 * EXECUTIVE_REENTRY != EXECUTION.
 * EXECUTIVE_REENTRY != AUTOMATIC_CONTINUATION.
 * EXECUTIVE_REENTRY != CONTROLLED_AUTONOMY.
 */
@ConsistentCopyVisibility
data class CompoundWorkExecutiveReentryResult private constructor(
    val traceId: TraceId,
    val status: CompoundWorkExecutiveReentryStatus,
    val record: CompoundWorkExecutiveReentryRecord?,
) {
    companion object {

        fun create(
            traceId: TraceId,
            status: CompoundWorkExecutiveReentryStatus,
            record: CompoundWorkExecutiveReentryRecord? = null,
        ): CompoundWorkExecutiveReentryResult {
            when (status) {
                CompoundWorkExecutiveReentryStatus.PREPARED -> {
                    require(record != null) {
                        "Prepared compound-work Executive re-entry results require one Executive re-entry record."
                    }
                }

                CompoundWorkExecutiveReentryStatus.DEFERRED -> {
                    require(record == null) {
                        "Deferred compound-work Executive re-entry results must not contain an Executive re-entry record."
                    }
                }
            }

            require(
                record == null ||
                    record.request
                        .capabilityReentry
                        .request
                        .plan
                        .task
                        .decision
                        .understanding
                        .context
                        .traceId == traceId,
            ) {
                "Compound-work Executive re-entry result and preserved Plan must use the same current trace identity."
            }

            require(
                record == null ||
                    record.request
                        .capabilityReentry
                        .request
                        .planReentry
                        .request
                        .taskReentry
                        .request
                        .reconsideration
                        .request
                        .continuation
                        .request
                        .decision
                        .understanding
                        .context
                        .traceId != traceId,
            ) {
                "Compound-work Executive re-entry current trace must remain distinct from the originating compound-work trace."
            }

            return CompoundWorkExecutiveReentryResult(
                traceId = traceId,
                status = status,
                record = record,
            )
        }
    }
}
