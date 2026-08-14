package com.devil.app.device.tablet

/**
 * Stage 82 bounded tablet-form-factor assessment status.
 *
 * TABLET means genuine Android configuration evidence satisfied the
 * Stage 82 tablet form-factor threshold.
 *
 * NON_TABLET means genuine Android configuration evidence was available
 * but did not satisfy that threshold.
 *
 * DEFERRED means a truthful assessment could not be made.
 *
 * TABLET does not mean:
 *
 * - another Devil intelligence exists;
 * - another Brain or runtime exists;
 * - the subject is authenticated;
 * - Devil authorization exists;
 * - Android permission exists;
 * - capabilities are available;
 * - execution is permitted;
 * - or an Outcome has occurred.
 */
enum class AndroidTabletFormFactorAssessmentStatus {
    TABLET,
    NON_TABLET,
    DEFERRED,
}
