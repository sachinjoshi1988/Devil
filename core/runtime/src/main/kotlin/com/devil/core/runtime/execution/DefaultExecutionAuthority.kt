package com.devil.core.runtime.execution

import com.devil.core.model.context.ContextEnvelope
import com.devil.core.runtime.authorization.AuthorizationResult
import com.devil.core.runtime.capability.CapabilitySelectionResult
import com.devil.core.runtime.decision.DecisionAuthorityResult
import com.devil.core.runtime.executive.ExecutiveReadinessResult
import com.devil.core.runtime.identity.IdentityResult
import com.devil.core.runtime.plan.PlanAuthorityResult
import com.devil.core.runtime.task.TaskAuthorityResult
import com.devil.core.runtime.trust.TrustResult
import com.devil.core.runtime.understanding.UnderstandingAuthorityResult

/**
 * Default Stage 12 constitutional Execution Authority coordinator.
 *
 * This authority prepares one bounded ExecutionRequest, delegates constitutional
 * execution evaluation, and maps the evaluation into the stable operational
 * ExecutionResult contract.
 *
 * It does not resolve identity, evaluate trust, grant authorization, produce
 * understanding, select decisions, create tasks or plans, select capabilities,
 * establish Executive readiness, invent execution policy, activate
 * capabilities, invoke platform APIs, perform actions, observe execution,
 * verify outcomes, or report final success.
 */
class DefaultExecutionAuthority(
    private val requestProvider:
        ExecutionRequestProvider =
        DefaultExecutionRequestProvider(),
    private val evaluator:
        ExecutionEvaluator =
        DefaultExecutionEvaluator(),
    private val resultMapper:
        ExecutionResultMapper =
        DefaultExecutionResultMapper(),
) : ExecutionAuthority {

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
        readiness: ExecutiveReadinessResult,
    ): ExecutionResult {
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

        require(readiness.traceId == context.traceId) {
            "Context and Executive readiness result must use the same trace identity."
        }

        val requestResult = requestProvider.provide(
            plan = plan,
            capability = capability,
            readiness = readiness,
        )

        require(requestResult.traceId == context.traceId) {
            "Context and execution request result must use the same trace identity."
        }

        return when (requestResult.status) {
            ExecutionRequestStatus.AVAILABLE -> {
                val evaluation = evaluator.evaluate(
                    traceId = context.traceId,
                    request = requireNotNull(requestResult.request),
                )

                require(evaluation.traceId == context.traceId) {
                    "Context and execution evaluation result must use the same trace identity."
                }

                val result = resultMapper.map(
                    traceId = context.traceId,
                    evaluation = evaluation,
                )

                require(result.traceId == context.traceId) {
                    "Context and mapped execution result must use the same trace identity."
                }

                result
            }

            ExecutionRequestStatus.UNAVAILABLE ->
                ExecutionResult.create(
                    traceId = context.traceId,
                    status = ExecutionStatus.DEFERRED,
                )

            ExecutionRequestStatus.FAILED ->
                ExecutionResult.create(
                    traceId = context.traceId,
                    status = ExecutionStatus.FAILED,
                    error = requireNotNull(requestResult.error),
                )
        }
    }
}
