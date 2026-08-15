package com.devil.core.runtime.learning

import com.devil.core.model.common.TraceId
import com.devil.core.model.learning.EvidenceBasedLearningRecord
import com.devil.core.model.learning.FailureLearningRecord
import com.devil.core.model.outcome.OutcomeState

/**
 * Stage 93 bounded Failure Learning Foundation coordinator.
 *
 * This coordinator accepts:
 *
 * - one constitutional TraceId;
 * - one existing Stage 92 EvidenceBasedLearningRecord;
 * - one explicitly supplied established OutcomeState;
 * - and one explicitly supplied bounded lesson.
 *
 * Preparation is permitted only for OutcomeState.VERIFIED_FAILURE.
 *
 * This distinction is constitutional:
 *
 * OutcomeState.VERIFIED_FAILURE describes a verified result of the attempted
 * task or action.
 *
 * Runtime statuses such as OutcomeStatus.FAILED,
 * VerificationStatus.FAILED, LearningStatus.FAILED, or evidence-port FAILED
 * states describe operational pipeline failures and must not be converted into
 * Failure Learning.
 *
 * This coordinator does not:
 *
 * - create another Devil intelligence;
 * - create another Brain;
 * - create another Constitution;
 * - create another Learning Authority;
 * - replace the existing Learning Authority;
 * - create another Memory Authority;
 * - establish OutcomeState;
 * - convert operational failure into VERIFIED_FAILURE;
 * - fabricate evidence;
 * - establish Observation;
 * - establish Verification;
 * - establish Outcome;
 * - mutate World Model state;
 * - claim constitutional Learning occurred;
 * - create a LearningRequest;
 * - create a Memory Proposal;
 * - invoke Memory Authority;
 * - commit or persist Memory;
 * - alter a constitutional Decision;
 * - create or change Tasks;
 * - create or change Plans;
 * - adapt Planner strategy;
 * - grant authorization;
 * - create ExecutionRequests;
 * - execute actions;
 * - or grant Controlled Autonomy.
 *
 * OPERATIONAL_FAILURE != VERIFIED_OUTCOME_FAILURE.
 * VERIFIED_FAILURE != FAILURE_LEARNING.
 * FAILURE_LEARNING_PREPARATION != STRATEGY_ADAPTATION.
 * FAILURE_LEARNING != MEMORY_PROPOSAL.
 * FAILURE_LEARNING != CONTROLLED_AUTONOMY.
 */
class FailureLearningCoordinator {

    fun prepare(
        traceId: TraceId,
        evidenceBasedLearning: EvidenceBasedLearningRecord,
        outcomeState: OutcomeState,
        lesson: String,
    ): FailureLearningPreparationResult {
        if (
            evidenceBasedLearning
                .worldModelRepresentation
                .traceId != traceId ||
            outcomeState != OutcomeState.VERIFIED_FAILURE ||
            lesson.isBlank()
        ) {
            return deferred(
                traceId = traceId,
            )
        }

        val record =
            FailureLearningRecord.create(
                evidenceBasedLearning =
                    evidenceBasedLearning,
                outcomeState =
                    outcomeState,
                lesson =
                    lesson,
            )

        return FailureLearningPreparationResult.create(
            traceId = traceId,
            status =
                FailureLearningPreparationStatus.PREPARED,
            record = record,
        )
    }

    private fun deferred(
        traceId: TraceId,
    ): FailureLearningPreparationResult {
        return FailureLearningPreparationResult.create(
            traceId = traceId,
            status =
                FailureLearningPreparationStatus.DEFERRED,
        )
    }
}
