package com.devil.app.ui.launch

import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Stage 51 governance gate for the presentation-only Devil awakening sequence.
 *
 * Approved artwork remains Devil's identity source.
 *
 * Canvas drawing is permitted only for environmental launch presentation,
 * including code rain, circuit traces, bounded energy nodes, and glow.
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
    fun `awakening uses approved Devil artwork`() {
        val source =
            source()

        assertTrue(
            source.contains(
                "R.drawable.devil_runtime_core",
            ),
        )

        assertTrue(
            source.contains(
                "R.drawable.devil_primary_logo",
            ),
        )
    }

    @Test
    fun `awakening contains full screen code and circuit environment`() {
        val source =
            source()

        assertTrue(
            source.contains(
                "DevilCodeRainLayer(",
            ),
        )

        assertTrue(
            source.contains(
                "DevilCircuitLayer(",
            ),
        )

        assertTrue(
            source.contains(
                "DevilIdentityGlowLayer(",
            ),
        )

        assertTrue(
            source.contains(
                "nativeCanvas",
            ),
        )
    }

    @Test
    fun `environmental canvas does not reconstruct Devil identity`() {
        val source =
            source()

        assertTrue(
            !source.contains(
                "drawPath(",
            ),
        )

        assertTrue(
            !source.contains(
                "text = \"D\"",
            ),
        )

        assertTrue(
            !source.contains(
                "text = \"DEVIL\"",
            ),
        )

        assertTrue(
            !source.contains(
                "Custom horned-D identity",
            ),
        )

        assertTrue(
            !source.contains(
                "Lucifer horns",
            ),
        )
    }

    @Test
    fun `awakening preserves presentation only boundary`() {
        val source =
            source()

        assertTrue(
            source.contains(
                "Environmental animation != Devil identity.",
            ),
        )

        assertTrue(
            source.contains(
                "Runtime Core artwork != runtime readiness.",
            ),
        )

        assertTrue(
            source.contains(
                "Primary Devil artwork != authentication.",
            ),
        )

        assertTrue(
            source.contains(
                "Awakening animation != authorization.",
            ),
        )

        assertTrue(
            source.contains(
                "Awakening animation != execution.",
            ),
        )

        assertTrue(
            source.contains(
                "Awakening completion != verified success.",
            ),
        )

        assertTrue(
            source.contains(
                "Awakening completion != Outcome.",
            ),
        )
    }

    private fun source(): String {
        val candidates =
            listOf(
                "app/src/main/kotlin/com/devil/app/ui/launch/DevilAwakeningScreen.kt",
                "src/main/kotlin/com/devil/app/ui/launch/DevilAwakeningScreen.kt",
            )

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
