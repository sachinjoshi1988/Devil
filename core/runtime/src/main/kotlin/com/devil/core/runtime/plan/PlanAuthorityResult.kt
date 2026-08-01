package com.devil.core.runtime.plan

import com.devil.core.model.common.TraceId
import com.devil.core.model.error.UniversalErrorRecord
import com.devil.core.model.plan.PlanRecord

/**
 * Represents the structured operational result of plan creation.
 *
 * A created result contains a PlanRecord. A deferred result contains neither
 * plan nor error. A failed result contains a matching error.
 */
@ConsistentCopyVisibility
data class PlanAuthorityResult private constructor(
    val traceId: TraceId,
    val status: PlanAuthorityStatus,
    val plan: PlanRecord?,
    val error: UniversalErrorRecord?,
) {
    companion object {
        fun create(
            traceId: TraceId,
            status: PlanAuthorityStatus,
            plan: PlanRecord? = null,
            error: UniversalErrorRecord? = null,
        ): PlanAuthorityResult {
            when (status) {
                PlanAuthorityStatus.CREATED -> {
                    require(plan != null && error == null) {
                        "Created plan results require a plan and must not contain an error."
                    }
                }

                PlanAuthorityStatus.DEFERRED -> {
                    require(plan == null && error == null) {
                        "Deferred plan results must not contain a plan or error."
                    }
                }

                PlanAuthorityStatus.FAILED -> {
                    require(plan == null && error != null) {
                        "Failed plan results require an error and must not contain a plan."
                    }
                }
            }

            require(
                plan == null ||
                    plan.task.decision.understanding.context.traceId == traceId,
            ) {
                "Plan result and plan must use the same trace identity."
            }

            require(error == null || error.traceId == traceId) {
                "Plan result and error must use the same trace identity."
            }

            return PlanAuthorityResult(
                traceId = traceId,
                status = status,
                plan = plan,
                error = error,
            )
        }
    }
}
