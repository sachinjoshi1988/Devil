package com.devil.core.runtime.education

/**
 * Stage 123 bounded Pronunciation Intelligence preparation status.
 *
 * PREPARED means one structurally valid pronunciation-practice context was
 * prepared from an existing Stage 122 conversation-practice context and one
 * explicitly supplied pronunciation target.
 *
 * PREPARED does not mean:
 *
 * - audio was captured;
 * - speech was recognized;
 * - phonemes were extracted;
 * - pronunciation was scored;
 * - pronunciation was verified;
 * - proficiency or learner progress was verified;
 * - execution occurred;
 * - or Memory was committed.
 *
 * DEFERRED means no truthful pronunciation-practice context was produced.
 */
enum class PronunciationIntelligencePreparationStatus {
    PREPARED,
    DEFERRED,
}
