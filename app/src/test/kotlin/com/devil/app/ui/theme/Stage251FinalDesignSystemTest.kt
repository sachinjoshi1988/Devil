package com.devil.app.ui.theme

import java.io.File
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Stage 251 Final Design System governance tests.
 *
 * These tests protect the final Phase R design-system foundation without
 * implementing Stage 252 Startup Experience or any later Phase R screen.
 */
class Stage251FinalDesignSystemTest {

    @Test
    fun `final design system preserves locked Devil black red white identity`() {
        val palette =
            source(
                "app/src/main/kotlin/com/devil/app/ui/theme/DevilColorPalette.kt",
                "src/main/kotlin/com/devil/app/ui/theme/DevilColorPalette.kt",
            )

        assertTrue(palette.contains("VoidBlack"))
        assertTrue(palette.contains("InfernalRed"))
        assertTrue(palette.contains("DevilWhite"))
        assertTrue(palette.contains("SignalRed"))
        assertTrue(palette.contains("InfernalGlow"))
    }

    @Test
    fun `final Devil theme remains dark only`() {
        val theme =
            source(
                "app/src/main/kotlin/com/devil/app/ui/theme/DevilTheme.kt",
                "src/main/kotlin/com/devil/app/ui/theme/DevilTheme.kt",
            )

        assertTrue(theme.contains("darkColorScheme("))
        assertFalse(theme.contains("lightColorScheme("))
        assertTrue(theme.contains("DevilTypography"))
        assertTrue(theme.contains("DevilShapes"))
    }

    @Test
    fun `final design system includes futuristic angular shape language`() {
        val shapes =
            source(
                "app/src/main/kotlin/com/devil/app/ui/theme/DevilShapes.kt",
                "src/main/kotlin/com/devil/app/ui/theme/DevilShapes.kt",
            )

        assertTrue(shapes.contains("CutCornerShape"))
        assertTrue(shapes.contains("DESIGN_SHAPE != CONSTITUTIONAL_STATE"))
    }

    @Test
    fun `final typography exposes complete reusable hierarchy`() {
        val typography =
            source(
                "app/src/main/kotlin/com/devil/app/ui/theme/DevilTypography.kt",
                "src/main/kotlin/com/devil/app/ui/theme/DevilTypography.kt",
            )

        assertTrue(typography.contains("displaySmall"))
        assertTrue(typography.contains("headlineLarge"))
        assertTrue(typography.contains("titleLarge"))
        assertTrue(typography.contains("bodyLarge"))
        assertTrue(typography.contains("labelLarge"))
    }

    @Test
    fun `locked Devil logo assets remain present`() {
        assertTrue(assetExists("drawable/devil_primary_logo.png"))
        assertTrue(assetExists("drawable/devil_launcher_foreground.png"))
        assertTrue(assetExists("drawable/devil_splash_foreground.png"))
    }

    @Test
    fun `Stage 251 does not redesign Stage 252 startup experience`() {
        val theme =
            source(
                "app/src/main/kotlin/com/devil/app/ui/theme/DevilTheme.kt",
                "src/main/kotlin/com/devil/app/ui/theme/DevilTheme.kt",
            )

        assertTrue(theme.contains("does not"))
        assertTrue(theme.contains("Stage 252 Startup Experience"))
    }

    private fun assetExists(relativePath: String): Boolean {
        val candidates =
            listOf(
                File("app/src/main/res/$relativePath"),
                File("src/main/res/$relativePath"),
            )

        return candidates.any { it.exists() }
    }

    private fun source(
        vararg candidates: String,
    ): String {
        val file =
            candidates
                .map(::File)
                .firstOrNull { it.exists() }
                ?: error(
                    "Unable to locate Stage 251 production source from: " +
                        candidates.joinToString(),
                )

        return file.readText()
    }
}
