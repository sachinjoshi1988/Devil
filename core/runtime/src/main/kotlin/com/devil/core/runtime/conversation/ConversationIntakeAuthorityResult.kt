package com.devil.core.runtime.conversation

import com.devil.core.model.common.TraceId
import com.devil.core.model.conversation.ConversationIntakeResult
import com.devil.core.model.error.UniversalErrorRecord

/**
 * Represents the structured operational result of conversation intake.
 *
 * A produced result contains a ConversationIntakeResult. A deferred result
 * contains neither intake result nor error. A failed result contains a matching
 * error from the same trace.
 *
 * This contract does not interpret language, establish understanding, create
 * memory, make decisions, plan work, execute capabilities, or verify outcomes.
 */
@ConsistentCopyVisibility
data class ConversationIntakeAuthorityResult private constructor(
    val traceId: TraceId,
    val status: ConversationIntakeAuthorityStatus,
    val intake: ConversationIntakeResult?,
    val error: UniversalErrorRecord?,
) {
    companion object {
        fun create(
            traceId: TraceId,
            status: ConversationIntakeAuthorityStatus,
            intake: ConversationIntakeResult? = null,
            error: UniversalErrorRecord? = null,
        ): ConversationIntakeAuthorityResult {
            when (status) {
                ConversationIntakeAuthorityStatus.PRODUCED -> {
                    require(intake != null && error == null) {
                        "Produced conversation-intake results require an intake result and must not contain an error."
                    }
                }

                ConversationIntakeAuthorityStatus.DEFERRED -> {
                    require(intake == null && error == null) {
                        "Deferred conversation-intake results must not contain an intake result or error."
                    }
                }

                ConversationIntakeAuthorityStatus.FAILED -> {
                    require(intake == null && error != null) {
                        "Failed conversation-intake results require an error and must not contain an intake result."
                    }
                }
            }

            require(
                intake == null ||
                    intake.record.input.context.traceId == traceId,
            ) {
                "Conversation-intake authority result and intake result must use the same trace identity."
            }

            require(error == null || error.traceId == traceId) {
                "Conversation-intake authority result and error must use the same trace identity."
            }

            return ConversationIntakeAuthorityResult(
                traceId = traceId,
                status = status,
                intake = intake,
                error = error,
            )
        }
    }
}
