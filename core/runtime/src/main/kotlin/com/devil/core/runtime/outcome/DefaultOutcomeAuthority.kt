package com.devil.core.runtime.outcome

import com.devil.core.model.context.ContextEnvelope
import com.devil.core.runtime.authorization.AuthorizationResult
import com.devil.core.runtime.capability.CapabilitySelectionResult
import com.devil.core.runtime.decision.DecisionAuthorityResult
import com.devil.core.runtime.executive.ExecutiveReadinessResult
import com.devil.core.runtime.execution.ExecutionResult
import com.devil.core.runtime.identity.IdentityResult
import com.devil.core.runtime.observation.ObservationResult
import com.devil.core.runtime.plan.PlanAuthorityResult
import com.devil.core.runtime.task.TaskAuthorityResult
import com.devil.core.runtime.trust.TrustResult
import com.devil.core.runtime.understanding.UnderstandingAuthorityResult
import com.devil.core.runtime.verification.VerificationResult

/**
 * Default constitutional Outcome Authority coordinator.
 *
 * This authority prepares one bounded OutcomeRequest, evaluates it against
 * genuine outcome evidence, and maps the evaluation into the stable operational
 * OutcomeResult contract.
 *
 * It does not resolve identity, evaluate trust, grant authorization, produce
 * understanding, select decisions, create tasks or plans, select capabilities,
 * establish Executive readiness, approve execution, create observations,
 * establish verification evidence, fabricate outcome evidence, update World
 * Model state, change task or plan state, create memory or learning, or
 * communicate externally.
 */
class DefaultOutcomeAuthority(
    private val requestProvider: OutcomeRequestProvider =
        DefaultOutcomeRequestProvider(),
    private val evaluator: OutcomeEvaluator =
        DefaultOutcomeEvaluator(),
    private val resultMapper: OutcomeResultMapper =
        DefaultOutcomeResultMapper(),
) : OutcomeAuthority {

    override fun establish(
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
        outcomeEvidence: OutcomeEvidenceResult,
    ): OutcomeResult {
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

        require(outcomeEvidence.traceId == context.traceId) {
            "Context and outcome-evidence result must use the same trace identity."
        }

        val requestResult =
            requestProvider.provide(
                verification = verification,
            )

        require(requestResult.traceId == context.traceId) {
            "Context and outcome request result must use the same trace identity."
        }

        return when (requestResult.status) {
            OutcomeRequestStatus.AVAILABLE -> {
                val evaluation =
                    evaluator.evaluate(
                        traceId = context.traceId,
                        request = requireNotNull(requestResult.request),
                        evidence = outcomeEvidence,
                    )

                require(evaluation.traceId == context.traceId) {
                    "Context and outcome evaluation result must use the same trace identity."
                }

                val result =
                    resultMapper.map(
                        traceId = context.traceId,
                        evaluation = evaluation,
                    )

                require(result.traceId == context.traceId) {
                    "Context and mapped outcome result must use the same trace identity."
                }

                result
            }

            OutcomeRequestStatus.UNAVAILABLE ->
                OutcomeResult.create(
                    traceId = context.traceId,
                    status = OutcomeStatus.DEFERRED,
                )

            OutcomeRequestStatus.FAILED ->
                OutcomeResult.create(
                    traceId = context.traceId,
                    status = OutcomeStatus.FAILED,
                    error = requireNotNull(requestResult.error),
                )
        }
    }
}
