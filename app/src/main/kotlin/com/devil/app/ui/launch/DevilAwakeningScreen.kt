package com.devil.app.ui.launch

import android.graphics.Paint
import android.graphics.Typeface
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devil.app.R
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Stage 51 presentation-only Devil awakening.
 *
 * Approved visual sequence:
 *
 * black void
 * -> continuous vertical red code rain
 * -> large approved Devil D emerges from center
 * -> DEVIL INSIDE wordmark
 * -> brief identity hold
 * -> application
 *
 * Environmental animation != Devil identity.
 * Devil D artwork != authentication.
 * Awakening animation != authorization.
 * Awakening animation != execution.
 * Awakening completion != runtime readiness.
 * Awakening completion != verified success.
 * Awakening completion != Outcome.
 */
@Composable
fun DevilAwakeningScreen(
    onComplete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val backgroundColor =
        MaterialTheme.colorScheme.background

    val devilRed =
        MaterialTheme.colorScheme.primary

    val foregroundColor =
        MaterialTheme.colorScheme.onBackground

    val codeProgress =
        remember {
            Animatable(0f)
        }

    val codeAlpha =
        remember {
            Animatable(0f)
        }

    val identityAlpha =
        remember {
            Animatable(0f)
        }

    val identityScale =
        remember {
            Animatable(0.48f)
        }

    val identityGlow =
        remember {
            Animatable(0f)
        }

    val wordmarkAlpha =
        remember {
            Animatable(0f)
        }

    val wordmarkScale =
        remember {
            Animatable(0.94f)
        }

    val codePaint =
        remember {
            Paint(
                Paint.ANTI_ALIAS_FLAG,
            ).apply {
                typeface =
                    Typeface.create(
                        Typeface.MONOSPACE,
                        Typeface.NORMAL,
                    )
            }
        }

    LaunchedEffect(Unit) {
        /*
         * Phase 1 — Void.
         *
         * Presentation pause only.
         */
        delay(
            DevilLaunchTiming.VOID_DURATION_MILLIS,
        )

        /*
         * Continuous code travel runs throughout the complete post-void
         * awakening without extending the five-second launch contract.
         */
        launch {
            codeProgress.animateTo(
                targetValue = 4.8f,
                animationSpec =
                    tween(
                        durationMillis =
                            (
                                DevilLaunchTiming.TOTAL_DURATION_MILLIS -
                                    DevilLaunchTiming.VOID_DURATION_MILLIS
                            ).toInt(),
                        easing =
                            LinearEasing,
                    ),
            )
        }

        /*
         * Phase 2 — Falling-code field comes alive.
         */
        codeAlpha.animateTo(
            targetValue = 0.88f,
            animationSpec =
                tween(
                    durationMillis =
                        DevilLaunchTiming
                            .CORE_IGNITION_DURATION_MILLIS
                            .toInt(),
                    easing =
                        FastOutSlowInEasing,
                ),
        )

        /*
         * Phase 3 — Approved Devil D emerges from the center.
         *
         * No alternate identity geometry is created here.
         */
        coroutineScope {
            launch {
                identityAlpha.animateTo(
                    targetValue = 1f,
                    animationSpec =
                        tween(
                            durationMillis =
                                DevilLaunchTiming
                                    .CORE_PULSE_DURATION_MILLIS
                                    .toInt(),
                            easing =
                                FastOutSlowInEasing,
                        ),
                )
            }

            launch {
                identityScale.animateTo(
                    targetValue = 1f,
                    animationSpec =
                        tween(
                            durationMillis =
                                DevilLaunchTiming
                                    .CORE_PULSE_DURATION_MILLIS
                                    .toInt(),
                            easing =
                                FastOutSlowInEasing,
                        ),
                )
            }

            launch {
                identityGlow.animateTo(
                    targetValue = 1f,
                    animationSpec =
                        tween(
                            durationMillis =
                                DevilLaunchTiming
                                    .CORE_PULSE_DURATION_MILLIS
                                    .toInt(),
                            easing =
                                FastOutSlowInEasing,
                        ),
                )
            }

            launch {
                codeAlpha.animateTo(
                    targetValue = 0.62f,
                    animationSpec =
                        tween(
                            durationMillis =
                                DevilLaunchTiming
                                    .CORE_PULSE_DURATION_MILLIS
                                    .toInt(),
                        ),
                )
            }
        }

        /*
         * Phase 4 — DEVIL INSIDE revelation.
         */
        coroutineScope {
            launch {
                wordmarkAlpha.animateTo(
                    targetValue = 1f,
                    animationSpec =
                        tween(
                            durationMillis =
                                DevilLaunchTiming
                                    .IDENTITY_REVEAL_DURATION_MILLIS
                                    .toInt(),
                            easing =
                                FastOutSlowInEasing,
                        ),
                )
            }

            launch {
                wordmarkScale.animateTo(
                    targetValue = 1f,
                    animationSpec =
                        tween(
                            durationMillis =
                                DevilLaunchTiming
                                    .IDENTITY_REVEAL_DURATION_MILLIS
                                    .toInt(),
                            easing =
                                FastOutSlowInEasing,
                        ),
                )
            }

            launch {
                codeAlpha.animateTo(
                    targetValue = 0.48f,
                    animationSpec =
                        tween(
                            durationMillis =
                                DevilLaunchTiming
                                    .IDENTITY_REVEAL_DURATION_MILLIS
                                    .toInt(),
                        ),
                )
            }
        }

        /*
         * Phase 5 — Identity hold.
         */
        delay(
            DevilLaunchTiming.WORDMARK_DURATION_MILLIS,
        )

        onComplete()
    }

    Box(
        modifier =
            modifier
                .fillMaxSize()
                .background(
                    backgroundColor,
                ),
        contentAlignment =
            Alignment.Center,
    ) {
        DevilCodeRainLayer(
            progress =
                codeProgress.value,
            alpha =
                codeAlpha.value,
            color =
                devilRed,
            paint =
                codePaint,
            modifier =
                Modifier.fillMaxSize(),
        )

        DevilIdentityHalo(
            alpha =
                identityGlow.value,
            color =
                devilRed,
            modifier =
                Modifier.fillMaxSize(),
        )

        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .alpha(
                        identityAlpha.value,
                    ),
            horizontalAlignment =
                Alignment.CenterHorizontally,
        ) {
            Image(
                painter =
                    painterResource(
                        id =
                            R.drawable.devil_launcher_foreground,
                    ),
                contentDescription =
                    "Devil",
                modifier =
                    Modifier
                        .fillMaxWidth(0.96f)
                        .sizeIn(
                            maxWidth = 620.dp,
                            maxHeight = 620.dp,
                        )
                        .scale(
                            identityScale.value,
                        ),
                contentScale =
                    ContentScale.Fit,
            )

            Spacer(
                modifier =
                    Modifier.height(
                        2.dp,
                    ),
            )

            Text(
                text =
                    "DEVIL INSIDE",
                modifier =
                    Modifier
                        .scale(
                            wordmarkScale.value,
                        )
                        .alpha(
                            wordmarkAlpha.value,
                        ),
                color =
                    foregroundColor,
                fontSize =
                    28.sp,
                fontWeight =
                    FontWeight.Black,
                letterSpacing =
                    4.8.sp,
            )
        }
    }
}

/**
 * Stage 252 continuous full-screen vertical Devil code rain.
 *
 * Binary streams fall in fixed straight columns from top to bottom.
 * Columns may vary in speed, phase, brightness, and trail intensity,
 * but their horizontal position never changes.
 *
 * No rotation, radial travel, orbiting, diagonal motion, or sideways drift.
 *
 * Code is environmental presentation only.
 *
 * CODE_RAIN != DEVIL_IDENTITY.
 * CODE_RAIN != RUNTIME_STATE.
 * CODE_RAIN != AUTHENTICATION.
 * CODE_RAIN != AUTHORIZATION.
 * CODE_RAIN != EXECUTION.
 * CODE_RAIN != VERIFICATION.
 */
@Composable
private fun DevilCodeRainLayer(
    progress: Float,
    alpha: Float,
    color: Color,
    paint: Paint,
    modifier: Modifier = Modifier,
) {
    Canvas(
        modifier = modifier,
    ) {
        if (alpha <= 0f) {
            return@Canvas
        }

        paint.color = color.toArgb()
        paint.textSize = 14.dp.toPx()

        val columnCount = 28
        val columnWidth =
            size.width /
                columnCount.toFloat()

        val rowSpacing = 21.dp.toPx()
        val streamLength = 22
        val trailHeight =
            streamLength *
                rowSpacing

        val travelDistance =
            size.height +
                trailHeight +
                80.dp.toPx()

        repeat(columnCount) { column ->
            val x =
                columnWidth *
                    (
                        column.toFloat() +
                            0.5f
                    )

            val speed =
                0.78f +
                    (
                        column %
                            6
                    ) *
                    0.055f

            val stagger =
                (
                    column *
                        0.137f
                ) % 1f

            val normalizedTravel =
                (
                    progress *
                        speed +
                        stagger
                ) % 1f

            val headY =
                normalizedTravel *
                    travelDistance -
                    trailHeight

            repeat(streamLength) { row ->
                val glyphY =
                    headY -
                        row *
                        rowSpacing

                if (
                    glyphY >= -40.dp.toPx() &&
                    glyphY <= size.height + 40.dp.toPx()
                ) {
                    val trailFade =
                        (
                            1f -
                                row.toFloat() /
                                streamLength.toFloat()
                        )
                            .coerceIn(
                                0.08f,
                                1f,
                            )

                    val depthVariation =
                        when (
                            (
                                column +
                                    row
                            ) % 9
                        ) {
                            0 -> 1f
                            1, 2 -> 0.78f
                            else -> 0.52f
                        }

                    paint.alpha =
                        (
                            255f *
                                alpha *
                                trailFade *
                                depthVariation
                        )
                            .toInt()
                            .coerceIn(
                                0,
                                255,
                            )

                    val glyph =
                        if (
                            (
                                column * 17 +
                                    row * 31
                            ) % 2 == 0
                        ) {
                            "0"
                        } else {
                            "1"
                        }

                    drawContext
                        .canvas
                        .nativeCanvas
                        .drawText(
                            glyph,
                            x,
                            glyphY,
                            paint,
                        )
                }
            }
        }
    }
}


/**
 * Restrained circular energy treatment behind the approved Devil mark.
 *
 * Halo != runtime readiness.
 * Halo != authority.
 */
@Composable
private fun DevilIdentityHalo(
    alpha: Float,
    color: Color,
    modifier: Modifier = Modifier,
) {
    Canvas(
        modifier =
            modifier,
    ) {
        if (alpha <= 0f) {
            return@Canvas
        }

        val center =
            Offset(
                x =
                    size.width /
                        2f,
                y =
                    size.height *
                        0.46f,
            )

        val radius =
            size.minDimension *
                0.37f

        drawCircle(
            color =
                color.copy(
                    alpha =
                        alpha *
                            0.055f,
                ),
            radius =
                radius *
                    1.16f,
            center =
                center,
        )

        drawCircle(
            color =
                color.copy(
                    alpha =
                        alpha *
                            0.12f,
                ),
            radius =
                radius,
            center =
                center,
            style =
                Stroke(
                    width =
                        1.1.dp.toPx(),
                ),
        )

        drawCircle(
            color =
                color.copy(
                    alpha =
                        alpha *
                            0.08f,
                ),
            radius =
                radius *
                    0.84f,
            center =
                center,
            style =
                Stroke(
                    width =
                        0.8.dp.toPx(),
                ),
        )

        drawLine(
            color =
                color.copy(
                    alpha =
                        alpha *
                            0.32f,
                ),
            start =
                Offset(
                    x = 0f,
                    y = center.y,
                ),
            end =
                Offset(
                    x = size.width,
                    y = center.y,
                ),
            strokeWidth =
                0.8.dp.toPx(),
            cap =
                StrokeCap.Round,
        )

        drawCircle(
            color =
                Color.White.copy(
                    alpha =
                        alpha *
                            0.74f,
                ),
            radius =
                2.4.dp.toPx(),
            center =
                Offset(
                    x =
                        center.x -
                            radius *
                            0.77f,
                    y =
                        center.y,
                ),
        )
    }
}
