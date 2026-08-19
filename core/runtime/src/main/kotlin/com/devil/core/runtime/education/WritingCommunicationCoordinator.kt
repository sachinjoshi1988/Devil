package com.devil.core.runtime.education

import com.devil.core.model.common.TraceId
import com.devil.core.model.education.LanguageEducationSessionRecord
import com.devil.core.model.education.WritingCommunicationPracticeRecord

/**
 * Stage 127 bounded Writing & Communication coordinator.
 *
 * This coordinator prepares one Education Domain writing/communication context
 * directly from an existing Stage 120 Language Education session plus one
 * explicitly supplied writing target and communication purpose.
 *
 * Stage 126 Reading & Vocabulary is not a required predecessor.
 *
 * It does not:
 *
 * - send email, text, chat, or any external communication;
 * - create external communication authorization;
 * - create or submit Tasks or Plans;
 * - invoke execution;
 * - score or verify writing quality;
 * - assess proficiency or learner progress;
 * - create confidence, academic, or professional-English coaching;
 * - invoke communication providers or Android/platform APIs;
 * - create another intelligence, Brain, Executive, Planner, Memory Authority,
 *   or Security Authority;
 * - establish Observation, Verification, or Outcome;
 * - perform constitutional Learning;
 * - create or persist Memory.
 *
 * WRITING_COMMUNICATION_EDUCATION != EXTERNAL_COMMUNICATION.
 * PREPARED != WRITTEN.
 * PREPARED != SENT.
 * PREPARED != QUALITY_VERIFIED.
 */
class WritingCommunicationCoordinator {

    fun prepare(
        traceId: TraceId,
        languageEducationSession: LanguageEducationSessionRecord,
        writingTarget: String,
        communicationPurpose: String,
    ): WritingCommunicationPreparationResult {
        if (writingTarget.isBlank() || communicationPurpose.isBlank()) {
            return deferred(
                traceId = traceId,
            )
        }

        val practice =
            WritingCommunicationPracticeRecord.create(
                languageEducationSession = languageEducationSession,
                writingTarget = writingTarget,
                communicationPurpose = communicationPurpose,
            )

        return WritingCommunicationPreparationResult.create(
            traceId = traceId,
            status = WritingCommunicationPreparationStatus.PREPARED,
            practice = practice,
        )
    }

    private fun deferred(
        traceId: TraceId,
    ): WritingCommunicationPreparationResult {
        return WritingCommunicationPreparationResult.create(
            traceId = traceId,
            status = WritingCommunicationPreparationStatus.DEFERRED,
        )
    }
}
