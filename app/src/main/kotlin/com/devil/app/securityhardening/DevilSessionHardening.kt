package com.devil.app.securityhardening

/**
 * Stage 277 Session Hardening.
 *
 * This bounded contract evaluates explicitly supplied session-hardening evidence
 * while preserving one exact Stage 276 Authentication Hardening result as
 * authoritative upstream security-hardening provenance.
 *
 * Existing Stage 23 Session Validity remains authoritative for actual bounded
 * session-validity evaluation.
 *
 * SESSION_HARDENED != SESSION_CREATED.
 * SESSION_HARDENED != SESSION_RENEWED.
 * SESSION_HARDENED != SESSION_REVOKED.
 * SESSION_HARDENED != AUTHENTICATED.
 * SESSION_VALID != AUTHENTICATED.
 * SESSION_VALID != AUTHORIZATION.
 * SESSION_HARDENED != OWNER_MODE.
 * SESSION_HARDENED != AUTHORIZATION.
 * SESSION_HARDENED != EXECUTION_APPROVAL.
 * SESSION_HARDENED != VERIFIED_OUTCOME.
 *
 * Stage 277 does not create, renew, extend, revoke, persist, transmit, or
 * terminate sessions. It does not authenticate a subject, grant authorization,
 * enter Owner Mode, approve execution, or implement Stage 278 Capability
 * Authorization Hardening.
 */
enum class DevilSessionHardeningStatus {
    HARDENED,
    NOT_HARDENED,
}

/**
 * Explicitly supplied Stage 277 session-hardening evidence.
 *
 * These values describe already-established security properties only.
 * They do not mutate or independently validate any SessionRecord.
 */
data class DevilSessionHardeningEvidence(
    val authenticationHardening: DevilAuthenticationHardeningResult,
    val nonActiveSessionsRejected: Boolean,
    val validityWindowEnforced: Boolean,
    val authoritativeObservationTimeRequired: Boolean,
    val revokedSessionsInvalidated: Boolean,
    val sessionValiditySeparatedFromAuthentication: Boolean,
    val sessionValiditySeparatedFromAuthorization: Boolean,
) {
    fun isComplete(): Boolean =
        authenticationHardening.status ==
            DevilAuthenticationHardeningStatus.HARDENED &&
            authenticationHardening.evidence
                .threatModel
                .coveredCategories
                .contains(
                    DevilThreatCategory.SESSION_COMPROMISE_REPLAY,
                ) &&
            nonActiveSessionsRejected &&
            validityWindowEnforced &&
            authoritativeObservationTimeRequired &&
            revokedSessionsInvalidated &&
            sessionValiditySeparatedFromAuthentication &&
            sessionValiditySeparatedFromAuthorization
}

/**
 * Bounded Stage 277 Session Hardening result.
 *
 * HARDENED means only that every required Stage 277 architectural property was
 * explicitly supplied and the preserved Stage 275 threat model includes the
 * session-compromise/replay threat domain.
 *
 * HARDENED does not mean a session was created, renewed, revoked, authenticated,
 * authorized, or used for execution.
 */
@ConsistentCopyVisibility
data class DevilSessionHardeningResult private constructor(
    val status: DevilSessionHardeningStatus,
    val evidence: DevilSessionHardeningEvidence,
) {
    companion object {
        fun create(
            evidence: DevilSessionHardeningEvidence,
        ): DevilSessionHardeningResult =
            DevilSessionHardeningResult(
                status =
                    if (evidence.isComplete()) {
                        DevilSessionHardeningStatus.HARDENED
                    } else {
                        DevilSessionHardeningStatus.NOT_HARDENED
                    },
                evidence = evidence,
            )
    }
}

/**
 * Stage 277 bounded Session Hardening coordinator.
 *
 * It evaluates explicitly supplied hardening evidence only.
 *
 * It does not:
 *
 * - construct or mutate SessionRecord;
 * - create, renew, extend, revoke, persist, or terminate sessions;
 * - evaluate current session validity independently of Stage 23;
 * - authenticate an owner, subject, speaker, or device;
 * - establish trust;
 * - enter Owner Mode;
 * - grant constitutional authorization;
 * - request or grant Android permission;
 * - create an ExecutionRequest or execute capabilities;
 * - establish constitutional Observation, Verification, or Outcome;
 * - implement Stage 278 Capability Authorization Hardening.
 */
class DevilSessionHardeningCoordinator {
    fun evaluate(
        evidence: DevilSessionHardeningEvidence,
    ): DevilSessionHardeningResult =
        DevilSessionHardeningResult.create(
            evidence = evidence,
        )
}
