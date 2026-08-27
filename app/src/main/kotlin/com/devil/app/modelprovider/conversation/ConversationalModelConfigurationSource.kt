package com.devil.app.modelprovider.conversation

/**
 * Stage 313 boundary for obtaining conversational-model configuration from
 * an external build/runtime configuration mechanism.
 *
 * Implementations must not hard-code production credentials merely to make
 * inference available.
 *
 * This source performs no networking, authentication, authorization,
 * provider invocation, model inference, execution, Verification, Outcome,
 * Learning, or Memory persistence.
 */
fun interface ConversationalModelConfigurationSource {

    fun resolve(): ConversationalModelConfigurationResult
}
