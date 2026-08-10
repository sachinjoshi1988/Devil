package com.devil.app.vision

import com.devil.app.capability.DefaultAndroidCapabilityAvailabilitySource
import com.devil.core.model.capability.CapabilityAvailabilityState
import kotlin.test.Test
import kotlin.test.assertEquals

class Stage41VisionCapabilityAvailabilityTest {

    @Test
    fun `Vision remains unavailable without explicit camera inventory evidence`() {
        val source =
            DefaultAndroidCapabilityAvailabilitySource()

        assertEquals(
            CapabilityAvailabilityState.UNAVAILABLE,
            source.availability(
                AndroidVisionCapability.contract,
            ),
        )
    }

    @Test
    fun `Vision is available when genuine inventory contains a camera`() {
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
            DefaultAndroidCapabilityAvailabilitySource(
                visionCameraInventorySource =
                    inventorySource,
            )

        assertEquals(
            CapabilityAvailabilityState.AVAILABLE,
            source.availability(
                AndroidVisionCapability.contract,
            ),
        )
    }

    @Test
    fun `Vision remains unavailable when genuine inventory contains no camera`() {
        val source =
            DefaultAndroidCapabilityAvailabilitySource(
                visionCameraInventorySource =
                    AndroidCameraInventorySource {
                        AndroidCameraInventory(
                            cameras = emptyList(),
                        )
                    },
            )

        assertEquals(
            CapabilityAvailabilityState.UNAVAILABLE,
            source.availability(
                AndroidVisionCapability.contract,
            ),
        )
    }

    @Test
    fun `Vision availability fails closed when inventory inspection fails`() {
        val source =
            DefaultAndroidCapabilityAvailabilitySource(
                visionCameraInventorySource =
                    AndroidCameraInventorySource {
                        error("camera inventory unavailable")
                    },
            )

        assertEquals(
            CapabilityAvailabilityState.UNAVAILABLE,
            source.availability(
                AndroidVisionCapability.contract,
            ),
        )
    }
}
