package com.devil.app.modelprovider.conversation

/**
 * Stage 337B result of reading the bounded conversational-model credential
 * store.
 *
 * The raw credential is deliberately not exposed through a generated data-class
 * toString() or copy() representation.
 *
 * A recovered credential remains provider configuration only.
 *
 * CREDENTIAL_AVAILABLE != CREDENTIAL_VALID.
 * CREDENTIAL_AVAILABLE != PROVIDER_AVAILABLE.
 * CREDENTIAL_AVAILABLE != DEVIL_AUTHORIZATION.
 * MODEL != DEVIL.
 * MODEL != BRAIN.
 * MODEL != AUTHORITY.
 */
class ConversationalModelCredentialStoreResult private constructor(
    val status: ConversationalModelCredentialStoreStatus,
    private val credentialValue: String?,
) {

    fun credentialOrNull(): String? {
        return credentialValue
    }

    override fun toString(): String {
        return "ConversationalModelCredentialStoreResult(status=$status)"
    }

    companion object {

        fun available(
            credential: String,
        ): ConversationalModelCredentialStoreResult {
            require(credential.isNotBlank()) {
                "Conversational-model credential must not be blank."
            }

            return ConversationalModelCredentialStoreResult(
                status = ConversationalModelCredentialStoreStatus.AVAILABLE,
                credentialValue = credential,
            )
        }

        fun unavailable(): ConversationalModelCredentialStoreResult {
            return ConversationalModelCredentialStoreResult(
                status = ConversationalModelCredentialStoreStatus.UNAVAILABLE,
                credentialValue = null,
            )
        }
    }
}
