package com.devil.app.voice

/**
 * Stage 195 bounded Voice Architecture V2 status.
 *
 * AVAILABLE means both existing bounded Android voice directions were supplied.
 *
 * DEFERRED means the bounded Stage 195 architecture was not established.
 */
enum class AndroidVoiceArchitectureV2Status {
    AVAILABLE,
    DEFERRED,
}
