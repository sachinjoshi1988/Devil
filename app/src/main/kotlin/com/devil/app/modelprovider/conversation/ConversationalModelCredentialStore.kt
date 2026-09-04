package com.devil.app.modelprovider.conversation

/**
 * Stage 337B Android boundary for storing one owner-provisioned
 * conversational-model credential.
 *
 * Implementations may protect the credential using Android platform security,
 * but this contract itself does not authenticate an owner, grant Devil
 * authorization, validate the credential with a provider, invoke a model,
 * execute capabilities, or establish verified truth.
 *
 * CREDENTIAL_STORED != CREDENTIAL_VALID.
 * CREDENTIAL_AVAILABLE != PROVIDER_AVAILABLE.
 * CREDENTIAL_AVAILABLE != DEVIL_AUTHORIZATION.
 * ANDROID_AUTHENTICATION != DEVIL_AUTHORIZATION.
 */
interface ConversationalModelCredentialStore {

    fun read(): ConversationalModelCredentialStoreResult

    fun replace(
        credential: String,
    ): Boolean

    fun clear(): Boolean
}
