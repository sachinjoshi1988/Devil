package com.devil.app.ui.launch

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Stage 51 presentation-only Devil awakening sequence.
 *
 * The Runtime Core visual and horned-D identity are branding and launch
 * presentation only.
 *
 * Core animation != runtime readiness.
 * Core animation != authentication.
 * Core animation != authorization.
 * Core animation != execution.
 * Core animation != verified success.
 *
 * Horned-D identity != owner authentication.
 * Horned-D identity != Owner Mode.
 */
@Composable
fun DevilAwakeningScreen(
    onComplete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val primaryColor =
        MaterialTheme.colorScheme.primary

    val backgroundColor =
        MaterialTheme.colorScheme.background

    val foregroundColor =
        MaterialTheme.colorScheme.onBackground

    val coreScale =
        remember {
            Animatable(0.08f)
        }

    val coreAlpha =
        remember {
            Animatable(0f)
        }

    val ringAlpha =
        remember {
            Animatable(0f)
        }

    val identityAlpha =
        remember {
            Animatable(0f)
        }

    val wordmarkAlpha =
        remember {
            Animatable(0f)
        }

    LaunchedEffect(Unit) {
        delay(
            DevilLaunchTiming.VOID_DURATION_MILLIS,
        )

        coreAlpha.animateTo(
            targetValue = 1f,
            animationSpec =
                tween(
                    durationMillis =
                        DevilLaunchTiming
                            .CORE_IGNITION_DURATION_MILLIS
                            .toInt(),
                    easing = LinearEasing,
                ),
        )

        coroutineScope {
            launch {
                coreScale.animateTo(
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
                ringAlpha.animateTo(
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
        }

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

        wordmarkAlpha.animateTo(
            targetValue = 1f,
            animationSpec =
                tween(
                    durationMillis =
                        DevilLaunchTiming
                            .WORDMARK_DURATION_MILLIS
                            .toInt(),
                    easing =
                        FastOutSlowInEasing,
                ),
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
        Canvas(
            modifier =
                Modifier.size(240.dp),
        ) {
            val center =
                Offset(
                    x = size.width / 2f,
                    y = size.height / 2f,
                )

            val baseRadius =
                size.minDimension * 0.17f

            /*
             * Infernal runtime-core presentation.
             *
             * This is visual identity only and does not represent real runtime
             * state, readiness, authentication, authority, or success.
             */
            drawCircle(
                color =
                    primaryColor.copy(
                        alpha =
                            coreAlpha.value *
                                0.88f,
                    ),
                radius =
                    baseRadius *
                        coreScale.value,
                center =
                    center,
            )

            drawCircle(
                color =
                    primaryColor.copy(
                        alpha =
                            ringAlpha.value *
                                0.72f,
                    ),
                radius =
                    size.minDimension *
                        0.30f,
                center =
                    center,
                style =
                    Stroke(
                        width =
                            size.minDimension *
                                0.016f,
                    ),
            )

            drawCircle(
                color =
                    primaryColor.copy(
                        alpha =
                            ringAlpha.value *
                                0.30f,
                    ),
                radius =
                    size.minDimension *
                        0.40f,
                center =
                    center,
                style =
                    Stroke(
                        width =
                            size.minDimension *
                                0.008f,
                    ),
            )

            val circuitAlpha =
                ringAlpha.value *
                    0.48f

            val circuitStart =
                size.minDimension *
                    0.43f

            val circuitEnd =
                size.minDimension *
                    0.58f

            drawLine(
                color =
                    primaryColor.copy(
                        alpha = circuitAlpha,
                    ),
                start =
                    Offset(
                        center.x,
                        center.y - circuitStart,
                    ),
                end =
                    Offset(
                        center.x,
                        center.y - circuitEnd,
                    ),
                strokeWidth =
                    size.minDimension *
                        0.010f,
                cap =
                    StrokeCap.Round,
            )

            drawLine(
                color =
                    primaryColor.copy(
                        alpha = circuitAlpha,
                    ),
                start =
                    Offset(
                        center.x,
                        center.y + circuitStart,
                    ),
                end =
                    Offset(
                        center.x,
                        center.y + circuitEnd,
                    ),
                strokeWidth =
                    size.minDimension *
                        0.010f,
                cap =
                    StrokeCap.Round,
            )

            drawLine(
                color =
                    primaryColor.copy(
                        alpha = circuitAlpha,
                    ),
                start =
                    Offset(
                        center.x - circuitStart,
                        center.y,
                    ),
                end =
                    Offset(
                        center.x - circuitEnd,
                        center.y,
                    ),
                strokeWidth =
                    size.minDimension *
                        0.010f,
                cap =
                    StrokeCap.Round,
            )

            drawLine(
                color =
                    primaryColor.copy(
                        alpha = circuitAlpha,
                    ),
                start =
                    Offset(
                        center.x + circuitStart,
                        center.y,
                    ),
                end =
                    Offset(
                        center.x + circuitEnd,
                        center.y,
                    ),
                strokeWidth =
                    size.minDimension *
                        0.010f,
                cap =
                    StrokeCap.Round,
            )

            /*
             * Custom horned-D identity.
             *
             * Deliberately drawn rather than represented as ordinary text so
             * Devil owns a distinct visual mark independent of system fonts.
             */
            val identity =
                identityAlpha.value

            val left =
                center.x -
                    size.minDimension *
                    0.115f

            val top =
                center.y -
                    size.minDimension *
                    0.205f

            val bottom =
                center.y +
                    size.minDimension *
                    0.205f

            val right =
                center.x +
                    size.minDimension *
                    0.145f

            val dPath =
                Path().apply {
                    moveTo(
                        left,
                        top,
                    )

                    lineTo(
                        left,
                        bottom,
                    )

                    moveTo(
                        left,
                        top,
                    )

                    cubicTo(
                        right,
                        top,
                        right,
                        bottom,
                        left,
                        bottom,
                    )
                }

            drawPath(
                path =
                    dPath,
                color =
                    foregroundColor.copy(
                        alpha = identity,
                    ),
                style =
                    Stroke(
                        width =
                            size.minDimension *
                                0.043f,
                        cap =
                            StrokeCap.Round,
                    ),
            )

            /*
             * Lucifer horns.
             *
             * They are visual identity accents only.
             */
            val hornRise =
                size.minDimension *
                    0.105f

            val hornSpread =
                size.minDimension *
                    0.095f

            drawLine(
                color =
                    primaryColor.copy(
                        alpha = identity,
                    ),
                start =
                    Offset(
                        left +
                            size.minDimension *
                                0.018f,
                        top +
                            size.minDimension *
                                0.010f,
                    ),
                end =
                    Offset(
                        left - hornSpread,
                        top - hornRise,
                    ),
                strokeWidth =
                    size.minDimension *
                        0.034f,
                cap =
                    StrokeCap.Round,
            )

            drawLine(
                color =
                    primaryColor.copy(
                        alpha = identity,
                    ),
                start =
                    Offset(
                        center.x +
                            size.minDimension *
                                0.055f,
                        top +
                            size.minDimension *
                                0.025f,
                    ),
                end =
                    Offset(
                        center.x +
                            size.minDimension *
                                0.145f,
                        top - hornRise,
                    ),
                strokeWidth =
                    size.minDimension *
                        0.034f,
                cap =
                    StrokeCap.Round,
            )

            /*
             * Small infernal eye/core point gives the identity a controlled
             * asymmetric signature without implying sentience or awareness.
             */
            drawCircle(
                color =
                    primaryColor.copy(
                        alpha = identity,
                    ),
                radius =
                    size.minDimension *
                        0.018f,
                center =
                    Offset(
                        center.x +
                            size.minDimension *
                                0.046f,
                        center.y -
                            size.minDimension *
                                0.012f,
                    ),
            )
        }

        Text(
            text = "DEVIL",
            modifier =
                Modifier
                    .align(
                        Alignment.Center,
                    )
                    .offset(
                        y = 158.dp,
                    )
                    .alpha(
                        wordmarkAlpha.value,
                    ),
            color =
                foregroundColor,
            style =
                MaterialTheme
                    .typography
                    .titleMedium
                    .copy(
                        fontWeight =
                            FontWeight.Bold,
                        letterSpacing =
                            6.sp,
                    ),
        )
    }
}
