package com.devil.app.modelprovider.conversation

/**
 * Stage 337B bounded conversational-model credential provisioning boundary.
 *
 * This coordinator may replace or clear one credential only after an upstream
 * caller has independently established the evidence required to permit that
 * local credential-management operation.
 *
 * This coordinator does not:
 *
 * - authenticate an owner;
 * - establish Devil identity, trust, session, or authorization;
 * - validate a credential with a provider;
 * - invoke a conversational model;
 * - execute a Devil capability;
 * - revoke a provider-side credential;
 * - establish Verification or Outcome.
 *
 * ANDROID_AUTHENTICATION_SUCCESS != DEVIL_AUTHORIZATION.
 * CREDENTIAL_PROVISIONED != CREDENTIAL_VALID.
 * CREDENTIAL_AVAILABLE != PROVIDER_AVAILABLE.
 * CREDENTIAL_REMOVED != PROVIDER_REVOKED.
 * MODEL != DEVIL.
 * MODEL != BRAIN.
 * MODEL != AUTHORITY.
 */
class DefaultConversationalModelCredentialProvisioningCoordinator(
    private val credentialStore: ConversationalModelCredentialStore,
) {

    fun provision(
        credential: String,
    ): Boolean {
        if (credential.isBlank()) {
            return false
        }

        return credentialStore.replace(
            credential = credential,
        )
    }

    fun remove(): Boolean {
        return credentialStore.clear()
    }
}
