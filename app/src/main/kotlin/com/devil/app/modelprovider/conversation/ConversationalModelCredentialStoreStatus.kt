package com.devil.app.modelprovider.conversation

/**
 * Stage 337B bounded conversational-model credential-store status.
 *
 * AVAILABLE means one previously provisioned credential was recovered from the
 * approved Android-private credential store.
 *
 * UNAVAILABLE means no usable credential was recovered.
 *
 * AVAILABLE != CREDENTIAL_VALID.
 * AVAILABLE != PROVIDER_AVAILABLE.
 * AVAILABLE != DEVIL_AUTHORIZATION.
 * CREDENTIAL != IDENTITY.
 * CREDENTIAL != AUTHORIZATION.
 */
enum class ConversationalModelCredentialStoreStatus {
    AVAILABLE,
    UNAVAILABLE,
}
