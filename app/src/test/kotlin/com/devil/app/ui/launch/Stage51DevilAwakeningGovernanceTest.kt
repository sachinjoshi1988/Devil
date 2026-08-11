package com.devil.app.ui.launch

import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Stage 51 governance gate for the presentation-only Devil awakening sequence.
 *
 * This test protects the approved image-driven launch identity while preserving
 * the constitutional boundary that launch presentation is not authority,
 * runtime readiness, execution, verification, Outcome, or memory state.
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
    fun `awakening uses approved artwork and preserves presentation only boundary`() {
        val source =
            source(
                "app/src/main/kotlin/com/devil/app/ui/launch/DevilAwakeningScreen.kt",
                "src/main/kotlin/com/devil/app/ui/launch/DevilAwakeningScreen.kt",
            )

        assertTrue(
            source.contains("R.drawable.devil_runtime_core"),
        )

        assertTrue(
            source.contains("R.drawable.devil_primary_logo"),
        )

        assertTrue(
            !source.contains("Canvas("),
        )

        assertTrue(
            !source.contains("drawCircle"),
        )

        assertTrue(
            !source.contains("drawLine"),
        )

        assertTrue(
            !source.contains("drawPath"),
        )

        assertTrue(
            source.contains("Runtime Core artwork != runtime readiness."),
        )

        assertTrue(
            source.contains("Primary Devil artwork != authentication."),
        )

        assertTrue(
            source.contains("Awakening animation != authorization."),
        )

        assertTrue(
            source.contains("Awakening animation != execution."),
        )

        assertTrue(
            source.contains("Awakening completion != verified success."),
        )

        assertTrue(
            source.contains("Awakening completion != Outcome."),
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
