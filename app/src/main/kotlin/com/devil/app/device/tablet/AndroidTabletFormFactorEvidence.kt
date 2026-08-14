package com.devil.app.device.tablet

/**
 * Immutable Stage 82 evidence describing one Android embodiment's
 * smallest available screen-width configuration.
 *
 * smallestScreenWidthDp must originate from genuine Android Configuration
 * evidence supplied by the Android embodiment boundary.
 *
 * This record does not establish:
 *
 * - Devil identity;
 * - subject identity;
 * - trust;
 * - authentication;
 * - authorization;
 * - session validity;
 * - Owner Mode;
 * - capability registration;
 * - capability availability;
 * - capability health;
 * - Android permission;
 * - execution approval;
 * - Observation;
 * - Verification;
 * - Outcome;
 * - Memory eligibility;
 * - persistence authority;
 * - or another Unified Devil Runtime.
 *
 * SCREEN_CONFIGURATION_EVIDENCE != AUTHORITY.
 */
@ConsistentCopyVisibility
data class AndroidTabletFormFactorEvidence private constructor(
    val smallestScreenWidthDp: Int,
) {
    companion object {

        fun create(
            smallestScreenWidthDp: Int,
        ): AndroidTabletFormFactorEvidence {
            require(smallestScreenWidthDp > 0) {
                "Tablet form-factor evidence requires positive smallest-screen-width dp."
            }

            return AndroidTabletFormFactorEvidence(
                smallestScreenWidthDp = smallestScreenWidthDp,
            )
        }
    }
}
