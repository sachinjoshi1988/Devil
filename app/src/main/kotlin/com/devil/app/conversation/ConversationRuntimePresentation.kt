package com.devil.app.conversation

import com.devil.core.model.common.TraceId
import com.devil.core.runtime.RuntimeResult
import com.devil.core.runtime.RuntimeStatus

/**
 * Represents one truth-preserving UI presentation of an immediate RuntimeResult.
 *
 * This contract preserves trace identity and runtime truth without converting
 * runtime acceptance into execution success or a verified outcome.
 *
 * It does not fabricate a Devil response, execute capabilities, mutate runtime
 * state, create conversation persistence, or create logical memory.
 */
data class ConversationRuntimePresentation(
    val traceId: TraceId,
    val status: ConversationRuntimePresentationStatus,
    val message: String,
) {
    companion object {

        fun from(
            result: RuntimeResult,
        ): ConversationRuntimePresentation {
            return when (result.status) {
                RuntimeStatus.ACCEPTED ->
                    ConversationRuntimePresentation(
                        traceId = result.traceId,
                        status =
                            ConversationRuntimePresentationStatus.ACCEPTED,
                        message =
                            "Accepted for constitutional processing.",
                    )

                RuntimeStatus.DEFERRED ->
                    ConversationRuntimePresentation(
                        traceId = result.traceId,
                        status =
                            ConversationRuntimePresentationStatus.DEFERRED,
                        message =
                            "Deferred by the Devil runtime.",
                    )

                RuntimeStatus.REJECTED ->
                    ConversationRuntimePresentation(
                        traceId = result.traceId,
                        status =
                            ConversationRuntimePresentationStatus.REJECTED,
                        message = requireNotNull(result.error).summary,
                    )
            }
        }
    }
}
