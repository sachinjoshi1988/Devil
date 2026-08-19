package com.devil.core.runtime.education

/**
 * Stage 122 bounded Spoken English conversation-practice preparation status.
 *
 * PREPARED means one structurally valid educational conversation-practice
 * context was prepared from an existing Stage 121 beginner session and one
 * explicitly supplied nonblank topic.
 *
 * PREPARED does not mean:
 *
 * - a Conversation Domain record was created;
 * - conversation practice occurred;
 * - a conversation was completed;
 * - pronunciation or listening was assessed;
 * - grammar was evaluated;
 * - proficiency or learner progress was verified;
 * - execution occurred;
 * - or Memory was committed.
 *
 * DEFERRED means no truthful conversation-practice context was produced.
 */
enum class SpokenEnglishConversationPreparationStatus {
    PREPARED,
    DEFERRED,
}
