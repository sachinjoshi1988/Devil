package com.devil.app.vision

import java.io.File
import kotlin.test.Test
import kotlin.test.assertTrue

class Stage41VisionFrameProductionCompositionTest {

    @Test
    fun `DevilApplication owns bounded Stage 41 frame perception composition`() {
        val applicationSource =
            applicationSourceText()

        assertTrue(
            applicationSource.contains(
                "DefaultAndroidVisionFrameSource",
            ),
        )

        assertTrue(
            applicationSource.contains(
                "AndroidVisionFramePerceptionCoordinator",
            ),
        )

        assertTrue(
            applicationSource.contains(
                "visionFramePerceptionCoordinator",
            ),
        )

        assertTrue(
            applicationSource.contains(
                "context = applicationContext",
            ),
        )
    }

    private fun applicationSourceText(): String {
        val candidates =
            listOf(
                File(
                    "src/main/kotlin/com/devil/app/DevilApplication.kt",
                ),
                File(
                    "app/src/main/kotlin/com/devil/app/DevilApplication.kt",
                ),
            )

        val sourceFile =
            candidates.firstOrNull {
                it.isFile
            }
                ?: error(
                    "Unable to locate DevilApplication production source.",
                )

        return sourceFile.readText()
    }
}
