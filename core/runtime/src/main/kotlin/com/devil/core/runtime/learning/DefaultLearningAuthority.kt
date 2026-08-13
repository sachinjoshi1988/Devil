package com.devil.core.runtime.learning

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
import com.devil.core.runtime.worldmodel.WorldModelUpdateResult

/**
 * Default Stage 17 constitutional Learning Authority coordinator.
 *
 * This authority prepares one bounded LearningRequest, delegates constitutional
 * learning evaluation, and maps that evaluation into the stable operational
 * LearningResult contract.
 *
 * It does not resolve identity, evaluate trust, grant authorization, produce
 * understanding, select decisions, create tasks or plans, select capabilities,
 * establish Executive readiness, approve execution, create observations,
 * establish verification, outcome, or World Model update evidence, create
 * learning, create or commit memory, mutate world state, or communicate
 * externally.
 */
class DefaultLearningAuthority(
    private val requestProvider: LearningRequestProvider =
        DefaultLearningRequestProvider(),
    private val evaluator: LearningEvaluator =
        DefaultLearningEvaluator(),
    private val resultMapper: LearningResultMapper =
        DefaultLearningResultMapper(),
) : LearningAuthority {

    override fun evaluateLearning(
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
        worldModelUpdate: WorldModelUpdateResult,
        learningEvidence: LearningEvidenceResult,
    ): LearningResult {
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

        require(learningEvidence.traceId == context.traceId) {
            "Context and Learning-evidence result must use the same trace identity."
        }

        require(worldModelUpdate.traceId == context.traceId) {
            "Context and World Model update result must use the same trace identity."
        }

        val requestResult = requestProvider.provide(
            worldModelUpdate = worldModelUpdate,
        )

        require(requestResult.traceId == context.traceId) {
            "Context and learning request result must use the same trace identity."
        }

        return when (requestResult.status) {
            LearningRequestStatus.AVAILABLE -> {
                val evaluation = evaluator.evaluate(
                    traceId = context.traceId,
                    request = requireNotNull(requestResult.request),
                    evidence = learningEvidence,
                )

                require(evaluation.traceId == context.traceId) {
                    "Context and learning evaluation result must use the same trace identity."
                }

                val result = resultMapper.map(
                    traceId = context.traceId,
                    evaluation = evaluation,
                )

                require(result.traceId == context.traceId) {
                    "Context and mapped learning result must use the same trace identity."
                }

                result
            }

            LearningRequestStatus.UNAVAILABLE ->
                LearningResult.create(
                    traceId = context.traceId,
                    status = LearningStatus.DEFERRED,
                )

            LearningRequestStatus.FAILED ->
                LearningResult.create(
                    traceId = context.traceId,
                    status = LearningStatus.FAILED,
                    error = requireNotNull(requestResult.error),
                )
        }
    }
}
