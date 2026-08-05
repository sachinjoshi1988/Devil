package com.devil.core.runtime.plan

import com.devil.core.model.common.TraceId
import com.devil.core.model.error.UniversalErrorRecord

/**
 * Represents the result of attempting to supply one bounded constitutional
 * planning strategy.
 *
 * An available result contains one normalized nonblank strategy. An unavailable
 * result contains neither strategy nor error. A failed result contains a
 * matching error.
 *
 * This result does not create strategy, generate plan identity, create plans,
 * bind capabilities, execute actions, observe results, verify outcomes, or
 * report final outcomes.
 */
@ConsistentCopyVisibility
data class PlanningStrategyProvisionResult private constructor(
    val traceId: TraceId,
    val status: PlanningStrategyProvisionStatus,
    val strategy: String?,
    val error: UniversalErrorRecord?,
) {
    companion object {
        fun create(
            traceId: TraceId,
            status: PlanningStrategyProvisionStatus,
            strategy: String? = null,
            error: UniversalErrorRecord? = null,
        ): PlanningStrategyProvisionResult {
            val normalizedStrategy = strategy?.trim()

            when (status) {
                PlanningStrategyProvisionStatus.AVAILABLE -> {
                    require(
                        !normalizedStrategy.isNullOrEmpty() &&
                            error == null,
                    ) {
                        "Available planning strategy results require a nonblank strategy and must not contain an error."
                    }
                }

                PlanningStrategyProvisionStatus.UNAVAILABLE -> {
                    require(strategy == null && error == null) {
                        "Unavailable planning strategy results must not contain a strategy or error."
                    }
                }

                PlanningStrategyProvisionStatus.FAILED -> {
                    require(strategy == null && error != null) {
                        "Failed planning strategy results require an error and must not contain a strategy."
                    }
                }
            }

            require(error == null || error.traceId == traceId) {
                "Planning strategy result and error must use the same trace identity."
            }

            return PlanningStrategyProvisionResult(
                traceId = traceId,
                status = status,
                strategy = normalizedStrategy,
                error = error,
            )
        }
    }
}
