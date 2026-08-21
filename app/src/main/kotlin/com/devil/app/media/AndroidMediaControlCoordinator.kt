package com.devil.app.media

/**
 * Stage 186 bounded Media Control coordinator.
 *
 * This coordinator accepts only an explicitly supplied media-control command.
 *
 * A non-null command becomes READY.
 *
 * Absence of a command remains DEFERRED.
 *
 * It does not:
 *
 * - inspect or select an Android MediaSession;
 * - call MediaController;
 * - call AudioManager;
 * - dispatch media key events;
 * - grant Devil authorization;
 * - establish constitutional Execution APPROVED;
 * - change playback;
 * - establish Observation, Verification, or Outcome;
 * - implement Stage 187 Device Settings Control.
 *
 * MEDIA_CONTROL_READY != MEDIA_ACTION_ATTEMPTED.
 * MEDIA_ACTION_ATTEMPTED != PLAYBACK_CHANGED.
 * PLAYBACK_CHANGED != VERIFIED_OUTCOME.
 */
class AndroidMediaControlCoordinator {

    fun prepare(
        command: AndroidMediaControlCommand?,
    ): AndroidMediaControlResult {
        if (command == null) {
            return AndroidMediaControlResult.create(
                status = AndroidMediaControlStatus.DEFERRED,
            )
        }

        return AndroidMediaControlResult.create(
            status = AndroidMediaControlStatus.READY,
            command = command,
        )
    }
}
