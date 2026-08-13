package com.devil.core.runtime.observation

import com.devil.core.model.context.ContextEnvelope
import com.devil.core.runtime.authorization.AuthorizationResult
import com.devil.core.runtime.capability.CapabilitySelectionResult
import com.devil.core.runtime.decision.DecisionAuthorityResult
import com.devil.core.runtime.executive.ExecutiveReadinessResult
import com.devil.core.runtime.execution.ExecutionAttemptResult
import com.devil.core.runtime.execution.ExecutionResult
import com.devil.core.runtime.identity.IdentityResult
import com.devil.core.runtime.plan.PlanAuthorityResult
import com.devil.core.runtime.task.TaskAuthorityResult
import com.devil.core.runtime.trust.TrustResult
import com.devil.core.runtime.understanding.UnderstandingAuthorityResult

/**
 * Default Stage 13 constitutional Observation Authority coordinator.
 *
 * This authority prepares one bounded ObservationRequest, delegates
 * constitutional observation evaluation, and maps the evaluation into the
 * stable operational ObservationResult contract.
 *
 * It does not resolve identity, evaluate trust, grant authorization, produce
 * understanding, select decisions, create tasks or plans, select capabilities,
 * establish Executive readiness, approve execution, activate capabilities,
 * invoke platform APIs, perform actions, fabricate execution attempts, invent
 * observation evidence, verify outcomes, update world state, report success, or
 * produce final outcomes.
 */
class DefaultObservationAuthority(
    private val requestProvider: ObservationRequestProvider =
        DefaultObservationRequestProvider(),
    private val evaluator: ObservationEvaluator =
        DefaultObservationEvaluator(),
    private val resultMapper: ObservationResultMapper =
        DefaultObservationResultMapper(),
) : ObservationAuthority {

    override fun observe(
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
        executionAttempt: ExecutionAttemptResult,
        observationEvidence: ObservationEvidenceResult,
    ): ObservationResult {
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

        require(executionAttempt.traceId == context.traceId) {
            "Context and execution-attempt result must use the same trace identity."
        }

        require(observationEvidence.traceId == context.traceId) {
            "Context and observation-evidence result must use the same trace identity."
        }

        val requestResult = requestProvider.provide(
            executionAttempt = executionAttempt,
        )

        require(requestResult.traceId == context.traceId) {
            "Context and observation request result must use the same trace identity."
        }

        return when (requestResult.status) {
            ObservationRequestStatus.AVAILABLE -> {
                val evaluation = evaluator.evaluate(
                    traceId = context.traceId,
                    request = requireNotNull(requestResult.request),
                    evidence = observationEvidence,
                )

                require(evaluation.traceId == context.traceId) {
                    "Context and observation evaluation result must use the same trace identity."
                }

                val result = resultMapper.map(
                    traceId = context.traceId,
                    evaluation = evaluation,
                )

                require(result.traceId == context.traceId) {
                    "Context and mapped observation result must use the same trace identity."
                }

                result
            }

            ObservationRequestStatus.UNAVAILABLE ->
                ObservationResult.create(
                    traceId = context.traceId,
                    status = ObservationStatus.DEFERRED,
                )

            ObservationRequestStatus.FAILED ->
                ObservationResult.create(
                    traceId = context.traceId,
                    status = ObservationStatus.FAILED,
                    error = requireNotNull(requestResult.error),
                )
        }
    }
}
