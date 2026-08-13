package com.devil.core.runtime.verification

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

/**
 * Default Stage 14 constitutional Verification Authority coordinator.
 *
 * This authority prepares one bounded VerificationRequest, delegates
 * constitutional verification evaluation, and maps the evaluation into the
 * stable operational VerificationResult contract.
 *
 * It does not resolve identity, evaluate trust, grant authorization, produce
 * understanding, select decisions, create tasks or plans, select capabilities,
 * establish Executive readiness, approve execution, create observations,
 * fabricate verification evidence, update world state, report final success,
 * change task or plan state, or produce final outcomes.
 */
class DefaultVerificationAuthority(
    private val requestProvider: VerificationRequestProvider =
        DefaultVerificationRequestProvider(),
    private val evaluator: VerificationEvaluator =
        DefaultVerificationEvaluator(),
    private val resultMapper: VerificationResultMapper =
        DefaultVerificationResultMapper(),
) : VerificationAuthority {

    override fun verify(
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
        verificationEvidence: VerificationEvidenceResult,
    ): VerificationResult {
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

        require(verificationEvidence.traceId == context.traceId) {
            "Context and verification-evidence result must use the same trace identity."
        }
            "Context and observation result must use the same trace identity."

        }


        val requestResult = requestProvider.provide(
            observation = observation,
        )

        require(requestResult.traceId == context.traceId) {
            "Context and verification request result must use the same trace identity."
        }

        return when (requestResult.status) {
            VerificationRequestStatus.AVAILABLE -> {
                val evaluation = evaluator.evaluate(
                    traceId = context.traceId,
                    request = requireNotNull(requestResult.request),
                    evidence = verificationEvidence,
                )

                require(evaluation.traceId == context.traceId) {
                    "Context and verification evaluation result must use the same trace identity."
                }

                val result = resultMapper.map(
                    traceId = context.traceId,
                    evaluation = evaluation,
                )

                require(result.traceId == context.traceId) {
                    "Context and mapped verification result must use the same trace identity."
                }

                result
            }

            VerificationRequestStatus.UNAVAILABLE ->
                VerificationResult.create(
                    traceId = context.traceId,
                    status = VerificationStatus.DEFERRED,
                )

            VerificationRequestStatus.FAILED ->
                VerificationResult.create(
                    traceId = context.traceId,
                    status = VerificationStatus.FAILED,
                    error = requireNotNull(requestResult.error),
                )
        }
    }
}
