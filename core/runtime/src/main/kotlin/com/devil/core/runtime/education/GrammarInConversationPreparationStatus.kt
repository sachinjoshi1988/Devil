package com.devil.core.runtime.education

/**
 * Stage 125 bounded Grammar-in-Conversation preparation status.
 *
 * PREPARED means one structurally valid grammar-practice context was prepared
 * from an existing Stage 122 conversation-practice context and one explicitly
 * supplied grammar target.
 *
 * PREPARED does not mean:
 *
 * - grammar instruction occurred;
 * - speech or text was parsed for grammar;
 * - grammatical errors were detected or corrected;
 * - grammar was scored;
 * - mastery or learner progress was verified;
 * - execution occurred;
 * - or Memory was committed.
 *
 * DEFERRED means no truthful Grammar-in-Conversation context was produced.
 */
enum class GrammarInConversationPreparationStatus {
    PREPARED,
    DEFERRED,
}
