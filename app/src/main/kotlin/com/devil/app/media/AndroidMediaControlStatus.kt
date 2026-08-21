package com.devil.app.media

/**
 * Stage 186 bounded Media Control status.
 *
 * READY means one explicit bounded media-control command has been prepared.
 *
 * DEFERRED means no bounded media-control command was prepared.
 *
 * MEDIA_CONTROL_READY != MEDIA_ACTION_ATTEMPTED.
 * MEDIA_ACTION_ATTEMPTED != PLAYBACK_CHANGED.
 */
enum class AndroidMediaControlStatus {
    READY,
    DEFERRED,
}
