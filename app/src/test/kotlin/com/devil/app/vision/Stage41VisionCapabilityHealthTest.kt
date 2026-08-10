package com.devil.app.vision

import com.devil.app.capability.DefaultAndroidCapabilityHealthSource
import com.devil.core.model.capability.CapabilityHealthState
import kotlin.test.Test
import kotlin.test.assertEquals

class Stage41VisionCapabilityHealthTest {

    @Test
    fun `Vision health remains unavailable without explicit camera evidence`() {
        val source =
            DefaultAndroidCapabilityHealthSource()

        assertEquals(
            CapabilityHealthState.UNAVAILABLE,
            source.health(
                AndroidVisionCapability.contract,
            ),
        )
    }

    @Test
    fun `Vision health is ready when genuine inventory contains a camera`() {
        val inventorySource =
            AndroidCameraInventorySource {
                AndroidCameraInventory(
                    cameras =
                        listOf(
                            AndroidCameraDescriptor.create(
                                cameraId = "camera-0",
                                facing = AndroidCameraFacing.BACK,
                            ),
                        ),
                )
            }

        val source =
            DefaultAndroidCapabilityHealthSource(
                visionCameraInventorySource =
                    inventorySource,
            )

        assertEquals(
            CapabilityHealthState.READY,
            source.health(
                AndroidVisionCapability.contract,
            ),
        )
    }

    @Test
    fun `Vision health remains unavailable when no camera exists`() {
        val source =
            DefaultAndroidCapabilityHealthSource(
                visionCameraInventorySource =
                    AndroidCameraInventorySource {
                        AndroidCameraInventory(
                            cameras = emptyList(),
                        )
                    },
            )

        assertEquals(
            CapabilityHealthState.UNAVAILABLE,
            source.health(
                AndroidVisionCapability.contract,
            ),
        )
    }

    @Test
    fun `Vision health fails closed when inventory inspection fails`() {
        val source =
            DefaultAndroidCapabilityHealthSource(
                visionCameraInventorySource =
                    AndroidCameraInventorySource {
                        error("camera inventory unavailable")
                    },
            )

        assertEquals(
            CapabilityHealthState.UNAVAILABLE,
            source.health(
                AndroidVisionCapability.contract,
            ),
        )
    }
}
