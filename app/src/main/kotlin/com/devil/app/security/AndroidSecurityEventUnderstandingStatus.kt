package com.devil.app.security

/**
 * Stage 226 bounded Security Event Understanding status.
 *
 * UNDERSTOOD means one exact available Stage 225 Camera / Network-Camera Adapter
 * context has been associated with one explicitly supplied bounded event-understanding
 * description while preserving exact Stage 90 surveillance-signal provenance.
 *
 * DEFERRED means Stage 226 cannot truthfully claim bounded event understanding.
 *
 * SECURITY_EVENT_UNDERSTOOD != VERIFIED_REALITY.
 * SECURITY_EVENT_UNDERSTOOD != CONSTITUTIONAL_OBSERVATION.
 * SECURITY_EVENT_UNDERSTOOD != THREAT.
 * SECURITY_EVENT_UNDERSTOOD != INTRUSION.
 * SECURITY_EVENT_UNDERSTOOD != EMERGENCY.
 * SECURITY_EVENT_UNDERSTOOD != AUTHORIZATION.
 * SECURITY_EVENT_UNDERSTOOD != SECURITY_RESPONSE.
 * SECURITY_EVENT_UNDERSTOOD != EXECUTION.
 */
enum class AndroidSecurityEventUnderstandingStatus {
    UNDERSTOOD,
    DEFERRED,
}
