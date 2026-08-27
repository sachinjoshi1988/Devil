package com.devil.core.runtime.modelprovider.conversation

import com.devil.core.model.common.TraceId

/**
 * Stage 313 provider-neutral result of one bounded conversational model
 * inference attempt.
 *
 * Generated output remains untrusted model output. It is not constitutional
 * Verification, verified truth, verified Outcome, Memory, or authority.
 */
@ConsistentCopyVisibility
data class ConversationalModelInferenceResult private constructor(
    val traceId: TraceId,
    val status: ConversationalModelInferenceStatus,
    val generatedOutput: String?,
    val errorDescription: String?,
) {
    companion object {
        fun available(
            traceId: TraceId,
            generatedOutput: String,
        ): ConversationalModelInferenceResult {
            val normalizedOutput = generatedOutput.trim()

            require(normalizedOutput.isNotEmpty()) {
                "Available conversational model output must not be blank."
            }

            return ConversationalModelInferenceResult(
                traceId = traceId,
                status = ConversationalModelInferenceStatus.AVAILABLE,
                generatedOutput = normalizedOutput,
                errorDescription = null,
            )
        }

        fun unavailable(
            traceId: TraceId,
        ): ConversationalModelInferenceResult {
            return ConversationalModelInferenceResult(
                traceId = traceId,
                status = ConversationalModelInferenceStatus.UNAVAILABLE,
                generatedOutput = null,
                errorDescription = null,
            )
        }

        fun failed(
            traceId: TraceId,
            errorDescription: String,
        ): ConversationalModelInferenceResult {
            val normalizedError = errorDescription.trim()

            require(normalizedError.isNotEmpty()) {
                "Failed conversational model inference requires an error description."
            }

            return ConversationalModelInferenceResult(
                traceId = traceId,
                status = ConversationalModelInferenceStatus.FAILED,
                generatedOutput = null,
                errorDescription = normalizedError,
            )
        }
    }
}
