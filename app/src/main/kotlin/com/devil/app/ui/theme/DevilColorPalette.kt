package com.devil.app.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * Stage 251 Final Design System color architecture.
 *
 * This finalizes and extends the Stage 51 visual-identity foundation without
 * replacing Devil's established black / infernal-red / white identity.
 *
 * The palette supports a high-contrast futuristic presentation language:
 *
 * - near-black environmental depth;
 * - layered black surfaces;
 * - infernal red identity/accent energy;
 * - restrained luminous-red emphasis;
 * - white foreground hierarchy;
 * - neutral structural borders.
 *
 * Color is presentation only.
 *
 * It does not establish:
 *
 * - Devil identity;
 * - authentication;
 * - trust;
 * - authorization;
 * - Owner Mode;
 * - capability availability;
 * - runtime readiness;
 * - execution;
 * - Observation;
 * - Verification;
 * - Outcome;
 * - Memory state.
 *
 * DESIGN_COLOR != CONSTITUTIONAL_STATE.
 * DESIGN_COLOR != SECURITY_STATE.
 * DESIGN_COLOR != EXECUTION_STATE.
 * DESIGN_COLOR != VERIFIED_STATE.
 */
internal object DevilColorPalette {

    /**
     * Primary environmental blacks.
     */
    val VoidBlack =
        Color(0xFF050505)

    val AbyssBlack =
        Color(0xFF080808)

    val SurfaceBlack =
        Color(0xFF0D0D0D)

    val ElevatedBlack =
        Color(0xFF151515)

    val GlassBlack =
        Color(0xE6121212)

    /**
     * Devil identity reds.
     */
    val InfernalRed =
        Color(0xFFE10600)

    val SignalRed =
        Color(0xFFFF241C)

    val DeepInfernalRed =
        Color(0xFF800000)

    val EmberRed =
        Color(0xFF4A0808)

    val InfernalGlow =
        Color(0xFFFF3B30)

    /**
     * Foreground hierarchy.
     */
    val DevilWhite =
        Color(0xFFF5F5F5)

    val SoftWhite =
        Color(0xFFD9D9D9)

    val MutedWhite =
        Color(0xFFAAAAAA)

    val DimmedWhite =
        Color(0xFF747474)

    /**
     * Structural presentation tones.
     */
    val Divider =
        Color(0xFF2A2A2A)

    val StrongDivider =
        Color(0xFF3A3A3A)

    /**
     * Presentation-only error emphasis.
     *
     * Error presentation does not itself establish a constitutional failure.
     */
    val ErrorRed =
        Color(0xFFFF5252)
}
