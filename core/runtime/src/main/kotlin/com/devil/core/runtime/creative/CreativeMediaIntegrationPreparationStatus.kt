package com.devil.core.runtime.creative

/**
 * Stage 166 bounded Creative Media Integration preparation status.
 *
 * PREPARED means one structurally valid Creative Media Integration context was
 * prepared from one exact existing Stage 87 CreativeMediaProjectRecord and
 * explicitly supplied integration metadata.
 *
 * PREPARED does not mean:
 *
 * - another intelligence was created;
 * - a provider was selected or invoked;
 * - a capability was registered, authorized, activated, or executed;
 * - media was generated;
 * - generated media was verified;
 * - publishing was authorized;
 * - Stage 167–174 behavior was implemented;
 * - or constitutional Verification occurred.
 *
 * DEFERRED means no truthful Creative Media Integration context was produced.
 */
enum class CreativeMediaIntegrationPreparationStatus {
    PREPARED,
    DEFERRED,
}
