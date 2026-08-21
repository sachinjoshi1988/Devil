package com.devil.app.media

/**
 * Stage 186 bounded media-control command.
 *
 * This command represents only an explicitly supplied media-control intention.
 *
 * It does not establish execution approval or prove that playback changed.
 */
enum class AndroidMediaControlCommand {
    PLAY,
    PAUSE,
    NEXT,
    PREVIOUS,
}
