package com.devil.app.ui.launch

import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Stage 51 governance gate for the approved Devil Inside awakening.
 *
 * The approved Devil D remains the identity source.
 * Canvas is limited to environmental code and restrained halo treatment.
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
    fun `awakening uses approved Devil D and wordmark`() {
        val source =
            source()

        assertTrue(
            source.contains(
                "R.drawable.devil_launcher_foreground",
            ),
        )

        assertTrue(
            source.contains(
                "\"DEVIL INSIDE\"",
            ),
        )
    }

    @Test
    fun `awakening uses continuous vertical code environment`() {
        val source =
            source()

        assertTrue(
            source.contains(
                "DevilCodeRainLayer(",
            ),
        )

        assertTrue(
            source.contains(
                "nativeCanvas",
            ),
        )

        assertTrue(
            source.contains(
                "DevilLaunchTiming.TOTAL_DURATION_MILLIS -",
            ),
        )

        assertTrue(
            source.contains(
                "travelDistance",
            ),
        )
    }

    @Test
    fun `obsolete Alpha 5 circuit and card presentation are removed`() {
        val source =
            source()

        assertTrue(
            !source.contains(
                "DevilCircuitLayer(",
            ),
        )

        assertTrue(
            !source.contains(
                "circuitProgress",
            ),
        )

        assertTrue(
            !source.contains(
                "circuitCurrentProgress",
            ),
        )

        assertTrue(
            !source.contains(
                "R.drawable.devil_runtime_core",
            ),
        )

        assertTrue(
            !source.contains(
                "R.drawable.devil_primary_logo",
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
                "Devil D artwork != authentication.",
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
                "Awakening completion != runtime readiness.",
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
