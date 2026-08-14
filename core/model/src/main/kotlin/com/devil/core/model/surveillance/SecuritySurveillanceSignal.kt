package com.devil.core.model.surveillance

/**
 * Immutable Stage 90 representation of one explicitly supplied bounded surveillance signal.
 *
 * A signal preserves:
 *
 * - the supplied SecuritySurveillanceSource;
 * - the supplied event time;
 * - and one bounded supplied description.
 *
 * The description is descriptive surveillance-domain input only.
 *
 * Stage 90 does not independently establish that the description is true, complete,
 * current, correctly interpreted, or externally verified.
 *
 * A SecuritySurveillanceSignal does not:
 *
 * - capture an image;
 * - open a camera;
 * - connect to CCTV;
 * - connect to a network camera;
 * - interpret pixels;
 * - recognize objects;
 * - recognize a face;
 * - identify a person;
 * - authenticate a person;
 * - infer ownership;
 * - infer intent;
 * - classify someone as suspicious;
 * - classify an intrusion;
 * - classify a threat;
 * - determine emergency severity;
 * - create a constitutional Decision;
 * - create a Task or Plan;
 * - establish constitutional Observation;
 * - establish Verification;
 * - establish an Outcome;
 * - authorize a Security Response;
 * - execute an alarm or defensive action;
 * - perform constitutional Learning;
 * - create or commit Memory;
 * - or persist surveillance state.
 *
 * SURVEILLANCE_SIGNAL != CONSTITUTIONAL_OBSERVATION.
 * SURVEILLANCE_SIGNAL != VERIFIED_REALITY.
 * SIGNAL_DESCRIPTION != IDENTITY.
 * SIGNAL_DESCRIPTION != THREAT.
 * SIGNAL != SECURITY_RESPONSE.
 */
@ConsistentCopyVisibility
data class SecuritySurveillanceSignal private constructor(
    val source: SecuritySurveillanceSource,
    val occurredAtEpochMilliseconds: Long,
    val description: String,
) {
    companion object {

        fun create(
            source: SecuritySurveillanceSource,
            occurredAtEpochMilliseconds: Long,
            description: String,
        ): SecuritySurveillanceSignal {
            val normalizedDescription =
                description.trim()

            require(occurredAtEpochMilliseconds >= 0L) {
                "Security-surveillance signal time must not be negative."
            }

            require(normalizedDescription.isNotEmpty()) {
                "Security-surveillance signal description must not be blank."
            }

            return SecuritySurveillanceSignal(
                source = source,
                occurredAtEpochMilliseconds =
                    occurredAtEpochMilliseconds,
                description = normalizedDescription,
            )
        }
    }
}
