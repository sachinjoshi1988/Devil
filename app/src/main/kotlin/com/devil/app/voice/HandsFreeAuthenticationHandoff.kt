package com.devil.app.voice

/**
 * Boundary through which Stage 37 may request genuine authentication after
 * recognizing Code Red.
 *
 * Implementations must not treat Code Red itself as authentication evidence.
 */
fun interface HandsFreeAuthenticationHandoff {

    fun requestAuthentication():
        HandsFreeAuthenticationHandoffResult
}
