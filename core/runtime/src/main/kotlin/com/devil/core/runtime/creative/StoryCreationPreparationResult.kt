package com.devil.core.runtime.creative

import com.devil.core.model.common.TraceId
import com.devil.core.model.creative.StoryCreationRecord

/**
 * Stable Stage 169 result of bounded Story Creation preparation.
 *
 * PREPARED requires exactly one StoryCreationRecord.
 * DEFERRED must contain none.
 *
 * This result establishes no generated story, provider selection, scene
 * decomposition, storyboard, execution, publishing authorization,
 * constitutional Verification, Stage 170–174 behavior, World Model mutation,
 * or Memory persistence.
 */
@ConsistentCopyVisibility
data class StoryCreationPreparationResult private constructor(
    val traceId: TraceId,
    val status: StoryCreationPreparationStatus,
    val storyCreation: StoryCreationRecord?,
) {
    companion object {

        fun create(
            traceId: TraceId,
            status: StoryCreationPreparationStatus,
            storyCreation: StoryCreationRecord? = null,
        ): StoryCreationPreparationResult {
            when (status) {
                StoryCreationPreparationStatus.PREPARED -> {
                    require(storyCreation != null) {
                        "Prepared Story Creation results require one Story Creation context."
                    }
                }

                StoryCreationPreparationStatus.DEFERRED -> {
                    require(storyCreation == null) {
                        "Deferred Story Creation results must not contain a Story Creation context."
                    }
                }
            }

            return StoryCreationPreparationResult(
                traceId = traceId,
                status = status,
                storyCreation = storyCreation,
            )
        }
    }
}
