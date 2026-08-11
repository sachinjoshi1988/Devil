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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.devil.app.R
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Stage 51 presentation-only Devil awakening sequence.
 *
 * Approved Devil artwork remains the source of identity.
 *
 * Native Compose drawing is used only for environmental presentation:
 * falling code, circuit traces, electrical nodes, and bounded glow.
 *
 * Environmental animation != Devil identity.
 * Runtime Core artwork != runtime readiness.
 * Primary Devil artwork != authentication.
 * Awakening animation != authorization.
 * Awakening animation != execution.
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

    val codeProgress =
        remember {
            Animatable(0f)
        }

    val codeAlpha =
        remember {
            Animatable(0f)
        }

    val coreAlpha =
        remember {
            Animatable(0f)
        }

    val coreScale =
        remember {
            Animatable(0.86f)
        }

    val circuitProgress =
        remember {
            Animatable(0f)
        }


    val circuitCurrentProgress =
        remember {
            Animatable(0f)
        }

    val circuitAlpha =
        remember {
            Animatable(0f)
        }

    val identityAlpha =
        remember {
            Animatable(0f)
        }

    val identityScale =
        remember {
            Animatable(0.90f)
        }

    val identityGlow =
        remember {
            Animatable(0f)
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
         * Android has handed presentation to Compose.
         * This remains presentation state only.
         */
        delay(
            DevilLaunchTiming.VOID_DURATION_MILLIS,
        )

        /*
         * Phase 2 — Code rain + Runtime Core ignition.
         *
         * Falling code fills the otherwise empty display while the approved
         * Runtime Core artwork wakes in the center.
         */
        /*
         * Code travel runs concurrently with the complete remaining awakening.
         *
         * It is environmental presentation only and must not lengthen or block
         * the existing five-second launch sequence.
         */
        launch {
            codeProgress.animateTo(
                targetValue = 4.5f,
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
         * Phase 2 — Code rain + Runtime Core ignition.
         *
         * Falling code fills the otherwise empty display while the approved
         * Runtime Core artwork wakes in the center.
         */
        coroutineScope {
            launch {
                codeAlpha.animateTo(
                    targetValue = 0.92f,
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
            }

            launch {
                coreAlpha.animateTo(
                    targetValue = 1f,
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
            }
        }

        /*
         * Electrical current begins with circuit propagation and continues
         * through the remaining awakening.
         *
         * Moving circuit energy is presentation only.
         */
        launch {
            circuitCurrentProgress.animateTo(
                targetValue = 3.6f,
                animationSpec =
                    tween(
                        durationMillis =
                            (
                                DevilLaunchTiming.CORE_PULSE_DURATION_MILLIS +
                                    DevilLaunchTiming.IDENTITY_REVEAL_DURATION_MILLIS +
                                    DevilLaunchTiming.WORDMARK_DURATION_MILLIS
                            ).toInt(),
                        easing =
                            LinearEasing,
                    ),
            )
        }

        /*
         * Phase 3 — Runtime Core pulse + circuit propagation.
         *
         * Circuit traces spread across the screen while the approved Runtime
         * Core pulses. The circuit is visual energy, not execution evidence.
         */
        coroutineScope {
            launch {
                coreScale.animateTo(
                    targetValue = 1.045f,
                    animationSpec =
                        tween(
                            durationMillis =
                                (
                                    DevilLaunchTiming
                                        .CORE_PULSE_DURATION_MILLIS /
                                        2L
                                ).toInt(),
                            easing =
                                FastOutSlowInEasing,
                        ),
                )

                coreScale.animateTo(
                    targetValue = 1f,
                    animationSpec =
                        tween(
                            durationMillis =
                                (
                                    DevilLaunchTiming
                                        .CORE_PULSE_DURATION_MILLIS /
                                        2L
                                ).toInt(),
                            easing =
                                FastOutSlowInEasing,
                        ),
                )
            }

            launch {
                circuitAlpha.animateTo(
                    targetValue = 1f,
                    animationSpec =
                        tween(
                            durationMillis = 420,
                            easing =
                                FastOutSlowInEasing,
                        ),
                )
            }

            launch {
                circuitProgress.animateTo(
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
                    targetValue = 0.48f,
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
         * Phase 4 — Devil identity convergence.
         *
         * The circuit remains alive while the Runtime Core yields to the
         * approved Primary Devil artwork.
         */
        coroutineScope {
            launch {
                identityAlpha.animateTo(
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
                identityScale.animateTo(
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
                identityGlow.animateTo(
                    targetValue = 1f,
                    animationSpec =
                        tween(
                            durationMillis =
                                DevilLaunchTiming
                                    .IDENTITY_REVEAL_DURATION_MILLIS
                                    .toInt(),
                        ),
                )
            }

            launch {
                coreAlpha.animateTo(
                    targetValue = 0f,
                    animationSpec =
                        tween(
                            durationMillis =
                                DevilLaunchTiming
                                    .IDENTITY_REVEAL_DURATION_MILLIS
                                    .toInt(),
                        ),
                )
            }

            launch {
                codeAlpha.animateTo(
                    targetValue = 0.20f,
                    animationSpec =
                        tween(
                            durationMillis =
                                DevilLaunchTiming
                                    .IDENTITY_REVEAL_DURATION_MILLIS
                                    .toInt(),
                        ),
                )
            }

            launch {
                circuitAlpha.animateTo(
                    targetValue = 0.68f,
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
         * Phase 5 — Identity lock.
         *
         * Hold the approved Devil identity in the living circuit environment.
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

        DevilCircuitLayer(
            progress =
                circuitProgress.value,
            currentProgress =
                circuitCurrentProgress.value,
            alpha =
                circuitAlpha.value,
            color =
                devilRed,
            modifier =
                Modifier.fillMaxSize(),
        )

        DevilIdentityGlowLayer(
            alpha =
                identityGlow.value,
            color =
                devilRed,
            modifier =
                Modifier.fillMaxSize(),
        )

        Image(
            painter =
                painterResource(
                    id =
                        R.drawable.devil_runtime_core,
                ),
            contentDescription =
                null,
            modifier =
                Modifier
                    .fillMaxWidth(0.88f)
                    .sizeIn(
                        maxWidth = 520.dp,
                        maxHeight = 500.dp,
                    )
                    .scale(
                        coreScale.value,
                    )
                    .alpha(
                        coreAlpha.value,
                    ),
            contentScale =
                ContentScale.Fit,
        )

        Image(
            painter =
                painterResource(
                    id =
                        R.drawable.devil_primary_logo,
                ),
            contentDescription =
                "Devil",
            modifier =
                Modifier
                    .fillMaxWidth(0.92f)
                    .sizeIn(
                        maxWidth = 560.dp,
                        maxHeight = 540.dp,
                    )
                    .scale(
                        identityScale.value,
                    )
                    .alpha(
                        identityAlpha.value,
                    ),
            contentScale =
                ContentScale.Fit,
        )
    }
}

/**
 * Full-screen falling-code atmosphere.
 *
 * This layer intentionally contains no Devil identity geometry.
 */
@Composable
private fun DevilCodeRainLayer(
    progress: Float,
    alpha: Float,
    color: Color,
    paint: Paint,
    modifier: Modifier = Modifier,
) {
    val glyphRows =
        listOf(
            "010DEVIL101011001",
            "1100101010011010",
            "1011000110010111",
            "0110101101100101",
            "1001010110011100",
            "0011101001101010",
            "1010011100011011",
            "0101110010100110",
        )

    Canvas(
        modifier = modifier,
    ) {
        if (alpha <= 0f) {
            return@Canvas
        }

        paint.color =
            color.toArgb()

        paint.textSize =
            12.dp.toPx()

        val columnCount =
            22

        val columnWidth =
            size.width /
                columnCount.toFloat()

        val travelDistance =
            size.height +
                260.dp.toPx()

        repeat(
            columnCount,
        ) { column ->
            val x =
                columnWidth *
                    (
                        column.toFloat() +
                            0.45f
                    )

            val stagger =
                (
                    column *
                        0.071f
                ) % 1f

            val normalizedTravel =
                (
                    progress +
                        stagger
                ) % 1f

            val headY =
                normalizedTravel *
                    travelDistance -
                    130.dp.toPx()

            val glyphs =
                glyphRows[
                    column %
                        glyphRows.size
                ]

            glyphs.forEachIndexed { row, glyph ->
                val glyphY =
                    headY -
                        row *
                        23.dp.toPx()

                if (
                    glyphY >=
                    -32.dp.toPx() &&
                    glyphY <=
                    size.height +
                        32.dp.toPx()
                ) {
                    val rowFade =
                        (
                            1f -
                                row.toFloat() /
                                glyphs.length.toFloat()
                        )
                            .coerceIn(
                                0.08f,
                                1f,
                            )

                    paint.alpha =
                        (
                            255f *
                                alpha *
                                rowFade
                        )
                            .toInt()
                            .coerceIn(
                                0,
                                255,
                            )

                    drawContext
                        .canvas
                        .nativeCanvas
                        .drawText(
                            glyph.toString(),
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
 * Screen-wide electrical circuit environment.
 *
 * Circuit propagation is branding/presentation only.
 */
@Composable
private fun DevilCircuitLayer(
    progress: Float,
    currentProgress: Float,
    alpha: Float,
    color: Color,
    modifier: Modifier = Modifier,
) {
    Canvas(
        modifier = modifier,
    ) {
        if (
            progress <= 0f ||
            alpha <= 0f
        ) {
            return@Canvas
        }

        val center =
            Offset(
                x =
                    size.width /
                        2f,
                y =
                    size.height /
                        2f,
            )

        val segments =
            listOf(
                CircuitSegment(
                    start = Offset(0.50f, 0.50f),
                    end = Offset(0.20f, 0.50f),
                ),
                CircuitSegment(
                    start = Offset(0.20f, 0.50f),
                    end = Offset(0.08f, 0.38f),
                ),
                CircuitSegment(
                    start = Offset(0.50f, 0.50f),
                    end = Offset(0.80f, 0.50f),
                ),
                CircuitSegment(
                    start = Offset(0.80f, 0.50f),
                    end = Offset(0.92f, 0.34f),
                ),
                CircuitSegment(
                    start = Offset(0.50f, 0.50f),
                    end = Offset(0.50f, 0.22f),
                ),
                CircuitSegment(
                    start = Offset(0.50f, 0.22f),
                    end = Offset(0.34f, 0.10f),
                ),
                CircuitSegment(
                    start = Offset(0.50f, 0.22f),
                    end = Offset(0.68f, 0.08f),
                ),
                CircuitSegment(
                    start = Offset(0.50f, 0.50f),
                    end = Offset(0.50f, 0.78f),
                ),
                CircuitSegment(
                    start = Offset(0.50f, 0.78f),
                    end = Offset(0.30f, 0.92f),
                ),
                CircuitSegment(
                    start = Offset(0.50f, 0.78f),
                    end = Offset(0.72f, 0.90f),
                ),
                CircuitSegment(
                    start = Offset(0.20f, 0.50f),
                    end = Offset(0.14f, 0.68f),
                ),
                CircuitSegment(
                    start = Offset(0.80f, 0.50f),
                    end = Offset(0.88f, 0.70f),
                ),
            )

        segments.forEachIndexed { index, segment ->
            val stagger =
                index.toFloat() *
                    0.055f

            val localProgress =
                (
                    progress *
                        1.60f -
                        stagger
                )
                    .coerceIn(
                        0f,
                        1f,
                    )

            if (localProgress > 0f) {
                val start =
                    segment.start.toAbsolute(
                        width =
                            size.width,
                        height =
                            size.height,
                    )

                val targetEnd =
                    segment.end.toAbsolute(
                        width =
                            size.width,
                        height =
                            size.height,
                    )

                val animatedEnd =
                    Offset(
                        x =
                            start.x +
                                (
                                    targetEnd.x -
                                        start.x
                                ) *
                                localProgress,
                        y =
                            start.y +
                                (
                                    targetEnd.y -
                                        start.y
                                ) *
                                localProgress,
                    )

                drawLine(
                    color =
                        color.copy(
                            alpha =
                                alpha *
                                    0.24f,
                        ),
                    start =
                        start,
                    end =
                        animatedEnd,
                    strokeWidth =
                        5.dp.toPx(),
                    cap =
                        StrokeCap.Round,
                )

                drawLine(
                    color =
                        color.copy(
                            alpha =
                                alpha *
                                    0.86f,
                        ),
                    start =
                        start,
                    end =
                        animatedEnd,
                    strokeWidth =
                        1.4.dp.toPx(),
                    cap =
                        StrokeCap.Round,
                )

                if (localProgress > 0.94f) {
                    drawCircle(
                        color =
                            color.copy(
                                alpha =
                                    alpha *
                                        0.78f,
                            ),
                        radius =
                            3.2.dp.toPx(),
                        center =
                            targetEnd,
                    )
                }
            }
        }

        drawCircle(
            color =
                color.copy(
                    alpha =
                        alpha *
                            0.10f,
                ),
            radius =
                size.minDimension *
                    0.23f,
            center =
                center,
        )

        /*
         * Bright electrical packets continue moving through completed traces.
         *
         * They are environmental presentation only.
         */
        if (progress > 0.72f) {
            segments.forEachIndexed { index, segment ->
                val start =
                    segment.start.toAbsolute(
                        width =
                            size.width,
                        height =
                            size.height,
                    )

                val end =
                    segment.end.toAbsolute(
                        width =
                            size.width,
                        height =
                            size.height,
                    )

                val phaseOffset =
                    (
                        index.toFloat() *
                            0.137f
                    ) % 1f

                val packetProgress =
                    (
                        currentProgress +
                            phaseOffset
                    ) % 1f

                val packet =
                    Offset(
                        x =
                            start.x +
                                (
                                    end.x -
                                        start.x
                                ) *
                                packetProgress,
                        y =
                            start.y +
                                (
                                    end.y -
                                        start.y
                                ) *
                                packetProgress,
                    )

                drawCircle(
                    color =
                        color.copy(
                            alpha =
                                alpha *
                                    0.96f,
                        ),
                    radius =
                        2.6.dp.toPx(),
                    center =
                        packet,
                )

                drawCircle(
                    color =
                        color.copy(
                            alpha =
                                alpha *
                                    0.20f,
                        ),
                    radius =
                        7.dp.toPx(),
                    center =
                        packet,
                )
            }
        }
    }
}

/**
 * Bounded central energy halo used only during approved identity reveal.
 */
@Composable
private fun DevilIdentityGlowLayer(
    alpha: Float,
    color: Color,
    modifier: Modifier = Modifier,
) {
    Canvas(
        modifier = modifier,
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
                    size.height /
                        2f,
            )

        drawCircle(
            color =
                color.copy(
                    alpha =
                        alpha *
                            0.045f,
                ),
            radius =
                size.minDimension *
                    0.42f,
            center =
                center,
        )

        drawCircle(
            color =
                color.copy(
                    alpha =
                        alpha *
                            0.08f,
                ),
            radius =
                size.minDimension *
                    0.28f,
            center =
                center,
        )
    }
}

private data class CircuitSegment(
    val start: Offset,
    val end: Offset,
)

private fun Offset.toAbsolute(
    width: Float,
    height: Float,
): Offset =
    Offset(
        x =
            x *
                width,
        y =
            y *
                height,
    )
