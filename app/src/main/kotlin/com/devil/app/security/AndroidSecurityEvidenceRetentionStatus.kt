package com.devil.app.security

/**
 * Stage 231 bounded Security Evidence Retention status.
 *
 * RETAINED means one exact AVAILABLE Stage 230 Owner Security Dashboard result
 * has been associated with one explicitly supplied bounded retention description.
 *
 * RETAINED means only that bounded retention context is represented.
 * It does not mean durable storage or retention enforcement occurred.
 *
 * DEFERRED means Stage 231 cannot truthfully claim bounded security-evidence
 * retention context.
 *
 * SECURITY_EVIDENCE_RETENTION != PERSISTENCE.
 * SECURITY_EVIDENCE_RETENTION != STORAGE_SUCCESS.
 * SECURITY_EVIDENCE_RETENTION != RETENTION_ENFORCEMENT.
 * SECURITY_EVIDENCE_RETENTION != MEMORY.
 * SECURITY_EVIDENCE_RETENTION != MEMORY_COMMITMENT.
 * SECURITY_EVIDENCE_RETENTION != CONSTITUTIONAL_OBSERVATION.
 * SECURITY_EVIDENCE_RETENTION != VERIFICATION.
 * SECURITY_EVIDENCE_RETENTION != VERIFIED_REALITY.
 * SECURITY_EVIDENCE_RETENTION != AUTHORIZATION.
 * SECURITY_EVIDENCE_RETENTION != EXECUTION.
 */
enum class AndroidSecurityEvidenceRetentionStatus {
    RETAINED,
    DEFERRED,
}
