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
 * Coordinates bounded constitutional logical-memory commitment evaluation under
 * the single Memory Authority.
 *
 * This authority may establish only whether one bounded MemoryCommitmentRequest
 * is eligible for a later controlled persistence mechanism.
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
interface MemoryCommitmentAuthority {

    fun evaluateCommitment(
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
    ): MemoryCommitmentResult
}
