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
 * Coordinates bounded constitutional logical-memory persistence evaluation
 * under the single Memory Authority.
 *
 * This coordinator may establish only whether one bounded
 * MemoryPersistenceRequest is eligible for a later explicitly authorized
 * persistence mechanism.
 *
 * It is not a second Memory Authority and grants itself no independent
 * memory authority.
 *
 * It does not create, persist, store, expose, recall, delete, or commit
 * logical memory.
 *
 * It does not assign or alter memory class, sensitivity, confidence,
 * retention policy, source attribution, owner-visible reason, storage
 * destination, deletion policy, encryption policy, replication policy,
 * or other logical-memory metadata.
 *
 * It does not invoke storage, mutate world state, change task or plan state,
 * communicate externally, bypass constitutional security review, bypass the
 * single Memory Authority, or absorb responsibilities belonging to earlier
 * constitutional authorities.
 */
interface MemoryPersistenceAuthority {

    fun evaluatePersistence(
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
        memoryCommitment: MemoryCommitmentResult,
    ): MemoryPersistenceResult
}
