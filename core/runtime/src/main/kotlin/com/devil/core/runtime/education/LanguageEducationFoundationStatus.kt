package com.devil.core.runtime.education

/**
 * Stage 120 bounded Language Education foundation preparation status.
 *
 * PREPARED means a structurally valid LanguageEducationSessionRecord was
 * created from an existing bounded education session and an explicitly
 * supplied target language.
 *
 * PREPARED does not mean instruction occurred, curriculum exists, conversation
 * practice occurred, pronunciation was evaluated, mastery was assessed,
 * progress was verified, authorization exists, execution occurred, or Memory
 * was committed.
 *
 * DEFERRED means no truthful Language Education foundation record was created.
 */
enum class LanguageEducationFoundationStatus {
    PREPARED,
    DEFERRED,
}
