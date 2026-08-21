package com.devil.core.model.creative

/**
 * Immutable Stage 172 representation of one bounded Audio & Music Creative Assistance context.
 *
 * This record preserves:
 *
 * - one exact existing Stage 171 StoryToAnimationPipelineRecord;
 * - one explicitly supplied nonblank audio/music focus;
 * - one explicitly supplied nonblank audio/music context description;
 * - one explicitly supplied nonblank audio/music objective.
 *
 * Stage 172 represents provider-neutral creative assistance context only.
 *
 * It does not:
 *
 * - capture or record audio;
 * - synthesize speech or voices;
 * - clone voices;
 * - generate music or sound effects;
 * - perform lip synchronization;
 * - mix or master audio;
 * - create audio bytes, files, rendered output, or persistent assets;
 * - select or invoke TTS, music, audio, speech, or multimodal providers or models;
 * - authorize or execute capabilities;
 * - mutate the preserved Stage 171 pipeline;
 * - implement Stage 173 Video Creation Assistance;
 * - persist Stage 174 Creative Project Workspace state;
 * - publish, upload, distribute, or transmit media;
 * - establish constitutional Observation, Verification, or Outcome;
 * - mutate World Model state;
 * - perform constitutional Learning;
 * - create or persist Memory.
 *
 * AUDIO_MUSIC_ASSISTANCE != PROVIDER.
 * AUDIO_MUSIC_ASSISTANCE_PREPARED != AUDIO_GENERATED.
 * VOICE_CONTEXT != SYNTHESIZED_VOICE.
 * MUSIC_DIRECTION != GENERATED_MUSIC.
 * AUDIO_REQUIREMENT != AUDIO_ASSET.
 * AUDIO_GENERATED != VERIFIED.
 * GENERATION != PUBLISHING_AUTHORIZATION.
 * PREPARED != EXECUTION.
 */
@ConsistentCopyVisibility
data class AudioMusicCreativeAssistanceRecord private constructor(
    val storyToAnimationPipeline: StoryToAnimationPipelineRecord,
    val audioMusicFocus: String,
    val suppliedAudioMusicContextDescription: String,
    val audioMusicObjective: String,
) {
    companion object {

        fun create(
            storyToAnimationPipeline: StoryToAnimationPipelineRecord,
            audioMusicFocus: String,
            suppliedAudioMusicContextDescription: String,
            audioMusicObjective: String,
        ): AudioMusicCreativeAssistanceRecord {
            val normalizedAudioMusicFocus =
                audioMusicFocus.trim()

            val normalizedSuppliedAudioMusicContextDescription =
                suppliedAudioMusicContextDescription.trim()

            val normalizedAudioMusicObjective =
                audioMusicObjective.trim()

            require(normalizedAudioMusicFocus.isNotEmpty()) {
                "Audio & Music Creative Assistance focus must not be blank."
            }

            require(normalizedSuppliedAudioMusicContextDescription.isNotEmpty()) {
                "Audio & Music Creative Assistance context description must not be blank."
            }

            require(normalizedAudioMusicObjective.isNotEmpty()) {
                "Audio & Music Creative Assistance objective must not be blank."
            }

            return AudioMusicCreativeAssistanceRecord(
                storyToAnimationPipeline = storyToAnimationPipeline,
                audioMusicFocus = normalizedAudioMusicFocus,
                suppliedAudioMusicContextDescription =
                    normalizedSuppliedAudioMusicContextDescription,
                audioMusicObjective = normalizedAudioMusicObjective,
            )
        }
    }
}
