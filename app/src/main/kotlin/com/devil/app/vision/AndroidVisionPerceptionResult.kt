package com.devil.app.vision

/**
 * Bounded Stage 41 result of camera-inventory perception.
 *
 * AVAILABLE establishes only camera-hardware presence.
 *
 * It does not mean:
 *
 * - CAMERA permission granted;
 * - camera opened;
 * - frame captured;
 * - image analyzed;
 * - identity established;
 * - authorization granted;
 * - or outcome verified.
 */
data class AndroidVisionPerceptionResult(
    val status: AndroidVisionPerceptionStatus,
    val inventory: AndroidCameraInventory,
)
