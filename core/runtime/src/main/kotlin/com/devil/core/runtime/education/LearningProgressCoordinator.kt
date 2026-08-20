package com.devil.core.runtime.education

import com.devil.core.model.common.TraceId
import com.devil.core.model.education.LearningProgressRecord
import com.devil.core.model.education.StudyCompanionRecord

/**
 * Stage 149 bounded Learning Progress coordinator.
 *
 * This coordinator prepares one Education Domain Learning Progress context
 * from an existing Stage 148 Study Companion context and explicitly supplied
 * progress evidence metadata.
 *
 * Stage 148 remains authoritative for preserved Study Companion provenance.
 *
 * This coordinator does not:
 *
 * - observe or invent learner evidence;
 * - calculate or infer a score;
 * - verify mastery or global proficiency;
 * - perform constitutional Observation or Verification;
 * - establish a verified Outcome;
 * - authenticate a child or guardian;
 * - establish guardian authority or approval;
 * - replace child/guardian policy;
 * - replace privacy policy;
 * - create Tasks or Plans;
 * - invoke Executive or execution;
 * - perform constitutional Learning;
 * - create or persist Memory;
 * - invoke external providers;
 * - communicate with Android or platform APIs;
 * - or implement Stage 150 Guardian Learning Summary.
 *
 * LEARNING_PROGRESS != VERIFIED_MASTERY.
 * LEARNING_PROGRESS != CONSTITUTIONAL_VERIFICATION.
 * LEARNING_PROGRESS != GLOBAL_PROFICIENCY.
 * LEARNING_PROGRESS != MEMORY_PERSISTENCE.
 */
class LearningProgressCoordinator {

    fun prepare(
        traceId: TraceId,
        studyCompanion: StudyCompanionRecord,
        progressFocus: String,
        learnerEvidenceDescription: String,
        progressInterpretation: String,
    ): LearningProgressPreparationResult {
        if (
            progressFocus.isBlank() ||
            learnerEvidenceDescription.isBlank() ||
            progressInterpretation.isBlank()
        ) {
            return deferred(
                traceId = traceId,
            )
        }

        val learningProgress =
            LearningProgressRecord.create(
                studyCompanion = studyCompanion,
                progressFocus = progressFocus,
                learnerEvidenceDescription = learnerEvidenceDescription,
                progressInterpretation = progressInterpretation,
            )

        return LearningProgressPreparationResult.create(
            traceId = traceId,
            status = LearningProgressPreparationStatus.PREPARED,
            learningProgress = learningProgress,
        )
    }

    private fun deferred(
        traceId: TraceId,
    ): LearningProgressPreparationResult {
        return LearningProgressPreparationResult.create(
            traceId = traceId,
            status = LearningProgressPreparationStatus.DEFERRED,
        )
    }
}
