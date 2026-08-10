package com.devil.app.vision

import kotlin.test.Test
import kotlin.test.assertEquals

class AndroidVisionPerceptionCoordinatorTest {

    @Test
    fun `camera hardware produces available perception status`() {
        val inventory =
            AndroidCameraInventory(
                cameras =
                    listOf(
                        AndroidCameraDescriptor.create(
                            cameraId = "0",
                            facing = AndroidCameraFacing.BACK,
                        ),
                    ),
            )

        val coordinator =
            AndroidVisionPerceptionCoordinator(
                inventorySource =
                    AndroidCameraInventorySource {
                        inventory
                    },
            )

        val result =
            coordinator.perceiveAvailability()

        assertEquals(
            AndroidVisionPerceptionStatus.AVAILABLE,
            result.status,
        )
        assertEquals(
            inventory,
            result.inventory,
        )
    }

    @Test
    fun `missing camera hardware remains explicit no camera state`() {
        val inventory =
            AndroidCameraInventory(
                cameras = emptyList(),
            )

        val coordinator =
            AndroidVisionPerceptionCoordinator(
                inventorySource =
                    AndroidCameraInventorySource {
                        inventory
                    },
            )

        val result =
            coordinator.perceiveAvailability()

        assertEquals(
            AndroidVisionPerceptionStatus.NO_CAMERA,
            result.status,
        )
        assertEquals(
            emptyList(),
            result.inventory.cameras,
        )
    }
}
