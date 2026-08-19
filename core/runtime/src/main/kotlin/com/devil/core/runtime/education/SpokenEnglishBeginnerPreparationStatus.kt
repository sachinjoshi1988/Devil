package com.devil.core.runtime.education

/**
 * Stage 121 bounded Spoken English Beginner preparation status.
 *
 * PREPARED means one structurally valid beginner Spoken English context was
 * prepared from an existing Stage 120 Language Education session targeting
 * English.
 *
 * PREPARED does not mean:
 *
 * - the learner was assessed as a beginner;
 * - Spoken English instruction occurred;
 * - conversation practice occurred;
 * - pronunciation or listening was evaluated;
 * - proficiency or progress was verified;
 * - authorization exists;
 * - execution occurred;
 * - or Memory was committed.
 *
 * DEFERRED means no truthful Spoken English Beginner context was produced.
 */
enum class SpokenEnglishBeginnerPreparationStatus {
    PREPARED,
    DEFERRED,
}
