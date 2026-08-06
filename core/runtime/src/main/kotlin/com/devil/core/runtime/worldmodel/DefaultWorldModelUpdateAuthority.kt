package com.devil.core.runtime.worldmodel

import com.devil.core.model.context.ContextEnvelope
import com.devil.core.runtime.authorization.AuthorizationResult
import com.devil.core.runtime.capability.CapabilitySelectionResult
import com.devil.core.runtime.decision.DecisionAuthorityResult
import com.devil.core.runtime.executive.ExecutiveReadinessResult
import com.devil.core.runtime.execution.ExecutionResult
import com.devil.core.runtime.identity.IdentityResult
import com.devil.core.runtime.observation.ObservationResult
import com.devil.core.runtime.outcome.OutcomeResult
import com.devil.core.runtime.plan.PlanAuthorityResult
import com.devil.core.runtime.task.TaskAuthorityResult
import com.devil.core.runtime.trust.TrustResult
import com.devil.core.runtime.understanding.UnderstandingAuthorityResult
import com.devil.core.runtime.verification.VerificationResult

/**
 * Default Stage 16 constitutional World Model Update Authority coordinator.
 *
 * This authority prepares one bounded WorldModelUpdateRequest, delegates
 * constitutional update evaluation, and maps that evaluation into the stable
 * operational WorldModelUpdateResult contract.
 *
 * It does not resolve identity, evaluate trust, grant authorization, produce
 * understanding, select decisions, create tasks or plans, select
 * capabilities, establish Executive readiness, approve execution, create
 * observations, establish verification or outcome evidence, mutate world
 * state, claim that world state changed, create memory or learning, or
 * communicate externally.
 */
class DefaultWorldModelUpdateAuthority(
    private val requestProvider: WorldModelUpdateRequestProvider =
        DefaultWorldModelUpdateRequestProvider(),
    private val evaluator: WorldModelUpdateEvaluator =
        DefaultWorldModelUpdateEvaluator(),
    private val resultMapper: WorldModelUpdateResultMapper =
        DefaultWorldModelUpdateResultMapper(),
) : WorldModelUpdateAuthority {

    override fun evaluateUpdate(
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
        execution: ExecutionResult,
        observation: ObservationResult,
        verification: VerificationResult,
        outcome: OutcomeResult,
    ): WorldModelUpdateResult {
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

        require(execution.traceId == context.traceId) {
            "Context and execution result must use the same trace identity."
        }

        require(observation.traceId == context.traceId) {
            "Context and observation result must use the same trace identity."
        }

        require(verification.traceId == context.traceId) {
            "Context and verification result must use the same trace identity."
        }

        require(outcome.traceId == context.traceId) {
            "Context and outcome result must use the same trace identity."
        }

        val requestResult = requestProvider.provide(
            outcome = outcome,
        )

        require(requestResult.traceId == context.traceId) {
            "Context and World Model update request result must use the same trace identity."
        }

        return when (requestResult.status) {
            WorldModelUpdateRequestStatus.AVAILABLE -> {
                val evaluation = evaluator.evaluate(
                    traceId = context.traceId,
                    request = requireNotNull(requestResult.request),
                )

                require(evaluation.traceId == context.traceId) {
                    "Context and World Model update evaluation result must use the same trace identity."
                }

                val result = resultMapper.map(
                    traceId = context.traceId,
                    evaluation = evaluation,
                )

                require(result.traceId == context.traceId) {
                    "Context and mapped World Model update result must use the same trace identity."
                }

                result
            }

            WorldModelUpdateRequestStatus.UNAVAILABLE ->
                WorldModelUpdateResult.create(
                    traceId = context.traceId,
                    status = WorldModelUpdateStatus.DEFERRED,
                )

            WorldModelUpdateRequestStatus.FAILED ->
                WorldModelUpdateResult.create(
                    traceId = context.traceId,
                    status = WorldModelUpdateStatus.FAILED,
                    error = requireNotNull(requestResult.error),
                )
        }
    }
}
