package com.devil.app.vision

/**
 * Stage 190 bounded Camera Capability integration status.
 *
 * READY means one exact Stage 41 camera descriptor produced one bounded
 * Stage 41 frame request.
 *
 * DEFERRED means no frame request was prepared.
 */
enum class AndroidCameraCapabilityIntegrationStatus {
    READY,
    DEFERRED,
}
