package com.devil.app.modelprovider.conversation

/**
 * Stage 313 Android-side configuration required by a future bounded
 * conversational-model inference adapter.
 *
 * This record preserves explicitly supplied provider configuration only.
 *
 * It does not:
 *
 * - select a provider;
 * - establish provider availability or health;
 * - validate remote credentials;
 * - perform networking;
 * - invoke a model;
 * - perform inference;
 * - grant Devil authorization;
 * - execute capabilities;
 * - establish constitutional Observation, Verification, or Outcome;
 * - perform Learning;
 * - create or persist Memory.
 *
 * CONFIGURATION_AVAILABLE != PROVIDER_AVAILABLE.
 * CONFIGURATION_AVAILABLE != CREDENTIAL_VALID.
 * CONFIGURATION_AVAILABLE != MODEL_INVOKED.
 * CONFIGURATION_AVAILABLE != INFERENCE_PERFORMED.
 * CONFIGURATION_AVAILABLE != AUTHORIZATION.
 * MODEL != DEVIL.
 * MODEL != BRAIN.
 * MODEL != AUTHORITY.
 */
@ConsistentCopyVisibility
data class ConversationalModelConfiguration private constructor(
    val endpoint: String,
    val modelId: String,
    val credential: String,
) {
    companion object {

        fun create(
            endpoint: String,
            modelId: String,
            credential: String,
        ): ConversationalModelConfiguration {
            require(endpoint.isNotBlank()) {
                "Conversational model endpoint must not be blank."
            }

            require(modelId.isNotBlank()) {
                "Conversational model identifier must not be blank."
            }

            require(credential.isNotBlank()) {
                "Conversational model credential must not be blank."
            }

            return ConversationalModelConfiguration(
                endpoint = endpoint,
                modelId = modelId,
                credential = credential,
            )
        }
    }
}
