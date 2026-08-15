package com.devil.core.model.learning

import com.devil.core.model.worldmodel.WorldModelRepresentation

/**
 * Immutable Stage 92 representation of one bounded Evidence-Based Learning V2
 * preparation grounded in one existing evidence-backed WorldModelRepresentation.
 *
 * The exact WorldModelRepresentation remains attached so its constitutional
 * provenance is preserved rather than copied into unrelated learning claims.
 *
 * proposition preserves one explicitly supplied bounded statement describing
 * what may later be considered by the existing constitutional Learning
 * architecture.
 *
 * Creating this record does not establish that learning has occurred.
 *
 * It does not:
 *
 * - create another Devil intelligence;
 * - create another Brain;
 * - create another Constitution;
 * - create another Learning Authority;
 * - create another Memory Authority;
 * - reinterpret unsupported information as evidence;
 * - fabricate World Model evidence;
 * - mutate the World Model;
 * - establish a new constitutional Observation;
 * - establish Verification;
 * - establish Outcome;
 * - create a constitutional Decision;
 * - create a Task;
 * - create a Plan;
 * - grant authorization;
 * - select or activate a capability;
 * - create an ExecutionRequest;
 * - execute an action;
 * - perform Failure Learning;
 * - adapt strategy;
 * - authorize Controlled Autonomy;
 * - create a Memory Proposal;
 * - approve Memory;
 * - commit Memory;
 * - persist Memory;
 * - or persist learning state.
 *
 * WORLD_MODEL_REPRESENTATION != LEARNING.
 * EVIDENCE_BASED_LEARNING_RECORD != LEARNING_AUTHORITY_RESULT.
 * EVIDENCE != MEMORY.
 * LEARNING != MEMORY_PROPOSAL.
 * MEMORY_PROPOSAL != MEMORY_AUTHORITY_APPROVAL.
 * LEARNING != AUTHORITY.
 * LEARNING != DECISION.
 * LEARNING != EXECUTION.
 * EVIDENCE_BASED_LEARNING_V2 != FAILURE_LEARNING.
 * EVIDENCE_BASED_LEARNING_V2 != STRATEGY_ADAPTATION.
 * EVIDENCE_BASED_LEARNING_V2 != CONTROLLED_AUTONOMY.
 */
@ConsistentCopyVisibility
data class EvidenceBasedLearningRecord private constructor(
    val worldModelRepresentation: WorldModelRepresentation,
    val proposition: String,
) {
    companion object {

        fun create(
            worldModelRepresentation: WorldModelRepresentation,
            proposition: String,
        ): EvidenceBasedLearningRecord {
            val normalizedProposition =
                proposition.trim()

            require(normalizedProposition.isNotEmpty()) {
                "Evidence-based learning proposition must not be blank."
            }

            return EvidenceBasedLearningRecord(
                worldModelRepresentation =
                    worldModelRepresentation,
                proposition =
                    normalizedProposition,
            )
        }
    }
}
