package com.devil.app.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * Stage 51 typography foundation for Devil.
 *
 * This intentionally uses Android/Compose system font resources for the
 * Owner Alpha. Final branded typography may be selected later without
 * changing Devil's constitutional architecture.
 */
internal val DevilTypography =
    Typography(
        headlineMedium =
            TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp,
                letterSpacing = 1.2.sp,
            ),
        titleMedium =
            TextStyle(
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                letterSpacing = 0.5.sp,
            ),
        bodyLarge =
            TextStyle(
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                lineHeight = 23.sp,
            ),
        bodyMedium =
            TextStyle(
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                lineHeight = 20.sp,
            ),
        bodySmall =
            TextStyle(
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                lineHeight = 17.sp,
            ),
        labelMedium =
            TextStyle(
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp,
                letterSpacing = 0.6.sp,
            ),
    )
