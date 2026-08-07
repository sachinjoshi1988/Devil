package com.devil.core.runtime.memory

import com.devil.core.model.context.ContextEnvelope
import com.devil.core.runtime.authorization.AuthorizationResult
import com.devil.core.runtime.capability.CapabilitySelectionResult
import com.devil.core.runtime.decision.DecisionAuthorityResult
import com.devil.core.runtime.executive.ExecutiveReadinessResult
import com.devil.core.runtime.execution.ExecutionResult
import com.devil.core.runtime.identity.IdentityResult
import com.devil.core.runtime.learning.LearningResult
import com.devil.core.runtime.observation.ObservationResult
import com.devil.core.runtime.outcome.OutcomeResult
import com.devil.core.runtime.plan.PlanAuthorityResult
import com.devil.core.runtime.task.TaskAuthorityResult
import com.devil.core.runtime.trust.TrustResult
import com.devil.core.runtime.understanding.UnderstandingAuthorityResult
import com.devil.core.runtime.verification.VerificationResult
import com.devil.core.runtime.worldmodel.WorldModelUpdateResult

/**
 * Default Stage 20 constitutional logical-memory commitment coordinator.
 *
 * This authority prepares one bounded MemoryCommitmentRequest, delegates
 * constitutional commitment evaluation, and maps that evaluation into the
 * stable operational MemoryCommitmentResult contract.
 *
 * It does not create, persist, store, expose, recall, or commit logical memory.
 * It does not assign memory class, sensitivity, confidence, retention policy,
 * source attribution, owner-visible reason, storage destination, or deletion
 * policy.
 *
 * It does not invoke storage, mutate world state, change task or plan state,
 * communicate externally, bypass the single Memory Authority, or absorb
 * responsibilities belonging to earlier constitutional authorities.
 */
class DefaultMemoryCommitmentAuthority(
    private val requestProvider: MemoryCommitmentRequestProvider =
        DefaultMemoryCommitmentRequestProvider(),
    private val evaluator: MemoryCommitmentEvaluator =
        DefaultMemoryCommitmentEvaluator(),
    private val resultMapper: MemoryCommitmentResultMapper =
        DefaultMemoryCommitmentResultMapper(),
) : MemoryCommitmentAuthority {

    override fun evaluateCommitment(
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
        learning: LearningResult,
        memoryProposal: MemoryProposalResult,
        memory: MemoryAuthorityResult,
    ): MemoryCommitmentResult {
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
            "Context and capability result must use the same trace identity."
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

        require(worldModelUpdate.traceId == context.traceId) {
            "Context and World Model update result must use the same trace identity."
        }

        require(learning.traceId == context.traceId) {
            "Context and learning result must use the same trace identity."
        }

        require(memoryProposal.traceId == context.traceId) {
            "Context and memory proposal result must use the same trace identity."
        }

        require(memory.traceId == context.traceId) {
            "Context and Memory Authority result must use the same trace identity."
        }

        val requestResult = requestProvider.provide(
            memory = memory,
        )

        require(requestResult.traceId == context.traceId) {
            "Context and memory commitment request result must use the same trace identity."
        }

        return when (requestResult.status) {
            MemoryCommitmentRequestStatus.AVAILABLE -> {
                val evaluation = evaluator.evaluate(
                    traceId = context.traceId,
                    request = requireNotNull(requestResult.request),
                )

                require(evaluation.traceId == context.traceId) {
                    "Context and memory commitment evaluation result must use the same trace identity."
                }

                val result = resultMapper.map(
                    traceId = context.traceId,
                    evaluation = evaluation,
                )

                require(result.traceId == context.traceId) {
                    "Context and mapped memory commitment result must use the same trace identity."
                }

                result
            }

            MemoryCommitmentRequestStatus.UNAVAILABLE ->
                MemoryCommitmentResult.create(
                    traceId = context.traceId,
                    status = MemoryCommitmentStatus.DEFERRED,
                )

            MemoryCommitmentRequestStatus.FAILED ->
                MemoryCommitmentResult.create(
                    traceId = context.traceId,
                    status = MemoryCommitmentStatus.FAILED,
                    error = requireNotNull(requestResult.error),
                )
        }
    }
}
