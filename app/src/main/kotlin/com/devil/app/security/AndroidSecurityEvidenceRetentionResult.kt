package com.devil.app.security

/**
 * Stage 231 bounded Security Evidence Retention result.
 *
 * RETAINED preserves:
 *
 * - one exact AVAILABLE Stage 230 Owner Security Dashboard result;
 * - all security/surveillance provenance already preserved transitively by it;
 * - one normalized explicitly supplied bounded retention description.
 *
 * DEFERRED preserves the exact Stage 230 upstream result without claiming
 * security-evidence retention context and contains no retention metadata.
 *
 * Stage 231 does not:
 *
 * - persist or store security evidence;
 * - write files, databases, cloud storage, or Memory;
 * - enforce a retention lifetime;
 * - calculate expiry;
 * - delete or prevent deletion;
 * - create, commit, persist, recall, or expose Memory;
 * - establish constitutional Observation;
 * - establish Verification;
 * - establish Outcome;
 * - establish that retained information is verified reality;
 * - authenticate a subject or owner;
 * - establish trust;
 * - grant constitutional authorization;
 * - create a Decision, Task, Plan, capability, or ExecutionRequest;
 * - execute local or remote capabilities;
 * - send alerts, notifications, calls, or messages;
 * - trigger alarms or operate locks;
 * - contact emergency services;
 * - mutate World Model state;
 * - perform constitutional Learning;
 * - apply privacy disclosure, redaction, suppression, or exposure policy;
 * - implement Stage 232 Surveillance Privacy Controls.
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
@ConsistentCopyVisibility
data class AndroidSecurityEvidenceRetentionResult private constructor(
    val status: AndroidSecurityEvidenceRetentionStatus,
    val ownerSecurityDashboard: AndroidOwnerSecurityDashboardResult,
    val retentionDescription: String?,
) {
    companion object {

        fun create(
            status: AndroidSecurityEvidenceRetentionStatus,
            ownerSecurityDashboard: AndroidOwnerSecurityDashboardResult,
            retentionDescription: String? = null,
        ): AndroidSecurityEvidenceRetentionResult {
            return when (status) {
                AndroidSecurityEvidenceRetentionStatus.RETAINED -> {
                    require(
                        ownerSecurityDashboard.status ==
                            AndroidOwnerSecurityDashboardStatus.AVAILABLE,
                    ) {
                        "Retained Stage 231 Security Evidence requires available Stage 230 Owner Security Dashboard context."
                    }

                    val normalizedDescription =
                        requireNotNull(retentionDescription)
                            .trim()

                    require(normalizedDescription.isNotEmpty()) {
                        "Stage 231 security evidence retention description must not be blank."
                    }

                    AndroidSecurityEvidenceRetentionResult(
                        status = status,
                        ownerSecurityDashboard = ownerSecurityDashboard,
                        retentionDescription = normalizedDescription,
                    )
                }

                AndroidSecurityEvidenceRetentionStatus.DEFERRED -> {
                    require(retentionDescription == null) {
                        "Deferred Stage 231 Security Evidence Retention must not contain retention metadata."
                    }

                    AndroidSecurityEvidenceRetentionResult(
                        status = status,
                        ownerSecurityDashboard = ownerSecurityDashboard,
                        retentionDescription = null,
                    )
                }
            }
        }
    }
}
