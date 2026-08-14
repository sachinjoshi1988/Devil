package com.devil.core.runtime.task

import com.devil.core.model.common.TraceId
import com.devil.core.model.task.CompoundWorkRequest

/**
 * Stable Stage 77 result for bounded compound-work preparation.
 *
 * A PREPARED result preserves the exact CompoundWorkRequest.
 *
 * A DEFERRED result contains no request.
 *
 * This result does not create TaskRecord values, PlanRecord values, capability
 * bindings, execution requests, observations, verification, Outcomes, Learning,
 * or Memory.
 */
@ConsistentCopyVisibility
data class CompoundWorkPreparationResult private constructor(
    val traceId: TraceId,
    val status: CompoundWorkPreparationStatus,
    val request: CompoundWorkRequest?,
) {
    companion object {

        fun create(
            traceId: TraceId,
            status: CompoundWorkPreparationStatus,
            request: CompoundWorkRequest? = null,
        ): CompoundWorkPreparationResult {
            when (status) {
                CompoundWorkPreparationStatus.PREPARED -> {
                    require(request != null) {
                        "Prepared compound work requires one compound-work request."
                    }
                }

                CompoundWorkPreparationStatus.DEFERRED -> {
                    require(request == null) {
                        "Deferred compound work must not contain a compound-work request."
                    }
                }
            }

            require(
                request == null ||
                    request.decision
                        .understanding
                        .context
                        .traceId == traceId,
            ) {
                "Compound-work preparation result and request must use the same trace identity."
            }

            return CompoundWorkPreparationResult(
                traceId = traceId,
                status = status,
                request = request,
            )
        }
    }
}
