package com.devil.app.voice

/**
 * Stage 315 Android hands-free authentication handoff.
 *
 * Stage 37 established the fail-closed hands-free authentication boundary.
 * Stage 314 later established a genuine Android owner-authentication/session
 * mechanism in the Android embodiment.
 *
 * Stage 315 may therefore truthfully report that genuine Android
 * authentication is required when the hands-free state machine requests it.
 *
 * This handoff does not:
 *
 * - perform Android authentication;
 * - treat Code Red as authentication evidence;
 * - identify a speaker;
 * - establish a SessionRecord;
 * - determine session validity;
 * - create ACTIVE_SESSION;
 * - grant Devil authorization;
 * - enter Owner Mode;
 * - invoke UnifiedDevilRuntime;
 * - or execute capabilities.
 *
 * The Android Activity remains responsible for invoking the genuine platform
 * authentication boundary after this REQUIRED result is presented.
 *
 * Code Red != authentication.
 * AUTHENTICATION_REQUIRED != AUTHENTICATED.
 * AUTHENTICATION_REQUIRED != ACTIVE_SESSION.
 * AUTHENTICATION_REQUIRED != AUTHORIZATION.
 */
class Stage315AndroidHandsFreeAuthenticationHandoff :
    HandsFreeAuthenticationHandoff {

    override fun requestAuthentication():
        HandsFreeAuthenticationHandoffResult {
        return HandsFreeAuthenticationHandoffResult(
            status =
                HandsFreeAuthenticationHandoffStatus.REQUIRED,
            message =
                "Android owner authentication is required for hands-free continuation.",
        )
    }
}
