package com.devil.app.voice

/**
 * Stage 202 bounded Emotional-Tone Awareness result.
 *
 * The exact normalized supplied source label is preserved together with one
 * bounded vocal-presentation classification.
 *
 * VOCAL_TONE != EMOTIONAL_STATE.
 * VOCAL_TONE != MENTAL_HEALTH_STATE.
 * VOCAL_TONE != AUTHENTICATION.
 */
@ConsistentCopyVisibility
data class AndroidEmotionalToneAwarenessResult private constructor(
    val vocalTone: AndroidVocalTone,
    val sourceLabel: String,
) {
    companion object {
        fun create(
            vocalTone: AndroidVocalTone,
            sourceLabel: String,
        ): AndroidEmotionalToneAwarenessResult {
            val normalizedSourceLabel =
                sourceLabel.trim()

            require(normalizedSourceLabel.isNotEmpty()) {
                "Stage 202 vocal-tone source label must not be blank."
            }

            return AndroidEmotionalToneAwarenessResult(
                vocalTone = vocalTone,
                sourceLabel = normalizedSourceLabel,
            )
        }
    }
}
