package com.devil.core.runtime.task

import com.devil.core.model.common.TraceId
import com.devil.core.model.task.CompoundWorkCapabilityReentryRecord

/**
 * Stable Stage 118 result of bounded compound-work Capability Selection
 * re-entry preparation.
 *
 * traceId belongs to the current fresh constitutional reasoning cycle.
 *
 * PREPARED contains exactly one CompoundWorkCapabilityReentryRecord.
 *
 * DEFERRED contains no Capability Selection re-entry record.
 *
 * This result preserves Stage 117 Plan re-entry provenance transitively but
 * does not select a capability or invoke the existing Capability Selection
 * Authority.
 *
 * It performs no Executive, ExecutionRequest, execution, Observation,
 * Verification, Outcome, World Model, Learning, Memory, platform, network,
 * automatic-continuation, or Controlled-Autonomy operation.
 *
 * PLAN_CREATED != CAPABILITY_SELECTED.
 * CAPABILITY_REENTRY_PREPARED != CAPABILITY_SELECTED.
 * CAPABILITY_REENTRY_PREPARED != CAPABILITY_SELECTION_RESULT.
 * CAPABILITY_REENTRY != EXECUTIVE_READINESS.
 * CAPABILITY_REENTRY != EXECUTION.
 * CAPABILITY_REENTRY != AUTOMATIC_CONTINUATION.
 * CAPABILITY_REENTRY != CONTROLLED_AUTONOMY.
 */
@ConsistentCopyVisibility
data class CompoundWorkCapabilityReentryResult private constructor(
    val traceId: TraceId,
    val status: CompoundWorkCapabilityReentryStatus,
    val record: CompoundWorkCapabilityReentryRecord?,
) {
    companion object {

        fun create(
            traceId: TraceId,
            status: CompoundWorkCapabilityReentryStatus,
            record: CompoundWorkCapabilityReentryRecord? = null,
        ): CompoundWorkCapabilityReentryResult {
            when (status) {
                CompoundWorkCapabilityReentryStatus.PREPARED -> {
                    require(record != null) {
                        "Prepared compound-work Capability re-entry results require one Capability re-entry record."
                    }
                }

                CompoundWorkCapabilityReentryStatus.DEFERRED -> {
                    require(record == null) {
                        "Deferred compound-work Capability re-entry results must not contain a Capability re-entry record."
                    }
                }
            }

            require(
                record == null ||
                    record.request
                        .plan
                        .task
                        .decision
                        .understanding
                        .context
                        .traceId == traceId,
            ) {
                "Compound-work Capability re-entry result and created Plan must use the same current trace identity."
            }

            require(
                record == null ||
                    record.request
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
                "Compound-work Capability re-entry current trace must remain distinct from the originating compound-work trace."
            }

            return CompoundWorkCapabilityReentryResult(
                traceId = traceId,
                status = status,
                record = record,
            )
        }
    }
}
