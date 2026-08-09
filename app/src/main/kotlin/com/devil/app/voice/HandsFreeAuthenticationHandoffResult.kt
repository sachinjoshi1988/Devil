package com.devil.app.voice

/**
 * Preserves one Stage 37 authentication handoff result.
 *
 * This result contains no authenticated identity and no session because Stage 37
 * must not fabricate either value.
 */
data class HandsFreeAuthenticationHandoffResult(
    val status: HandsFreeAuthenticationHandoffStatus,
    val message: String,
)
