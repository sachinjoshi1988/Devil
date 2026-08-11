package com.devil.app.ui.theme

import java.io.File
import kotlin.test.Test
import kotlin.test.assertTrue

/**
 * Stage 51 governance gate for Devil's visual identity foundation.
 *
 * This test protects the selected black/red/white presentation direction and
 * ensures the Android launcher surface uses the dedicated Devil theme instead
 * of falling back to an unbounded default MaterialTheme.
 */
class Stage51DevilVisualIdentityFoundationTest {

    @Test
    fun `devil theme preserves dark black red and white identity`() {
        val source =
            source(
                "app/src/main/kotlin/com/devil/app/ui/theme/DevilTheme.kt",
                "src/main/kotlin/com/devil/app/ui/theme/DevilTheme.kt",
            )

        assertTrue(
            source.contains("darkColorScheme("),
        )

        assertTrue(
            source.contains("DevilColorPalette.InfernalRed"),
        )

        assertTrue(
            source.contains("DevilColorPalette.VoidBlack"),
        )

        assertTrue(
            source.contains("DevilColorPalette.DevilWhite"),
        )
    }

    @Test
    fun `devil theme does not introduce light presentation identity`() {
        val source =
            source(
                "app/src/main/kotlin/com/devil/app/ui/theme/DevilTheme.kt",
                "src/main/kotlin/com/devil/app/ui/theme/DevilTheme.kt",
            )

        assertTrue(
            !source.contains("lightColorScheme("),
        )
    }

    @Test
    fun `launcher activity uses dedicated Devil theme`() {
        val source =
            source(
                "app/src/main/kotlin/com/devil/app/DevilActivity.kt",
                "src/main/kotlin/com/devil/app/DevilActivity.kt",
            )

        assertTrue(
            source.contains("import com.devil.app.ui.theme.DevilTheme"),
        )

        assertTrue(
            source.contains("DevilTheme {"),
        )
    }

    private fun source(
        vararg candidates: String,
    ): String {
        val sourceFile =
            candidates
                .map(::File)
                .firstOrNull { it.isFile }
                ?: error(
                    "Unable to locate required Stage 51 production source.",
                )

        return sourceFile.readText()
    }
}
