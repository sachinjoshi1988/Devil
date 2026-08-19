package com.devil.core.runtime.education

import com.devil.core.model.common.TraceId
import com.devil.core.model.education.ListeningComprehensionPracticeRecord
import com.devil.core.model.education.SpokenEnglishConversationPracticeRecord

/**
 * Stage 124 bounded Listening Comprehension coordinator.
 *
 * This coordinator prepares one Education Domain listening-comprehension
 * context from an existing Stage 122 Spoken English conversation-practice
 * context and one explicitly supplied listening target.
 *
 * Stage 123 Pronunciation Intelligence is not a required predecessor.
 *
 * It does not:
 *
 * - capture microphone or audio input;
 * - invoke speech recognition or transcription;
 * - decode or inspect audio;
 * - score or verify comprehension;
 * - grade learner answers;
 * - assess proficiency or learner progress;
 * - evaluate pronunciation;
 * - teach or assess grammar;
 * - create another intelligence, Brain, Executive, Planner, Memory Authority,
 *   or Security Authority;
 * - create Tasks or Plans;
 * - invoke execution;
 * - establish Observation, Verification, or Outcome;
 * - perform constitutional Learning;
 * - create or persist Memory;
 * - or communicate with Android, microphone, speech, or audio APIs.
 *
 * LISTENING_COMPREHENSION != SPEECH_RECOGNITION.
 * PREPARED != LISTENED.
 * PREPARED != COMPREHENSION_VERIFIED.
 */
class ListeningComprehensionCoordinator {

    fun prepare(
        traceId: TraceId,
        conversationPractice: SpokenEnglishConversationPracticeRecord,
        listeningTarget: String,
    ): ListeningComprehensionPreparationResult {
        if (listeningTarget.isBlank()) {
            return deferred(
                traceId = traceId,
            )
        }

        val practice =
            ListeningComprehensionPracticeRecord.create(
                conversationPractice = conversationPractice,
                listeningTarget = listeningTarget,
            )

        return ListeningComprehensionPreparationResult.create(
            traceId = traceId,
            status = ListeningComprehensionPreparationStatus.PREPARED,
            practice = practice,
        )
    }

    private fun deferred(
        traceId: TraceId,
    ): ListeningComprehensionPreparationResult {
        return ListeningComprehensionPreparationResult.create(
            traceId = traceId,
            status = ListeningComprehensionPreparationStatus.DEFERRED,
        )
    }
}
