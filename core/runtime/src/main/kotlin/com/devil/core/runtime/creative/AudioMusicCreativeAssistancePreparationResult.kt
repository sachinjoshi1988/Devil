package com.devil.core.runtime.creative

import com.devil.core.model.common.TraceId
import com.devil.core.model.creative.AudioMusicCreativeAssistanceRecord

/**
 * Stable Stage 172 result of bounded Audio & Music Creative Assistance preparation.
 *
 * PREPARED requires exactly one AudioMusicCreativeAssistanceRecord.
 * DEFERRED must contain none.
 *
 * This result establishes no audio generation, synthesized voice, generated music,
 * provider selection, execution, publishing authorization, constitutional Verification,
 * Stage 173–174 behavior, World Model mutation, Learning, or Memory persistence.
 */
@ConsistentCopyVisibility
data class AudioMusicCreativeAssistancePreparationResult private constructor(
    val traceId: TraceId,
    val status: AudioMusicCreativeAssistancePreparationStatus,
    val assistance: AudioMusicCreativeAssistanceRecord?,
) {
    companion object {

        fun create(
            traceId: TraceId,
            status: AudioMusicCreativeAssistancePreparationStatus,
            assistance: AudioMusicCreativeAssistanceRecord? = null,
        ): AudioMusicCreativeAssistancePreparationResult {
            when (status) {
                AudioMusicCreativeAssistancePreparationStatus.PREPARED -> {
                    require(assistance != null) {
                        "Prepared Audio & Music Creative Assistance results require one assistance context."
                    }
                }

                AudioMusicCreativeAssistancePreparationStatus.DEFERRED -> {
                    require(assistance == null) {
                        "Deferred Audio & Music Creative Assistance results must not contain an assistance context."
                    }
                }
            }

            return AudioMusicCreativeAssistancePreparationResult(
                traceId = traceId,
                status = status,
                assistance = assistance,
            )
        }
    }
}
