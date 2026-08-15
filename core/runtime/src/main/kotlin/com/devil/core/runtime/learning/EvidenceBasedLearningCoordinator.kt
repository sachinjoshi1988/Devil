package com.devil.core.runtime.learning

import com.devil.core.model.common.TraceId
import com.devil.core.model.learning.EvidenceBasedLearningRecord
import com.devil.core.model.worldmodel.WorldModelRepresentation

/**
 * Stage 92 bounded Evidence-Based Learning V2 Foundation coordinator.
 *
 * This coordinator accepts:
 *
 * - one constitutional TraceId;
 * - one existing evidence-backed WorldModelRepresentation;
 * - and one explicitly supplied bounded learning proposition.
 *
 * It preserves the exact WorldModelRepresentation instead of duplicating or
 * weakening its evidence provenance.
 *
 * It does not:
 *
 * - create another Devil intelligence;
 * - create another Brain;
 * - create another Constitution;
 * - create another Learning Authority;
 * - replace the existing Learning Authority;
 * - create another Memory Authority;
 * - fabricate World Model evidence;
 * - reinterpret unsupported text as evidence;
 * - mutate the supplied WorldModelRepresentation;
 * - mutate World Model state;
 * - establish Observation;
 * - establish Verification;
 * - establish Outcome;
 * - create a LearningRequest;
 * - claim constitutional Learning occurred;
 * - perform Failure Learning;
 * - classify an outcome as a reusable failure lesson;
 * - adapt Planner strategy;
 * - alter a constitutional Decision;
 * - create Tasks or Plans;
 * - grant authorization;
 * - create ExecutionRequests;
 * - execute actions;
 * - grant Controlled Autonomy;
 * - create Memory Proposal;
 * - invoke Memory Authority;
 * - commit or persist Memory;
 * - or persist learning state.
 *
 * WORLD_MODEL_REPRESENTATION != LEARNING.
 * EVIDENCE_BASED_LEARNING_PREPARATION != LEARNING.
 * LEARNING != MEMORY_PROPOSAL.
 * MEMORY_PROPOSAL != MEMORY_AUTHORITY_APPROVAL.
 * EVIDENCE_BASED_LEARNING_V2 != FAILURE_LEARNING.
 * EVIDENCE_BASED_LEARNING_V2 != STRATEGY_ADAPTATION.
 * EVIDENCE_BASED_LEARNING_V2 != CONTROLLED_AUTONOMY.
 */
class EvidenceBasedLearningCoordinator {

    fun prepare(
        traceId: TraceId,
        worldModelRepresentation: WorldModelRepresentation,
        proposition: String,
    ): EvidenceBasedLearningPreparationResult {
        if (
            worldModelRepresentation.traceId != traceId ||
            proposition.isBlank()
        ) {
            return deferred(
                traceId = traceId,
            )
        }

        val record =
            EvidenceBasedLearningRecord.create(
                worldModelRepresentation =
                    worldModelRepresentation,
                proposition =
                    proposition,
            )

        return EvidenceBasedLearningPreparationResult.create(
            traceId = traceId,
            status =
                EvidenceBasedLearningPreparationStatus.PREPARED,
            record = record,
        )
    }

    private fun deferred(
        traceId: TraceId,
    ): EvidenceBasedLearningPreparationResult {
        return EvidenceBasedLearningPreparationResult.create(
            traceId = traceId,
            status =
                EvidenceBasedLearningPreparationStatus.DEFERRED,
        )
    }
}
