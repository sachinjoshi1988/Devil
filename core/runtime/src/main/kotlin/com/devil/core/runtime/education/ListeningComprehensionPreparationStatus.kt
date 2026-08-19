package com.devil.core.runtime.education

/**
 * Stage 124 bounded Listening Comprehension preparation status.
 *
 * PREPARED means one structurally valid listening-comprehension practice context
 * was prepared from an existing Stage 122 conversation-practice context and one
 * explicitly supplied listening target.
 *
 * PREPARED does not mean:
 *
 * - audio was captured;
 * - speech was recognized or transcribed;
 * - the learner actually listened;
 * - comprehension was scored or verified;
 * - proficiency or learner progress was verified;
 * - execution occurred;
 * - or Memory was committed.
 *
 * DEFERRED means no truthful listening-comprehension context was produced.
 */
enum class ListeningComprehensionPreparationStatus {
    PREPARED,
    DEFERRED,
}
