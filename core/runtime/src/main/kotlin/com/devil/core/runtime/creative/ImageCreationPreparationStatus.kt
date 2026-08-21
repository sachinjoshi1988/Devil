package com.devil.core.runtime.creative

/**
 * Stage 167 bounded Image Creation preparation status.
 *
 * PREPARED means one structurally valid provider-neutral Image Creation context
 * was prepared from one exact existing Stage 166 Creative Media Integration
 * context and explicitly supplied image-creation metadata.
 *
 * PREPARED does not mean:
 *
 * - an image provider or model was selected or invoked;
 * - generation was authorized or executed;
 * - an image was generated;
 * - generated media was verified;
 * - publishing was authorized;
 * - image understanding or editing occurred;
 * - Stage 168–174 behavior was implemented;
 * - or constitutional Verification occurred.
 *
 * DEFERRED means no truthful Image Creation context was produced.
 */
enum class ImageCreationPreparationStatus {
    PREPARED,
    DEFERRED,
}
