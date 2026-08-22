package com.devil.app.vision

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class Stage190CameraCapabilityTest {

    @Test
    fun `available Stage 41 camera prepares exact frame request`() {
        val camera =
            AndroidCameraDescriptor.create(
                cameraId = "back-camera",
                facing = AndroidCameraFacing.BACK,
            )

        val perception =
            AndroidVisionPerceptionResult(
                status = AndroidVisionPerceptionStatus.AVAILABLE,
                inventory = AndroidCameraInventory(listOf(camera)),
            )

        val result =
            AndroidCameraCapabilityIntegrationCoordinator()
                .prepare(
                    perception = perception,
                    camera = camera,
                )

        assertEquals(AndroidCameraCapabilityIntegrationStatus.READY, result.status)
        assertEquals(perception, result.perception)
        assertEquals(camera, result.camera)
        assertEquals(camera.cameraId, result.frameRequest?.cameraId)
    }

    @Test
    fun `foreign camera remains deferred`() {
        val camera =
            AndroidCameraDescriptor.create(
                cameraId = "back-camera",
                facing = AndroidCameraFacing.BACK,
            )

        val foreign =
            AndroidCameraDescriptor.create(
                cameraId = "front-camera",
                facing = AndroidCameraFacing.FRONT,
            )

        val perception =
            AndroidVisionPerceptionResult(
                status = AndroidVisionPerceptionStatus.AVAILABLE,
                inventory = AndroidCameraInventory(listOf(camera)),
            )

        val result =
            AndroidCameraCapabilityIntegrationCoordinator()
                .prepare(perception, foreign)

        assertEquals(AndroidCameraCapabilityIntegrationStatus.DEFERRED, result.status)
        assertNull(result.camera)
        assertNull(result.frameRequest)
    }

    @Test
    fun `no camera perception remains deferred`() {
        val perception =
            AndroidVisionPerceptionResult(
                status = AndroidVisionPerceptionStatus.NO_CAMERA,
                inventory = AndroidCameraInventory(emptyList()),
            )

        val result =
            AndroidCameraCapabilityIntegrationCoordinator()
                .prepare(perception, null)

        assertEquals(AndroidCameraCapabilityIntegrationStatus.DEFERRED, result.status)
        assertNull(result.frameRequest)
    }

    @Test
    fun `ready result rejects foreign camera provenance`() {
        val camera =
            AndroidCameraDescriptor.create(
                cameraId = "back-camera",
                facing = AndroidCameraFacing.BACK,
            )

        val foreign =
            AndroidCameraDescriptor.create(
                cameraId = "front-camera",
                facing = AndroidCameraFacing.FRONT,
            )

        val perception =
            AndroidVisionPerceptionResult(
                status = AndroidVisionPerceptionStatus.AVAILABLE,
                inventory = AndroidCameraInventory(listOf(camera)),
            )

        assertFailsWith<IllegalArgumentException> {
            AndroidCameraCapabilityIntegrationResult.create(
                status = AndroidCameraCapabilityIntegrationStatus.READY,
                perception = perception,
                camera = foreign,
                frameRequest =
                    AndroidVisionFrameRequest.create(
                        cameraId = foreign.cameraId,
                    ),
            )
        }
    }

    @Test
    fun `deferred result rejects prepared frame request`() {
        val perception =
            AndroidVisionPerceptionResult(
                status = AndroidVisionPerceptionStatus.NO_CAMERA,
                inventory = AndroidCameraInventory(emptyList()),
            )

        assertFailsWith<IllegalArgumentException> {
            AndroidCameraCapabilityIntegrationResult.create(
                status = AndroidCameraCapabilityIntegrationStatus.DEFERRED,
                perception = perception,
                frameRequest =
                    AndroidVisionFrameRequest.create(
                        cameraId = "camera",
                    ),
            )
        }
    }
}
