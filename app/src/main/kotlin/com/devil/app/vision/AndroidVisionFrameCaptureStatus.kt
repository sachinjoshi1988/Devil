package com.devil.app.vision

/**
 * Stage 41 bounded visual-frame acquisition status.
 *
 * CAPTURED means one approved frame source supplied one real encoded frame.
 *
 * CAMERA_UNAVAILABLE means the requested Android camera could not currently
 * provide a frame.
 *
 * PERMISSION_UNAVAILABLE means Android CAMERA permission was not available to
 * the bounded source.
 *
 * FAILED means the bounded platform capture mechanism failed.
 *
 * CAPTURED
 * != understood
 * != trusted
 * != authenticated
 * != authorized
 * != verified success.
 */
enum class AndroidVisionFrameCaptureStatus {
    CAPTURED,
    CAMERA_UNAVAILABLE,
    PERMISSION_UNAVAILABLE,
    FAILED,
}
