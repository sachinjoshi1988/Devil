package com.devil.app.vision

import java.io.File
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class Stage41Camera2CaptureGovernanceTest {

    @Test
    fun `production Camera2 source remains bounded to explicit frame capture`() {
        val source =
            productionSourceText()

        assertTrue(
            source.contains(
                "class DefaultAndroidVisionFrameSource",
            ),
        )

        assertTrue(
            source.contains(
                "manager.openCamera(",
            ),
        )

        assertTrue(
            source.contains(
                "CameraDevice.TEMPLATE_STILL_CAPTURE",
            ),
        )

        assertTrue(
            source.contains(
                "ImageFormat.JPEG",
            ),
        )

        assertTrue(
            source.contains(
                "AndroidVisionFrame.create(",
            ),
        )

        assertTrue(
            source.contains(
                "Manifest.permission.CAMERA",
            ),
        )
    }

    @Test
    fun `production Camera2 source does not create forbidden intelligence authorities`() {
        val source =
            productionSourceText()

        assertFalse(
            source.contains(
                "DefaultUnifiedDevilRuntime(",
            ),
        )

        assertFalse(
            source.contains(
                "ConversationInput.create(",
            ),
        )

        assertFalse(
            source.contains(
                "MemoryProposal",
            ),
        )

        assertFalse(
            source.contains(
                "performAction(",
            ),
        )
    }

    private fun productionSourceText(): String {
        val candidates =
            listOf(
                File(
                    "src/main/kotlin/com/devil/app/vision/DefaultAndroidVisionFrameSource.kt",
                ),
                File(
                    "app/src/main/kotlin/com/devil/app/vision/DefaultAndroidVisionFrameSource.kt",
                ),
            )

        val sourceFile =
            candidates.firstOrNull {
                it.isFile
            }
                ?: error(
                    "Unable to locate Stage 41 production Camera2 source.",
                )

        return sourceFile.readText()
    }
}
