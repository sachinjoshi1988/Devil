package com.devil.app.ui.launch

import java.io.File
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Stage 252 Devil Startup Experience governance.
 *
 * Protects the approved startup refinement:
 *
 * - existing Devil awakening remains intact;
 * - approved D identity asset remains in use;
 * - binary code rain travels vertically in fixed columns;
 * - no spinning, radial, orbital, diagonal, or horizontal code movement.
 */
class Stage252DevilStartupExperienceTest {

    @Test
    fun `startup preserves approved Devil D identity asset`() {
        val source = awakeningSource()

        assertTrue(
            source.contains("R.drawable.devil_launcher_foreground"),
        )
        assertTrue(
            source.contains("\"DEVIL INSIDE\""),
        )
    }

    @Test
    fun `code rain uses fixed vertical columns`() {
        val source = awakeningSource()

        assertTrue(
            source.contains("Stage 252 continuous full-screen vertical Devil code rain"),
        )
        assertTrue(
            source.contains("val x ="),
        )
        assertTrue(
            source.contains("val headY ="),
        )
        assertTrue(
            source.contains("normalizedTravel *"),
        )
        assertTrue(
            source.contains("glyphY ="),
        )
    }

    @Test
    fun `code rain explicitly prohibits spinning and horizontal motion`() {
        val source = awakeningSource()

        assertTrue(
            source.contains("No rotation, radial travel, orbiting, diagonal motion, or sideways drift"),
        )

        assertFalse(
            source.contains(".rotate("),
        )
        assertFalse(
            source.contains("sin("),
        )
        assertFalse(
            source.contains("cos("),
        )
    }

    @Test
    fun `startup presentation remains non authoritative`() {
        val source = awakeningSource()

        assertTrue(source.contains("CODE_RAIN != DEVIL_IDENTITY"))
        assertTrue(source.contains("CODE_RAIN != AUTHENTICATION"))
        assertTrue(source.contains("CODE_RAIN != AUTHORIZATION"))
        assertTrue(source.contains("CODE_RAIN != EXECUTION"))
        assertTrue(source.contains("CODE_RAIN != VERIFICATION"))
    }

    private fun awakeningSource(): String {
        val candidates =
            listOf(
                File(
                    "app/src/main/kotlin/com/devil/app/ui/launch/DevilAwakeningScreen.kt",
                ),
                File(
                    "src/main/kotlin/com/devil/app/ui/launch/DevilAwakeningScreen.kt",
                ),
            )

        return candidates
            .firstOrNull { it.exists() }
            ?.readText()
            ?: error(
                "Unable to locate DevilAwakeningScreen production source.",
            )
    }
}
