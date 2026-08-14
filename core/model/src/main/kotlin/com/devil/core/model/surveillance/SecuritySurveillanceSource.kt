package com.devil.core.model.surveillance

/**
 * Immutable Stage 90 representation of one explicitly supplied security-surveillance source.
 *
 * sourceId identifies the supplied surveillance source.
 *
 * sourceType preserves an extensible descriptive source type.
 *
 * Future examples may include:
 *
 * - Android camera;
 * - CCTV camera;
 * - network camera;
 * - PC camera;
 * - door sensor;
 * - motion sensor;
 * - or another authorized surveillance embodiment.
 *
 * This type deliberately does not assume that any particular source actually exists.
 *
 * It does not establish:
 *
 * - hardware availability;
 * - network reachability;
 * - camera availability;
 * - platform permission;
 * - ownership;
 * - physical location;
 * - source trustworthiness;
 * - authentication;
 * - authorization;
 * - capability registration;
 * - capability availability;
 * - capability readiness;
 * - active monitoring;
 * - image capture;
 * - video streaming;
 * - identity;
 * - biometric recognition;
 * - threat status;
 * - constitutional Observation;
 * - Verification;
 * - Outcome;
 * - execution authority;
 * - Security Response;
 * - constitutional Learning;
 * - Memory;
 * - or persistence authority.
 *
 * SURVEILLANCE_SOURCE != CAPABILITY.
 * SURVEILLANCE_SOURCE != TRUSTED_SENSOR.
 * SURVEILLANCE_SOURCE != ACTIVE_MONITORING.
 * SURVEILLANCE_SOURCE != AUTHORITY.
 */
@ConsistentCopyVisibility
data class SecuritySurveillanceSource private constructor(
    val sourceId: String,
    val sourceType: String,
) {
    companion object {

        fun create(
            sourceId: String,
            sourceType: String,
        ): SecuritySurveillanceSource {
            val normalizedSourceId =
                sourceId.trim()

            val normalizedSourceType =
                sourceType.trim()

            require(normalizedSourceId.isNotEmpty()) {
                "Security-surveillance source identity must not be blank."
            }

            require(normalizedSourceType.isNotEmpty()) {
                "Security-surveillance source type must not be blank."
            }

            return SecuritySurveillanceSource(
                sourceId = normalizedSourceId,
                sourceType = normalizedSourceType,
            )
        }
    }
}
