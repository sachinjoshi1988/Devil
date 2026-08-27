package com.devil.app.modelprovider.conversation

import com.devil.core.model.common.TraceId

/**
 * Stage 313 result returned by the Android conversational-model transport
 * boundary.
 *
 * Generated text remains unverified model output.
 */
@ConsistentCopyVisibility
data class ConversationalModelTransportResult private constructor(
    val traceId: TraceId,
    val status: ConversationalModelTransportStatus,
    val generatedText: String?,
) {
    companion object {

        fun generated(
            traceId: TraceId,
            generatedText: String,
        ): ConversationalModelTransportResult {
            require(generatedText.isNotBlank()) {
                "Generated conversational-model text must not be blank."
            }

            return ConversationalModelTransportResult(
                traceId = traceId,
                status = ConversationalModelTransportStatus.GENERATED,
                generatedText = generatedText,
            )
        }

        fun unavailable(
            traceId: TraceId,
        ): ConversationalModelTransportResult {
            return ConversationalModelTransportResult(
                traceId = traceId,
                status = ConversationalModelTransportStatus.UNAVAILABLE,
                generatedText = null,
            )
        }
    }
}
