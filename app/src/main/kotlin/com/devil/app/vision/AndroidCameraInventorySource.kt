package com.devil.app.vision

/**
 * Supplies genuine bounded Android camera-hardware metadata.
 *
 * Implementations may inspect Android camera inventory only.
 *
 * They must not open a camera, capture imagery, identify a person, interpret
 * visual content, authenticate a subject, grant authorization, create memory,
 * execute an action, or claim a verified Outcome.
 */
fun interface AndroidCameraInventorySource {

    fun inventory(): AndroidCameraInventory
}
