package com.devil.core.runtime.creative

import com.devil.core.model.common.TraceId
import com.devil.core.model.creative.CreativeMediaIntegrationRecord
import com.devil.core.model.creative.ImageCreationRecord

/**
 * Stage 167 bounded Image Creation coordinator.
 *
 * This coordinator preserves one exact existing Stage 166 Creative Media
 * Integration record and prepares explicitly supplied Image Creation metadata.
 *
 * Stage 166 remains authoritative for preserved Creative Media integration
 * provenance.
 *
 * This coordinator does not:
 *
 * - create another Devil intelligence;
 * - replace or reconstruct the preserved Stage 166 integration;
 * - select or invoke image providers or models;
 * - create provider-specific architecture;
 * - authorize or execute capabilities;
 * - generate images, files, bytes, rendered output, or assets;
 * - establish generated-image existence;
 * - understand, inspect, edit, retouch, or transform images;
 * - publish, upload, distribute, or transmit media;
 * - establish constitutional Observation, Verification, or Outcome;
 * - mutate World Model state;
 * - perform constitutional Learning;
 * - create or persist Memory;
 * - or implement Stages 168 through 174.
 *
 * IMAGE_CREATION != PROVIDER.
 * IMAGE_CREATION_PREPARED != IMAGE_GENERATED.
 * GENERATED != VERIFIED.
 * GENERATION != PUBLISHING_AUTHORIZATION.
 * IMAGE_CREATION_PREPARED != EXECUTION.
 * IMAGE_CREATION != IMAGE_UNDERSTANDING_EDITING.
 */
class ImageCreationCoordinator {

    fun prepare(
        traceId: TraceId,
        creativeMediaIntegration: CreativeMediaIntegrationRecord,
        imageCreationFocus: String,
        suppliedImageCreationDescription: String,
        imageCreationObjective: String,
    ): ImageCreationPreparationResult {
        if (
            imageCreationFocus.isBlank() ||
            suppliedImageCreationDescription.isBlank() ||
            imageCreationObjective.isBlank()
        ) {
            return deferred(traceId)
        }

        val imageCreation =
            ImageCreationRecord.create(
                creativeMediaIntegration = creativeMediaIntegration,
                imageCreationFocus = imageCreationFocus,
                suppliedImageCreationDescription =
                    suppliedImageCreationDescription,
                imageCreationObjective = imageCreationObjective,
            )

        return ImageCreationPreparationResult.create(
            traceId = traceId,
            status = ImageCreationPreparationStatus.PREPARED,
            imageCreation = imageCreation,
        )
    }

    private fun deferred(
        traceId: TraceId,
    ): ImageCreationPreparationResult {
        return ImageCreationPreparationResult.create(
            traceId = traceId,
            status = ImageCreationPreparationStatus.DEFERRED,
        )
    }
}
