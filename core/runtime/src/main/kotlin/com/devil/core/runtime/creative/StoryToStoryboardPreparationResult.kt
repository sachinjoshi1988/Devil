package com.devil.core.runtime.creative

import com.devil.core.model.common.TraceId
import com.devil.core.model.creative.StoryToStoryboardRecord

/**
 * Stable Stage 170 result of bounded Story-to-Storyboard preparation.
 *
 * PREPARED requires exactly one StoryToStoryboardRecord.
 * DEFERRED must contain none.
 *
 * This result establishes no generated story, generated storyboard panel,
 * rendered media, detailed shot plan, provider selection, execution,
 * publishing authorization, constitutional Verification, Stage 171–174
 * behavior, World Model mutation, or Memory persistence.
 */
@ConsistentCopyVisibility
data class StoryToStoryboardPreparationResult private constructor(
    val traceId: TraceId,
    val status: StoryToStoryboardPreparationStatus,
    val storyboard: StoryToStoryboardRecord?,
) {
    companion object {

        fun create(
            traceId: TraceId,
            status: StoryToStoryboardPreparationStatus,
            storyboard: StoryToStoryboardRecord? = null,
        ): StoryToStoryboardPreparationResult {
            when (status) {
                StoryToStoryboardPreparationStatus.PREPARED -> {
                    require(storyboard != null) {
                        "Prepared Story-to-Storyboard results require one storyboard context."
                    }
                }

                StoryToStoryboardPreparationStatus.DEFERRED -> {
                    require(storyboard == null) {
                        "Deferred Story-to-Storyboard results must not contain a storyboard context."
                    }
                }
            }

            return StoryToStoryboardPreparationResult(
                traceId = traceId,
                status = status,
                storyboard = storyboard,
            )
        }
    }
}
