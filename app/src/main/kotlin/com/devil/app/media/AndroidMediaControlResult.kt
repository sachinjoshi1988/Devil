package com.devil.app.media

/**
 * Stage 186 bounded Media Control result.
 *
 * READY contains exactly one explicitly supplied media-control command.
 *
 * DEFERRED contains no command.
 *
 * This result does not:
 *
 * - select or control an Android MediaSession;
 * - grant Devil authorization;
 * - establish constitutional Execution APPROVED;
 * - attempt a media action;
 * - establish that playback changed;
 * - establish Observation, Verification, or Outcome.
 *
 * MEDIA_CONTROL_READY != MEDIA_ACTION_ATTEMPTED.
 * MEDIA_ACTION_ATTEMPTED != PLAYBACK_CHANGED.
 * PLAYBACK_CHANGED != VERIFIED_OUTCOME.
 */
@ConsistentCopyVisibility
data class AndroidMediaControlResult private constructor(
    val status: AndroidMediaControlStatus,
    val command: AndroidMediaControlCommand?,
) {
    companion object {
        fun create(
            status: AndroidMediaControlStatus,
            command: AndroidMediaControlCommand? = null,
        ): AndroidMediaControlResult {
            when (status) {
                AndroidMediaControlStatus.READY ->
                    require(command != null) {
                        "Ready Android media control requires one command."
                    }

                AndroidMediaControlStatus.DEFERRED ->
                    require(command == null) {
                        "Deferred Android media control must not contain a command."
                    }
            }

            return AndroidMediaControlResult(
                status = status,
                command = command,
            )
        }
    }
}
