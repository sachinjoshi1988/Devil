package com.devil.core.model.creative

/**
 * Immutable Stage 168 representation of one bounded Image Understanding &
 * Editing context.
 *
 * This record preserves:
 *
 * - one exact existing Stage 167 ImageCreationRecord;
 * - one explicitly supplied nonblank image-understanding/editing focus;
 * - one explicitly supplied nonblank reference-image context description;
 * - one explicitly supplied nonblank editing objective.
 *
 * Stage 168 represents supplied reference-image and editing-intent context only.
 *
 * It does not:
 *
 * - inspect, decode, or understand actual image bytes;
 * - alter pixels, files, assets, or rendered output;
 * - select or invoke image-understanding or editing providers or models;
 * - establish reference-image authenticity;
 * - determine identity from a face;
 * - verify visual similarity or character consistency;
 * - establish that any edit occurred;
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
 * IMAGE_UNDERSTANDING_EDITING != STORY_CREATION.
 */
@ConsistentCopyVisibility
data class ImageUnderstandingEditingRecord private constructor(
    val imageCreation: ImageCreationRecord,
    val imageUnderstandingEditingFocus: String,
    val suppliedReferenceImageContextDescription: String,
    val editingObjective: String,
) {
    companion object {

        fun create(
            imageCreation: ImageCreationRecord,
            imageUnderstandingEditingFocus: String,
            suppliedReferenceImageContextDescription: String,
            editingObjective: String,
        ): ImageUnderstandingEditingRecord {
            val normalizedFocus =
                imageUnderstandingEditingFocus.trim()

            val normalizedReferenceImageContextDescription =
                suppliedReferenceImageContextDescription.trim()

            val normalizedEditingObjective =
                editingObjective.trim()

            require(normalizedFocus.isNotEmpty()) {
                "Image Understanding & Editing focus must not be blank."
            }

            require(normalizedReferenceImageContextDescription.isNotEmpty()) {
                "Reference-image context description must not be blank."
            }

            require(normalizedEditingObjective.isNotEmpty()) {
                "Image Understanding & Editing objective must not be blank."
            }

            return ImageUnderstandingEditingRecord(
                imageCreation = imageCreation,
                imageUnderstandingEditingFocus = normalizedFocus,
                suppliedReferenceImageContextDescription =
                    normalizedReferenceImageContextDescription,
                editingObjective = normalizedEditingObjective,
            )
        }
    }
}
