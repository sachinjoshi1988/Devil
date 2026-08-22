package com.devil.app.voice

/**
 * Stage 201 bounded audio-level evidence.
 *
 * silenceThresholdDb must be lower than voiceThresholdDb.
 *
 * This record preserves supplied numeric evidence only.
 *
 * AUDIO_LEVEL != SPEECH_CONTENT.
 * AUDIO_LEVEL != SPEAKER_IDENTITY.
 */
@ConsistentCopyVisibility
data class AndroidVoiceActivityEvidence private constructor(
    val rmsDb: Float,
    val silenceThresholdDb: Float,
    val voiceThresholdDb: Float,
) {
    companion object {
        fun create(
            rmsDb: Float,
            silenceThresholdDb: Float,
            voiceThresholdDb: Float,
        ): AndroidVoiceActivityEvidence {
            require(rmsDb.isFinite()) {
                "Stage 201 RMS dB evidence must be finite."
            }

            require(silenceThresholdDb.isFinite()) {
                "Stage 201 silence threshold must be finite."
            }

            require(voiceThresholdDb.isFinite()) {
                "Stage 201 voice threshold must be finite."
            }

            require(silenceThresholdDb < voiceThresholdDb) {
                "Stage 201 silence threshold must be lower than voice threshold."
            }

            return AndroidVoiceActivityEvidence(
                rmsDb = rmsDb,
                silenceThresholdDb = silenceThresholdDb,
                voiceThresholdDb = voiceThresholdDb,
            )
        }
    }
}
