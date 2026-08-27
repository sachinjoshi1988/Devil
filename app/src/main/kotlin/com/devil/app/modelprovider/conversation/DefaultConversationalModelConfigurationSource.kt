package com.devil.app.modelprovider.conversation

/**
 * Default Stage 313 Android conversational-model configuration source.
 *
 * Values are supplied by bounded build/runtime value providers composed
 * outside this class.
 *
 * No endpoint, model identifier, or credential is hard-coded here.
 *
 * If any required value is absent or blank, configuration remains
 * UNAVAILABLE.
 *
 * Missing configuration fails closed.
 *
 * CONFIGURATION_AVAILABLE != PROVIDER_AVAILABLE.
 * CONFIGURATION_AVAILABLE != CREDENTIAL_VALID.
 * CONFIGURATION_AVAILABLE != MODEL_INVOKED.
 * CONFIGURATION_AVAILABLE != INFERENCE_PERFORMED.
 * CONFIGURATION_AVAILABLE != AUTHORIZATION.
 */
class DefaultConversationalModelConfigurationSource(
    private val endpointProvider: () -> String?,
    private val modelIdProvider: () -> String?,
    private val credentialProvider: () -> String?,
) : ConversationalModelConfigurationSource {

    override fun resolve(): ConversationalModelConfigurationResult {
        val endpoint = endpointProvider()
        val modelId = modelIdProvider()
        val credential = credentialProvider()

        if (
            endpoint.isNullOrBlank() ||
            modelId.isNullOrBlank() ||
            credential.isNullOrBlank()
        ) {
            return ConversationalModelConfigurationResult.unavailable()
        }

        return ConversationalModelConfigurationResult.available(
            configuration =
                ConversationalModelConfiguration.create(
                    endpoint = endpoint,
                    modelId = modelId,
                    credential = credential,
                ),
        )
    }
}
