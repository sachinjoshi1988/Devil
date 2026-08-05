package com.devil.core.runtime.plan

import com.devil.core.model.context.ContextEnvelope
import com.devil.core.runtime.authorization.AuthorizationResult
import com.devil.core.runtime.decision.DecisionAuthorityResult
import com.devil.core.runtime.identity.IdentityResult
import com.devil.core.runtime.task.TaskAuthorityResult
import com.devil.core.runtime.trust.TrustResult
import com.devil.core.runtime.understanding.UnderstandingAuthorityResult

/**
 * Default Stage 9 constitutional Plan Authority coordinator.
 *
 * This authority obtains one bounded plan-creation request, obtains one
 * constitutional planning strategy and one genuine plan identity, delegates
 * plan creation to the resolver, and maps the resulting PlanRecord into the
 * stable operational result contract.
 *
 * It does not resolve identity, evaluate trust, grant authorization, produce
 * understanding, select decisions, create tasks, invent planning strategy,
 * generate plan identity, bind capabilities, execute actions, observe results,
 * verify outcomes, or report final outcomes.
 */
class DefaultPlanAuthority(
    private val requestProvider:
        PlanCreationRequestProvider =
        DefaultPlanCreationRequestProvider(),
    private val strategyProvider:
        PlanningStrategyProvider =
        DefaultPlanningStrategyProvider(),
    private val planIdentityProvider:
        PlanIdentityProvider =
        DefaultPlanIdentityProvider(),
    private val resolver:
        PlanCreationResolver =
        DefaultPlanCreationResolver(),
    private val resultMapper:
        PlanCreationResultMapper =
        DefaultPlanCreationResultMapper(),
) : PlanAuthority {

    override fun createPlan(
        context: ContextEnvelope,
        identity: IdentityResult,
        trust: TrustResult,
        authorization: AuthorizationResult,
        understanding: UnderstandingAuthorityResult,
        decision: DecisionAuthorityResult,
        task: TaskAuthorityResult,
    ): PlanAuthorityResult {
        require(identity.traceId == context.traceId) {
            "Context and identity result must use the same trace identity."
        }

        require(trust.traceId == context.traceId) {
            "Context and trust result must use the same trace identity."
        }

        require(authorization.traceId == context.traceId) {
            "Context and authorization result must use the same trace identity."
        }

        require(understanding.traceId == context.traceId) {
            "Context and understanding result must use the same trace identity."
        }

        require(decision.traceId == context.traceId) {
            "Context and decision result must use the same trace identity."
        }

        require(task.traceId == context.traceId) {
            "Context and task result must use the same trace identity."
        }

        val requestResult = requestProvider.provide(task)

        require(requestResult.traceId == context.traceId) {
            "Context and plan-creation request result must use the same trace identity."
        }

        return when (requestResult.status) {
            PlanCreationRequestStatus.AVAILABLE -> {
                val request = requireNotNull(requestResult.request)

                val strategyResult = strategyProvider.provide(
                    traceId = context.traceId,
                    request = request,
                )

                require(strategyResult.traceId == context.traceId) {
                    "Context and planning strategy result must use the same trace identity."
                }

                when (strategyResult.status) {
                    PlanningStrategyProvisionStatus.AVAILABLE -> {
                        val identityResult =
                            planIdentityProvider.provide(
                                traceId = context.traceId,
                                request = request,
                            )

                        require(identityResult.traceId == context.traceId) {
                            "Context and plan identity result must use the same trace identity."
                        }

                        when (identityResult.status) {
                            PlanIdentityProvisionStatus.AVAILABLE -> {
                                val plan = resolver.create(
                                    request = request,
                                    planId =
                                        requireNotNull(identityResult.planId),
                                    strategy =
                                        requireNotNull(strategyResult.strategy),
                                )

                                val result = resultMapper.map(
                                    traceId = context.traceId,
                                    plan = plan,
                                )

                                require(result.traceId == context.traceId) {
                                    "Context and mapped plan result must use the same trace identity."
                                }

                                result
                            }

                            PlanIdentityProvisionStatus.UNAVAILABLE ->
                                PlanAuthorityResult.create(
                                    traceId = context.traceId,
                                    status = PlanAuthorityStatus.DEFERRED,
                                )

                            PlanIdentityProvisionStatus.FAILED ->
                                PlanAuthorityResult.create(
                                    traceId = context.traceId,
                                    status = PlanAuthorityStatus.FAILED,
                                    error =
                                        requireNotNull(identityResult.error),
                                )
                        }
                    }

                    PlanningStrategyProvisionStatus.UNAVAILABLE ->
                        PlanAuthorityResult.create(
                            traceId = context.traceId,
                            status = PlanAuthorityStatus.DEFERRED,
                        )

                    PlanningStrategyProvisionStatus.FAILED ->
                        PlanAuthorityResult.create(
                            traceId = context.traceId,
                            status = PlanAuthorityStatus.FAILED,
                            error = requireNotNull(strategyResult.error),
                        )
                }
            }

            PlanCreationRequestStatus.UNAVAILABLE ->
                PlanAuthorityResult.create(
                    traceId = context.traceId,
                    status = PlanAuthorityStatus.DEFERRED,
                )

            PlanCreationRequestStatus.FAILED ->
                PlanAuthorityResult.create(
                    traceId = context.traceId,
                    status = PlanAuthorityStatus.FAILED,
                    error = requireNotNull(requestResult.error),
                )
        }
    }
}
