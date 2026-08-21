package com.devil.core.runtime.creative

import com.devil.core.model.common.TraceId
import com.devil.core.model.creative.ImageCreationRecord

/**
 * Stable Stage 167 result of bounded Image Creation preparation.
 *
 * PREPARED requires exactly one ImageCreationRecord.
 * DEFERRED must contain none.
 *
 * This result establishes no provider selection, generation, generated-image
 * existence, publishing authorization, execution, constitutional Verification,
 * Stage 168–174 behavior, World Model mutation, or Memory persistence.
 */
@ConsistentCopyVisibility
data class ImageCreationPreparationResult private constructor(
    val traceId: TraceId,
    val status: ImageCreationPreparationStatus,
    val imageCreation: ImageCreationRecord?,
) {
    companion object {

        fun create(
            traceId: TraceId,
            status: ImageCreationPreparationStatus,
            imageCreation: ImageCreationRecord? = null,
        ): ImageCreationPreparationResult {
            when (status) {
                ImageCreationPreparationStatus.PREPARED -> {
                    require(imageCreation != null) {
                        "Prepared Image Creation results require one image-creation context."
                    }
                }

                ImageCreationPreparationStatus.DEFERRED -> {
                    require(imageCreation == null) {
                        "Deferred Image Creation results must not contain an image-creation context."
                    }
                }
            }

            return ImageCreationPreparationResult(
                traceId = traceId,
                status = status,
                imageCreation = imageCreation,
            )
        }
    }
}
