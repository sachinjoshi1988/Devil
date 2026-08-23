package com.devil.app.security

/**
 * Stage 233 bounded Security Production Validation result.
 *
 * VALIDATED preserves:
 *
 * - one exact CONTROLLED Stage 232 Surveillance Privacy Controls result;
 * - all bounded Stage 224 -> 232 security/surveillance provenance preserved
 *   transitively through that exact object;
 * - one normalized explicitly supplied validation focus;
 * - one normalized explicitly supplied validation evidence description.
 *
 * DEFERRED contains no Stage 232 validation context and no validation metadata.
 *
 * Stage 233 does not:
 *
 * - establish constitutional Verification or Outcome;
 * - establish real-device validation;
 * - connect to or operate cameras;
 * - execute surveillance;
 * - execute Security Response;
 * - confirm threat, intrusion, or emergency state;
 * - authenticate an owner or subject;
 * - establish trust;
 * - grant constitutional authorization;
 * - perform privacy disclosure, redaction, or suppression;
 * - enforce retention;
 * - persist or store security evidence;
 * - create, commit, persist, recall, expose, or delete Memory;
 * - create Decision, Task, Plan, capability, or ExecutionRequest;
 * - execute local or remote capabilities;
 * - send notifications, alerts, calls, or messages;
 * - trigger alarms or operate locks;
 * - contact emergency services;
 * - mutate World Model state;
 * - perform constitutional Learning;
 * - establish verified reality;
 * - implement Stage 234 Model Provider Architecture.
 *
 * SECURITY_PRODUCTION_VALIDATED != CONSTITUTIONAL_VERIFICATION.
 * SECURITY_PRODUCTION_VALIDATED != REAL_DEVICE_VALIDATED.
 * SECURITY_PRODUCTION_VALIDATED != CAMERA_CONNECTED.
 * SECURITY_PRODUCTION_VALIDATED != SURVEILLANCE_EXECUTED.
 * SECURITY_PRODUCTION_VALIDATED != SECURITY_RESPONSE_EXECUTED.
 * SECURITY_PRODUCTION_VALIDATED != EMERGENCY_CONFIRMED.
 * SECURITY_PRODUCTION_VALIDATED != OWNER_AUTHENTICATED.
 * SECURITY_PRODUCTION_VALIDATED != AUTHORIZATION.
 * SECURITY_PRODUCTION_VALIDATED != PRIVACY_DISCLOSURE_PERFORMED.
 * SECURITY_PRODUCTION_VALIDATED != RETENTION_ENFORCED.
 * SECURITY_PRODUCTION_VALIDATED != PERSISTENCE.
 * SECURITY_PRODUCTION_VALIDATED != VERIFIED_REALITY.
 */
@ConsistentCopyVisibility
data class AndroidSecurityProductionValidationResult private constructor(
    val status: AndroidSecurityProductionValidationStatus,
    val privacyControls: AndroidSurveillancePrivacyControlsResult?,
    val validationFocus: String?,
    val validationEvidenceDescription: String?,
) {
    companion object {

        fun create(
            status: AndroidSecurityProductionValidationStatus,
            privacyControls: AndroidSurveillancePrivacyControlsResult? = null,
            validationFocus: String? = null,
            validationEvidenceDescription: String? = null,
        ): AndroidSecurityProductionValidationResult {
            return when (status) {
                AndroidSecurityProductionValidationStatus.VALIDATED -> {
                    val controlledPrivacy =
                        requireNotNull(privacyControls) {
                            "Validated Stage 233 Security Production requires one Stage 232 Surveillance Privacy Controls result."
                        }

                    require(
                        controlledPrivacy.status ==
                            AndroidSurveillancePrivacyControlsStatus.CONTROLLED,
                    ) {
                        "Validated Stage 233 Security Production requires controlled Stage 232 Surveillance Privacy context."
                    }

                    val normalizedFocus =
                        requireNotNull(validationFocus)
                            .trim()

                    val normalizedEvidence =
                        requireNotNull(validationEvidenceDescription)
                            .trim()

                    require(normalizedFocus.isNotEmpty()) {
                        "Stage 233 validation focus must not be blank."
                    }

                    require(normalizedEvidence.isNotEmpty()) {
                        "Stage 233 validation evidence description must not be blank."
                    }

                    AndroidSecurityProductionValidationResult(
                        status = status,
                        privacyControls = controlledPrivacy,
                        validationFocus = normalizedFocus,
                        validationEvidenceDescription = normalizedEvidence,
                    )
                }

                AndroidSecurityProductionValidationStatus.DEFERRED -> {
                    require(privacyControls == null) {
                        "Deferred Stage 233 Security Production Validation must not contain Stage 232 privacy controls."
                    }

                    require(validationFocus == null) {
                        "Deferred Stage 233 Security Production Validation must not contain validation focus."
                    }

                    require(validationEvidenceDescription == null) {
                        "Deferred Stage 233 Security Production Validation must not contain validation evidence."
                    }

                    AndroidSecurityProductionValidationResult(
                        status = status,
                        privacyControls = null,
                        validationFocus = null,
                        validationEvidenceDescription = null,
                    )
                }
            }
        }
    }
}
