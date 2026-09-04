package com.devil.app.modelprovider.conversation

/**
 * Stage 337B bounded runtime credential reader for conversational-model
 * configuration.
 *
 * This adapter exposes only an already-provisioned credential recovered from
 * the approved credential store.
 *
 * It does not:
 *
 * - provision or replace credentials;
 * - authenticate an owner;
 * - grant Devil authorization;
 * - validate a credential with a provider;
 * - invoke a model;
 * - execute capabilities;
 * - establish verified truth.
 *
 * CREDENTIAL_AVAILABLE != CREDENTIAL_VALID.
 * CREDENTIAL_AVAILABLE != PROVIDER_AVAILABLE.
 * CREDENTIAL_AVAILABLE != DEVIL_AUTHORIZATION.
 * MODEL != DEVIL.
 * MODEL != BRAIN.
 * MODEL != AUTHORITY.
 */
class DefaultConversationalModelRuntimeCredentialProvider(
    private val credentialStore: ConversationalModelCredentialStore,
) {

    fun credential(): String? {
        val result =
            credentialStore.read()

        return when (result.status) {
            ConversationalModelCredentialStoreStatus.AVAILABLE ->
                result.credentialOrNull()

            ConversationalModelCredentialStoreStatus.UNAVAILABLE ->
                null
        }
    }
}
