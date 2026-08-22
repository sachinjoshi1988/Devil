package com.devil.app.voice

/**
 * Stage 199 bounded Devil Voice coordinator.
 *
 * It prepares only a Devil voice presentation profile.
 *
 * It does not:
 *
 * - invoke Android TextToSpeech;
 * - claim a deep or masculine Android voice is installed;
 * - generate conversational content;
 * - establish Devil personality;
 * - authenticate or identify a speaker;
 * - implement Stage 200 Natural Turn-Taking.
 *
 * DEVIL_VOICE_PROFILE != SPOKEN_OUTPUT.
 * PREFERRED_VOICE != AVAILABLE_VOICE.
 */
class DevilVoiceCoordinator {

    fun prepare(
        languageTag: String?,
    ): DevilVoiceProfile? {
        if (languageTag.isNullOrBlank()) {
            return null
        }

        return DevilVoiceProfile.create(
            presentation =
                DevilVoicePresentation.DEEP_MASCULINE,
            languageTag = languageTag,
            speechRate = DEFAULT_SPEECH_RATE,
            pitch = DEFAULT_PITCH,
        )
    }

    private companion object {
        const val DEFAULT_SPEECH_RATE = 0.9f
        const val DEFAULT_PITCH = 0.8f
    }
}
