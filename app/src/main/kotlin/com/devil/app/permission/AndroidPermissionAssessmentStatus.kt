package com.devil.app.permission

/**
 * Describes Android operating-system permission state for one registered
 * capability.
 *
 * NOT_REQUIRED means approved Android permission policy establishes that the
 * capability requires no Android runtime permission.
 *
 * GRANTED means all explicitly required Android permissions are currently
 * granted by Android.
 *
 * DENIED means at least one explicitly required Android permission is not
 * currently granted by Android.
 *
 * UNAVAILABLE means no approved capability-to-Android-permission requirement
 * mapping is currently available.
 *
 * None of these states grant or deny Devil constitutional authorization,
 * Executive readiness, execution permission, or verified outcome.
 *
 * Android permission != Devil authorization.
 */
enum class AndroidPermissionAssessmentStatus {
    NOT_REQUIRED,
    GRANTED,
    DENIED,
    UNAVAILABLE,
}
