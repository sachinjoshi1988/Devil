package com.devil.app.vision

/**
 * Bounded Stage 41 camera-facing classification.
 *
 * The value reflects Android camera metadata only.
 *
 * FRONT or BACK does not establish what the camera currently sees.
 */
enum class AndroidCameraFacing {
    FRONT,
    BACK,
    EXTERNAL,
    UNKNOWN,
}
