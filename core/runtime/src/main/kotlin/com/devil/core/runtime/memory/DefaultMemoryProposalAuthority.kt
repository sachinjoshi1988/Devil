package com.devil.core.runtime.memory

import com.devil.core.model.context.ContextEnvelope
import com.devil.core.model.memory.MemoryProposalRequest
import com.devil.core.runtime.authorization.AuthorizationResult
import com.devil.core.runtime.capability.CapabilitySelectionResult
import com.devil.core.runtime.decision.DecisionAuthorityResult
import com.devil.core.runtime.executive.ExecutiveReadinessResult
import com.devil.core.runtime.execution.ExecutionResult
import com.devil.core.runtime.identity.IdentityResult
import com.devil.core.runtime.learning.LearningResult
import com.devil.core.runtime.learning.LearningStatus
import com.devil.core.runtime.observation.ObservationResult
import com.devil.core.runtime.outcome.OutcomeResult
import com.devil.core.runtime.plan.PlanAuthorityResult
import com.devil.core.runtime.task.TaskAuthorityResult
import com.devil.core.runtime.trust.TrustResult
import com.devil.core.runtime.understanding.UnderstandingAuthorityResult
import com.devil.core.runtime.verification.VerificationResult
import com.devil.core.runtime.worldmodel.WorldModelUpdateResult

/**
 * Default constitutional Memory Proposal Authority coordinator.
 *
 * This authority selects exactly one bounded MemoryProposalRequest, delegates
 * constitutional Memory Proposal evaluation, and maps that evaluation into the
 * stable operational MemoryProposalResult contract.
 *
 * A supplied prepared request is accepted only when constitutional Learning is
 * LEARNABLE and the request preserves the exact LearningRequest carried by that
 * LearningResult.
 *
 * Otherwise the existing MemoryProposalRequestProvider remains the request
 * source.
 *
 * A prepared request therefore changes only request preparation. It grants no
 * Memory Proposal authority of its own and cannot bypass proposal evaluation,
 * Memory Authority, Memory Commitment, or Memory Persistence.
 *
 * This authority does not resolve identity, evaluate trust, grant
 * authorization, produce understanding, select decisions, create tasks or
 * plans, select capabilities, establish readiness, approve execution, create
 * observations, establish verification, outcome, World Model update, or
 * learning evidence, approve logical memory, commit logical memory, persist
 * logical memory, mutate world state, or communicate externally.
 */
class DefaultMemoryProposalAuthority(
    private val requestProvider: MemoryProposalRequestProvider =
        DefaultMemoryProposalRequestProvider(),
    private val evaluator: MemoryProposalEvaluator =
        DefaultMemoryProposalEvaluator(),
    private val resultMapper: MemoryProposalResultMapper =
        DefaultMemoryProposalResultMapper(),
) : MemoryProposalAuthority {

    override fun evaluateProposal(
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
        memoryProposalEvidence: MemoryProposalEvidenceResult,
        preparedRequest: MemoryProposalRequest?,
    ): MemoryProposalResult {
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

        require(worldModelUpdate.traceId == context.traceId) {
            "Context and World Model update result must use the same trace identity."
        }

        require(learning.traceId == context.traceId) {
            "Context and learning result must use the same trace identity."
        }

        require(memoryProposalEvidence.traceId == context.traceId) {
            "Context and Memory Proposal evidence result must use the same trace identity."
        }

        val requestResult =
            if (preparedRequest != null) {
                require(learning.status == LearningStatus.LEARNABLE) {
                    "A prepared Memory Proposal request requires constitutionally learnable Learning."
                }

                val learningRequest =
                    requireNotNull(learning.request) {
                        "Learnable Learning must preserve its bounded Learning request."
                    }

                require(preparedRequest.learning == learningRequest) {
                    "Prepared Memory Proposal request must preserve the exact constitutional Learning request."
                }

                MemoryProposalRequestResult.create(
                    traceId = context.traceId,
                    status = MemoryProposalRequestStatus.AVAILABLE,
                    request = preparedRequest,
                )
            } else {
                requestProvider.provide(
                    learning = learning,
                )
            }

        require(requestResult.traceId == context.traceId) {
            "Context and memory proposal request result must use the same trace identity."
        }

        return when (requestResult.status) {
            MemoryProposalRequestStatus.AVAILABLE -> {
                val evaluation =
                    evaluator.evaluate(
                        traceId = context.traceId,
                        request = requireNotNull(requestResult.request),
                        evidence = memoryProposalEvidence,
                    )

                require(evaluation.traceId == context.traceId) {
                    "Context and memory proposal evaluation result must use the same trace identity."
                }

                val result =
                    resultMapper.map(
                        traceId = context.traceId,
                        evaluation = evaluation,
                    )

                require(result.traceId == context.traceId) {
                    "Context and mapped memory proposal result must use the same trace identity."
                }

                result
            }

            MemoryProposalRequestStatus.UNAVAILABLE ->
                MemoryProposalResult.create(
                    traceId = context.traceId,
                    status = MemoryProposalStatus.DEFERRED,
                )

            MemoryProposalRequestStatus.FAILED ->
                MemoryProposalResult.create(
                    traceId = context.traceId,
                    status = MemoryProposalStatus.FAILED,
                    error = requireNotNull(requestResult.error),
                )
        }
    }
}
