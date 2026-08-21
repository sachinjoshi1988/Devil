package com.devil.core.runtime.creative

import com.devil.core.model.common.TraceId
import com.devil.core.model.creative.VideoCreationAssistanceRecord

/**
 * Stable Stage 173 result of bounded Video Creation Assistance preparation.
 *
 * PREPARED requires exactly one VideoCreationAssistanceRecord.
 * DEFERRED must contain none.
 *
 * This result establishes no image inspection, provider selection, generation, output-file
 * existence, execution, rendering, publishing authorization, constitutional Verification,
 * Stage 174 behavior, World Model mutation, Learning, or Memory persistence.
 */
@ConsistentCopyVisibility
data class VideoCreationAssistancePreparationResult private constructor(
    val traceId: TraceId,
    val status: VideoCreationAssistancePreparationStatus,
    val assistance: VideoCreationAssistanceRecord?,
) {
    companion object {

        fun create(
            traceId: TraceId,
            status: VideoCreationAssistancePreparationStatus,
            assistance: VideoCreationAssistanceRecord? = null,
        ): VideoCreationAssistancePreparationResult {
            when (status) {
                VideoCreationAssistancePreparationStatus.PREPARED -> {
                    require(assistance != null) {
                        "Prepared Video Creation Assistance results require one assistance context."
                    }
                }

                VideoCreationAssistancePreparationStatus.DEFERRED -> {
                    require(assistance == null) {
                        "Deferred Video Creation Assistance results must not contain an assistance context."
                    }
                }
            }

            return VideoCreationAssistancePreparationResult(
                traceId = traceId,
                status = status,
                assistance = assistance,
            )
        }
    }
}
