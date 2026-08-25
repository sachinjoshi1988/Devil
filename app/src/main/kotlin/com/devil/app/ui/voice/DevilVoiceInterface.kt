package com.devil.app.ui.voice

import com.devil.app.ui.accessibility.devilInclusiveHeading
import com.devil.app.ui.accessibility.devilInclusiveInteractiveTarget

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.devil.app.R

/**
 * Stage 254 dedicated Devil Voice Interface.
 *
 * This component presents already-existing bounded voice state supplied by the
 * established Android voice architecture.
 *
 * It does not perform speech recognition, TextToSpeech, wake-phrase matching,
 * authentication, authorization, runtime submission, execution, verification,
 * memory operations, or voice-intelligence reasoning.
 *
 * The locked Devil identity is rendered from the existing
 * R.drawable.devil_primary_logo resource. Stage 254 does not create,
 * redraw, reinterpret, or replace Devil's identity mark.
 *
 * VOICE_INTERFACE != SPEECH_RECOGNITION.
 * VOICE_INTERFACE != TEXT_TO_SPEECH.
 * VOICE_INTERFACE != DEVIL_IDENTITY_AUTHORITY.
 * VOICE_INTERFACE != AUTHENTICATION.
 * VOICE_INTERFACE != AUTHORIZATION.
 * VOICE_INTERFACE != EXECUTION.
 * VOICE_INTERFACE != VERIFICATION.
 * VOICE_INTERFACE != MEMORY.
 * LISTENING != UNDERSTANDING.
 * SPEAKING != VERIFIED_OUTCOME.
 * WAKE_PHRASE != AUTHENTICATION.
 *
 * Stage 254 does not implement Stage 255 Memory Interface.
 */
@Composable
fun DevilVoiceInterface(
    isVoiceListening: Boolean,
    isSubmitting: Boolean,
    isVoiceSpeaking: Boolean,
    voiceInputEnabled: Boolean,
    handsFreeEnabled: Boolean,
    onVoiceInput: () -> Unit,
    onHandsFreeToggle: () -> Unit,
    modifier: Modifier = Modifier,
    voiceInputMessage: String? = null,
    voiceOutputMessage: String? = null,
    handsFreeMessage: String? = null,
) {
    val devilRed =
        MaterialTheme.colorScheme.primary

    val foreground =
        MaterialTheme.colorScheme.onBackground

    val muted =
        MaterialTheme.colorScheme.onSurfaceVariant

    val surface =
        MaterialTheme.colorScheme.surface

    val state =
        voicePresentationState(
            isVoiceListening = isVoiceListening,
            isVoiceSpeaking = isVoiceSpeaking,
            handsFreeEnabled = handsFreeEnabled,
        )

    val transition =
        rememberInfiniteTransition(
            label = "DevilVoiceInterfacePulse",
        )

    val activePulse by
        transition.animateFloat(
            initialValue = 0.34f,
            targetValue = 1f,
            animationSpec =
                infiniteRepeatable(
                    animation =
                        tween(
                            durationMillis =
                                when (state) {
                                    DevilVoicePresentationState.LISTENING ->
                                        920

                                    DevilVoicePresentationState.SPEAKING ->
                                        720

                                    DevilVoicePresentationState.HANDS_FREE ->
                                        1250

                                    DevilVoicePresentationState.IDLE ->
                                        1800
                                },
                            easing = FastOutSlowInEasing,
                        ),
                    repeatMode = RepeatMode.Reverse,
                ),
            label = "DevilVoiceInterfacePulseAlpha",
        )

    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .background(
                    color = Color.Transparent,
                    shape = RoundedCornerShape(28.dp),
                )
                .border(
                    width = 1.dp,
                    color = devilRed.copy(alpha = 0.30f),
                    shape = RoundedCornerShape(28.dp),
                )
                .padding(
                    horizontal = 18.dp,
                    vertical = 20.dp,
                ),
        horizontalAlignment =
            Alignment.CenterHorizontally,
        verticalArrangement =
            Arrangement.spacedBy(14.dp),
    ) {
        Text(
            text = "DEVIL VOICE",
            color = devilRed,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
        )

        Text(
            text = state.title,
            color = foreground,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier =
                Modifier.semantics {
                    liveRegion =
                        LiveRegionMode.Polite
                },
        )

        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .semantics {
                        contentDescription =
                            "Devil voice presentation: ${state.accessibilityDescription}"
                    },
            contentAlignment =
                Alignment.Center,
        ) {
            DevilVoiceEnergyField(
                color = devilRed,
                state = state,
                pulse = activePulse,
                modifier =
                    Modifier.size(278.dp),
            )

            Image(
                painter =
                    painterResource(
                        id = R.drawable.devil_primary_logo,
                    ),
                contentDescription = "Devil",
                modifier =
                    Modifier
                        .size(174.dp)
                        .alpha(
                            when (state) {
                                DevilVoicePresentationState.IDLE ->
                                    0.90f

                                else ->
                                    1f
                            },
                        ),
            )
        }

        VoiceStatePill(
            state = state,
            devilRed = devilRed,
            foreground = foreground,
            muted = muted,
        )

        VoiceStatusPresentation(
            state = state,
            voiceInputMessage = voiceInputMessage,
            voiceOutputMessage = voiceOutputMessage,
            handsFreeMessage = handsFreeMessage,
            surface = surface,
            devilRed = devilRed,
            foreground = foreground,
            muted = muted,
        )

        Row(
            modifier =
                Modifier.fillMaxWidth(),
            horizontalArrangement =
                Arrangement.spacedBy(12.dp),
        ) {
            OutlinedButton(
                onClick = onVoiceInput,
                enabled =
                    voiceInputEnabled &&
                        !isSubmitting &&
                        !isVoiceListening &&
                        !isVoiceSpeaking &&
                        !handsFreeEnabled,
                modifier =
                    Modifier.weight(1f).devilInclusiveInteractiveTarget(),
            ) {
                Text(
                    text =
                        if (
                            isVoiceListening &&
                            !handsFreeEnabled
                        ) {
                            "LISTENING"
                        } else {
                            "VOICE"
                        },
                    fontWeight =
                        FontWeight.Bold,
                )
            }

            OutlinedButton(
                onClick = onHandsFreeToggle,
                enabled =
                    !isSubmitting &&
                        !isVoiceSpeaking &&
                        (
                            !isVoiceListening ||
                                handsFreeEnabled
                        ),
                modifier =
                    Modifier.weight(1f).devilInclusiveInteractiveTarget(),
            ) {
                Text(
                    text =
                        if (handsFreeEnabled) {
                            "STOP HANDS-FREE"
                        } else {
                            "HANDS-FREE"
                        },
                    fontWeight =
                        FontWeight.Bold,
                )
            }
        }

        Text(
            text =
                "Voice presentation reflects existing voice state only.",
            color = muted,
            style = MaterialTheme.typography.bodySmall,
        )
    }
}

private enum class DevilVoicePresentationState(
    val title: String,
    val accessibilityDescription: String,
) {
    IDLE(
        title = "READY TO LISTEN",
        accessibilityDescription = "voice interface idle",
    ),
    LISTENING(
        title = "DEVIL IS LISTENING",
        accessibilityDescription = "voice input listening",
    ),
    SPEAKING(
        title = "DEVIL IS SPEAKING",
        accessibilityDescription = "voice output speaking",
    ),
    HANDS_FREE(
        title = "HANDS-FREE ACTIVE",
        accessibilityDescription = "hands-free voice interaction active",
    ),
}

private fun voicePresentationState(
    isVoiceListening: Boolean,
    isVoiceSpeaking: Boolean,
    handsFreeEnabled: Boolean,
): DevilVoicePresentationState {
    return when {
        isVoiceSpeaking ->
            DevilVoicePresentationState.SPEAKING

        handsFreeEnabled ->
            DevilVoicePresentationState.HANDS_FREE

        isVoiceListening ->
            DevilVoicePresentationState.LISTENING

        else ->
            DevilVoicePresentationState.IDLE
    }
}

@Composable
private fun DevilVoiceEnergyField(
    color: Color,
    state: DevilVoicePresentationState,
    pulse: Float,
    modifier: Modifier = Modifier,
) {
    Canvas(
        modifier = modifier,
    ) {
        val center =
            Offset(
                x = size.width / 2f,
                y = size.height / 2f,
            )

        val baseRadius =
            size.minDimension * 0.31f

        val activity =
            when (state) {
                DevilVoicePresentationState.IDLE ->
                    0.35f

                DevilVoicePresentationState.LISTENING ->
                    1f

                DevilVoicePresentationState.SPEAKING ->
                    0.86f

                DevilVoicePresentationState.HANDS_FREE ->
                    0.68f
            }

        drawCircle(
            color =
                color.copy(
                    alpha =
                        0.05f +
                            0.06f *
                            pulse *
                            activity,
                ),
            radius =
                baseRadius *
                    (
                        1.34f +
                            0.10f *
                            pulse *
                            activity
                    ),
            center = center,
        )

        drawCircle(
            color =
                color.copy(
                    alpha =
                        0.24f +
                            0.30f *
                            pulse *
                            activity,
                ),
            radius =
                baseRadius *
                    (
                        1.18f +
                            0.06f *
                            pulse *
                            activity
                    ),
            center = center,
            style =
                Stroke(
                    width = 2.dp.toPx(),
                ),
        )

        drawCircle(
            color =
                color.copy(
                    alpha =
                        0.38f +
                            0.28f *
                            pulse *
                            activity,
                ),
            radius = baseRadius,
            center = center,
            style =
                Stroke(
                    width = 1.2.dp.toPx(),
                ),
        )

        repeat(24) { index ->
            val angle =
                Math.toRadians(
                    index * 15.0,
                )

            val startRadius =
                baseRadius * 1.30f

            val energyLength =
                (
                    8.dp.toPx() +
                        12.dp.toPx() *
                        pulse *
                        activity *
                        (
                            0.45f +
                                (index % 4) * 0.15f
                        )
                )

            val start =
                Offset(
                    x =
                        center.x +
                            kotlin.math.cos(angle)
                                .toFloat() *
                            startRadius,
                    y =
                        center.y +
                            kotlin.math.sin(angle)
                                .toFloat() *
                            startRadius,
                )

            val end =
                Offset(
                    x =
                        center.x +
                            kotlin.math.cos(angle)
                                .toFloat() *
                            (
                                startRadius +
                                    energyLength
                            ),
                    y =
                        center.y +
                            kotlin.math.sin(angle)
                                .toFloat() *
                            (
                                startRadius +
                                    energyLength
                            ),
                )

            drawLine(
                color =
                    color.copy(
                        alpha =
                            0.18f +
                                0.44f *
                                pulse *
                                activity,
                    ),
                start = start,
                end = end,
                strokeWidth =
                    if (index % 6 == 0) {
                        2.dp.toPx()
                    } else {
                        1.dp.toPx()
                    },
            )
        }
    }
}

@Composable
private fun VoiceStatePill(
    state: DevilVoicePresentationState,
    devilRed: Color,
    foreground: Color,
    muted: Color,
) {
    Row(
        modifier =
            Modifier
                .background(
                    color =
                        devilRed.copy(
                            alpha =
                                if (
                                    state ==
                                    DevilVoicePresentationState.IDLE
                                ) {
                                    0.06f
                                } else {
                                    0.12f
                                },
                        ),
                    shape =
                        RoundedCornerShape(
                            999.dp,
                        ),
                )
                .border(
                    width = 1.dp,
                    color =
                        devilRed.copy(
                            alpha = 0.65f,
                        ),
                    shape =
                        RoundedCornerShape(
                            999.dp,
                        ),
                )
                .padding(
                    horizontal = 18.dp,
                    vertical = 9.dp,
                ),
        horizontalArrangement =
            Arrangement.spacedBy(9.dp),
        verticalAlignment =
            Alignment.CenterVertically,
    ) {
        Canvas(
            modifier =
                Modifier.size(9.dp),
        ) {
            drawCircle(
                color =
                    if (
                        state ==
                        DevilVoicePresentationState.IDLE
                    ) {
                        muted
                    } else {
                        devilRed
                    },
            )
        }

        Text(
            text =
                when (state) {
                    DevilVoicePresentationState.IDLE ->
                        "VOICE READY"

                    DevilVoicePresentationState.LISTENING ->
                        "LISTENING"

                    DevilVoicePresentationState.SPEAKING ->
                        "SPEAKING"

                    DevilVoicePresentationState.HANDS_FREE ->
                        "HANDS-FREE"
                },
            color = foreground,
            style =
                MaterialTheme.typography.labelLarge,
            fontWeight =
                FontWeight.Bold,
        )
    }
}

@Composable
private fun VoiceStatusPresentation(
    state: DevilVoicePresentationState,
    voiceInputMessage: String?,
    voiceOutputMessage: String?,
    handsFreeMessage: String?,
    surface: Color,
    devilRed: Color,
    foreground: Color,
    muted: Color,
) {
    val truthfulMessage =
        when {
            state ==
                DevilVoicePresentationState.HANDS_FREE &&
                !handsFreeMessage.isNullOrBlank() ->
                handsFreeMessage

            state ==
                DevilVoicePresentationState.SPEAKING &&
                !voiceOutputMessage.isNullOrBlank() ->
                voiceOutputMessage

            state ==
                DevilVoicePresentationState.LISTENING &&
                !voiceInputMessage.isNullOrBlank() ->
                voiceInputMessage

            !voiceInputMessage.isNullOrBlank() ->
                voiceInputMessage

            !voiceOutputMessage.isNullOrBlank() ->
                voiceOutputMessage

            !handsFreeMessage.isNullOrBlank() ->
                handsFreeMessage

            else ->
                when (state) {
                    DevilVoicePresentationState.IDLE ->
                        "Voice interface ready."

                    DevilVoicePresentationState.LISTENING ->
                        "Listening for voice input."

                    DevilVoicePresentationState.SPEAKING ->
                        "Voice output active."

                    DevilVoicePresentationState.HANDS_FREE ->
                        "Hands-Free voice interaction active."
                }
        }

    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .background(
                    color = surface,
                    shape =
                        RoundedCornerShape(
                            20.dp,
                        ),
                )
                .border(
                    width = 1.dp,
                    color =
                        devilRed.copy(
                            alpha = 0.22f,
                        ),
                    shape =
                        RoundedCornerShape(
                            20.dp,
                        ),
                )
                .padding(16.dp)
                .semantics {
                    liveRegion =
                        LiveRegionMode.Polite
                },
        verticalArrangement =
            Arrangement.spacedBy(6.dp),
    ) {
        Text(
            text = "VOICE STATUS",
            color = devilRed,
            style =
                MaterialTheme.typography.labelMedium,
            fontWeight =
                FontWeight.Bold,
        )

        Text(
            text = truthfulMessage,
            color = foreground,
            style =
                MaterialTheme.typography.bodyLarge,
        )

        Spacer(
            modifier =
                Modifier.height(1.dp),
        )

        Text(
            text =
                "Listening state does not establish understanding, authentication, authorization, or verification.",
            color = muted,
            style =
                MaterialTheme.typography.bodySmall,
        )
    }
}
