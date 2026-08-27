package com.devil.app.modelprovider.conversation

import com.devil.core.runtime.modelprovider.conversation.ConversationalModelInferenceRequest

/**
 * Stage 313 Android transport boundary for one bounded conversational-model
 * request.
 *
 * This boundary deliberately knows nothing about Devil constitutional
 * authority. It transports an already-established inference request together
 * with explicitly resolved Android provider configuration.
 *
 * Implementations may eventually communicate with a concrete model provider,
 * but this contract itself performs no networking.
 *
 * TRANSPORT != DEVIL.
 * TRANSPORT != BRAIN.
 * TRANSPORT != AUTHORITY.
 * TRANSPORT SUCCESS != VERIFIED TRUTH.
 */
fun interface ConversationalModelTransport {

    fun invoke(
        request: ConversationalModelInferenceRequest,
        configuration: ConversationalModelConfiguration,
    ): ConversationalModelTransportResult
}
