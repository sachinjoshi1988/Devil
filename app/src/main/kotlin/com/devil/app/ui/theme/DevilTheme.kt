package com.devil.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

/**
 * Stage 251 Final Design System theme.
 *
 * This finalizes the Stage 51 dark Devil visual foundation into the reusable
 * theme contract for Phase R.
 *
 * Established identity remains:
 *
 * black
 * + infernal red
 * + white
 * + restrained supporting neutrals.
 *
 * Stage 251 establishes design-system presentation only.
 *
 * It does not:
 *
 * - redesign Stage 252 Startup Experience;
 * - redesign Stage 253 Main Conversation Experience;
 * - implement voice, memory, task, education, research, finance, security,
 *   settings, tablet, or accessibility interfaces;
 * - create or alter Devil identity;
 * - authenticate a subject;
 * - establish trust;
 * - grant authorization;
 * - enter Owner Mode;
 * - activate capabilities;
 * - create runtime state;
 * - execute actions;
 * - establish Observation, Verification, or Outcome;
 * - mutate World Model state;
 * - perform Learning;
 * - create, commit, persist, recall, or expose Memory.
 *
 * DESIGN_SYSTEM != AUTHENTICATION.
 * DESIGN_SYSTEM != AUTHORIZATION.
 * DESIGN_SYSTEM != RUNTIME_STATE.
 * DESIGN_SYSTEM != EXECUTION.
 * DESIGN_SYSTEM != VERIFICATION.
 * DESIGN_SYSTEM != MEMORY.
 * VISUAL_STATE != CONSTITUTIONAL_STATE.
 */
private val DevilDarkColorScheme =
    darkColorScheme(
        primary =
            DevilColorPalette.InfernalRed,
        onPrimary =
            DevilColorPalette.DevilWhite,
        primaryContainer =
            DevilColorPalette.EmberRed,
        onPrimaryContainer =
            DevilColorPalette.DevilWhite,
        secondary =
            DevilColorPalette.SoftWhite,
        onSecondary =
            DevilColorPalette.VoidBlack,
        secondaryContainer =
            DevilColorPalette.ElevatedBlack,
        onSecondaryContainer =
            DevilColorPalette.DevilWhite,
        tertiary =
            DevilColorPalette.SignalRed,
        onTertiary =
            DevilColorPalette.VoidBlack,
        tertiaryContainer =
            DevilColorPalette.DeepInfernalRed,
        onTertiaryContainer =
            DevilColorPalette.DevilWhite,
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
        outlineVariant =
            DevilColorPalette.StrongDivider,
        error =
            DevilColorPalette.ErrorRed,
        onError =
            DevilColorPalette.VoidBlack,
        errorContainer =
            DevilColorPalette.EmberRed,
        onErrorContainer =
            DevilColorPalette.DevilWhite,
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
        shapes =
            DevilShapes,
        content =
            content,
    )
}
