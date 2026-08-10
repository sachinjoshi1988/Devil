package com.devil.app.vision

/**
 * Stage 41 bounded camera-perception readiness status.
 *
 * AVAILABLE means at least one Android camera is genuinely exposed by the
 * platform inventory source.
 *
 * NO_CAMERA means no camera was exposed by that source.
 *
 * Neither state grants Android permission or Devil constitutional authority.
 */
enum class AndroidVisionPerceptionStatus {
    AVAILABLE,
    NO_CAMERA,
}
