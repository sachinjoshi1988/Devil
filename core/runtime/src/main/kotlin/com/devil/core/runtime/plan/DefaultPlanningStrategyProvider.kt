package com.devil.core.runtime.plan

import com.devil.core.model.common.TraceId
import com.devil.core.model.plan.PlanCreationRequest
import com.devil.core.model.understanding.UnderstandingActionability
import com.devil.core.model.understanding.UnderstandingIntent
import com.devil.core.model.understanding.UnderstandingState

/**
 * Default bounded constitutional planning-strategy provider.
 *
 * Stage 59 derives one deliberately small planning strategy only from
 * structured semantic meaning already established by Understanding and carried
 * through the selected Decision and created Task.
 *
 * Planner may formulate how to pursue the established goal, but it must not
 * reinterpret or change that goal.
 *
 * Providing a strategy does not generate plan identity, create a plan,
 * select or authorize a capability, execute an action, observe execution,
 * verify an effect, or establish an Outcome.
 */
class DefaultPlanningStrategyProvider : PlanningStrategyProvider {

    override fun provide(
        traceId: TraceId,
        request: PlanCreationRequest,
    ): PlanningStrategyProvisionResult {
        require(
            request.task.decision.understanding.context.traceId == traceId,
        ) {
            "Planning strategy trace and plan-creation request must use the same trace identity."
        }

        val understanding =
            request.task.decision.understanding

        if (understanding.state != UnderstandingState.COMPLETE) {
            return PlanningStrategyProvisionResult.create(
                traceId = traceId,
                status = PlanningStrategyProvisionStatus.UNAVAILABLE,
            )
        }

        val semantics =
            understanding.semantics
                ?: return PlanningStrategyProvisionResult.create(
                    traceId = traceId,
                    status = PlanningStrategyProvisionStatus.UNAVAILABLE,
                )

        val strategy =
            when (semantics.intent) {
                UnderstandingIntent.GREETING -> {
                    if (
                        semantics.actionability ==
                        UnderstandingActionability.NON_ACTIONABLE
                    ) {
                        "Prepare a bounded conversational acknowledgement of the greeting."
                    } else {
                        null
                    }
                }

                UnderstandingIntent.OPEN_TARGET -> {
                    val target = semantics.target

                    if (
                        semantics.actionability ==
                        UnderstandingActionability.ACTIONABLE &&
                        target != null
                    ) {
                        "Prepare the bounded plan for opening target: $target."
                    } else {
                        null
                    }
                }


                UnderstandingIntent.ACTION_REQUEST,
                UnderstandingIntent.INFORMATION_QUERY,
                -> null
                UnderstandingIntent.INFORMATIONAL -> {
                    if (
                        semantics.actionability ==
                        UnderstandingActionability.NON_ACTIONABLE
                    ) {
                        "Prepare bounded conversational handling of the supplied informational statement."
                    } else {
                        null
                    }
                }
            }

        return if (strategy != null) {
            PlanningStrategyProvisionResult.create(
                traceId = traceId,
                status = PlanningStrategyProvisionStatus.AVAILABLE,
                strategy = strategy,
            )
        } else {
            PlanningStrategyProvisionResult.create(
                traceId = traceId,
                status = PlanningStrategyProvisionStatus.UNAVAILABLE,
            )
        }
    }
}
