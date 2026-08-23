package com.devil.app.security

/**
 * Stage 232 bounded Surveillance Privacy Controls status.
 *
 * CONTROLLED means one exact RETAINED Stage 231 Security Evidence Retention
 * result has been associated with one explicitly supplied bounded surveillance
 * privacy-controls description.
 *
 * DEFERRED means Stage 232 cannot truthfully claim bounded surveillance privacy
 * controls context.
 *
 * CONTROLLED means representation of privacy-control context only.
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
enum class AndroidSurveillancePrivacyControlsStatus {
    CONTROLLED,
    DEFERRED,
}
