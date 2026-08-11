package com.devil.app.ui.launch

import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Stage 51 governance gate for the presentation-only Devil awakening sequence.
 */
class Stage51DevilAwakeningGovernanceTest {

    @Test
    fun `awakening target remains approximately five seconds`() {
        assertEquals(
            5_000L,
            DevilLaunchTiming.TOTAL_DURATION_MILLIS,
        )
    }

    @Test
    fun `awakening exposes explicit presentation phases`() {
        assertEquals(
            listOf(
                DevilLaunchPhase.VOID,
                DevilLaunchPhase.CORE_IGNITION,
                DevilLaunchPhase.CORE_PULSE,
                DevilLaunchPhase.IDENTITY_REVEAL,
                DevilLaunchPhase.WORDMARK,
                DevilLaunchPhase.COMPLETE,
            ),
            DevilLaunchPhase.entries,
        )
    }

    @Test
    fun `awakening presentation does not claim runtime or authentication authority`() {
        val source =
            source(
                "app/src/main/kotlin/com/devil/app/ui/launch/DevilAwakeningScreen.kt",
                "src/main/kotlin/com/devil/app/ui/launch/DevilAwakeningScreen.kt",
            )

        assertTrue(
            source.contains(
                "Core animation != runtime readiness.",
            ),
        )

        assertTrue(
            source.contains(
                "Core animation != authentication.",
            ),
        )

        assertTrue(
            source.contains(
                "Core animation != authorization.",
            ),
        )

        assertTrue(
            source.contains(
                "Core animation != verified success.",
            ),
        )
    }

    private fun source(
        vararg candidates: String,
    ): String {
        val file =
            candidates
                .map(::File)
                .firstOrNull { it.isFile }
                ?: error(
                    "Unable to locate Stage 51 awakening source.",
                )

        return file.readText()
    }
}
