package com.devil.core.model.security

/**
 * Identifies one bounded constitutional security session.
 *
 * Session creation belongs to the Security Authority and its approved session
 * mechanism. This type only validates and represents an already-established
 * session identity.
 *
 * Possessing a SessionId does not authenticate a subject, prove owner identity,
 * establish trust, grant authorization, establish session validity, enter
 * Owner Mode, approve high-security confirmation, grant Android permission,
 * or permit execution.
 */
@ConsistentCopyVisibility
data class SessionId private constructor(
    val value: String,
) {
    companion object {
        fun from(rawValue: String): SessionId {
            val normalizedValue = rawValue.trim()

            require(normalizedValue.isNotEmpty()) {
                "Session identity must not be blank."
            }

            return SessionId(normalizedValue)
        }
    }
}
