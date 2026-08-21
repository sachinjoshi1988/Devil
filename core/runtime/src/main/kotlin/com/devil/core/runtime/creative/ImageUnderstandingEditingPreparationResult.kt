package com.devil.core.runtime.creative

import com.devil.core.model.common.TraceId
import com.devil.core.model.creative.ImageUnderstandingEditingRecord

/**
 * Stable Stage 168 result of bounded Image Understanding & Editing preparation.
 *
 * PREPARED requires exactly one ImageUnderstandingEditingRecord.
 * DEFERRED must contain none.
 *
 * This result establishes no image understanding, edit execution, identity
 * verification, character-consistency verification, provider selection,
 * publishing authorization, constitutional Verification, Stage 169–174
 * behavior, World Model mutation, or Memory persistence.
 */
@ConsistentCopyVisibility
data class ImageUnderstandingEditingPreparationResult private constructor(
    val traceId: TraceId,
    val status: ImageUnderstandingEditingPreparationStatus,
    val imageUnderstandingEditing: ImageUnderstandingEditingRecord?,
) {
    companion object {

        fun create(
            traceId: TraceId,
            status: ImageUnderstandingEditingPreparationStatus,
            imageUnderstandingEditing: ImageUnderstandingEditingRecord? = null,
        ): ImageUnderstandingEditingPreparationResult {
            when (status) {
                ImageUnderstandingEditingPreparationStatus.PREPARED -> {
                    require(imageUnderstandingEditing != null) {
                        "Prepared Image Understanding & Editing results require one context."
                    }
                }

                ImageUnderstandingEditingPreparationStatus.DEFERRED -> {
                    require(imageUnderstandingEditing == null) {
                        "Deferred Image Understanding & Editing results must not contain a context."
                    }
                }
            }

            return ImageUnderstandingEditingPreparationResult(
                traceId = traceId,
                status = status,
                imageUnderstandingEditing = imageUnderstandingEditing,
            )
        }
    }
}
