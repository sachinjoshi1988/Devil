package com.devil.core.runtime.task

import com.devil.core.model.common.TraceId
import com.devil.core.model.task.CompoundWorkReconsiderationRecord

/**
 * Stable Stage 115 result of bounded compound-work reconsideration preparation.
 *
 * traceId belongs to the fresh constitutional reasoning cycle.
 *
 * PREPARED contains exactly one CompoundWorkReconsiderationRecord.
 *
 * DEFERRED contains no reconsideration record.
 *
 * The originating Stage 77 / Stage 114 trace remains preserved transitively and
 * must remain distinct from this fresh trace.
 *
 * This result grants no authorization and performs no Task, Plan, capability,
 * Executive, execution, Observation, Verification, Outcome, World Model,
 * Learning, Memory, platform, network, or Controlled Autonomy operation.
 *
 * ORIGINAL_TRACE != FRESH_RECONSIDERATION_TRACE.
 * RECONSIDERATION != AUTHORIZATION.
 * RECONSIDERATION != AUTOMATIC_CONTINUATION.
 * RECONSIDERATION != CONTROLLED_AUTONOMY.
 */
@ConsistentCopyVisibility
data class CompoundWorkReconsiderationResult private constructor(
    val traceId: TraceId,
    val status: CompoundWorkReconsiderationStatus,
    val record: CompoundWorkReconsiderationRecord?,
) {
    companion object {

        fun create(
            traceId: TraceId,
            status: CompoundWorkReconsiderationStatus,
            record: CompoundWorkReconsiderationRecord? = null,
        ): CompoundWorkReconsiderationResult {
            when (status) {
                CompoundWorkReconsiderationStatus.PREPARED -> {
                    require(record != null) {
                        "Prepared compound-work reconsideration results require one reconsideration record."
                    }
                }

                CompoundWorkReconsiderationStatus.DEFERRED -> {
                    require(record == null) {
                        "Deferred compound-work reconsideration results must not contain a reconsideration record."
                    }
                }
            }

            require(
                record == null ||
                    record.request
                        .freshDecision
                        .understanding
                        .context
                        .traceId == traceId,
            ) {
                "Compound-work reconsideration result and fresh Decision must use the same fresh trace identity."
            }

            require(
                record == null ||
                    record.request
                        .continuation
                        .request
                        .decision
                        .understanding
                        .context
                        .traceId != traceId,
            ) {
                "Compound-work reconsideration fresh trace must remain distinct from the originating compound-work trace."
            }

            return CompoundWorkReconsiderationResult(
                traceId = traceId,
                status = status,
                record = record,
            )
        }
    }
}
