package com.devil.app.ui.launch

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
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
 * The visual identity shown here comes directly from the approved Devil
 * artwork resources rather than a procedural reconstruction.
 *
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

    val coreAlpha =
        remember {
            Animatable(0f)
        }

    val coreScale =
        remember {
            Animatable(0.82f)
        }

    val identityAlpha =
        remember {
            Animatable(0f)
        }

    val identityScale =
        remember {
            Animatable(0.94f)
        }

    LaunchedEffect(Unit) {
        /*
         * Phase 1 — Void.
         *
         * Pure presentation pause. It says nothing about runtime state.
         */
        delay(
            DevilLaunchTiming.VOID_DURATION_MILLIS,
        )

        /*
         * Phase 2 — Runtime Core ignition.
         *
         * Fade the exact approved Runtime Core artwork into view.
         */
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

        /*
         * Phase 3 — Runtime Core pulse.
         *
         * Scale and fade are presentation effects only.
         */
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
                coreAlpha.animateTo(
                    targetValue = 0.22f,
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

        /*
         * Phase 4 — Primary Devil identity reveal.
         *
         * The approved Primary Logo artwork replaces the procedural
         * horned-D reconstruction used by the earlier Owner Alpha.
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
        }

        /*
         * Phase 5 — Identity hold.
         *
         * WORDMARK_DURATION_MILLIS is retained as the final visual hold
         * so the total Stage 51 launch target remains five seconds.
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
                    .fillMaxWidth(0.72f)
                    .sizeIn(
                        maxWidth = 420.dp,
                        maxHeight = 420.dp,
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
                    .fillMaxWidth(0.82f)
                    .sizeIn(
                        maxWidth = 460.dp,
                        maxHeight = 460.dp,
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
