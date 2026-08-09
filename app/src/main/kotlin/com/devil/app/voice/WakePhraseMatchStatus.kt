package com.devil.app.voice

/**
 * Describes whether one bounded recognized transcript matches an approved
 * Stage 37 wake phrase.
 *
 * MATCHED establishes attention only.
 *
 * MATCHED does not authenticate a speaker, prove owner identity, establish
 * subject trust, create a session, enter Owner Mode, grant authorization,
 * permit execution, or establish task success.
 */
enum class WakePhraseMatchStatus {
    MATCHED,
    NOT_MATCHED,
}
