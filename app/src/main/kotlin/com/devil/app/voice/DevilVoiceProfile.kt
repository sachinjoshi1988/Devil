package com.devil.app.voice

/**
 * Stage 199 bounded Devil Voice profile.
 *
 * The profile expresses preferred presentation only.
 *
 * It does not establish that Android can render the requested presentation.
 *
 * VOICE_PROFILE != VOICE_AVAILABLE.
 * VOICE_PROFILE != SPOKEN_OUTPUT.
 */
@ConsistentCopyVisibility
data class DevilVoiceProfile private constructor(
    val presentation: DevilVoicePresentation,
    val languageTag: String,
    val speechRate: Float,
    val pitch: Float,
) {
    companion object {
        fun create(
            presentation: DevilVoicePresentation,
            languageTag: String,
            speechRate: Float,
            pitch: Float,
        ): DevilVoiceProfile {
            val normalizedLanguageTag =
                languageTag.trim()

            require(normalizedLanguageTag.isNotEmpty()) {
                "Devil Voice language tag must not be blank."
            }

            require(speechRate > 0.0f) {
                "Devil Voice speech rate must be positive."
            }

            require(pitch > 0.0f) {
                "Devil Voice pitch must be positive."
            }

            return DevilVoiceProfile(
                presentation = presentation,
                languageTag = normalizedLanguageTag,
                speechRate = speechRate,
                pitch = pitch,
            )
        }
    }
}
