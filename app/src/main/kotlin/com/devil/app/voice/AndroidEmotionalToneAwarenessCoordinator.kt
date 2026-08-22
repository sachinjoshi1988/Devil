package com.devil.app.voice

import java.util.Locale

/**
 * Stage 202 bounded Emotional-Tone Awareness coordinator.
 *
 * It consumes one explicitly supplied vocal-presentation label and maps only
 * that label into a bounded presentation-tone classification.
 *
 * It does not:
 *
 * - inspect microphone audio;
 * - infer tone from RMS evidence;
 * - diagnose or infer emotional or psychological state;
 * - infer intent;
 * - identify or authenticate a speaker;
 * - generate or alter conversational responses;
 * - implement Stage 203 Spoken Education Mode.
 *
 * VOCAL_TONE != EMOTIONAL_STATE.
 * VOCAL_TONE != MENTAL_HEALTH_STATE.
 * VOCAL_TONE != INTENT.
 * VOCAL_TONE != SPEAKER_IDENTITY.
 * TONE_AWARENESS != RESPONSE_GENERATION.
 */
class AndroidEmotionalToneAwarenessCoordinator {

    fun classify(
        sourceLabel: String,
    ): AndroidEmotionalToneAwarenessResult {
        val normalized =
            sourceLabel
                .trim()
                .lowercase(Locale.ROOT)

        require(normalized.isNotEmpty()) {
            "Stage 202 vocal-tone source label must not be blank."
        }

        val vocalTone =
            when (normalized) {
                "calm" ->
                    AndroidVocalTone.CALM

                "neutral" ->
                    AndroidVocalTone.NEUTRAL

                "energetic" ->
                    AndroidVocalTone.ENERGETIC

                "tense" ->
                    AndroidVocalTone.TENSE

                else ->
                    AndroidVocalTone.UNKNOWN
            }

        return AndroidEmotionalToneAwarenessResult.create(
            vocalTone = vocalTone,
            sourceLabel = sourceLabel,
        )
    }
}
