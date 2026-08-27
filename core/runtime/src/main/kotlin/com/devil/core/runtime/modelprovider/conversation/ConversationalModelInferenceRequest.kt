package com.devil.core.runtime.modelprovider.conversation

import com.devil.core.model.common.TraceId

/**
 * Stage 313 provider-neutral request presented to a conversational model
 * inference boundary after upstream constitutional processing has established
 * the bounded request context.
 *
 * This request does not grant authorization, select a provider, invoke a model,
 * prove execution, or establish verified truth.
 */
@ConsistentCopyVisibility
data class ConversationalModelInferenceRequest private constructor(
    val traceId: TraceId,
    val content: String,
) {
    companion object {
        fun create(
            traceId: TraceId,
            content: String,
        ): ConversationalModelInferenceRequest {
            val normalizedContent = content.trim()

            require(normalizedContent.isNotEmpty()) {
                "Conversational model inference content must not be blank."
            }

            return ConversationalModelInferenceRequest(
                traceId = traceId,
                content = normalizedContent,
            )
        }
    }
}
