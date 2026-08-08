package com.devil.core.model.security

/**
 * Records one established bounded constitutional security position.
 *
 * The record preserves the current SecurityStage and a concise rationale for
 * why that stage is being represented.
 *
 * It does not advance security stages, authenticate a subject, prove owner
 * identity, establish trust, grant authorization, create or validate a session,
 * enter Owner Mode, approve high-security confirmation, grant Android
 * permission, or permit execution.
 *
 * In particular, recording WAKE does not establish authentication.
 */
@ConsistentCopyVisibility
data class SecurityStateRecord private constructor(
    val stage: SecurityStage,
    val rationale: String,
) {
    companion object {
        fun create(
            stage: SecurityStage,
            rationale: String,
        ): SecurityStateRecord {
            val normalizedRationale = rationale.trim()

            require(normalizedRationale.isNotEmpty()) {
                "Security state rationale must not be blank."
            }

            return SecurityStateRecord(
                stage = stage,
                rationale = normalizedRationale,
            )
        }
    }
}
