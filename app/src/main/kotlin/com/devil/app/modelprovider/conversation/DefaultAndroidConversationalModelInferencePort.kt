package com.devil.app.modelprovider.conversation

import com.devil.core.runtime.modelprovider.conversation.ConversationalModelInferencePort
import com.devil.core.runtime.modelprovider.conversation.ConversationalModelInferenceRequest
import com.devil.core.runtime.modelprovider.conversation.ConversationalModelInferenceResult

/**
 * Stage 313 Android implementation of the provider-neutral conversational-model
 * inference port.
 *
 * This adapter resolves explicitly supplied Android configuration and delegates
 * transport to one bounded transport implementation.
 *
 * Missing configuration fails closed.
 *
 * This adapter does not:
 *
 * - establish identity, trust, authentication, or authorization;
 * - perform Conversation Intake Authority;
 * - select a model provider;
 * - invent configuration or credentials;
 * - execute Devil capabilities;
 * - establish constitutional Observation, Verification, or Outcome;
 * - perform Learning;
 * - create, commit, persist, or recall Memory;
 * - or treat generated model text as verified truth.
 *
 * CONFIGURATION_AVAILABLE != PROVIDER_AVAILABLE.
 * TRANSPORT_SUCCESS != VERIFIED.
 * GENERATED != VERIFIED.
 * MODEL != DEVIL.
 * MODEL != BRAIN.
 * MODEL != AUTHORITY.
 */
class DefaultAndroidConversationalModelInferencePort(
    private val configurationSource:
        ConversationalModelConfigurationSource,
    private val transport:
        ConversationalModelTransport,
) : ConversationalModelInferencePort {

    override fun infer(
        request: ConversationalModelInferenceRequest,
    ): ConversationalModelInferenceResult {
        val configurationResult =
            configurationSource.resolve()

        val configuration =
            configurationResult.configuration
                ?: return ConversationalModelInferenceResult.unavailable(
                    traceId = request.traceId,
                )

        val transportResult =
            transport.invoke(
                request = request,
                configuration = configuration,
            )

        require(
            transportResult.traceId == request.traceId,
        ) {
            "Conversational-model transport result must preserve request trace identity."
        }

        return when (transportResult.status) {
            ConversationalModelTransportStatus.GENERATED -> {
                val generatedText =
                    requireNotNull(
                        transportResult.generatedText,
                    ) {
                        "Generated transport result must contain generated text."
                    }

                ConversationalModelInferenceResult.available(
                    traceId = request.traceId,
                    generatedOutput = generatedText,
                )
            }

            ConversationalModelTransportStatus.UNAVAILABLE ->
                ConversationalModelInferenceResult.unavailable(
                    traceId = request.traceId,
                )
        }
    }
}
