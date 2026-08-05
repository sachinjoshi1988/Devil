package com.devil.core.runtime.plan

import com.devil.core.model.common.TraceId
import com.devil.core.model.error.UniversalErrorRecord
import com.devil.core.model.plan.PlanId

/**
 * Represents the result of attempting to supply one genuine plan identity.
 *
 * An available result contains one PlanId. An unavailable result contains
 * neither plan identity nor error. A failed result contains a matching error.
 *
 * This result does not generate plan identity, create planning strategy, create
 * plans, bind capabilities, execute actions, observe results, verify outcomes,
 * or report final outcomes.
 */
@ConsistentCopyVisibility
data class PlanIdentityProvisionResult private constructor(
    val traceId: TraceId,
    val status: PlanIdentityProvisionStatus,
    val planId: PlanId?,
    val error: UniversalErrorRecord?,
) {
    companion object {
        fun create(
            traceId: TraceId,
            status: PlanIdentityProvisionStatus,
            planId: PlanId? = null,
            error: UniversalErrorRecord? = null,
        ): PlanIdentityProvisionResult {
            when (status) {
                PlanIdentityProvisionStatus.AVAILABLE -> {
                    require(planId != null && error == null) {
                        "Available plan identity results require a plan identity and must not contain an error."
                    }
                }

                PlanIdentityProvisionStatus.UNAVAILABLE -> {
                    require(planId == null && error == null) {
                        "Unavailable plan identity results must not contain a plan identity or error."
                    }
                }

                PlanIdentityProvisionStatus.FAILED -> {
                    require(planId == null && error != null) {
                        "Failed plan identity results require an error and must not contain a plan identity."
                    }
                }
            }

            require(error == null || error.traceId == traceId) {
                "Plan identity result and error must use the same trace identity."
            }

            return PlanIdentityProvisionResult(
                traceId = traceId,
                status = status,
                planId = planId,
                error = error,
            )
        }
    }
}
