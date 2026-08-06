package com.devil.core.runtime.executive

import com.devil.core.model.context.ContextEnvelope
import com.devil.core.runtime.authorization.AuthorizationResult
import com.devil.core.runtime.capability.CapabilitySelectionResult
import com.devil.core.runtime.decision.DecisionAuthorityResult
import com.devil.core.runtime.identity.IdentityResult
import com.devil.core.runtime.plan.PlanAuthorityResult
import com.devil.core.runtime.task.TaskAuthorityResult
import com.devil.core.runtime.trust.TrustResult
import com.devil.core.runtime.understanding.UnderstandingAuthorityResult

/**
 * Default Stage 11 constitutional Executive Readiness Authority coordinator.
 *
 * This authority obtains one bounded Executive-readiness request, delegates
 * constitutional readiness evaluation, and maps the evaluation into the stable
 * operational readiness result contract.
 *
 * It does not resolve identity, evaluate trust, grant authorization, produce
 * understanding, select decisions, create tasks or plans, select capabilities,
 * invent readiness policy, establish capability availability or health, check
 * operating-system permission, execute actions, observe results, verify
 * outcomes, or report final outcomes.
 */
class DefaultExecutiveReadinessAuthority(
    private val requestProvider:
        ExecutiveReadinessRequestProvider =
        DefaultExecutiveReadinessRequestProvider(),
    private val evaluator:
        ExecutiveReadinessEvaluator =
        DefaultExecutiveReadinessEvaluator(),
    private val resultMapper:
        ExecutiveReadinessResultMapper =
        DefaultExecutiveReadinessResultMapper(),
) : ExecutiveReadinessAuthority {

    override fun evaluate(
        context: ContextEnvelope,
        identity: IdentityResult,
        trust: TrustResult,
        authorization: AuthorizationResult,
        understanding: UnderstandingAuthorityResult,
        decision: DecisionAuthorityResult,
        task: TaskAuthorityResult,
        plan: PlanAuthorityResult,
        capability: CapabilitySelectionResult,
    ): ExecutiveReadinessResult {
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

        require(plan.traceId == context.traceId) {
            "Context and plan result must use the same trace identity."
        }

        require(capability.traceId == context.traceId) {
            "Context and capability selection result must use the same trace identity."
        }

        val requestResult = requestProvider.provide(
            plan = plan,
            capability = capability,
        )

        require(requestResult.traceId == context.traceId) {
            "Context and Executive readiness request result must use the same trace identity."
        }

        return when (requestResult.status) {
            ExecutiveReadinessRequestStatus.AVAILABLE -> {
                val evaluation = evaluator.evaluate(
                    traceId = context.traceId,
                    request = requireNotNull(requestResult.request),
                )

                require(evaluation.traceId == context.traceId) {
                    "Context and Executive readiness evaluation result must use the same trace identity."
                }

                val result = resultMapper.map(
                    traceId = context.traceId,
                    evaluation = evaluation,
                )

                require(result.traceId == context.traceId) {
                    "Context and mapped Executive readiness result must use the same trace identity."
                }

                result
            }

            ExecutiveReadinessRequestStatus.UNAVAILABLE ->
                ExecutiveReadinessResult.create(
                    traceId = context.traceId,
                    status = ExecutiveReadinessStatus.DEFERRED,
                )

            ExecutiveReadinessRequestStatus.FAILED ->
                ExecutiveReadinessResult.create(
                    traceId = context.traceId,
                    status = ExecutiveReadinessStatus.FAILED,
                    error = requireNotNull(requestResult.error),
                )
        }
    }
}
