package com.devil.core.model.learning

import com.devil.core.model.outcome.OutcomeState

/**
 * Immutable Stage 93 representation of one bounded Failure Learning preparation.
 *
 * Failure Learning is permitted only when the caller supplies:
 *
 * - one existing Stage 92 EvidenceBasedLearningRecord;
 * - one explicitly established OutcomeState.VERIFIED_FAILURE;
 * - and one nonblank bounded lesson.
 *
 * The exact Stage 92 record remains attached so its evidence-backed World Model
 * provenance is preserved.
 *
 * Stage 93 deliberately distinguishes:
 *
 * - OutcomeState.VERIFIED_FAILURE:
 *   a semantically verified failure of the intended outcome;
 *
 * from:
 *
 * - operational FAILED statuses in outcome, verification, learning, evidence,
 *   or other runtime pipelines.
 *
 * Operational failure does not establish that the attempted task itself failed.
 *
 * Creating this record does not:
 *
 * - create another Devil intelligence;
 * - create another Brain;
 * - create another Constitution;
 * - create another Learning Authority;
 * - create another Memory Authority;
 * - establish a new Outcome;
 * - establish new Verification;
 * - establish new Observation;
 * - reinterpret runtime operational failure as task failure;
 * - fabricate evidence;
 * - mutate World Model state;
 * - perform constitutional Learning by itself;
 * - create a Memory Proposal;
 * - invoke Memory Authority;
 * - commit or persist Memory;
 * - change a constitutional Decision;
 * - change a Task;
 * - change a Plan;
 * - adapt Planner strategy;
 * - authorize a capability;
 * - create an ExecutionRequest;
 * - execute an action;
 * - or grant Controlled Autonomy.
 *
 * OPERATIONAL_FAILURE != VERIFIED_OUTCOME_FAILURE.
 * VERIFIED_FAILURE != FAILURE_LEARNING.
 * FAILURE_LEARNING_RECORD != LEARNING_AUTHORITY_RESULT.
 * FAILURE_LEARNING != MEMORY_PROPOSAL.
 * FAILURE_LEARNING != STRATEGY_ADAPTATION.
 * FAILURE_LEARNING != CONTROLLED_AUTONOMY.
 */
@ConsistentCopyVisibility
data class FailureLearningRecord private constructor(
    val evidenceBasedLearning: EvidenceBasedLearningRecord,
    val outcomeState: OutcomeState,
    val lesson: String,
) {
    companion object {

        fun create(
            evidenceBasedLearning: EvidenceBasedLearningRecord,
            outcomeState: OutcomeState,
            lesson: String,
        ): FailureLearningRecord {
            val normalizedLesson =
                lesson.trim()

            require(
                outcomeState == OutcomeState.VERIFIED_FAILURE,
            ) {
                "Failure Learning requires an explicitly established VERIFIED_FAILURE outcome state."
            }

            require(normalizedLesson.isNotEmpty()) {
                "Failure Learning lesson must not be blank."
            }

            return FailureLearningRecord(
                evidenceBasedLearning =
                    evidenceBasedLearning,
                outcomeState =
                    outcomeState,
                lesson =
                    normalizedLesson,
            )
        }
    }
}
