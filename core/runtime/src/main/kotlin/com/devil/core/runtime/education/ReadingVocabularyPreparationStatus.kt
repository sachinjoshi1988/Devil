package com.devil.core.runtime.education

/**
 * Stage 126 bounded Reading & Vocabulary Development preparation status.
 *
 * PREPARED means one structurally valid reading/vocabulary practice context
 * was prepared from an existing Stage 120 Language Education session and
 * explicitly supplied reading and vocabulary targets.
 *
 * PREPARED does not mean:
 *
 * - reading occurred;
 * - comprehension was scored or verified;
 * - vocabulary was taught or mastered;
 * - external dictionary or knowledge lookup occurred;
 * - proficiency or learner progress was verified;
 * - execution occurred;
 * - or Memory was committed.
 *
 * DEFERRED means no truthful reading/vocabulary context was produced.
 */
enum class ReadingVocabularyPreparationStatus {
    PREPARED,
    DEFERRED,
}
