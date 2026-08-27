package com.devil.core.runtime.modelprovider.conversation

/**
 * Stage 313 provider-neutral conversational model-inference boundary.
 *
 * Implementations may eventually represent local or cloud model adapters.
 * This contract itself performs no provider selection, networking, credential
 * handling, model invocation, inference, authorization, execution,
 * constitutional Verification, Outcome establishment, Learning, or Memory.
 *
 * MODEL != DEVIL.
 * MODEL != BRAIN.
 * MODEL != AUTHORITY.
 * GENERATED != VERIFIED.
 */
fun interface ConversationalModelInferencePort {

    fun infer(
        request: ConversationalModelInferenceRequest,
    ): ConversationalModelInferenceResult
}
