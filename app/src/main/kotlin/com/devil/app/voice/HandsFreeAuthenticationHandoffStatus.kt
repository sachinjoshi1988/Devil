package com.devil.app.voice

/**
 * Describes the bounded Stage 37 handoff to authentication.
 *
 * REQUIRED means genuine authentication must occur before hands-free
 * conversation may continue.
 *
 * UNAVAILABLE means Stage 37 has no approved Android authentication bridge
 * capable of proving owner identity and establishing an authenticated session.
 *
 * Neither state represents successful authentication.
 */
enum class HandsFreeAuthenticationHandoffStatus {
    REQUIRED,
    UNAVAILABLE,
}
