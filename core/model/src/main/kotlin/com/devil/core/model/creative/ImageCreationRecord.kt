package com.devil.core.model.creative

/**
 * Immutable Stage 167 representation of one bounded Image Creation context.
 *
 * This record preserves:
 *
 * - one exact existing Stage 166 CreativeMediaIntegrationRecord;
 * - one explicitly supplied nonblank image-creation focus;
 * - one explicitly supplied nonblank image-creation description;
 * - one explicitly supplied nonblank image-creation objective.
 *
 * Stage 167 represents provider-neutral Image Creation preparation only.
 *
 * It does not:
 *
 * - create another Devil intelligence;
 * - replace or reconstruct the preserved Stage 166 integration;
 * - select or invoke an image-generation provider or model;
 * - create provider-specific architecture;
 * - authorize or execute a capability;
 * - generate image bytes, files, assets, or rendered output;
 * - establish that any generated image exists;
 * - understand, inspect, edit, retouch, or transform images;
 * - implement Stages 168 through 174;
 * - publish, upload, distribute, or transmit media;
 * - establish constitutional Observation, Verification, or Outcome;
 * - mutate World Model state;
 * - perform constitutional Learning;
 * - create or persist Memory.
 *
 * IMAGE_CREATION = DOMAIN_CAPABILITY_CONTEXT.
 * IMAGE_CREATION != PROVIDER.
 * IMAGE_CREATION_PREPARED != IMAGE_GENERATED.
 * GENERATED != VERIFIED.
 * GENERATION != PUBLISHING_AUTHORIZATION.
 * IMAGE_CREATION_PREPARED != EXECUTION.
 * IMAGE_CREATION != IMAGE_UNDERSTANDING_EDITING.
 */
@ConsistentCopyVisibility
data class ImageCreationRecord private constructor(
    val creativeMediaIntegration: CreativeMediaIntegrationRecord,
    val imageCreationFocus: String,
    val suppliedImageCreationDescription: String,
    val imageCreationObjective: String,
) {
    companion object {

        fun create(
            creativeMediaIntegration: CreativeMediaIntegrationRecord,
            imageCreationFocus: String,
            suppliedImageCreationDescription: String,
            imageCreationObjective: String,
        ): ImageCreationRecord {
            val normalizedImageCreationFocus =
                imageCreationFocus.trim()

            val normalizedSuppliedImageCreationDescription =
                suppliedImageCreationDescription.trim()

            val normalizedImageCreationObjective =
                imageCreationObjective.trim()

            require(normalizedImageCreationFocus.isNotEmpty()) {
                "Image Creation focus must not be blank."
            }

            require(normalizedSuppliedImageCreationDescription.isNotEmpty()) {
                "Image Creation description must not be blank."
            }

            require(normalizedImageCreationObjective.isNotEmpty()) {
                "Image Creation objective must not be blank."
            }

            return ImageCreationRecord(
                creativeMediaIntegration = creativeMediaIntegration,
                imageCreationFocus = normalizedImageCreationFocus,
                suppliedImageCreationDescription =
                    normalizedSuppliedImageCreationDescription,
                imageCreationObjective = normalizedImageCreationObjective,
            )
        }
    }
}
