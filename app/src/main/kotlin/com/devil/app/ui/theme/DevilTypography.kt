package com.devil.app.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * Stage 251 Final Design System typography.
 *
 * Devil deliberately remains on Android/Compose system font resources here.
 * Stage 251 does not introduce speculative branded-font assets or external
 * font-provider dependencies.
 *
 * The hierarchy is designed for a futuristic technical presentation:
 *
 * - strong identity headlines;
 * - restrained wide tracking for interface titles;
 * - highly readable conversation/body text;
 * - compact technical labels.
 *
 * Typography affects presentation only.
 *
 * TYPOGRAPHY != DEVIL_IDENTITY.
 * TYPOGRAPHY != AUTHORITY.
 * TYPOGRAPHY != RUNTIME_STATE.
 */
internal val DevilTypography =
    Typography(
        displaySmall =
            TextStyle(
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.Black,
                fontSize = 38.sp,
                lineHeight = 42.sp,
                letterSpacing = 2.2.sp,
            ),
        headlineLarge =
            TextStyle(
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.Black,
                fontSize = 34.sp,
                lineHeight = 38.sp,
                letterSpacing = 1.8.sp,
            ),
        headlineMedium =
            TextStyle(
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp,
                lineHeight = 34.sp,
                letterSpacing = 1.4.sp,
            ),
        headlineSmall =
            TextStyle(
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                lineHeight = 29.sp,
                letterSpacing = 1.0.sp,
            ),
        titleLarge =
            TextStyle(
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.Bold,
                fontSize = 21.sp,
                lineHeight = 27.sp,
                letterSpacing = 0.8.sp,
            ),
        titleMedium =
            TextStyle(
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                lineHeight = 24.sp,
                letterSpacing = 0.6.sp,
            ),
        titleSmall =
            TextStyle(
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp,
                lineHeight = 21.sp,
                letterSpacing = 0.5.sp,
            ),
        bodyLarge =
            TextStyle(
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                lineHeight = 24.sp,
                letterSpacing = 0.1.sp,
            ),
        bodyMedium =
            TextStyle(
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                lineHeight = 21.sp,
                letterSpacing = 0.1.sp,
            ),
        bodySmall =
            TextStyle(
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                lineHeight = 18.sp,
                letterSpacing = 0.2.sp,
            ),
        labelLarge =
            TextStyle(
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                lineHeight = 19.sp,
                letterSpacing = 0.7.sp,
            ),
        labelMedium =
            TextStyle(
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp,
                lineHeight = 17.sp,
                letterSpacing = 0.8.sp,
            ),
        labelSmall =
            TextStyle(
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.SemiBold,
                fontSize = 10.sp,
                lineHeight = 15.sp,
                letterSpacing = 1.0.sp,
            ),
    )
