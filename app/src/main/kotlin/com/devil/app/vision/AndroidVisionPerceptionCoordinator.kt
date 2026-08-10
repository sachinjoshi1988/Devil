package com.devil.app.vision

/**
 * Stage 41 bounded coordinator for Android camera perception readiness.
 *
 * Flow:
 *
 * Android CameraManager metadata
 * -> AndroidCameraInventorySource
 * -> AndroidCameraInventory
 * -> AndroidVisionPerceptionResult.
 *
 * This coordinator does not:
 *
 * - invoke UnifiedDevilRuntime;
 * - create ConversationInput;
 * - open a camera;
 * - capture a frame;
 * - interpret pixels;
 * - perform face recognition;
 * - authenticate a subject;
 * - grant authorization;
 * - persist visual data;
 * - or execute an action.
 */
class AndroidVisionPerceptionCoordinator(
    private val inventorySource: AndroidCameraInventorySource,
) {

    fun perceiveAvailability(): AndroidVisionPerceptionResult {
        val inventory =
            inventorySource.inventory()

        return AndroidVisionPerceptionResult(
            status =
                if (inventory.hasCamera) {
                    AndroidVisionPerceptionStatus.AVAILABLE
                } else {
                    AndroidVisionPerceptionStatus.NO_CAMERA
                },
            inventory = inventory,
        )
    }
}
