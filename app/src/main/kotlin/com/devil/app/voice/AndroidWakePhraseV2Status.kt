package com.devil.app.voice

/**
 * Stage 196 bounded Wake Phrase V2 status.
 *
 * MATCHED establishes attention only.
 *
 * MATCHED != AUTHENTICATED.
 * MATCHED != ACTIVE_SESSION.
 */
enum class AndroidWakePhraseV2Status {
    MATCHED,
    NOT_MATCHED,
}
