package com.devil.core.runtime.creative

/**
 * Stage 168 bounded Image Understanding & Editing preparation status.
 *
 * PREPARED means one structurally valid supplied reference-image/editing context
 * was prepared from one exact existing Stage 167 Image Creation context.
 *
 * PREPARED does not mean:
 *
 * - image bytes were inspected or understood;
 * - an editing provider or model was selected or invoked;
 * - an image was edited;
 * - identity, similarity, or character consistency was verified;
 * - generation or editing was executed;
 * - publishing was authorized;
 * - Stage 169–174 behavior was implemented;
 * - or constitutional Verification occurred.
 *
 * DEFERRED means no truthful Image Understanding & Editing context was produced.
 */
enum class ImageUnderstandingEditingPreparationStatus {
    PREPARED,
    DEFERRED,
}
