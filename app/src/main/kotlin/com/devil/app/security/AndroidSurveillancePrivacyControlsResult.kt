package com.devil.app.security

/**
 * Stage 232 bounded Surveillance Privacy Controls result.
 *
 * CONTROLLED preserves:
 *
 * - one exact RETAINED Stage 231 Security Evidence Retention result;
 * - all upstream security/surveillance provenance preserved transitively by it;
 * - one normalized explicitly supplied bounded privacy-controls description.
 *
 * DEFERRED preserves the exact Stage 231 upstream result without claiming
 * surveillance privacy controls and contains no privacy-controls metadata.
 *
 * Stage 232 does not:
 *
 * - perform privacy disclosure;
 * - authorize disclosure;
 * - expose surveillance information;
 * - redact or suppress content;
 * - delete surveillance information;
 * - enforce retention;
 * - calculate expiry;
 * - persist or store security evidence;
 * - write files, databases, cloud storage, or Memory;
 * - create, commit, persist, recall, expose, or delete Memory;
 * - mutate an existing SecuritySurveillanceRecord;
 * - alter camera or network-camera configuration;
 * - enable or disable cameras;
 * - change Android permissions;
 * - authenticate a subject or owner;
 * - establish trust;
 * - grant constitutional authorization;
 * - establish constitutional Observation, Verification, or Outcome;
 * - establish verified reality;
 * - create a Decision, Task, Plan, capability, or ExecutionRequest;
 * - execute local or remote capabilities;
 * - send notifications, alerts, calls, or messages;
 * - trigger alarms or operate locks;
 * - contact emergency services;
 * - mutate World Model state;
 * - perform constitutional Learning;
 * - replace the existing Stage 46 privacy architecture;
 * - implement Stage 233 Security Production Validation.
 *
 * SURVEILLANCE_PRIVACY_CONTROLLED != PRIVACY_DISCLOSURE_PERFORMED.
 * SURVEILLANCE_PRIVACY_CONTROLLED != REDACTION_PERFORMED.
 * SURVEILLANCE_PRIVACY_CONTROLLED != DATA_DELETED.
 * SURVEILLANCE_PRIVACY_CONTROLLED != RETENTION_ENFORCEMENT.
 * SURVEILLANCE_PRIVACY_CONTROLLED != PERSISTENCE.
 * SURVEILLANCE_PRIVACY_CONTROLLED != MEMORY.
 * SURVEILLANCE_PRIVACY_CONTROLLED != AUTHORIZATION.
 * SURVEILLANCE_PRIVACY_CONTROLLED != EXECUTION.
 * SECURITY_EVIDENCE_RETENTION != VERIFIED_REALITY.
 */
@ConsistentCopyVisibility
data class AndroidSurveillancePrivacyControlsResult private constructor(
    val status: AndroidSurveillancePrivacyControlsStatus,
    val evidenceRetention: AndroidSecurityEvidenceRetentionResult,
    val privacyControlsDescription: String?,
) {
    companion object {

        fun create(
            status: AndroidSurveillancePrivacyControlsStatus,
            evidenceRetention: AndroidSecurityEvidenceRetentionResult,
            privacyControlsDescription: String? = null,
        ): AndroidSurveillancePrivacyControlsResult {
            return when (status) {
                AndroidSurveillancePrivacyControlsStatus.CONTROLLED -> {
                    require(
                        evidenceRetention.status ==
                            AndroidSecurityEvidenceRetentionStatus.RETAINED,
                    ) {
                        "Controlled Stage 232 Surveillance Privacy requires retained Stage 231 Security Evidence context."
                    }

                    val normalizedDescription =
                        requireNotNull(privacyControlsDescription)
                            .trim()

                    require(normalizedDescription.isNotEmpty()) {
                        "Stage 232 surveillance privacy controls description must not be blank."
                    }

                    AndroidSurveillancePrivacyControlsResult(
                        status = status,
                        evidenceRetention = evidenceRetention,
                        privacyControlsDescription = normalizedDescription,
                    )
                }

                AndroidSurveillancePrivacyControlsStatus.DEFERRED -> {
                    require(privacyControlsDescription == null) {
                        "Deferred Stage 232 Surveillance Privacy Controls must not contain privacy-controls metadata."
                    }

                    AndroidSurveillancePrivacyControlsResult(
                        status = status,
                        evidenceRetention = evidenceRetention,
                        privacyControlsDescription = null,
                    )
                }
            }
        }
    }
}
