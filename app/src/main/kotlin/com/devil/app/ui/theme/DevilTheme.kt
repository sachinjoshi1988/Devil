package com.devil.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

/**
 * Stage 51 Devil presentation theme.
 *
 * The theme is intentionally dark-only for the original Owner Alpha visual
 * identity established for Devil.
 *
 * Theme state is presentation only.
 *
 * It does not:
 *
 * - create or alter Devil identity;
 * - establish authentication;
 * - establish a security stage or session;
 * - grant authorization;
 * - activate capabilities;
 * - represent execution, observation, verification, or Outcome;
 * - create or persist logical memory.
 */
private val DevilDarkColorScheme =
    darkColorScheme(
        primary =
            DevilColorPalette.InfernalRed,
        onPrimary =
            DevilColorPalette.DevilWhite,
        primaryContainer =
            DevilColorPalette.DeepInfernalRed,
        onPrimaryContainer =
            DevilColorPalette.DevilWhite,
        secondary =
            DevilColorPalette.MutedWhite,
        onSecondary =
            DevilColorPalette.VoidBlack,
        background =
            DevilColorPalette.VoidBlack,
        onBackground =
            DevilColorPalette.DevilWhite,
        surface =
            DevilColorPalette.SurfaceBlack,
        onSurface =
            DevilColorPalette.DevilWhite,
        surfaceVariant =
            DevilColorPalette.ElevatedBlack,
        onSurfaceVariant =
            DevilColorPalette.MutedWhite,
        outline =
            DevilColorPalette.Divider,
        error =
            DevilColorPalette.ErrorRed,
        onError =
            DevilColorPalette.VoidBlack,
    )

@Composable
fun DevilTheme(
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme =
            DevilDarkColorScheme,
        typography =
            DevilTypography,
        content =
            content,
    )
}
