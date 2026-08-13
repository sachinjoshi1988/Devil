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
 * Constitutional authority for bounded Learning evaluation.
 *
 * The authority may evaluate Learning only after the preceding constitutional
 * chain has produced one WorldModelUpdateResult and one genuine bounded
 * LearningEvidenceResult.
 *
 * It does not create Learning evidence, mutate world state, create or approve
 * Memory, commit Memory, persist Memory, change task or plan state, communicate
 * externally, or bypass the single Unified Devil Runtime.
 *
 * WORLD_MODEL_UPDATE != LEARNING_EVIDENCE.
 * LEARNING_EVIDENCE != LEARNING.
 * LEARNING != MEMORY_PROPOSAL.
 */
interface LearningAuthority {

    fun evaluateLearning(
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
    ): LearningResult
}
