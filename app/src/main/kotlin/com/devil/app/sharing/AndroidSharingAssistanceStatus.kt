package com.devil.app.sharing

/**
 * Stage 189 bounded Clipboard & Sharing assistance status.
 *
 * READY means explicitly supplied text was prepared only.
 * DEFERRED means no sharing payload was prepared.
 */
enum class AndroidSharingAssistanceStatus {
    READY,
    DEFERRED,
}
