package com.devil.core.model.creative

/**
 * Immutable Stage 174 representation of one explicitly supplied Creative
 * Project Workspace asset-continuity context.
 *
 * This record preserves:
 *
 * - one stable ordered position within the supplied workspace context;
 * - one bounded workspace asset type;
 * - one explicitly supplied nonblank name;
 * - one explicitly supplied nonblank continuity description.
 *
 * It represents supplied creative-production metadata only.
 *
 * It does not:
 *
 * - contain image, audio, video, model, or other asset bytes;
 * - establish that a file or generated asset exists;
 * - inspect supplied media;
 * - verify character, location, landmark, environment, or prop identity;
 * - verify visual similarity or continuity;
 * - generate or modify media;
 * - invoke a provider, model, generator, renderer, or codec;
 * - persist workspace state;
 * - create constitutional Memory.
 *
 * WORKSPACE_ASSET_RECORD != GENERATED_ASSET.
 * WORKSPACE_ASSET_RECORD != FILE.
 * CHARACTER_RECORD != VERIFIED_CHARACTER_IDENTITY.
 * LOCATION_RECORD != VERIFIED_LOCATION_IDENTITY.
 * SUPPLIED_CONTINUITY != VERIFIED_VISUAL_CONSISTENCY.
 */
@ConsistentCopyVisibility
data class CreativeWorkspaceAssetRecord private constructor(
    val position: Int,
    val type: CreativeWorkspaceAssetType,
    val name: String,
    val suppliedContinuityDescription: String,
) {
    companion object {

        fun create(
            position: Int,
            type: CreativeWorkspaceAssetType,
            name: String,
            suppliedContinuityDescription: String,
        ): CreativeWorkspaceAssetRecord {
            require(position >= 0) {
                "Creative Project Workspace asset position must not be negative."
            }

            val normalizedName =
                name.trim()

            val normalizedSuppliedContinuityDescription =
                suppliedContinuityDescription.trim()

            require(normalizedName.isNotEmpty()) {
                "Creative Project Workspace asset name must not be blank."
            }

            require(normalizedSuppliedContinuityDescription.isNotEmpty()) {
                "Creative Project Workspace asset continuity description must not be blank."
            }

            return CreativeWorkspaceAssetRecord(
                position = position,
                type = type,
                name = normalizedName,
                suppliedContinuityDescription =
                    normalizedSuppliedContinuityDescription,
            )
        }
    }
}
