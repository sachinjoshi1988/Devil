package com.devil.core.model.creative

/**
 * Stage 174 bounded classification for one supplied Creative Project Workspace
 * asset representation.
 *
 * The type classifies workspace continuity context only.
 *
 * It does not establish that:
 *
 * - an asset was generated;
 * - an asset file exists;
 * - character or location identity was verified;
 * - media was inspected;
 * - persistence occurred.
 *
 * WORKSPACE_ASSET_TYPE != GENERATED_ASSET.
 * WORKSPACE_ASSET_TYPE != FILE.
 */
enum class CreativeWorkspaceAssetType {
    CHARACTER,
    LOCATION,
    LANDMARK,
    ENVIRONMENT,
    PROP,
    OTHER,
}
