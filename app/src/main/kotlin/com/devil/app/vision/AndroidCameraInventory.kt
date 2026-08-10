package com.devil.app.vision

/**
 * Immutable Stage 41 snapshot of currently exposed Android camera hardware.
 *
 * This snapshot preserves only bounded platform camera metadata.
 *
 * It contains no image bytes, faces, recognized objects, location, owner
 * identity, biometric interpretation, conversation input, or memory.
 *
 * Camera inventory
 * != camera permission
 * != active camera
 * != image capture
 * != visual understanding
 * != authentication
 * != authorization.
 */
data class AndroidCameraInventory(
    val cameras: List<AndroidCameraDescriptor>,
) {

    val hasCamera: Boolean
        get() = cameras.isNotEmpty()
}
