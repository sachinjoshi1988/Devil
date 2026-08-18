package com.devil.core.runtime.task

import com.devil.core.model.common.TraceId
import com.devil.core.model.task.CompoundWorkPlanReentryRecord

/**
 * Stable Stage 117 result of bounded compound-work Plan re-entry preparation.
 *
 * traceId belongs to the current fresh constitutional reasoning cycle.
 *
 * PREPARED contains exactly one CompoundWorkPlanReentryRecord.
 *
 * DEFERRED contains no Plan re-entry record.
 *
 * This result preserves Stage 116 Task re-entry provenance transitively but
 * does not create a PlanRecord or invoke the existing Plan Authority.
 *
 * It performs no capability, Executive, ExecutionRequest, execution,
 * Observation, Verification, Outcome, World Model, Learning, Memory, platform,
 * network, automatic-continuation, or Controlled-Autonomy operation.
 *
 * TASK_CREATED != PLAN_CREATED.
 * PLAN_REENTRY_PREPARED != PLAN_CREATED.
 * PLAN_REENTRY_PREPARED != PLAN_AUTHORITY_RESULT.
 * PLAN_REENTRY != AUTOMATIC_CONTINUATION.
 * PLAN_REENTRY != CONTROLLED_AUTONOMY.
 */
@ConsistentCopyVisibility
data class CompoundWorkPlanReentryResult private constructor(
    val traceId: TraceId,
    val status: CompoundWorkPlanReentryStatus,
    val record: CompoundWorkPlanReentryRecord?,
) {
    companion object {

        fun create(
            traceId: TraceId,
            status: CompoundWorkPlanReentryStatus,
            record: CompoundWorkPlanReentryRecord? = null,
        ): CompoundWorkPlanReentryResult {
            when (status) {
                CompoundWorkPlanReentryStatus.PREPARED -> {
                    require(record != null) {
                        "Prepared compound-work Plan re-entry results require one Plan re-entry record."
                    }
                }

                CompoundWorkPlanReentryStatus.DEFERRED -> {
                    require(record == null) {
                        "Deferred compound-work Plan re-entry results must not contain a Plan re-entry record."
                    }
                }
            }

            require(
                record == null ||
                    record.request
                        .task
                        .decision
                        .understanding
                        .context
                        .traceId == traceId,
            ) {
                "Compound-work Plan re-entry result and created Task must use the same current trace identity."
            }

            require(
                record == null ||
                    record.request
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
                "Compound-work Plan re-entry current trace must remain distinct from the originating compound-work trace."
            }

            return CompoundWorkPlanReentryResult(
                traceId = traceId,
                status = status,
                record = record,
            )
        }
    }
}
