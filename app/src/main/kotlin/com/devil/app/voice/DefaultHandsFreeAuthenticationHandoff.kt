package com.devil.app.voice

/**
 * Default Stage 37 authentication handoff.
 *
 * No approved Android production authentication/session bridge is currently
 * composed into the voice path.
 *
 * Therefore the default implementation returns UNAVAILABLE rather than
 * fabricating owner authentication or an ACTIVE_SESSION.
 *
 * Code Red != Authentication.
 */
class DefaultHandsFreeAuthenticationHandoff :
    HandsFreeAuthenticationHandoff {

    override fun requestAuthentication():
        HandsFreeAuthenticationHandoffResult {
        return HandsFreeAuthenticationHandoffResult(
            status =
                HandsFreeAuthenticationHandoffStatus.UNAVAILABLE,
            message =
                "Authentication is not yet available for hands-free continuation.",
        )
    }
}
