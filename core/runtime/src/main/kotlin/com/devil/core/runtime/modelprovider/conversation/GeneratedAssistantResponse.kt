package com.devil.core.runtime.modelprovider.conversation

import com.devil.core.model.common.TraceId

/**
 * Stage 313 representation of generated assistant-facing conversational text.
 *
 * This is deliberately distinct from RuntimeResult and from constitutional
 * Observation, Verification, Outcome, Learning, and Memory.
 *
 * GENERATED_ASSISTANT_RESPONSE != RUNTIME_ACCEPTANCE.
 * GENERATED_ASSISTANT_RESPONSE != VERIFIED_TRUTH.
 * GENERATED_ASSISTANT_RESPONSE != VERIFIED_OUTCOME.
 */
@ConsistentCopyVisibility
data class GeneratedAssistantResponse private constructor(
    val traceId: TraceId,
    val content: String,
) {
    companion object {
        fun from(
            inference: ConversationalModelInferenceResult,
        ): GeneratedAssistantResponse {
            require(
                inference.status ==
                    ConversationalModelInferenceStatus.AVAILABLE,
            ) {
                "Generated assistant response requires available model output."
            }

            return GeneratedAssistantResponse(
                traceId = inference.traceId,
                content = requireNotNull(inference.generatedOutput),
            )
        }
    }
}
