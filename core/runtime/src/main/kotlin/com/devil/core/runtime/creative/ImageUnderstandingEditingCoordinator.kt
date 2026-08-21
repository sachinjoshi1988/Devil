package com.devil.core.runtime.creative

import com.devil.core.model.common.TraceId
import com.devil.core.model.creative.ImageCreationRecord
import com.devil.core.model.creative.ImageUnderstandingEditingRecord

/**
 * Stage 168 bounded Image Understanding & Editing coordinator.
 *
 * This coordinator preserves one exact existing Stage 167 Image Creation record
 * and prepares explicitly supplied reference-image/editing metadata.
 *
 * Stage 167 remains authoritative for preserved Image Creation provenance.
 *
 * This coordinator does not:
 *
 * - inspect, decode, or understand actual image bytes;
 * - alter pixels, files, assets, or rendered output;
 * - select or invoke image-understanding or editing providers or models;
 * - establish reference-image authenticity;
 * - determine identity from a face;
 * - verify visual similarity or character consistency;
 * - authorize or execute capabilities;
 * - publish, upload, distribute, or transmit media;
 * - establish constitutional Observation, Verification, or Outcome;
 * - mutate World Model state;
 * - perform constitutional Learning;
 * - create or persist Memory;
 * - or implement Stages 169 through 174.
 *
 * IMAGE_UNDERSTANDING_EDITING != PROVIDER.
 * REFERENCE_IMAGE_CONTEXT != VERIFIED_IMAGE_IDENTITY.
 * EDITING_PREPARED != IMAGE_EDITED.
 * SUPPLIED_CONSISTENCY_REQUIREMENT != VERIFIED_CHARACTER_CONSISTENCY.
 * GENERATED != VERIFIED.
 * GENERATION != PUBLISHING_AUTHORIZATION.
 * EDITING_PREPARED != EXECUTION.
 */
class ImageUnderstandingEditingCoordinator {

    fun prepare(
        traceId: TraceId,
        imageCreation: ImageCreationRecord,
        imageUnderstandingEditingFocus: String,
        suppliedReferenceImageContextDescription: String,
        editingObjective: String,
    ): ImageUnderstandingEditingPreparationResult {
        if (
            imageUnderstandingEditingFocus.isBlank() ||
            suppliedReferenceImageContextDescription.isBlank() ||
            editingObjective.isBlank()
        ) {
            return deferred(traceId)
        }

        val imageUnderstandingEditing =
            ImageUnderstandingEditingRecord.create(
                imageCreation = imageCreation,
                imageUnderstandingEditingFocus =
                    imageUnderstandingEditingFocus,
                suppliedReferenceImageContextDescription =
                    suppliedReferenceImageContextDescription,
                editingObjective = editingObjective,
            )

        return ImageUnderstandingEditingPreparationResult.create(
            traceId = traceId,
            status =
                ImageUnderstandingEditingPreparationStatus.PREPARED,
            imageUnderstandingEditing = imageUnderstandingEditing,
        )
    }

    private fun deferred(
        traceId: TraceId,
    ): ImageUnderstandingEditingPreparationResult {
        return ImageUnderstandingEditingPreparationResult.create(
            traceId = traceId,
            status =
                ImageUnderstandingEditingPreparationStatus.DEFERRED,
        )
    }
}
