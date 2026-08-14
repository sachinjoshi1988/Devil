package com.devil.app.device.tablet

import android.content.Context

/**
 * Default Stage 82 Android tablet-form-factor evidence source.
 *
 * The source reads only Android Configuration.smallestScreenWidthDp.
 *
 * It does not classify the device itself. Classification belongs to the
 * bounded Stage 82 coordinator.
 *
 * A non-positive or unavailable configuration value produces null rather
 * than fabricated evidence.
 *
 * Android configuration evidence:
 *
 * != Devil identity
 * != authentication
 * != authorization
 * != Android permission
 * != capability availability
 * != execution approval
 * != verified Outcome
 */
class DefaultAndroidTabletFormFactorEvidenceSource(
    context: Context,
) : AndroidTabletFormFactorEvidenceSource {

    private val applicationContext =
        context.applicationContext

    override fun evidence(): AndroidTabletFormFactorEvidence? {
        val smallestScreenWidthDp =
            applicationContext
                .resources
                .configuration
                .smallestScreenWidthDp

        if (smallestScreenWidthDp <= 0) {
            return null
        }

        return AndroidTabletFormFactorEvidence.create(
            smallestScreenWidthDp = smallestScreenWidthDp,
        )
    }
}
